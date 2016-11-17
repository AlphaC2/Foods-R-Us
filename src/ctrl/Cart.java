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
		
			//Pull items in cart from session scope, place into request
		@SuppressWarnings("unchecked")
		ArrayList<ItemDAO> cart = (ArrayList<ItemDAO>) session.getAttribute("cart");
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
		
		System.out.println("TO REMOVE " + toRemove.get(0));
		if((toRemove.get(0) != null) && (cart != null))
		{
			ItemBean removePlease = toRemove.get(0);
			cart.remove(removePlease);
			System.out.println("NEW CART: " + cart);
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
