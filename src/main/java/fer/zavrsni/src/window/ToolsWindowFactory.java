package fer.zavrsni.src.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class ToolsWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolsWindow toolsWindow = new ToolsWindow(project, toolWindow);
        JComponent parent = toolWindow.getComponent();
        parent.add(toolsWindow.getContent());
        parent.setVisible(true);
        parent.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
    }

    @Override
    public void init(ToolWindow window) {

    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    @Deprecated
    public boolean isDoNotActivateOnStart() {
        return false;
    }

}