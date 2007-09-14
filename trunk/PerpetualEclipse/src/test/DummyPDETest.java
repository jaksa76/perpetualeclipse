package test;

import junit.framework.TestCase;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import perpetualeclipse.PerpetualPlugin;

public class DummyPDETest extends TestCase {
	public void testMessage() throws Exception {
		Shell shell = PerpetualPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openInformation(shell, "info", "message");
	}
}
