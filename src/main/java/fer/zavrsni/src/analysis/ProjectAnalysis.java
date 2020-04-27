package fer.zavrsni.src.analysis;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private static Path scatFolderPath;


    public ProjectAnalysis(@NotNull Project project, List<String> tools, String packageName, JTextField packageChooser) throws IOException {
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

        scatFolderPath = Paths.get(projectRootPath + "SCAT/");
        if(!Files.exists(scatFolderPath)){
            Files.createDirectory(scatFolderPath);
        }

        reportPath = Paths.get(projectRootPath + "report.html");
        if (!Files.exists(reportPath)) {
            Files.createFile(reportPath);
        }

        auxClasspathFromFile = Paths.get(projectRootPath + "/auxClasspathFromFile");
        if (!Files.exists(auxClasspathFromFile)) {
            Files.createFile(auxClasspathFromFile);
        }

//        Files.copy(getClass().getResourceAsStream(""), scatFolderPath);
        scriptPath = Paths.get(getClass().getResource("/Script/script.py").getFile());
        if (!Files.exists(scriptPath)) {
            packageChooser.setText(scriptPath.toString());
        } else {
            packageChooser.setText("Success!");
        }
    }

    public void executeAnalysis() throws IOException {
        StringBuilder toolsArg = new StringBuilder();
        tools.forEach(t -> toolsArg.append(t).append(","));

        ProjectRootManager projectManager = ProjectRootManager.getInstance(project);
        projectManager.getContentRootsFromAllModules();
        Sdk projectSdk = projectManager.getProjectSdk();
        if(Objects.isNull(projectSdk)){
         //TODO ispiši grešku
        }

//        InterpreterExample ie = new InterpreterExample();
//        ie.execfile("Script/script.py");
//        PyInstance hello = ie.createClass("Hello", "None");
//        hello.invoke("run");

        try {
            String arg = "cd " + projectRootPath + " && touch proba";
//            Runtime.getRuntime().exec("/bin/bash -c " + arg);
            String[] args = new String[] {"/bin/bash", "-c", arg};
            Process proc = new ProcessBuilder(args).start();
            proc.wait();
            if(proc.exitValue() != 0) {
                JOptionPane.showMessageDialog(null,
                        "Something went wrong while running script: " + proc.getOutputStream(),
                        "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch(IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null,
                    "Something went wrong while running script: " + e.getMessage(),
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }

//        String classpath = projectRootPath + "gradle";
//        writeToClasspathFile(classpath);
//
//        String arg = "python3 " + scriptPath + " -t " + toolsArg.toString()
//                + " -c " + auxClasspathFromFile + " -p " + packageName + " " + projectRootPath;
//        String[] args = new String[] {"/bin/bash", "-c", arg};
//        Process proc;
//        try {
//            proc = new ProcessBuilder(args).start();
//            if(proc.exitValue() != 0) {
//                JOptionPane.showMessageDialog(null,
//                        "Something went wrong while running script: " + proc.getOutputStream(),
//                        "Error", JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch(IOException e) {
//            JOptionPane.showMessageDialog(null,
//                    "Something went wrong while running script: " + e.getMessage(),
//                    "Error", JOptionPane.INFORMATION_MESSAGE);
//        }
//
//        reportSrcPath = Paths.get(getClass().getResource("/Script/report.html").toString());
//        try {
//            Files.copy(reportSrcPath, reportPath, StandardCopyOption.REPLACE_EXISTING);
//        } catch(IOException e) {
//            JOptionPane.showMessageDialog(null,
//                    "Something went wrong when copying files: " + e.getMessage(),
//                    "Error", JOptionPane.INFORMATION_MESSAGE);
//        }
    }

    private void writeToClasspathFile(String classpath) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(auxClasspathFromFile);
        bw.write(classpath);
        bw.flush();
        bw.close();
    }

}
