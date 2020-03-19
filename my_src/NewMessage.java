
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;	
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewMessage
 */
@WebServlet("/NewMessage")
public class NewMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try(Connection conn = DriverManager.getConnection(Login.Aurl, Login.Auser, Login.Apassword)){
			try {
				conn.setAutoCommit(false);
				PrintWriter out = response.getWriter();
				HttpSession session = request.getSession(false);
				if(session != null) {
					String uid = (String) session.getAttribute("id");
					int thread_id  = Integer.parseInt(request.getParameter("thread_id"));
					String text = request.getParameter("message");
					PreparedStatement stmt = conn.prepareStatement("select *\n" + 
							"from conversations\n" + 
							"where thread_id = ? and (uid1 = ? or uid2 = ?)");
			        stmt.setInt(1,thread_id);
			        stmt.setString(2,uid);
			        stmt.setString(3,uid);
			        ResultSet rs= stmt.executeQuery();
			        if (rs.isBeforeFirst()) {
			        	stmt = conn.prepareStatement("insert into posts(thread_id, uid, text, timestamp)"
								+ " values( ?, ?, ?, now())");
				        stmt.setInt(1,thread_id);
				        stmt.setString(2,uid);
				        stmt.setString(3,text);
				        stmt.executeUpdate();
				        conn.commit();
				        response.sendRedirect("ConversationDetails?thread_id="+thread_id);
			        }
			        else {
			        	out.println("<p>You are not a part of this conversation</p>");
			        }
				}
				else {
					 response.sendRedirect("Login");
				}
			}
			catch(Exception ex) {
				conn.rollback();
                throw ex;
			}
			finally {
                conn.setAutoCommit(true);
            }
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		
	}

}
