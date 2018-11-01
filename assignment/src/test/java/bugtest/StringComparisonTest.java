package bugtest;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringComparisonTest
{
	
	@Test
	public void compareByReference1Test()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByReference1();
		assertEquals( true, result );
	}
	
	@Test
	public void compareByReference2Test()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByReference2();
		assertEquals( true, result );
	}
	
	@Test
	public void compareByObjectTest()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByObject();
		assertNotEquals( true, result );
	}
	
	@Test
	public void compareByIntern1Test()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByIntern1();
		assertEquals( true, result );
	}
	
	@Test
	public void compareByIntern2Test()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByIntern2();
		assertNotEquals( true, result );
	}
	
	@Test
	public void compareByEquals1Test()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByEquals1();
		assertEquals( true, result );
	}
	
	@Test
	public void compareByEquals2Test()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByEquals2();
		assertNotEquals( true, result );
	}
	
	@Test
	public void compareByConstantTest()
	{
		StringComparison compare=new StringComparison();
		boolean result = compare.compareByConstant();
		assertEquals( true, result );
	}
}