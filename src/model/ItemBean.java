package model;

public class ItemBean
{
		//Item attributes
	private String name;
	private Double price;
	private int priceRound;
	
		//Constructor
	public ItemBean(String name, Double price)
	{
		super();
		this.name = name;
		this.price = price;
		this.priceRound = (int) Math.round(price);
	}
	public ItemBean(String name, int price)
	{
		super();
		this.name = name;
		this.price = (double) price;
		this.priceRound = price;
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
	
	public boolean equals(ItemBean other)
	{
		if((other.getName().equals(this.name)) && (other.getPrice().toString().equals(this.price.toString())))
			return true;
		else
			return false;
	}

	public int getPriceRound()
	{
		return priceRound;
	}

	public void setPriceRound(int priceRound)
	{
		this.priceRound = priceRound;
	}
}
