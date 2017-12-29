package com.maqiao.was.plugin;

import java.io.File;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings({ "unused" })
public class JavaReflected3DActionMethod implements IObjectActionDelegate, IActionDelegate, IWorkbenchWindowActionDelegate {
	IStructuredSelection selected;

	Shell shell = Display.getDefault().getActiveShell();

	//long eclipseStartTime = Long.parseLong(System.getProperty("eclipse.startTime"));
	//long costTime = System.currentTimeMillis() - eclipseStartTime;

	@Override
	public void run(IAction action) {
		Object element = selected.getFirstElement();
		IProject iproject = Utils.getCurrentProject();
		if (element == null) return;
		//File f = Utils.getCurrentFile();
		//System.out.println("file:" + Utils.readFile(f).toString());
		Object element1 = selected.getFirstElement();
		if (!(element1 instanceof IJavaElement)) return;
		IJavaElement iJavaElement = (IJavaElement) element1;
		boolean isMainClassElement=Utils.isMasterClassElement(iJavaElement);
		if(!isMainClassElement) {
			
		}
		System.out.println("isMainClassElement:"+isMainClassElement);
		IMethod method = (IMethod) element1;
		VisitorClass vc = new VisitorClass();
		System.out.println("=================================================");
		try {
			IJavaElement childClass = method.getParent();
			System.out.println("childClass getElementName:" + childClass.getElementName());
			if (childClass instanceof IType) {
				IType childclassType = (IType) childClass;
				IJavaElement[] arrs = childclassType.getChildren();
				/*
				 * 此类的所有属性
				 */
				IField[] iFieldArr = childclassType.getFields();
				for (int i = 0; i < iFieldArr.length; i++) {
					System.out.println("childclassType IField[" + i + "]:" + iFieldArr[i].getTypeSignature());
					System.out.println("childclassType IField[" + i + "]:" + iFieldArr[i].getSource());
					System.out.println("childclassType IField[" + i + "]:" + iFieldArr[i].getNameRange());
				}
				/*
				 * 此类的所有方法
				 */
				IMethod[] methodArr = childclassType.getMethods();
				for (int i = 0; i < methodArr.length; i++) {
					IMethod iMethod = methodArr[i];
					System.out.println("IMethod[" + i + "]名称:" + iMethod.getElementName());
					System.out.println("IMethod[" + i + "]内容:" + iMethod.getSource());
					IJavaElement[] childrenArr = iMethod.getChildren();
					for (int ii = 0; ii < childrenArr.length; ii++) {
						IJavaElement ar = childrenArr[ii];
						System.out.println("arr11ar[" + ii + "]:" + ar.getElementType());
						System.out.println("arr11ar[" + ii + "]:" + ar.getElementName());
					}
					char[] methodSource = iMethod.getSource().toCharArray();
					CompilationUnit comp = Utils.getCompilationUnit(methodSource);
					AST ast = comp.getAST();
					ASTNode astNode = (ASTNode) comp;
					MethodDeclaration me = Utils.getMethodDeclaration(methodArr[i], astNode);
					if (me == null) continue;
					System.out.println("me:" + me.toString());
					System.out.println("me:" + me.getStartPosition());
					System.out.println("me:" + me.getName());
					comp.accept(vc);
				}
				IType[] typesArr = childclassType.getTypes();
				for (int i = 0; i < typesArr.length; i++) {
					IType e = typesArr[i];
					System.out.println("Type[" + i + "]:" + e.getElementName());
					System.out.println("Type[" + i + "]:" + e.getElementType());
					System.out.println("Type[" + i + "]:" + e.getKey());
					//e.delete(false,null);
				}
				MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Java 源代码", childclassType.getSource());
				System.out.println("==========================================================================");
				char[] source = childclassType.getSource().toCharArray();
				CompilationUnit comp = Utils.getCompilationUnit(source);
				AST ast = comp.getAST();
				comp.accept(vc);
				System.out.println("==========================================================================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		selected = (IStructuredSelection) selection;

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		//String path = Activator.getDefault().getStateLocation().makeAbsolute().toFile().getAbsolutePath();
		//System.out.println(path);

		//String bundlePath = Activator.getDefault().getBundle().getLocation();
		//String pathBundle = bundlePath.replace("reference:file:/", "");
		//System.out.println("pathBundle:" + pathBundle);
		shell = targetPart.getSite().getShell();

	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow window) {

	}

	public JavaReflected3DActionMethod() {
		super();
	}

}
