package fer.zavrsni.src.analysis;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectAnalysis {

    private Project project;
    private List<String> tools;
    private String packageName;
    private JTextField packageChooser;

    private Path projectRootPath;
    private Path auxClasspathFromFile;
    private Path reportPath;
    private Path reportSrcPath;

    private VirtualFile scriptFolder;
    private Path scriptPath;


    public ProjectAnalysis(@NotNull Project project, List<String> tools, String packageName,
                           JTextField packageChooser) throws IOException {
        this.project = project;
        this.tools = tools;
        this.packageName = packageName;
        this.packageChooser = packageChooser;

        String path = project.getBasePath();
        if(Objects.isNull(path)) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: ",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.projectRootPath = Paths.get(path);
        if(!Files.exists(projectRootPath)) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: ",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        this.scriptFolder = LocalFileSystem.getInstance().findFileByPath(
                projectRootPath.toString() + "/PPProjekt/");
        if(Objects.isNull(scriptFolder) || !scriptFolder.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: ",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.scriptPath = Paths.get(scriptFolder.getPath() + "/script.py");
        if(!Files.exists(scriptPath)) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: ",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        auxClasspathFromFile = Paths.get(scriptFolder.getPath() + "/auxClasspathFromFile");
        if (!Files.exists(auxClasspathFromFile)) {
            Files.createFile(auxClasspathFromFile);
        }

    }

    public void executeAnalysis() throws IOException {
        StringBuilder toolsArg = new StringBuilder();
        tools.forEach(t -> toolsArg.append(t).append(","));
        toolsArg.replace(toolsArg.length()-1, toolsArg.length(), "");

        ProjectRootManager projectManager = ProjectRootManager.getInstance(project);
        projectManager.getContentRootsFromAllModules(); //use this for all modules of a project
        Sdk projectSdk = projectManager.getProjectSdk();
        if(Objects.isNull(projectSdk)){
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: ",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String sdkPath = projectSdk.getHomePath();
        //pogledati sto treba ukljuciti u auxclasspath da radi spotbugs

        String classpath = projectRootPath + "/gradle";
        writeToAuxClasspathFile(classpath, sdkPath);

        List<String> cmds = new ArrayList<>();
        cmds.add("python3");
        cmds.add(scriptPath.toAbsolutePath().toString());
        cmds.add("-t");
        cmds.add(toolsArg.toString());
        cmds.add("-c");
        cmds.add(auxClasspathFromFile.toAbsolutePath().toString());
        cmds.add("-p");
        cmds.add(packageName);
        cmds.add(projectRootPath.toAbsolutePath().toString());

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Analysis") {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setText("Processing...");
                try {
                    GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);
                    generalCommandLine.setCharset(StandardCharsets.UTF_8);
                    generalCommandLine.setWorkDirectory(scriptFolder.getPath());
                    OSProcessHandler processHandler = new OSProcessHandler(generalCommandLine);
                    processHandler.startNotify();
                    processHandler.waitFor(1000*60*10);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                progressIndicator.setText("Done");
            }
        });

//            processHandler.wait(1000 * 60 * 5);
//            processHandler.waitFor(1000 * 60 * 5);
//            String commandLineOutputStr = ScriptRunnerUtil.getProcessOutput(generalCommandLine);
//            packageChooser.setText(commandLineOutputStr);
    }

    private void writeToAuxClasspathFile(String classpath, String sdkPath) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(auxClasspathFromFile);
        bw.write(classpath + "\n" + sdkPath + "\n");
        bw.flush();
        bw.close();
    }

}
