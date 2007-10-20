package perpetualeclipse.webinterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.mortbay.jetty.Server;

public class FileServerTest extends TestCase {
    private static final String DUMMY_CONTENT = "dummy contents";


    public void testMXMLFileServer() throws Exception {
        // create a file server with a dummmy document
        File docRoot = new File("webinterface/");
        System.out.println(docRoot.getCanonicalPath());
        Server server = WebInterfaceTestUtils.createFileServer(docRoot);
        try {
            assertTrue(docRoot.exists());
            String relativePath = "mxml/Summary.swf";
            assertTrue(new File(docRoot, relativePath).exists());
            
            // retrieve the file from the web server
            String content = WebInterfaceTestUtils.getContentsFromLocalhost(relativePath);

            // chech the contents of the file
            assertFalse(content.length() == 0);

        } finally {
            // stop the server
            server.stop();
        }
    }

    public void testFileServer() throws Exception {
        // create a file server with a dummmy document
        File docRoot = createDocRoot();
        File dummyFile = createDummyFileIn(docRoot);
        Server server = WebInterfaceTestUtils.createFileServer(docRoot);

        try {
            
            // retrieve the file from the web server
            String content = WebInterfaceTestUtils.getContentsFromLocalhost(dummyFile.getName());
    
            // chech the contents of the file
            assertEquals(DUMMY_CONTENT, content);

        } finally {
            // stop the server
            server.stop();
            FileUtils.deleteDirectory(docRoot);
            assertFalse(docRoot.exists());
        }
    }

    private File createDummyFileIn(File docRoot) throws IOException {
        File dummyFile = new File(docRoot, "dummyFile.txt");
        dummyFile.createNewFile();
        FileWriter writer = new FileWriter(dummyFile);
        writer.append(DUMMY_CONTENT);
        writer.close();
        return dummyFile;
    }

    private File createDocRoot() throws IOException {
        File docRoot = File.createTempFile("docRoot", "");
        docRoot.delete();
        docRoot.mkdirs();
        assertTrue(docRoot.isDirectory());
        return docRoot;
    }
}
