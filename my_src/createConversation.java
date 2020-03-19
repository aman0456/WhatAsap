
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


// rollback might be required



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class createConversation
 */
@WebServlet("/createConversation")
public class createConversation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createConversation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session != null) {
			String uid = (String) session.getAttribute("id");
			String usid = (String) request.getParameter("usid");
			try (
				    Connection conn = DriverManager.getConnection(
				    		Login.Aurl, Login.Auser, Login.Apassword);
				    Statement stmt = conn.createStatement();
				){
				conn.setAutoCommit(false);
				try{
					PreparedStatement stmt4 = conn.prepareStatement("select uid from users where uid = ?");
					stmt4.setString(1,  usid);
					ResultSet rs1 = stmt4.executeQuery();
					if (rs1.next()) {
						PreparedStatement stmt3 = conn.prepareStatement("select * from conversations where (uid1 = ? and uid2 = ?) or (uid1 = ? and uid2 = ?)");
					    stmt3.setString(1, uid);
					    stmt3.setString(2, usid);
					    stmt3.setString(4, uid);
					    stmt3.setString(3, usid);
					    ResultSet rs = stmt3.executeQuery();
						if (!rs.next()) {
							stmt3 = conn.prepareStatement("insert into conversations values(?, ?)");
						    //System.out.println("here");
						    //System.out.println(usid + uid);
						    
							if (usid.compareTo(uid) > 0) {
								//System.out.println("here1");
								stmt3.setString(1, uid);
							    stmt3.setString(2, usid);
							    stmt3.executeUpdate();
							    out.println("<html><body>Conversation created<br>\n");
								
							    //System.out.println("here1");
						    }
						    else if (usid.compareTo(uid) < 0) {
						    	//System.out.println("here2");
						    	stmt3.setString(2, uid);
							    stmt3.setString(1, usid);
							    stmt3.executeUpdate();
							    out.println("<html><body>Conversation created<br>\n");
								
							    //System.out.println("here2");
						    }
						    else {
						    	out.println("<html><body>You can't create a conversation with yourself");
						    	//System.out.println("here3");
						    }
							
						}
						else {
							out.println("<html><body>Conversation already present\n");
						}
					}
					else {
						out.println("<html><body>No such user present\n");
					}
				    out.println("<br><a href=\"Home\"> Go to home </a>");
				    conn.commit();
				}
				catch (Exception sqle) {
					sqle.printStackTrace();
					conn.rollback();
				}
				finally {
					conn.setAutoCommit(true);
				}
			}
			catch (Exception sqle)
			{
				System.out.println("Exception : " + sqle);
			}
			//response.getWriter().append("Served at: ").append(request.getContextPath());
		}
		else {
			 response.sendRedirect("Login");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}