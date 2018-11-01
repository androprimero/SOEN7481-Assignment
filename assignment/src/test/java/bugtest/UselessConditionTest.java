package bugtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class UselessConditionTest
{
	@Test
	public void scenario1Test(){
		UselessCondition condition=new UselessCondition();
		assertEquals( "it gets executed always", condition.scenario1() );		
	}
	
	@Test
	public void scenario2Test(){
		UselessCondition condition=new UselessCondition();
		List<String> list=new ArrayList<String>();
		list.add( "Java" );
		assertNotEquals( 0, condition.scenario2(list) );		
	}
	
	@Test
	public void scenario3Test(){
		UselessCondition condition=new UselessCondition();
		assertEquals( 3, condition.scenario3() );		
	}
	

	
}