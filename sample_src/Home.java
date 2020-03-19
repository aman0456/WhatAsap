
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Home
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String html = "<html><head><title>WhatASap</title>" + 
				"    <script src=\"jquery-3.3.1.js\"> </script>" + 
				"    <script src=\"jquery.dataTables.min.js\"></script>" + 
				"    <script src=\"jquery-ui.min.js\"></script>" + 

				"    <link rel=\"stylesheet\" href=\"jquery-ui.css\" />" + 
				"    <link rel=\"stylesheet\" href=\"jquery.dataTables.min.css\"/>" + 
				
				"	 <script src=\"whatasap_home.js\"></script>" +
				"</head>" + 
				"<body>" + 
				"    <button onclick=\"onLoad1()\">Home</button><br><br>" +
				"<input type=\"text\" id= \"search\" placeholder=\"Search..\"><br><br>" +
				"<a href=\"#content\" id=\"create\">Create Conversation</a><br><br>" +
				"    <div id=\"content\">" +
				"	 </div> <br><br>" + 
//				"    <table id=\"usersTable\" class=\"display\">" + 
//				"        <thead>" + 
//				"        <tr> <th>User ID</th> <th>Timestamp</th> <th>Number of messages</th> </tr>" + 
//				"        </thead>" + 
//				"    </table>" + 
				"</body>" + 
				"</html>";
		
		response.setContentType("text/html");
		response.getWriter().print(html);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
