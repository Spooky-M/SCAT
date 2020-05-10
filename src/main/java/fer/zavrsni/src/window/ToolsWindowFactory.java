package fer.zavrsni.src.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a factory class for ToolWindow objects, as specified by {@code plugin.xml}
 */
public class ToolsWindowFactory implements ToolWindowFactory {

    /**
     * Class which should add the desired content to toolWindow parameter.
     * It is called by default after initialization.
     * @param project - currently active project in IDE which runs plugin
     * @param toolWindow - ToolWindow object which we are modifying
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolsWindow toolsWindow = new ToolsWindow(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolsWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * Initialization method, called before {@code createToolWindowContent()}
     * @param window - ToolWindow object which we are modifying
     */
    @Override
    public void init(@NotNull ToolWindow window) {

    }

    /**
     * Specifies whether our ToolWindow should be available or completely hidden when the IDE is run.
     * @param project - currently active project in IDE which runs plugin
     * @return true if ToolWindow should be visible, false otherwise
     */
    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

}