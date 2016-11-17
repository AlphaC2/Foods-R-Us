package ctrl;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;

/**
 * Servlet implementation class Start
 */
@WebServlet({"/eFoodz" })
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Start() {
        super();
        
    }

    public void init(){
    	ItemDAO i;
		ArrayList<ItemBean> catalogueList;
		try{
			i = new ItemDAO();
			catalogueList = i.getCatalogueList();
			this.getServletContext().setAttribute("model", i);
			this.getServletContext().setAttribute("catalogueList", catalogueList);
		}catch (Exception e){
			e.printStackTrace();
			//throw new ServletException(e.getMessage());
		}
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Request Start");
			//Set content pane to the home page
		String target = null; //Request.forward parameter 
		if(request.getParameter("search") != null){
			System.out.println("Redirecting to catalog");
			request.setAttribute("target", "Catalog");
			target = "/eFoods";
		}else if(request.getParameter("cartAdd") != null){
			System.out.println("Redirecting to cart");
			request.setAttribute("target", "cart");
			target = "/Cart";
		}else{
			System.out.println("Redirecting to home");
			request.setAttribute("target", "Home");
			target = "/Dashboard.jspx";
		}

		this.getServletContext().getRequestDispatcher(target).forward(request, response);
		System.out.println("Request End");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
