package auth;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Admin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//Redirected request from red server  
		if(request.getAttribute("username") != null && request.getAttribute("hash") != null){
			//Pull nonce from context
			
			//Hash username and nonce and check against hash
			
			//If hashes match, user is now logged in
			
			//Otherwise, remove loggedIn attribute and forward to Auth again
			
		}else if (request.getAttribute("loggedIn") == null){
			//Generate a nonce
			SecureRandom random = new SecureRandom();
			int nonce = random.nextInt();
			System.out.println("nonce:"+nonce);
			
			//Poke the user and nonce into the context (request.getServletContext())
			
			
			//Redirect request to perl server with nonce and back pointer
			String back = "http://localhost:4413/Food_R_Us/Admin";
			response.sendRedirect("http://www.eecs.yorku.ca/~cse13179/auth/Auth.cgi");
			//response.sendRedirect("http://www.eecs.yorku.ca/~roumani/course/4413/lab/C/C2.html");
			
		//Log out request
		}else if (request.getAttribute("loggedIn").equals("out")){
			request.removeAttribute("loggedIn");
			System.out.println("Logged out");
			this.getServletContext().getRequestDispatcher("/Dashboard.jspx").forward(request, response);
			
		}else{
			this.getServletContext().getRequestDispatcher("/Dashboard.jspx").forward(request, response);
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
