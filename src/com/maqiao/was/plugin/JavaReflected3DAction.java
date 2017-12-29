package com.maqiao.was.plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.swing.tree.TreePath;

import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.internal.PluginAction;
import org.eclipse.ui.internal.Workbench;

@SuppressWarnings({ "unused", "restriction" })
public class JavaReflected3DAction implements IObjectActionDelegate, IActionDelegate, IWorkbenchWindowActionDelegate {
	IStructuredSelection selected;

	private Shell shell;

	public JavaReflected3DAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		Object element = selected.getFirstElement();
		IProject iproject = Utils.getCurrentProject();
		if (element == null) 
			return;
		//File f = Utils.getCurrentFile();
		//System.out.println("file:" + Utils.readFile(f).toString());
		Object element1 = selected.getFirstElement();
		if (!(element1 instanceof IJavaElement)) return;
		IJavaElement iJavaElement = (IJavaElement) element1;
		File file=iJavaElement.getPath().toFile();
		System.out.println("iproject.getFullPath:" + iproject.getFullPath());
		System.out.println("iproject.getLocation:" + iproject.getLocation());
		System.out.println("iproject.getProjectRelativePath:" + iproject.getProjectRelativePath());
		System.out.println("iproject.getLocationURI:" + iproject.getLocationURI());
		System.out.println("iJavaElement.getPath(:" + iJavaElement.getPath());
		try {
			System.out.println("iJavaElement.getAllPackageFragmentRoots(:" + iJavaElement.getJavaProject().getAllPackageFragmentRoots());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("file.getAbsolutePath():" + file.getAbsolutePath());
		System.out.println("file.getPath():" + file.getPath());
		System.out.println("file.getName():" + file.getName());
		System.out.println("file:" + Utils.readFile(file).toString());

		IMethod method=(IMethod)element1;
		try {
			System.out.println("method:"+method.getSource());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (action instanceof PluginAction) {
			PluginAction opAction = (PluginAction) action;
			ISelection selection = opAction.getSelection();
			System.out.println("aaaa");

		}
		//String path = ((IFile) element).getLocation().makeAbsolute().toFile().getAbsolutePath();

		//System.out.println("path:" + path);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		selected = (IStructuredSelection) selection;

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		String path = Activator.getDefault().getStateLocation().makeAbsolute().toFile().getAbsolutePath();
		System.out.println(path);

		String bundlePath = Activator.getDefault().getBundle().getLocation();
		String pathBundle = bundlePath.replace("reference:file:/", "");
		System.out.println(pathBundle);

		shell = targetPart.getSite().getShell();

	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow window) {

	}
}
