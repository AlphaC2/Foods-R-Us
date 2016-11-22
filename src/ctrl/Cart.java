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
        // TODO Auto-generated constructor stub
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			//Forward to the cart page
		request.setAttribute("target", "Cart");
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
