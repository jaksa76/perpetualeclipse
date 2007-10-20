package perpetualeclipse.webinterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.ResourceHandler;


public class WebInterface {
    static final String FLEX_CONFIGURATION_FILE = "/crossdomain.xml";
    static final String BUILD_REPORTS_CONTEXT = "build";
    static final String SUMMARY_CONTEXT = "summary";
    public static final String FILE_ROOT_DIR = "webinterface"; 
    public static final File resourcesDir = new File(FILE_ROOT_DIR);
    
    /**
     * Forwards the request to the ContentProvider registered with this target.
     * 
     * @author Jaksa
     */
    private final class MappingHandler extends AbstractHandler {
        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) 
        throws IOException, ServletException {
            System.out.println("Handling request for " + target);

            ContentProvider contentProvider = mappings.get(target);

            String content;
            if (contentProvider == null) content = "<html><body>Page not found</body></html>"; 
            else content = contentProvider.invoke(request.getParameterMap());
            response.getWriter().append(content);
            response.setContentType("text/xml");
            response.setStatus(HttpServletResponse.SC_OK);
            ((Request)request).setHandled(true);
        }
    }

    private Server server;
    private Map<String, ContentProvider> mappings = new HashMap<String, ContentProvider>();

    /**
     * Start the perpetual eclipse web interface on port 8080. 
     * 
     * @throws Exception
     */
    public WebInterface() throws Exception {
        this(8080);
    }

    /**
     * Start the perpetual eclipse web interface on the specified port.
     * 
     * @param port
     * @throws Exception
     */
    public WebInterface(int port) throws Exception {
        Handler mappingsHandler = new MappingHandler();
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(resourcesDir.getAbsolutePath());
        
        server = new Server(port);

        server.setHandlers(new Handler[] { resourceHandler, mappingsHandler });
        
        addContentProvider(SUMMARY_CONTEXT, new BuildSummaryContentProvider());
        addContentProvider(BUILD_REPORTS_CONTEXT, new BuildReportContentProvider());
        addContentProvider("/dummy", new DummyXMLContentProvider());
        addContentProvider(FLEX_CONFIGURATION_FILE, new FlexSecurityConfigurationContentProvider());

        server.start();
        System.out.println("WebInterface.WebInterface()");
    }

    public void addContentProvider(String target, ContentProvider provider) {
        if (!target.startsWith("/")) target = "/" + target;
        this.mappings.put(target, provider);
    }

    public void stop() throws Exception {
        server.stop();
    }
}
