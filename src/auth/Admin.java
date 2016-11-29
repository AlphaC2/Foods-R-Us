package auth;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		int nonce; 
		HttpSession session = request.getSession();
		
		//Redirected request from red server  
		if(request.getParameter("user") != null && request.getParameter("hash") != null){
			System.out.println("Redirected request from auth server, now verifying credentials");
			
			//Pull nonce from context
			nonce = (int) request.getSession().getAttribute("nonce");
			//System.out.println("Nonce:"+nonce);
			
			//Hash nonce+username and check against hash
			String hashParam = request.getParameter("hash").substring(0, request.getParameter("hash").length()-3);
			String userParam = request.getParameter("user");
			String dataString = nonce+userParam;
			try{
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] data = dataString.getBytes();
				dataString = bytesToHex(md.digest(data));
				//System.out.println("Computed Hash:"+dataString);
				//System.out.println("Hash Param   :"+hashParam);
			}catch (NoSuchAlgorithmException e){
				e.printStackTrace();
			}
			
			//Check computer has and hash from request
			if (dataString.equals(hashParam)){
				//Log the user in and set appropriate attributes, then forward back to dashboard
				request.setAttribute("loggedIn", true);
				System.out.println("User is now logged in");
				request.getSession().setAttribute("user", userParam);
				request.setAttribute("target", "Home");
				this.getServletContext().getRequestDispatcher("/Dashboard.jspx").forward(request, response);
				session.setAttribute("loggedIn", true);
				
			}else{//Otherwise, remove loggedIn attribute and forward to Auth again
				System.out.println("Invalid credentials, please try again");
				request.removeAttribute("loggedIn");
				this.getServletContext().getRequestDispatcher("/Admin").forward(request, response);
				session.removeAttribute("loggedIn");
			}
		
			
		}else if (request.getAttribute("loggedIn") == null){
			//Generate a nonce
			System.out.println("Attempting to authenticate user");
			SecureRandom random = new SecureRandom();
			nonce = random.nextInt();
			System.out.println("nonce:"+nonce);
			
			//Poke the user and nonce into the context (request.getServletContext())
			request.getSession().setAttribute("nonce", nonce);
			
			//Redirect request to perl server with nonce and back pointer
			String back = "http://localhost:4413/Food_R_Us/Admin";
			String authServer = "http://www.eecs.yorku.ca/~cse13179/auth/Auth.cgi";
			System.out.println("Redirecting to authentication server");
			response.sendRedirect("https://www.eecs.yorku.ca/~cse13179/auth/Auth.cgi?back="+back+"&nonce="+nonce);
			//response.sendRedirect("http://www.eecs.yorku.ca/~roumani/course/4413/lab/C/C2.html");
			
		//Log out request
		}else if (request.getParameter("loggedIn").equals("out")){
			request.removeAttribute("loggedIn");
			System.out.println("Logged out");
			this.getServletContext().getRequestDispatcher("/Dashboard.jspx").forward(request, response);
			session.removeAttribute("loggedIn");
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
	
	/**
	 * Given an array of bytes, convert it to a string of hex digits.
	 */
	private static String bytesToHex(byte[] data)
	{
		assert (data != null);
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < data.length; i++)
		{
			int tmp = data[i] & 0xFF;
			if (tmp < 16)
				buffer.append("0");
			buffer.append(Integer.toHexString(tmp));
		}
		return buffer.toString().toLowerCase();
	}
}
