package com.maqiao.was.plugin;

//import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
//import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.resources.ProjectExplorer;
//import org.eclipse.ui.views.IViewDescriptor;
/**
 * 
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 *
 */
public class PerspectivesAction implements IPerspectiveFactory {
	static final String myview="com.maqiao.was.plugin.viewClass";
	@Override
	public void createInitialLayout(IPageLayout layout) {

		String editorArea = layout.getEditorArea();

		layout.setEditorAreaVisible(false);//编辑器隐藏
		layout.setFixed(true);
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.20f, editorArea);  
		//layout.addView("org.eclipse.jdt.ui.PackageExplorer", IPageLayout.LEFT, 0.2f, editorArea);

		//      layout.addView("org.eclipse.ui.views.ContentOutline", IPageLayout.RIGHT, 0.7f, editorArea);  
		layout.addView(myview, IPageLayout.RIGHT, 0.8f, editorArea);
		//IFolderLayout top = layout.createFolder("top", IPageLayout.TOP, 0.65f, editorArea);  
		//top.addView("ee.actions.perspectives.view.Mview");  

		//IFolderLayout folder = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.3f, editorArea);
		//folder.addView("org.eclipse.ui.console.ConsoleView");
		//folder.addView("org.eclipse.ui.views.ContentOutline");
		//layout.addView("org.eclipse.ui.console.ConsoleView",IPageLayout.BOTTOM, 0.5f, editorArea);
		//layout.addNewWizardShortcut(BasicNewProjectResourceWizard.WIZARD_ID); 
		//layout.getViewLayout("org.eclipse.ui.console.ConsoleView").setCloseable(false);
		//layout.getViewLayout("org.eclipse.ui.console.ConsoleView").s
		layout.getViewLayout(IPageLayout.ID_PROJECT_EXPLORER).setCloseable(false);
		layout.getViewLayout(IPageLayout.ID_PROJECT_EXPLORER).setMoveable(false);
		layout.getViewLayout(myview).setCloseable(false);
		layout.getViewLayout(myview).setMoveable(false);
		layout.addShowViewShortcut(ProjectExplorer.VIEW_ID);
	}

}
