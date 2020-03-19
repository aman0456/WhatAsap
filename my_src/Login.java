import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static final String Aurl = "jdbc:postgresql://localhost:5432/postgres";
    public static final String Auser = "kunal";
    public static final String Apassword = "12345678";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		PrintWriter out = response.getWriter();
		HttpSession sess = request.getSession(false);
		if (sess == null) {
			if (name != null && pass != null) {
				try (
					    Connection conn = DriverManager.getConnection(
					    		Aurl, Auser, Apassword);
					    Statement stmt = conn.createStatement();
					){
					PreparedStatement stmt3 = conn.prepareStatement("select id, password from password where id = ? and password = ?");
	    		    stmt3.setString(1, name);
	    		    stmt3.setString(2, pass);
					ResultSet rs = stmt3.executeQuery();
					int cnt = 0;
					while(rs.next()) cnt++;
					if (cnt == 1) {
						sess = request.getSession(true);
						sess.setAttribute("id", name);
						//sess.setAttribute("pass", pass);
						response.sendRedirect("Home");
						//out.println("<html><body> Succesfully authenticated</body></html>");
					}
					else {
						out.println("<html>\n" + 
								"<head>\n" + 
								"<meta charset=\"UTF-8\">\n" + 
								"<title>WhatASap</title>\n" + 
								"</head>\n" + 
								"<body>\n"
								+ "<h2> Welcome to WhatASap</h2><br>" + 
								"You entered wrong credentials.<br> Please login with your CORRECT credentials<br>\n" + 
								"<form action=\"Login\" method=\"post\">\n" + 
								"           Enter your login: <input type=\"text\" name = \"name\"><br>\n" + 
								"           Enter your password: <input type=\"text\" name = \"pass\"><br>\n" + 
								"           <input type=\"submit\" value = \"Submit\">\n" + 
								"       </form>\n" + 
								"</body>\n" + 
								"</html>");
					    }
				}
				catch (Exception sqle)
				{
					System.out.println("Exception : " + sqle);
				}
			}
			else {
				out.println("<html>\n" + 
						"<head>\n" + 
						"<meta charset=\"UTF-8\">\n" + 
						"<title>WhatASap</title>\n" + 
						"</head>\n" + 
						"<body>\n"
						+ "<h2> Welcome to WhatASap</h2><br>" + 
						"Please login with your credentials<br>\n" + 
						"<form action=\"Login\" method=\"post\">\n" + 
						"           Enter your login: <input type=\"text\" name = \"name\"><br>\n" + 
						"           Enter your password: <input type=\"text\" name = \"pass\"><br>\n" + 
						"           <input type=\"submit\" value = \"Submit\">\n" + 
						"       </form>\n" + 
						"</body>\n" + 
						"</html>");
			    }
	    }
		else {
			response.sendRedirect("Home");		    }
		//doGet(request, response);
	}

}