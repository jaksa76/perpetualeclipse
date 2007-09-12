package perpetual.eclipse.webinterface;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import perpetualeclipse.History;


public class WebInterface {
	private Server server;

	public WebInterface() throws Exception {
		this(8080);
	}
		
	public WebInterface(int port) throws Exception {
		Handler handler = new AbstractHandler() {
		    private History history = History.getInstance();

			public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) 
		        throws IOException, ServletException {
		        response.setContentType("text/html");
		        response.setStatus(HttpServletResponse.SC_OK);
		        response.getWriter().println("<html><body>");
				response.getWriter().println(history.getLatestReport().toHTML());
		        response.getWriter().println("</body></html>");
		        ((Request)request).setHandled(true);
		    }
		};

		server = new Server(port);
		server.setHandler(handler);
		server.start();
		System.out.println("WebInterface.WebInterface()");
	}
	
	public void stop() throws Exception {
		server.stop();
	}
}
