package perpetualeclipse;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModelMarker;

import perpetualeclipse.eclipse.Eclipse;
import report.CompileReport;

public class Compile {

	public CompileReport compileProject(String projectName) throws CoreException {
		IProject project = Eclipse.getProject(projectName);
		CompileReport buildReport = new CompileReport(project.getName());
	
		System.out.println("building " + project.getName());
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);
	
		IMarker[] markers = project.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
		System.out.println("found " + markers.length + " problems");
	
		for (IMarker marker : markers) {
			if (((Integer)marker.getAttribute("severity")) > 1) {
				String errorMessage = (String) marker.getAttribute("message");
				buildReport.addError(errorMessage);
				System.out.println(errorMessage);
			}
		}
	
		return buildReport;
	}

}
