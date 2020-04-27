package fer.zavrsni.src.analysis;

import com.google.common.collect.Lists;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
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
    private String projectRootPath;

    private JTextField packageChooser;

    private static Path auxClasspathFromFile;
    private static Path scriptPath;
    private static Path reportPath;
    private static Path reportSrcPath;

    private VirtualFile scriptFolder;


    public ProjectAnalysis(@NotNull Project project, List<String> tools, String packageName,
                           JTextField packageChooser) throws IOException {
        this.project = project;
        this.tools = tools;
        this.packageName = packageName;
        this.projectRootPath = project.getBasePath();
        this.packageChooser = packageChooser;

        if(Objects.isNull(projectRootPath)) {
            //TODO ispiši grešku
        }
        if(!projectRootPath.endsWith("/")) {
            projectRootPath += "/";
        }

        File scriptFolderPath = Paths.get(projectRootPath + "/PPProjekt").toFile();

        scriptFolder = LocalFileSystem.getInstance().findFileByIoFile(scriptFolderPath);
        if(Objects.isNull(scriptFolder) || !scriptFolder.exists()) {
            packageChooser.setText("Nije uspjelo!");
        }

        auxClasspathFromFile = Paths.get(scriptFolder.getPath() + "/auxClasspathFromFile");
        if (!Files.exists(auxClasspathFromFile)) {
            Files.createFile(auxClasspathFromFile);
        }

        scriptPath = Paths.get(scriptFolderPath + "/script.py");
        if (!Files.exists(scriptPath)) {
            packageChooser.setText(scriptPath.toString());
        } else {
            packageChooser.setText("Success!");
        }

        VirtualFile f = LocalFileSystem.getInstance().findFileByPath(scriptPath.toString());
        if(Objects.isNull(f) || !f.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: ",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void executeAnalysis() throws IOException {
        StringBuilder toolsArg = new StringBuilder();
        tools.forEach(t -> toolsArg.append(t).append(","));
        toolsArg.replace(toolsArg.length()-1, toolsArg.length(), "");

        ProjectRootManager projectManager = ProjectRootManager.getInstance(project);
        projectManager.getContentRootsFromAllModules();
        Sdk projectSdk = projectManager.getProjectSdk();
        if(Objects.isNull(projectSdk)){
         //TODO ispiši grešku
        }

        String classpath = projectRootPath + "gradle";
        writeToClasspathFile(classpath);

        try {
            List<String> cmds = new ArrayList<>();
            cmds.add("python3");
            cmds.add(scriptPath.toAbsolutePath().toString());
            cmds.add("-t " + toolsArg.toString());
            cmds.add("-c " + auxClasspathFromFile.toString());
            cmds.add("-p " + packageName);
            cmds.add(projectRootPath);

            GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);
            generalCommandLine.setCharset(StandardCharsets.UTF_8);
            generalCommandLine.setWorkDirectory(scriptFolder.getPath());

            OSProcessHandler processHandler = new OSProcessHandler(generalCommandLine);
            processHandler.startNotify();
//            String commandLineOutputStr = ScriptRunnerUtil.getProcessOutput(generalCommandLine);
//            packageChooser.setText(commandLineOutputStr);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void writeToClasspathFile(String classpath) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(auxClasspathFromFile);
        bw.write(classpath);
        bw.flush();
        bw.close();
    }

}
