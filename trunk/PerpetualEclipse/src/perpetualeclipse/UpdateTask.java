package perpetualeclipse;

import org.eclipse.core.resources.IProject;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import perpetualeclipse.eclipse.Eclipse;
import perpetualeclipse.report.UpdateReport;

public class UpdateTask implements Task {
	private final String projectName;

	UpdateTask(String projectName) {
		this.projectName = projectName;
	}
	
	public UpdateReport execute() throws Exception {
		IProject project = Eclipse.getProject(projectName);

		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		long revisionNumber = updateClient.doUpdate(project.getLocation().toFile(), SVNRevision.HEAD, true);
		return new UpdateReport(projectName);
	}
}
