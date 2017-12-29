package com.maqiao.was.plugin;

import java.awt.Component;
import java.awt.Frame;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class ViewClass extends ViewPart {
	Frame frame = null;

	@Override
	public void createPartControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.EMBEDDED);
		frame = SWT_AWT.new_Frame(comp);
		frame.setBounds(0, 0, 200, 200);
	}

	/**
	 * 重置
	 * @param comp Component
	 */
	public void reset(Component comp) {
		if (frame == null) return;
		frame.setVisible(false);
		frame.removeAll();
		frame.add(comp);
		frame.setVisible(true);
		this.setFocus();
	}

	@SuppressWarnings("unused")
	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
	}

	@Override
	public void setFocus() {

	}
}
