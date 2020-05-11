package fer.zavrsni.src.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a factory class for {@link ToolWindow} objects, as specified by {@code plugin.xml}
 */
public class ToolsWindowFactory implements ToolWindowFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolsWindow toolsWindow = new ToolsWindow(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolsWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(@NotNull ToolWindow window) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

}