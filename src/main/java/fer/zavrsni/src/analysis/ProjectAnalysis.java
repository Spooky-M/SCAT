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
import fer.zavrsni.src.utils.Util;
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

/**
 * Analyses the current project by calling Script's script.py from command line with specified arguments.
 */
public class ProjectAnalysis {

    /**
     *  Currently active project in IDE which runs plugin
     */
    private final Project project;

    /**
     * Tools which should be run in analysis, argument passed by constructor
     */
    private final List<String> tools;

    /**
     * The name of package which should be analysed, argument passed by constructor
     */
    private final String packageName;

    /**
     * Path to the root of currently active project
     */
    private Path projectRootPath;

    /**
     * Path to auxClasspathFromFile text file, which has paths to all additional resources necessary for analysis.
     * auxClasspathFromFile has to have a single valid path in each row.
     */
    private Path auxClasspathFromFile;

    /**
     * VirtualFile object of Script project's folder
     */
    private VirtualFile scriptFolder;

    /**
     * Path to script.py, located in Script project's root folder
     */
    private Path scriptPath;


    /**
     * Constructor which takes all necessary data. Sets {@code projectRootPath}
     * @param project -
     * @param tools
     * @param packageName
     * @throws IOException
     */
    public ProjectAnalysis(@NotNull Project project, List<String> tools, String packageName) throws IOException {
        this.project = project;
        this.tools = tools;
        this.packageName = packageName;

        String path = project.getBasePath();
        if(Objects.isNull(path)) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: " + "object base path is null",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.projectRootPath = Paths.get(path);
        if(!Files.exists(projectRootPath)) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: " +
                            "path " + projectRootPath.toAbsolutePath().toString() + " doesn't exist",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        this.scriptFolder = LocalFileSystem.getInstance().findFileByPath(
                projectRootPath.toString() + "/Script/");
        if(Objects.isNull(scriptFolder) || !scriptFolder.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: " + "Script folder couldn't be found. " +
                            "Script folder should be located in project's root folder. " +
                            "For example ./my_android_studio_project/Script",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.scriptPath = Paths.get(scriptFolder.getPath() + "/script.py");
        if(!Files.exists(scriptPath)) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: " +
                            "couldn't find the script from its path " + scriptPath.toAbsolutePath().toString(),
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
        Sdk projectSdk = projectManager.getProjectSdk();
        if(Objects.isNull(projectSdk)){
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: " + "couldn't get project's SDK",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String sdkPath = projectSdk.getHomePath();
        if(Objects.isNull(sdkPath) || !Files.exists(Paths.get(sdkPath))){
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: " + "SDK path couldn't be obtained or found",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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
                    processHandler.waitFor(Util.TIMEOUT);

                    JOptionPane.showMessageDialog(null,
                            "The report is available in Script folder. ("
                                    + projectRootPath.toAbsolutePath().toString() + "/Script/report.html)",
                            "Done", JOptionPane.INFORMATION_MESSAGE);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Something went wrong while running script: " + e.getMessage(),
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void writeToAuxClasspathFile(String classpath, String sdkPath) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(auxClasspathFromFile);
        bw.write(classpath + "\n" + sdkPath + "\n");
        bw.flush();
        bw.close();
    }

}
