package adhoc;

import javax.servlet.jsp.tagext.*;

import model.ItemBean;
import model.ItemDAO;

import javax.servlet.jsp.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

/**
 * Servlet Filter implementation class FinePrint
 */
@WebFilter(
		dispatcherTypes = {DispatcherType.FORWARD }
					, 
		urlPatterns = { 
				"/Cart", 
				"/Dashboard.jspx"
		})
public class FinePrint implements Filter {

    /**
     * Default constructor. 
     */
    public FinePrint() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		ItemDAO itemDao = new ItemDAO();
		String suggestionBase = "1409S413";
		String toSuggest = "2002H712";
		ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
		ArrayList<ItemBean> mustMatch = new ArrayList<ItemBean>();
		
		try
		{
			itemList = itemDao.getItemsByName(suggestionBase);
			mustMatch = itemDao.getItemsByName((String) request.getParameter("cartAdd"));
			System.out.println("SUGGESTION BASE: " + itemList);
			
			System.out.println(mustMatch.size());
			
			System.out.println(itemList.equals(mustMatch));
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			//Check that the user is adding to cart
		if((request.getParameter("cartAdd") != null) && (itemList.get(0).equals(mustMatch.get(0))))
		{
			System.out.println("ITEM IS IN THE CART");
			ArrayList<ItemBean> suggestedItem = new ArrayList<ItemBean>();
			try
			{
				suggestedItem = itemDao.getItemsByName(toSuggest);
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MyResponse myResp = new MyResponse((HttpServletResponse) response);
				//pass the request along the filter chain
			chain.doFilter(request, myResp);
			String payload = myResp.getContent();
			System.out.println(response.isCommitted());
			
				//Define replacement strings
			String replacement = "Since you have " + mustMatch.get(0).getName() + "in your cart you may like " + suggestedItem.get(0).getName();

			payload = payload.replaceAll("Suggestions", replacement);

			response.getWriter().println(payload);

		}
		else
			chain.doFilter(request, response);
		return;
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
