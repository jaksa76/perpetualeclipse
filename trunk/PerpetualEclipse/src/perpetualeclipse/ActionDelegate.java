package perpetualeclipse;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import perpetualeclipse.tasks.TestTask;

/**
 * Just used for testing purposes when launched in interactive mode.
 * 
 * @author Jaksa
 */
public class ActionDelegate implements IWorkbenchWindowActionDelegate {

	private Shell shell;


	public void init(IWorkbenchWindow window) {
		shell = PerpetualPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}


	public void run(IAction action) {
		try {
			new TestTask("test").execute();
		} catch (Exception e) {
			say(e.getMessage());
		}
	}
	
	private void say(int n) {
		say(String.valueOf(n));
	}

	private void say(String message) {
		MessageDialog.openInformation(shell, "info", message);
	}


	public void selectionChanged(IAction action, ISelection selection) {}
	public void dispose() {}
}
