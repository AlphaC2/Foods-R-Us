package model;

public class ItemBean
{
		//Item attributes
	private String name;
	private Double price;
	
		//Constructor
	public ItemBean(String name, Double price)
	{
		super();
		this.name = name;
		this.price = price;
	}

										/////////MUTATORS/ACCESSORS
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}
									//TOSTRING
	@Override
	public String toString()
	{
		return "ItemBean [name=" + name + ", price=" + price + "]";
	}
	
	
}
