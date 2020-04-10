package fer.zavrsni.src.analysis;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class ProjectAnalysis {

    private Project project;
    private List<String> tools;

    public ProjectAnalysis(@NotNull Project project, List<String> tools) {
        this.project = project;
        this.tools = tools;
    }

    public void executeAnalysis() throws IOException {
        StringBuilder toolsArg = new StringBuilder();
        tools.forEach(toolsArg::append);

        String[] args = new String[] {"/bin/bash", "-c", "python3", toolsArg.toString(), "args"};
        Process proc = new ProcessBuilder(args).start();


    }

    public Project getProject() {
        return project;
    }
}
