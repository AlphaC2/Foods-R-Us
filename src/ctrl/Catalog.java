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
		System.out.println("Catalog.java start");
		String target = null;
		//Get session
		HttpSession session = request.getSession();
		try
		{
			Start.getCategories(request, response);
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//List for items along with DAO for database interaction
		ItemDAO itemDao = new ItemDAO();

		searchCatalog(request, response, itemDao, session);
		addToCart(request, response, itemDao, session);

			//Set target of content pane to catalog view - Refresh Dashboard
		System.out.println("Catalog.java end");
		if((request.getParameter("cartAdd") == null) && (request.getParameter("cartRemove") == null)){
			request.setAttribute("target", "Catalog");
			target = "/Dashboard.jspx";
		}else{
			request.setAttribute("target", "Cart");
			target = "/Cart";
		}
	
		this.getServletContext().getRequestDispatcher(target).forward(request, response);
	}

				/**Handle searching the catalog by user input**/ 
	private void searchCatalog(HttpServletRequest request, HttpServletResponse response, ItemDAO itemDao, HttpSession session)
	{
		
		System.out.println("SEARCHING FOR " + request.getParameter("search"));
		ArrayList<ItemBean> itemList = null;
		
		//Check if user has entered a search query
		if(request.getParameter("search") != null)
		{//Search entered
			System.out.println("In Catalog, searching by name.");
			try
			{
				itemList = itemDao.getItemsByName(request.getParameter("search").toString());
				System.out.println("In Catalog, search success.");
			} catch (SQLException e)
			{
				System.out.println("In Catalog, search failed.");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(NullPointerException e)
			{
				System.out.println("No such item found!");
			}
		}//End search entered
		
			//Check if user has selected a category from sidebar
		if(request.getParameter("category") != null)
		{//Category selected
			System.out.println("In Catalog, doing categories.");
			try
			{
				itemList = itemDao.getItemsByCategory(Integer.parseInt((String) request.getParameter("category")));
				System.out.println("In Catalog, category success.");
			} catch (SQLException e)
			{
				System.out.println("In Catalog, category fail.");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//End category selected
		
		System.out.println("What was found: ");
		System.out.println(itemList);
		
		//Poke the itemList into request scope to be displayed by jspx file
		request.setAttribute("items", itemList);
	}
				/**Handle adding items from catalog view to cart**/
	
	@SuppressWarnings("unchecked")
	public static void addToCart(HttpServletRequest request, HttpServletResponse response, ItemDAO itemDao, HttpSession session)
	{
		
		ArrayList<ItemBean> cart = new ArrayList<ItemBean>();
		if(session.getAttribute("cart") != null)
		{
			cart = (ArrayList<ItemBean>) session.getAttribute("cart");
		}

		
		String itemNameToAdd = request.getParameter("cartAdd");
		System.out.println("Item to add to cart:" + itemNameToAdd);
		try
		{
			System.out.println("In Catalog, adding to cart.");
			if(itemNameToAdd != null)
			{
				ItemBean toAdd = itemDao.getItemsByName(itemNameToAdd).get(0);
				int inCartFlag = 0;
									//cart.contains doesnt work - iterate over all elements checking equality
				for(int i = 0; i < cart.size(); i++)
				{
						//If item exists in cart add one to current quantity
					if(cart.get(i).equals(toAdd))
					{
						cart.get(i).setQuantity(cart.get(i).getQuantity()+1);
						inCartFlag = 1;
					}
				}
					//New item - add to cart with quantity 1
				if(inCartFlag == 0)
					cart.add(toAdd);
			}
			System.out.println("In Catalog, cart success.");
		} catch (SQLException e)
		{
			System.out.println("In Catalog, cart failure.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setAttribute("cart", cart);
		System.out.println("PRINTING CART: " + cart);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
