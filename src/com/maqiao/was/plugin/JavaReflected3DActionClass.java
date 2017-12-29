package com.maqiao.was.plugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
// import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
// import org.eclipse.ui.IWorkbenchPage;
// import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.maqiao.was.d3.MQMaster;

// import com.sun.j3d.utils.applet.MainFrame;
/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class JavaReflected3DActionClass extends AbstractHandler implements IObjectActionDelegate, IActionDelegate, IWorkbenchWindowActionDelegate {
	IStructuredSelection selected;

	Shell shell = Display.getDefault().getActiveShell();

	@Override
	public void run(IAction action) {
		Object element = selected.getFirstElement();
		run(element);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow window) {

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		selected = (IStructuredSelection) selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public JavaReflected3DActionClass() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//IWorkbench workbench = PlatformUI.getWorkbench();
		//IWorkbenchWindow window = workbench == null ? null : workbench.getActiveWorkbenchWindow();
		//IWorkbenchPage activePage = window == null ? null : window.getActivePage();
		Object obj = Utils.getCurrentObject();
		run(obj);
		//System.out.println("activePage:"+activePage.getLabel());
		return null;
	}

	void run(Object element) {
		if (element == null) {
			Utils.showMessage("请选中类Class!!");
			return;
		}
		if (!(element instanceof IJavaElement)) {
			Utils.showMessage("不是类Class!!");
			return;
		}
		IJavaElement iJavaElement = (IJavaElement) element;
		run(iJavaElement);
	}

	void run(IJavaElement iJavaElement) {
		boolean isClass = Utils.isClass(iJavaElement);
		if (!isClass) {
			Utils.showMessage("不是类Class!!");
			return;
		}
		IType classType = (IType) iJavaElement;
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			PlatformUI.getWorkbench().showPerspective("PerspectivesAction", window);
			IViewPart viewPart = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(PerspectivesAction.myview);
			ViewClass p = (ViewClass) viewPart;
			p.reset(MQMaster.getJApplet(classType));
			viewPart.setFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
