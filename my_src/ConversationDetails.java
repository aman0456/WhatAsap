
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;	
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Servlet implementation class ConversationDetails
 */
@WebServlet("/ConversationDetails")
public class ConversationDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConversationDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		try(Connection conn = DriverManager.getConnection(Login.Aurl, Login.Auser, Login.Apassword)){
			conn.setAutoCommit(false);
			try {
				HttpSession session = request.getSession(false);
				if(session != null) {
					String uid = (String) session.getAttribute("id");
					int thread_id  = Integer.parseInt(request.getParameter("thread_id"));
					PreparedStatement stmt = conn.prepareStatement("select *\n" + 
							"from conversations\n" + 
							"where thread_id = ? and (uid1 = ? or uid2 = ?)");
			        stmt.setInt(1,thread_id);
			        stmt.setString(2,uid);
			        stmt.setString(3,uid);
			        ResultSet rs= stmt.executeQuery();
			        if (rs.isBeforeFirst()) {
			        	
			        	stmt = conn.prepareStatement("select *\n" + 
			        			"from posts\n" + 
			        			"where thread_id = ?\n" + 
			        			"order by posts.timestamp desc");
				        stmt.setInt(1,thread_id);
				        rs= stmt.executeQuery();
				        if(rs.next()) {
				        	PreparedStatement stmt1 = conn.prepareStatement("select \n" + 
				        			"	case \n" + 
				        			"	when	? =  uid1 then lr1\n" + 
				        			"	else lr2\n" + 
				        			"	end as lr\n" + 
				        			"from conversations\n" + 
				        			"where thread_id = ?");
					        stmt1.setString(1,uid);
					        stmt1.setInt(2,  thread_id);
					        ResultSet rs1= stmt1.executeQuery();
					        
					        PreparedStatement stmt2 = conn.prepareStatement("with temp as(\n" + 
					        		"select max(timestamp) as t\n" + 
					        		"from posts\n" + 
					        		"where thread_id = ?)\n" + 
					        		"select * \n" + 
					        		"from posts, temp\n" + 
					        		"where timestamp = temp.t and uid = ?");
					        stmt2.setString(2,uid);
					        stmt2.setInt(1, thread_id);
					        ResultSet rs2= stmt2.executeQuery();
					        if(rs1.next() && !rs2.next()) {
					        	String lr = rs1.getString("lr");
					        	if(!rs1.wasNull()) {
						        	out.println("<head> <style>\n" + 
						        			"    .scroll tbody { display:block; height:200px;  overflow:auto;}\n" + 
						        			"</style>  <script>\n" + 
						        			"\n" + 
						        			"    window.onload = function() {\n" + 
						        			"       document.getElementById(\""+ lr +"\").scrollIntoView();\n" + 
						        			"    };\n" + 
						        			"</script>\n" + 
						        			"\n" + 
						        			"</head>");
					        	}
					        	else {
					        		out.println("<head> <style>\n" + 
						        			"    .scroll tbody { display:block; height:200px;  overflow:auto;}\n" + 
						        			"</style>  <script>\n" + 
						        			"\n" + 
						        			"    window.onload = function() {\n" + 
						        			"       window.scrollTo(0,document.body.scrollHeight);\n" + 
						        			"    };\n" + 
						        			"</script>\n" + 
						        			"\n" + 
						        			"</head>");
					        	}
					        }
					        
					        rs= stmt.executeQuery();
				        }
			        	out.println("<html> <form action=\"NewMessage\" method=\"post\">\n" + 
					    		"   message <input type=\"text\" name = \"message\"> \n" + 
					    		"   <input type=\"hidden\" id=\"thread_id\" name=\"thread_id\" value=" + thread_id + ">"
					    		+ "<input type=\"submit\" value = \"Send\">\n" + 
					    		"</form>");
			        	out.println("<br><a href=\"Home\"> Go to home </a><br>");
			        	ResultSetMetaData rsmd = rs.getMetaData();
				    	out.println("<div class=\"scroll\">");
				    	int count = 0;
				    	int post_id=0;
				    	while(rs.next()) {
				    		if(count == 0) {
				    			post_id = rs.getInt("post_id");
				    		}
				    		count++;
				    		if(uid.equals(rs.getString("uid"))){
				    			out.print("<p id = "+ rs.getString("post_id")+ " style=\"background-color:#95f57b;text-align:right;\">");
				    			out.print(rs.getString("text"));
				        		out.print("</p> ");
				        		out.print("<p style=\"color:90918f;text-align:right;font-size:50%;margin-top:.2em;\">");
				        		out.print(rs.getString("timestamp"));
				        		out.print("</p>");
				    		}
				    		else {
				    			out.print("<p id = "+ rs.getString("post_id")+ " style=\"background-color:#e3e2e2;text-align:left;\">");
				    			out.print(rs.getString("text"));
				        		out.print("</p> ");
				        		out.print("<p style=\"color:90918f;text-align:left;font-size:50%;margin-top: 0.2em;\">");
				        		out.print(rs.getString("timestamp"));
				        		out.print("</p>");
				    		}
			        		
				    	}
				    	out.println("</div>");
				    	
				    	stmt = conn.prepareStatement("update conversations\n" + 
				    			"set lr1 = case when uid1= ? then ? else lr1 end,\n" + 
				    			"lr2 = case when uid2 = ? then ? else lr2 end \n" + 
				    			"where thread_id = ?;");
				    	
				    	stmt.setString(1, uid);
				    	stmt.setInt(2, post_id);
				    	stmt.setString(3, uid);
				    	stmt.setInt(4, post_id);
				    	stmt.setInt(5, thread_id);
				    	stmt.executeUpdate();
			        }
			        else {
			        	out.println("<p>You are not a part of this conversation</p>");
			        }
			        
				}
				else {
					 response.sendRedirect("Login");
				}
				conn.commit();
			}
			catch(Exception ex) {
				conn.rollback();
				ex.printStackTrace();
			}
			finally {
				conn.setAutoCommit(true);
			}
		}
		catch (Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	    
	}
	public static void toHTML(ResultSet rs, PrintWriter out) {
    	try {
    		
	    	out.print("<table>\n");
	    	ResultSetMetaData rsmd = rs.getMetaData();
	    	
	    	out.print("	<tr> ");
	    	for(int i=1; i<=rsmd.getColumnCount();i++) {
	    		out.print("<th>");
	    		out.print(rsmd.getColumnName(i));
	    		out.print("</th> ");
	    	}
	    	out.print("</tr>\n");
	    	
	    	while(rs.next()) {
	    		out.print("	<tr> ");
	    		for(int i=1; i<=rsmd.getColumnCount();i++) {
	        		out.print("<td>");
	        		out.print(rs.getString(i));
	        		out.print("</td> ");
	        	}
	    		out.print("</tr>\n");
	    	}
	    	out.print("</table>\n");
    	}
    	catch (Exception e) {
            e.printStackTrace();
        }
    }
}
