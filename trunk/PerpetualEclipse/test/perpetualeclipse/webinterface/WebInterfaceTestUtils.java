package perpetualeclipse.webinterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;

public class WebInterfaceTestUtils {

    public static Server createFileServer(File docRoot) throws Exception {
    	// create a file server
    	Server server = new Server(WebInterfaceTestUtils.PORT);
    	ResourceHandler handler = new ResourceHandler();
    
    	// set the root to point to a folder with a file
    	handler.setResourceBase(docRoot.getAbsolutePath());
    
    	HandlerList handlers = new HandlerList();
    	handlers.setHandlers(new Handler[] { handler, new DefaultHandler() });
    	server.setHandler(handlers);
    
    	// start the file server
    	server.start();
    	return server;
    }

    public static String getContentsFromLocalhost(String relativePath) throws MalformedURLException, IOException {
        return WebInterfaceTestUtils.getContentAsString(new URL("http://localhost:" + WebInterfaceTestUtils.PORT + "/" + relativePath));
    }

    public static String getContentAsString(URL url) throws MalformedURLException, IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = reader.readLine();
        while (line != null) {
            s.append(line);
            line = reader.readLine();
        }
        return s.toString();
    }

    static final int PORT = 8080;

    public static URL urlOnLocalhost(String resourcePath) throws MalformedURLException {
        return new URL("http://localhost:" + PORT + "/" + resourcePath);
    }

    public static File fileOnDisk(String filePath) {
        return new File(WebInterface.resourcesDir, filePath);
    }

    public static boolean sameOnWebServerAndFileSystem(String relativePath) throws MalformedURLException,
            IOException, FileNotFoundException {
        URL fileOnServer = urlOnLocalhost(relativePath);
        File fileOnDisk = fileOnDisk(relativePath);
        return IOUtils.contentEquals(fileOnServer.openStream(), new FileInputStream(fileOnDisk));
    }

}
