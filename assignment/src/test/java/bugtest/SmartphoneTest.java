

package bugtest;

import static org.junit.Assert.*;

import org.junit.Test;

import bugtest.Smartphone;

public class SmartphoneTest
{
	
	@Test
	public void equalsTest()
	{
		Smartphone phone1 = new Smartphone( "Apple", "iphoneX", 1000);
		Smartphone phone2 = new Smartphone( "Apple", "iphoneX", 1000);
		
		assertEquals("Both smartphones are equal", phone1, phone2);	
	}
	
	@Test
	public void hashcodeTest()
	{
		Smartphone phone1 = new Smartphone( "Apple", "iphoneX", 1000);
		Smartphone phone2 = new Smartphone( "Apple", "iphoneX", 1000);
		
		int phoneCode1=phone1.hashCode();
		int phoneCode2=phone2.hashCode();
		
		assertNotEquals( phoneCode1, phoneCode2 );
	}
	
}