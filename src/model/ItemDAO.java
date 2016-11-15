package model;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;

public class ItemDAO
{
		//Database source
	private DataSource dataSource;
	
		//Default construction
	public ItemDAO()
	{
		try
		{	//Set the data source
			this.dataSource = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
		} catch (NamingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ITEMDAO CREATED.");
	}
	
								//TODO: UNIFY THE TWO SEARCH METHODS INTO ONE
		//Get items from search
	public ArrayList<ItemBean> getItemsByName(String name) throws SQLException
	{
		System.out.println("Searching by name!!!");
		//SQL query
		String query = "select * from roumani.item where name like ?";
		
		ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
		
		try{
			//Open connection to database
		Connection con = dataSource.getConnection();
			//Create prepared statement
		PreparedStatement statement = con.prepareStatement(query);

			//Replace ? in query with values
		statement.setString(1, "%"+name+"%");
		
			//Query the database
		ResultSet rs = statement.executeQuery();
		
		
			//If there are remaining items matching search criteria, place them in list
		while(rs.next() != false)
			itemList.add(new ItemBean(rs.getString("name"), rs.getDouble("price")));

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			//Return list of found items
		System.out.println("Done searching by name");
		return itemList;
	}
	
	public ArrayList<ItemBean> getItemsByCategory(int category) throws SQLException
	{
		System.out.println("Searching by category");
		//SQL query
		String query = "select * from roumani.item where name=?";
		
		ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
		
		try{
			//Open connection to database
		Connection con = dataSource.getConnection();
			//Create prepared statement
		PreparedStatement statement = con.prepareStatement(query);

			//Replace ? in query with values
		statement.setInt(1, category);
		
			//Query the database
		ResultSet rs = statement.executeQuery();
		
		
			//If there are remaining items matching search criteria, place them in list
		while(rs.next() != false)
			itemList.add(new ItemBean(rs.getString("name"), rs.getDouble("price")));

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			//Return list of found items
		System.out.println("Done searching by category");
		return itemList;
	}
		
}