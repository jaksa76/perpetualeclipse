package perpetualeclipse.webinterface;


import static perpetualeclipse.webinterface.WebInterfaceTestUtils.getContentAsString;
import static perpetualeclipse.webinterface.WebInterfaceTestUtils.sameOnWebServerAndFileSystem;
import static perpetualeclipse.webinterface.WebInterfaceTestUtils.urlOnLocalhost;

import java.util.Map;

import junit.framework.TestCase;
import perpetualeclipse.BuildManager;
import perpetualeclipse.report.BuildReport;
import perpetualeclipse.tasks.Build;



public class WebInterfaceTest extends TestCase {
    private WebInterface webInterface;
    private static final String XML_CONTENT = "<xml />";
    private static final String FAKE_BUILD_NAME = "fake-build";
    private BuildManager buildManager = BuildManager.getInstance();


    @Override protected void setUp() throws Exception {
        // start web interface
        this.webInterface = new WebInterface();
    }


    public void testRegisteringContentProviders() throws Exception {
        String path = "test/dummyContentProvider";
        final String content = "some content";
        this.webInterface.addContentProvider(path, new ContentProvider() {
            public String invoke(Map parameters) {
                return content;
            }
        });
        
        assertEquals(content, getContentAsString(WebInterfaceTestUtils.urlOnLocalhost(path)));
    }
    
    public void testGettingFlexFiles() throws Exception {
        String[] swfFileNames = new String[] { "Summary.swf", "BuildReport.swf" };

        for (String fileName : swfFileNames) {
            String relativePath = "mxml/" + fileName;
            assertTrue(sameOnWebServerAndFileSystem(relativePath));
        }
    }


    public void testGettingSummary() throws Exception {
        // set up fake summary
        FakeBuild fakeBuild = new FakeBuild();
        buildManager.addBuild(fakeBuild);
        
        // get the XML format through the web interface
        String content = getContentAsString(urlOnLocalhost(WebInterface.SUMMARY_CONTEXT));

        // assert that is has the right contents
        assertEquals(buildManager.getOverview().toXML(), content);
        
        // tear down
        buildManager.removeBuild(fakeBuild);
    }


    public void testGettingBuildReport() throws Exception {
        // set up a fake build report
        FakeBuild fakeBuild = new FakeBuild();
        buildManager.addBuild(fakeBuild);

        // get the XML format through the web interface
        String content = getContentAsString(urlOnLocalhost(WebInterface.BUILD_REPORTS_CONTEXT));

        // assert that is has the right contents
        assertEquals(fakeBuild.getLatestReport().toXML(), content);

        // tear down
        buildManager.removeBuild(fakeBuild);
    }


    @Override protected void tearDown() throws Exception {
        webInterface.stop();
    }
    

    private class FakeBuild extends Build {

        FakeBuild() {
            super(FAKE_BUILD_NAME);
        }

        @Override public BuildReport getLatestReport() {
            return new FakeReport();
        }
    }
        

    private class FakeReport extends BuildReport {
        @Override public String toXML() {
            return XML_CONTENT;
        }
    }
}
