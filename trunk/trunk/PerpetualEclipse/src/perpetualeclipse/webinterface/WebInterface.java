package perpetualeclipse.webinterface;

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


public class WebInterface {
	
	
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

	public WebInterface() throws Exception {
		this(8080);
	}
		
	public WebInterface(int port) throws Exception {
		Handler handler = new MappingHandler();

		server = new Server(port);
		server.setHandler(handler);
		server.start();
		System.out.println("WebInterface.WebInterface()");
	}
	
	public void addContentProvider(String target, ContentProvider provider) {
		this.mappings.put(target, provider);
	}

	public void stop() throws Exception {
		server.stop();
	}
}
