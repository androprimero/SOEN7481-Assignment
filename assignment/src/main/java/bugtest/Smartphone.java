

package bugtest;

import java.util.HashMap;
import java.util.Map;

public class Smartphone
{
	private String company;
	private String model;
	private int price;
	public Smartphone(String company, String model, int price)
	{
		super();
		this.company = company;
		this.model = model;
		this.price = price;
	}
	public String getCompany()
	{
		return company;
	}
	public void setCompany(String company)
	{
		this.company = company;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public int getPrice()
	{
		return price;
	}
	public void setPrice(int price)
	{
		this.price = price;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		Smartphone other = (Smartphone)obj;
		if( company == null )
		{
			if( other.company != null )
				return false;
		}
		else if( !company.equals( other.company ) )
			return false;
		if( model == null )
		{
			if( other.model != null )
				return false;
		}
		else if( !model.equals( other.model ) )
			return false;
		if( price != other.price )
			return false;
		return true;
	}
	
	public static void main(String[] args)
	{
		Smartphone phone1 = new Smartphone( "Apple", "iphoneX", 1000);
		Smartphone phone2 = new Smartphone( "Apple", "iphoneX", 1000);
		
		Map<Smartphone,String> phones=new HashMap<Smartphone,String>();
		phones.put( phone1, "2017" );
		phones.put( phone2, "2018" );
		
		Smartphone input=new Smartphone( "Apple", "iphoneX", 1000);
		System.out.println( phones.get( input ) );
		
	}
	
}