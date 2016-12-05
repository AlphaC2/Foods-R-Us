package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ItemBean;
import model.ItemDAO;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Cart() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//Get session
		HttpSession session = request.getSession();
		try
		{
			Start.getCategories(request, response);
		} catch (SQLException e1)
		{
			System.out.println("ERROR! Could not get categories.\nDid you remember to start and connect to Derby?");
		}
			//Pull items in cart from session scope, place into request
		@SuppressWarnings("unchecked")
		ArrayList<ItemBean> cart = (ArrayList<ItemBean>) session.getAttribute("cart");
		ItemDAO itemDao = new ItemDAO();
		ArrayList<ItemBean> toRemove = new ArrayList<ItemBean>();
		
		try
		{
			toRemove = itemDao.getItemsByName(request.getParameter("cartRemove"));
		} catch (SQLException e)
		{
			System.out.println("Could not remove the item from cart!");
		}

		try
		{
				//Removing an item from cart
			if((toRemove.get(0) != null) && (cart != null))
			{
					//Find the item to be removed
				for(int i = 0; i < cart.size(); i++)
				{
						//Item found!
					if(cart.get(i).equals(toRemove.get(0)))
					{
							//Reset quantity to 0 - prevent previous quantity from being remembered if removed then readded to cart
						cart.get(i).setQuantity(0);
						toRemove.get(0).setQuantity(0);
						cart.remove(i); //Remove from cart
					}
				}
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			System.out.println("Nothing to be removed");
		}
		
		request.setAttribute("cart", cart);
		
			//Add to cart
		if(request.getParameter("cartAdd") != null)
			Catalog.addToCart(request, response, itemDao, session);
		
		String target;
		if((request.getParameter("category") == null) && ((request.getParameter("search") == null) || (request.getParameter("search").equals("")))){
			request.setAttribute("target", "Cart");
			target = "/Dashboard.jspx";
		}else{
			request.setAttribute("target", "Catalog");
			target = "/Catalog";
		}
			//Check if logged in
		if(session.getAttribute("loggedIn") != null)
			request.setAttribute("loggedIn", true);
			//Forward to the cart page
		if(session.getAttribute("cart") != null)
		{
			double tax = (double)session.getAttribute("cartTotal") * Double.parseDouble(this.getServletContext().getInitParameter("taxPercent"));
			request.setAttribute("tax", tax);
			
				//Check for free shipping before applying tax and shipping cost
			double shipping = Double.parseDouble(this.getServletContext().getInitParameter("shippingCost"));
			if((double)session.getAttribute("cartTotal") >= Double.parseDouble(this.getServletContext().getInitParameter("freeShipMin")))
				shipping = 0.00;
			request.setAttribute("shipping", shipping);
			request.setAttribute("cartTotal", (double)session.getAttribute("cartTotal")+tax+shipping);
		}
		this.getServletContext().getRequestDispatcher(target).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
