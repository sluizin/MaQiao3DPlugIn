package com.maqiao.was.plugin;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

@SuppressWarnings({ "restriction", "unused" })
public class Utils {
	/**
	 * 得到当前工程
	 * @return IProject
	 */
	public static IProject getCurrentProject() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		IProject project = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element).getJavaProject();
				project = jProject.getProject();
			} else if (element instanceof ICElement) {
				ICProject cProject = ((ICElement) element).getCProject();
				project = cProject.getProject();
			}
		}
		return project;
	}
	/**
	 * 得到当前选中的文件
	 * @return Object
	 */
	public static Object getCurrentObject() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		if (!(selection instanceof IStructuredSelection))return null;
		Object element = ((IStructuredSelection) selection).getFirstElement();
		return element;
	}

	/**
	 * 得到当前文件
	 * @return java.io.File
	 */
	public static java.io.File getCurrentFile() {
		ISelectionService istructured = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		IStructuredSelection structured = (IStructuredSelection) istructured.getSelection("org.eclipse.jdt.ui.PackageExplorer");
		Object selected = structured.getFirstElement();
		java.io.File f = null;
		if (selected == null) System.out.println("selected:null");
		if (selected instanceof File) {
			File file = (File) selected;
			f = file.getLocation().toFile();
			System.out.println("f:");
		} else {
			if (selected instanceof PackageFragmentRoot) {
				System.out.println("PackageFragmentRoot:");
				PackageFragmentRoot pfr = (PackageFragmentRoot) selected;
				f = pfr.getResource().getLocation().toFile();
			} else if (selected instanceof PackageFragment) {
				System.out.println("PackageFragment:");
				PackageFragment pf = (PackageFragment) selected;
				f = pf.getResource().getLocation().toFile();
			} else if (selected instanceof Folder) {
				System.out.println("Folder:");
				Folder folder = (Folder) selected;
				f = folder.getLocation().toFile();
			}
		}
		if (f == null) System.out.println("f:null");
		if (f != null) System.out.println("f:" + f.getAbsolutePath());
		return f;
	}

	/**
	 * 当前方法，向上内部类顺序，可以多级下的内部类
	 * @param javaElement
	 * @return
	 */
	public static final String classFileName(IJavaElement javaElement) {
		StringBuilder sb = new StringBuilder();
		IJavaElement p = javaElement.getParent();
		sb.append(p.getElementName());
		for (p = p.getParent(); p.getElementName().indexOf('.') < 0; p = p.getParent()) {
			String ename = p.getElementName();
			System.out.println("ename:" + ename);
			sb.insert(0, ename + "$");
		}
		return sb.toString();
	}

	/**
	 * 查看对象的接口
	 * @param obj Object
	 */
	public static final void showInterfaces(Object obj) {
		if (obj == null) return;
		Class<?>[] arr = obj.getClass().getInterfaces();
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("Object obj:" + obj.toString());
		for (int i = 0; i < arr.length; i++) {
			System.out.println("Interfaces[" + i + "]:" + arr[i].getName());
		}
		System.out.println("+++++++++++++++++++++++++++++++");
	}

	public static final MethodDeclaration getMethodDeclaration(IJavaElement methodElement, ASTNode root) {
		if (methodElement == null) return null;
		IMethod method = (IMethod) methodElement.getAncestor(IJavaElement.METHOD);
		if (method == null) return null;
		try {
			ISourceRange ms = method.getSourceRange();
			ISourceRange mj = method.getJavadocRange();
			method.getJavadocRange();
			if (mj == null || (mj != null && ms.getOffset() == mj.getOffset())) {
				// 消除注释的影响
				return (MethodDeclaration) NodeFinder.perform(root, ms);
			} else {
				return (MethodDeclaration) NodeFinder.perform(root, mj.getOffset(), ms.getLength() - mj.getOffset() + ms.getOffset());
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final IJavaElement getParentFile(IJavaElement javaElement) {
		if (javaElement.getParent().getElementName().indexOf('.') >= 0) return javaElement.getParent();
		return getParentFile(javaElement.getParent());

	}

	public static final CompilationUnit getCompilationUnit(char[] source) {
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setSource(source);
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
		return result;
	}

	public static final ConditionalExpression getConditionalExpression(char[] source) {
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setSource(source);
		astParser.setKind(ASTParser.K_EXPRESSION);
		ConditionalExpression result = (ConditionalExpression) (astParser.createAST(null));
		return result;
	}

	public static final String getJavaFile(String javaFilePath) {
		byte[] input = {};
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(javaFilePath));
			input = new byte[bis.available()];
			bis.read(input);
			bis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(input);
	}

	/**
	 * 提示信息框
	 * @param message String
	 */
	static void showMessage(String message) {
		MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "提示", message);
	}

	/**
	 * 判断是否是class(包含主类与内部类)
	 * @param iJava IJavaElement
	 * @return boolean
	 */
	static boolean isClass(IJavaElement iJava) {
		if(iJava==null)return false;
		return iJava instanceof IType;
	}

	/**
	 * 判断IJavaElement是否是主类中的元素
	 * @param iJava IJavaElement
	 * @return boolean
	 */
	static boolean isMasterClassElement(IJavaElement iJava) {
		IJavaElement e1 = iJava.getParent();
		if (e1 == null) return false;
		if (!(e1 instanceof IType)) return false;
		IJavaElement e2 = e1.getParent();/* 上一级，判断是否是文件名 */
		if (e2 == null) return false;
		if (e2.getElementName().indexOf(".java") <= 0) return false;
		return true;
	}

	/** 锁定时间 */
	private static final int lockTime = 20;

	/**
	 * 读取文件
	 * @param filePath String
	 * @param enterStr String
	 * @return StringBuilder
	 */
	public static final StringBuilder readFile(final String filePath, final String enterStr) {
		java.io.File file = new java.io.File(filePath);
		return readFile(file);

	}

	/**
	 * 通过RandomAccessFile读文件 按行读 randomFile.readLine
	 * @param file java.io.File
	 * @param enterStr
	 * @return StringBuilder
	 */
	public static final StringBuilder readFile(final java.io.File file) {
		return readFile(file, "\n");
	}

	/**
	 * 通过RandomAccessFile读文件 按行读 randomFile.readLine
	 * @param file java.io.File
	 * @param enterStr
	 * @return StringBuilder
	 */
	public static final StringBuilder readFile(final java.io.File file, final String enterStr) {
		StringBuilder sb = new StringBuilder();
		if (file == null) return sb;
		try {
			RandomAccessFile randomFile = new RandomAccessFile(file, "r");
			FileChannel filechannel = randomFile.getChannel();
			//将写文件指针移到文件头。
			randomFile.seek(0);
			FileLock lock;//= fc.tryLock();// lock  
			do {
				lock = filechannel.tryLock(0L, Long.MAX_VALUE, true);
			} while (null == lock);
			if (null != lock) {
				Thread.sleep(lockTime);// 本线程锁定10(lockTime)毫秒。过后任何程序对该文件的写操作将被禁止
				while (randomFile.getFilePointer() < randomFile.length()) {
					sb.append(changedLine(randomFile.readLine()));
					if (randomFile.getFilePointer() < randomFile.length()) sb.append(enterStr);
				}
				lock.release();// lock release
			}
			randomFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR :FileNotFoundException");
		} catch (IOException e) {
			System.out.println("ERROR :IOException");
		} catch (Exception e) {
			System.out.println("ERROR :Exception");
		}
		return sb;
	}

	/**
	 * RandomAccessFile
	 * RandomAccessFile读出时，转换成UTF-8
	 * @param line String
	 * @return String
	 */
	public static final String changedLine(final String line) {
		if (line == null) return null;
		try {
			final byte buf[] = new byte[1];
			final byte[] byteArray = new byte[line.length()];
			final StringReader aStringReader = new StringReader(line);
			int character, i = 0;
			while ((character = aStringReader.read()) != -1) {
				buf[0] = (byte) character;
				byteArray[i++] = buf[0];
			}
			return new String(byteArray, "UTF-8");
		} catch (Exception e) {
		}
		return null;
	}
}
