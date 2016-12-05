package ctrl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;

/**
 * Servlet implementation class Start
 */
@WebServlet({"/eFoods" })
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Start() {
        super();
        
    }

    public void init(){
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
//		System.out.println("Request Start");
		ItemBean ib = new ItemBean("Hello", 10.99);
		ArrayList<ItemBean> ibl = new ArrayList<ItemBean>();
		ibl.add(ib);
		
		XmlHandler.writeXml(new File(this.getServletContext().getRealPath("/WEB-INF/orders.xml")), "TEST", ibl);
			//Set content pane to the home page
		String target = null; //Request.forward parameter 
		try
		{
			getCategories(request, response);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(request.getParameter("search") != null){
//			System.out.println("Redirecting to catalog");
			request.setAttribute("target", "Catalog");
			target = "/Catalog";
		}else if(request.getParameter("category") != null){
//			System.out.println("Forwarding to catalog search");
			request.setAttribute("target", "Catalog");
			request.setAttribute("category", request.getParameter("category"));
			target = "/Catalog";
		}else if(request.getParameter("cartAdd") != null){
//			System.out.println("Redirecting to cart");
			request.setAttribute("target", "cart");
			target = "/Cart";
		}else if(request.getParameter("cartRemove") != null){
			request.setAttribute("target", "cart");
			request.setAttribute("cartRemove", request.getParameter("cartRemove"));
			target = "/Cart";
		}else{
//			System.out.println("Redirecting to home");
			request.setAttribute("target", "Home");
			target = "/Dashboard.jspx";
		}
			//Check if logged in
		if(session.getAttribute("loggedIn") != null)
			request.setAttribute("loggedIn", true);
		this.getServletContext().getRequestDispatcher(target).forward(request, response);
//		System.out.println("Request End");
	}

	public static void getCategories(HttpServletRequest request, HttpServletResponse response) throws SQLException
	{
//		System.out.println("GETTING CATEGoRiES");
			//Search database for all categories, collect them in an arraylist to poke into request
		ItemDAO itemDao = new ItemDAO();
		ArrayList<ItemBean> categoryList = itemDao.getCatalogueList();
		
		
		request.setAttribute("categoryList", categoryList);
//		System.out.println("Category List: " + categoryList);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
