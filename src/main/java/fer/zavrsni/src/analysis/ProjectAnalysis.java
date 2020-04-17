package fer.zavrsni.src.analysis;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ProjectAnalysis {

    private Project project;
    private List<String> tools;
    private String packageName;
    private Module skripta;

    private static Path auxClasspathFromFile;
    private static Path scriptPath;


    public ProjectAnalysis(@NotNull Project project, List<String> tools, String packageName) {
        this.project = project;
        this.tools = tools;
        this.packageName = packageName;
    }

    public void executeAnalysis() throws IOException {
        StringBuilder toolsArg = new StringBuilder();
        tools.forEach(toolsArg::append);

        ProjectRootManager projectManager = ProjectRootManager.getInstance(project);
        projectManager.getContentRootsFromAllModules();
        Sdk projectSdk = projectManager.getProjectSdk();
        if(Objects.isNull(projectSdk)){
         //TODO ispiši grešku
        }

        String root = project.getBasePath();
        if(Objects.isNull(root)) {
            //TODO ispiši grešku
        }
        if(!root.endsWith("/")) {
            root += "/";
        }

        scriptPath = Paths.get(getClass().getResource("/Skripta/script.py").getFile());
        auxClasspathFromFile = Paths.get(getClass().getResource("/ToolsResources/auxClasspathFromFile").getFile());

        String classpath = root + "gradle";
        writeToClasspathFile(classpath);

        String arg = "python3 " + scriptPath + " -t " + toolsArg.toString()
                + " -c " + auxClasspathFromFile + " -p " + packageName + " " + root;
        String[] args = new String[] {"/bin/bash", "-c", arg};
        Process proc = new ProcessBuilder(args).start();
        if(proc.exitValue() != 0) {
            //TODO ispiši grešku
        }

    }

    private void writeToClasspathFile(String classpath) throws IOException {
        if(!Files.exists(auxClasspathFromFile)){
            Files.createFile(auxClasspathFromFile);
        }

        BufferedWriter bw = Files.newBufferedWriter(auxClasspathFromFile);
        bw.write(classpath);
        bw.flush();
        bw.close();
    }

}
