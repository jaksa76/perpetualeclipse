package perpetualeclipse;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import perpetualeclipse.report.CompileReport;
import perpetualeclipse.report.TestReport;

public class PerpetualTest extends TestCase {

	private IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();

	public void testCompileProject() throws Exception {
		// prepare the project
		String projectName = "p1";
		IProject project = workspace.getProject(projectName);
		deleteOutputClasses(project);

		// compile it 
		CompileReport report = new CompileTask(projectName).execute();
		
		// check everything is ok
		assertTrue(project.getFile("bin/First.class").exists());
		assertEquals(projectName, report.getProjectName());
		assertEquals(0, report.getNumberOfErrors());
	}

	public void testCompileProjectWithError() throws Exception {
		// prepare the project
		String projectName = "p2";
		IProject project = workspace.getProject(projectName);
		deleteOutputClasses(project);

		// compile it 
		CompileReport report = new CompileTask(projectName).execute();
		
		assertEquals(projectName, report.getProjectName());
		assertEquals("Unexpected number of errors:\n" + report.toHTML(), 3, report.getNumberOfErrors());
	}

	public void testExportFeature() throws Exception {
		// prepare export directory
		File exportDir = cleanDir("exported-feature");
		deleteOutputClasses("dummy.feature"); // also delete the output classes
		
		new ExportFeatureTask("dummy.feature", exportDir.getAbsolutePath()).execute();	
		
		File featuresDir = assertExists(exportDir, "features");
		File dummyFeatureJar = assertExists(featuresDir, "dummy.feature_1.0.0.jar");
	}

	public void testExportPlugin() throws Exception {
		// prepare export directory
		File exportDir = new File("exported-plugin");
		cleanDir(exportDir.getAbsolutePath());
		
		new ExportPluginTask("dummy.plugin", exportDir.getAbsolutePath()).execute();
		
		File pluginsDir = assertExists(exportDir, "plugins");
		File dummyPluginJar = assertExists(pluginsDir, "dummy.plugin_1.0.0.jar");
	}

	public void testExportProduct() throws Exception {
		// prepare export directory
		File dir = cleanDir("exported-product");
		deleteOutputClasses("dummy.plugin"); // also delete the output classes
		
		new ExportProductTask("dummy.plugin/dummy.product", dir.getAbsolutePath()).execute();	
		
		assertExists(dir, "Dummy Product.zip");
	}
	
	public void testTestTask() throws Exception {
		String configurationName = "test";

		TestReport report = new TestTask(configurationName).execute();
		
		assertEquals(configurationName, report.getConfigurationName());
		assertEquals(2, report.getNumberOfTests());
		assertEquals(1, report.getNumberOfFailures());
	}

	public void testUpdateProject() {
		fail("Not yet implemented");
	}

	/**
	 * Assert that filename exists in the specified directory. If true return the file.
	 * @param dir
	 * @param fileName
	 * @return
	 */
	private File assertExists(File dir, String fileName) {
		File file = new File(dir, fileName);
		assertTrue(fileName + " does not exist." +
				" These are the contents of " + dir.getAbsolutePath() + ":\n" + 
				Arrays.toString(dir.list()), 
				file.exists());
		return file;
	}

	private File cleanDir(String exportDir) throws IOException {
		File dir = new File(exportDir);
		if (dir.exists()) {
			FileUtils.cleanDirectory(dir);
			assertTrue(dir.list().length == 0);
		}
		return dir;
	}

	private void deleteOutputClasses(IProject project) throws Exception {
		File folder = project.getFolder("bin").getLocation().toFile();
		cleanDir(folder.getAbsolutePath());
	}

	private void deleteOutputClasses(String projectName) throws Exception {
		deleteOutputClasses(workspace.getProject(projectName));
	}
}
