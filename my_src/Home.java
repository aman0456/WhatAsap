import java.sql.*;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//String name = request.getParameter("name");
		//String pass = request.getParameter("pass");
		PrintWriter out = response.getWriter();
		HttpSession sess = request.getSession(false);
		//System.out.println(sess);
		if (sess == null) {
			response.sendRedirect("Login");
		}
		else {
			String name = (String) sess.getAttribute("id");
			out.print("<html><body>");
			out.println("<form action=\"createConversation\" method=\"post\">\n" + 
					"           <input type=\"text\" name=\"usid\">" + 
	    			"           <input type=\"submit\" value = \"createConversation\">\n" + 
	    			"       </form>\n" + 
	    			"</body></html>");
			//System.out.println("<html><body> " + name + " </body></html>");
			try (
				    Connection conn = DriverManager.getConnection(
				    		Login.Aurl, Login.Auser, Login.Apassword);
				    Statement stmt = conn.createStatement();
				){
				PreparedStatement stmt3 = conn.prepareStatement("select u.name, f.timestamp, text, tid as thread_id from ((with help(uid, tid) as (select uid1, thread_id from conversations where uid2 = ?), help2(uid, timestamp, tid) as ( select help.uid, max(timestamp),  help.tid from help left outer join posts on (posts.thread_id = help.tid) group by help.uid, help.tid) select help2.uid, help2.timestamp, posts.text, help2.tid from help2 left outer join posts on (help2.tid = posts.thread_id and help2.timestamp = posts.timestamp)) union (with help(uid, tid) as (select uid2, thread_id from conversations where uid1 = ?), help2(uid, timestamp, tid) as ( select help.uid, max(timestamp),  help.tid from help left outer join posts on (posts.thread_id = help.tid) group by help.uid, help.tid) select help2.uid, help2.timestamp, posts.text, help2.tid from help2 left outer join posts on (help2.tid = posts.thread_id and help2.timestamp = posts.timestamp))) as f, users u where f.uid = u.uid order by case when timestamp is null then 1 else 0 end, timestamp desc;\n" + 
						"");
			    stmt3.setString(1, name);
			    stmt3.setString(2, name);
			    ResultSet rs = stmt3.executeQuery();
				
			    toHTML(out, rs);
//			    stmt3 = conn.prepareStatement("");
//			    stmt3.setString(1, name);
//			    stmt3.setString(2, name);
//			    rs = stmt3.executeQuery();
//				
//			    toHTML(out, rs);
			    out.println("<form action=\"Logout\" method=\"post\">\n" + 
		    			"           <input type=\"submit\" value = \"logout\">\n" + 
		    			"       </form>\n" + 
		    			"</body>");
			}
			catch (Exception sqle)
			{
				System.out.println("Exception : " + sqle);
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public static void toHTML(PrintWriter out, ResultSet rs) {
    	try {
    		
	    	out.print("<table cellpadding=\"15\">\n");
	    	ResultSetMetaData rsmd = rs.getMetaData();
	    	
	    	out.print("	<tr> ");
	    	for(int i=1; i<=rsmd.getColumnCount() - 1;i++) {
	    		out.print("<th>");
	    		out.print(rsmd.getColumnName(i));
	    		out.print("</th> ");
	    	}
	    	out.print("<th>");
    		out.print("Link to Conversation");
    		out.print("</th> ");
	    	out.print("</tr>\n");
	    	
	    	while(rs.next()) {
	    		out.print("	<tr> ");
	    		for(int i=1; i<=rsmd.getColumnCount() - 1;i++) {
	        		out.print("<td>");
	        		out.print(rs.getString(i));
	        		out.print("</td> ");
	        	}
	    		out.print("<td>");
        		out.print("<a href=\"ConversationDetails?thread_id=" + rs.getString(rsmd.getColumnCount()) + "\"> ConversationDetails </a>");
        		out.print("</td> ");
	    		out.print("</tr>\n");
	    	}
	    	out.print("</table>\n");
    	}
    	catch (Exception e) {
            e.printStackTrace();
        }
    }
}