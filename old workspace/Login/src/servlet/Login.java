package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String user = request.getParameter("user");
		
		if("chkName".equals(type)) {
			if("wnokkae".equals(user))
				send(response, user, "wnokkae.gif");
			else send(response, user, "");
		}
		else if("login".equals(type)) {
			String password = request.getParameter("password");
			
			if("wnokkae".equals(user) && "123".equals(password))
				send(response, "authorized", user);
			else send(response, "authorized", "f");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	private void send(HttpServletResponse response, String name, String value) throws IOException {
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        writer.write("<items>");
        writer.write("<item name=\"" + name + "\" value=\"" + value + "\"></item>");
		writer.write("</items>");
        writer.flush();
        writer.close();
	
	}
}
