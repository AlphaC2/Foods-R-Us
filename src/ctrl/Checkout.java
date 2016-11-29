package ctrl;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ItemBean;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		//Get externally decided costs minimum from web.xml
		final double freeShipMin = Double.parseDouble(this.getServletContext().getInitParameter("freeShipMin"));
		double shippingCost = Double.parseDouble(this.getServletContext().getInitParameter("shippingCost"));
		final double taxPercent = Double.parseDouble(this.getServletContext().getInitParameter("taxPercent"));
		
		//Pull cart from session scope, poke back into request
		@SuppressWarnings("unchecked")
		ArrayList<ItemBean> cart = (ArrayList<ItemBean>) session.getAttribute("cart");
		request.setAttribute("cart", cart);
		double totalCost = 0.00;
		double taxCost = 0.00;
				
			//Iterate over all items in cart, adding up cost of items
		for(int i = 0; i < cart.size(); i++)
		{
				//Add cost of item to total cart price
			totalCost += cart.get(i).getPrice(); 
		}
			//Poke subtotal into request before adding shipping and tax
		request.setAttribute("subtotal", totalCost);
		
			//If user goes above free shipping minimum cost eliminate shipping fee
		if(totalCost >= freeShipMin)
			shippingCost = 0.00;
			//Calculate tax, poke into request
		taxCost = totalCost * taxPercent;
		request.setAttribute("taxTotal", taxCost);
		request.setAttribute("shippingCost", shippingCost);
		
			//Calculate total, poke into request
		totalCost = (taxCost + totalCost) + shippingCost;
		request.setAttribute("total", totalCost);
		
			//Forward user to checkout page
		request.setAttribute("target", "Checkout");
		
			//Check if user is logged in
		if(session.getAttribute("loggedIn") != null)
			request.setAttribute("loggedIn", true);
		this.getServletContext().getRequestDispatcher("/Checkout.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}