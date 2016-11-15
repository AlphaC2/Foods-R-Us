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
			//System.out.println("!" + banks.get(0));
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
		
			//Set content pane to the home page
		request.setAttribute("target", "Home");
		this.getServletContext().getRequestDispatcher("/Dashboard.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
