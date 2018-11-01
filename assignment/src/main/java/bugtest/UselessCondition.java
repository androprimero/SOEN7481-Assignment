package bugtest;

import java.util.ArrayList;
import java.util.List;

public class UselessCondition
{
	
	public String scenario1(){
	
		if(true){
			return "it gets executed always";
		}
		
		return "it never gets executed always";
		
	}
	
	public int scenario2(List<String> list){
		
		if(list== null){
			list=new ArrayList<String>();
		}
		else{
			if(list != null){
				return  list.size();
			}
		}
		return 0;
	}
	
	public int scenario3(){
		
		
		boolean isEasy=true;
		
		int learningDuration=0;
		
		if(isEasy){
			learningDuration=3;
		}
		else{
			learningDuration=6;
		}
		return learningDuration;

	}
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		UselessCondition condition=new UselessCondition();
		System.out.println(condition.scenario1());
		System.out.println(condition.scenario2(new ArrayList<String>()));
		System.out.println(condition.scenario3());
	}
	
}