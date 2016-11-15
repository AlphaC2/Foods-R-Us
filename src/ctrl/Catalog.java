package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ItemBean;
import model.ItemDAO;

/**
 * Servlet implementation class Catalog
 */
@WebServlet("/Catalog")
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Catalog() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get session
		HttpSession session = request.getSession();
		
		//List for items along with DAO for database interaction
		ItemDAO itemDao = new ItemDAO();
		ArrayList<ItemBean> itemList = null;

		
			//Check if user has entered a search query
		if(request.getAttribute("search") != null)
		{//Search entered
			try
			{
				itemList = itemDao.getItemsByName(request.getAttribute("search").toString());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//End search entered
		
			//Check if user has selected a category from sidebar
		if(request.getAttribute("category") != null)
		{//Category selected
			try
			{
				itemList = itemDao.getItemsByCategory(Integer.parseInt((String) request.getAttribute("category")));
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//End category selected
		
			//Poke the itemList into request scope to be displayed by jspx file
		request.setAttribute("items", itemList);
		
			//Set target of content pane to catalog view - Refresh Dashboard
		request.setAttribute("target", "Catalog");
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
