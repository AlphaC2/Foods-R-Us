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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("Catalog.java start");
		String target = null;
		//Get session
		HttpSession session = request.getSession();
		try
		{
			Start.getCategories(request, response);
		} catch (SQLException e1)
		{
			System.out.println("ERROR! Could not get categories.\nDid you remember to start and connect to Derby?");
		}
		//List for items along with DAO for database interaction
		ItemDAO itemDao = new ItemDAO();

		searchCatalog(request, response, itemDao, session);
		addToCart(request, response, itemDao, session);

			//Set target of content pane to catalog view - Refresh Dashboard
//		System.out.println("Catalog.java end");
		if((request.getParameter("cartAdd") == null) && (request.getParameter("cartRemove") == null)){
			request.setAttribute("target", "Catalog");
			target = "/Dashboard.jspx";
		}else{
			request.setAttribute("target", "Cart");
			target = "/Cart";
		}
	
		if(session.getAttribute("loggedIn") != null)
			request.setAttribute("loggedIn", true);
		this.getServletContext().getRequestDispatcher(target).forward(request, response);
	}

				/**Handle searching the catalog by user input**/ 
	private void searchCatalog(HttpServletRequest request, HttpServletResponse response, ItemDAO itemDao, HttpSession session)
	{
		
//		System.out.println("SEARCHING FOR " + request.getParameter("search"));
		ArrayList<ItemBean> itemList = null;
		
		//Check if user has entered a search query
		if(request.getParameter("search") != null)
		{//Search entered
//			System.out.println("In Catalog, searching by name.");
			try
			{
				itemList = itemDao.getItemsByName(request.getParameter("search").toString());
//				System.out.println("In Catalog, search success.");
			} catch (SQLException e)
			{
//				System.out.println("In Catalog, search failed.");
				System.out.println("ERROR! Could not search database.\n Are you connected to Derby?");
			} catch(NullPointerException e)
			{
//				System.out.println("No such item found!");
			}
		}//End search entered
		
			//Check if user has selected a category from sidebar
		if(request.getParameter("category") != null)
		{//Category selected
//			System.out.println("In Catalog, doing categories.");
			try
			{
				itemList = itemDao.getItemsByCategory(Integer.parseInt((String) request.getParameter("category")));
//				System.out.println("In Catalog, category success.");
			} catch (SQLException e)
			{
//				System.out.println("In Catalog, category fail.");
				System.out.println("ERROR! (Catalog.java) Could not search database by category.\n Are you connected to Derby?");
			}
		}//End category selected
		
//		System.out.println("What was found: ");
//		System.out.println(itemList);
		
		//Poke the itemList into request scope to be displayed by jspx file
		request.setAttribute("items", itemList);
	}
				/**Handle adding items from catalog view to cart**/
	
	@SuppressWarnings("unchecked")
	public static void addToCart(HttpServletRequest request, HttpServletResponse response, ItemDAO itemDao, HttpSession session)
	{
		
		ArrayList<ItemBean> cart = new ArrayList<ItemBean>();
			//Get the users existing cart, if it exists
		if(session.getAttribute("cart") != null)
		{
			cart = (ArrayList<ItemBean>) session.getAttribute("cart");
		}
		//Initialize carttotal if necessary
		if(session.getAttribute("cartTotal") == null)
		{
			session.setAttribute("cartTotal", 0.00);
		}
			//Get time from session start to when user added to cart
		if(session.getAttribute("timeToCartChecked") == null)
		{
				//Calculate time to cart, average among other users
			long currentTime = System.currentTimeMillis();
			long timeToCart = currentTime - session.getCreationTime();
				//If this is the first user, create the context attribute
			if(request.getServletContext().getAttribute("avgTimeToCart") == null)
				request.getServletContext().setAttribute("avgTimeToCart", timeToCart);
			else
			{
				long currentAverage = (long) request.getServletContext().getAttribute("avgTimeToCart");
				currentAverage = (currentAverage + timeToCart)/2;
				request.getServletContext().setAttribute("avgTimeToCart", currentAverage);
			}
				//Ensure each user is only counted once
			session.setAttribute("timeToCartChecked", true);
//			System.out.println("Time to cart:" + request.getServletContext().getAttribute("avgTimeToCart"));
		}

		
		String itemNameToAdd = request.getParameter("cartAdd");
//		System.out.println("Item to add to cart:" + itemNameToAdd);
		try
		{
//			System.out.println("In Catalog, adding to cart.");
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
				
					//Add this items price to cart
				session.setAttribute("cartTotal", (double) session.getAttribute("cartTotal") + toAdd.getPrice());
			}
//			System.out.println("In Catalog, cart success.");
		} catch (SQLException e)
		{
//			System.out.println("In Catalog, cart failure.");
			e.printStackTrace();
		}
		
		session.setAttribute("cart", cart);
//		System.out.println("PRINTING CART: " + cart);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
