package bugtest;

public class StringComparison
{
	public boolean compareByReference1(){
		String message1="Java brains";
		String message2="Java brains";
		return message1 == message2;//true
	}
	
	public boolean compareByReference2(){
		String message1="Java brains";
		String message2="Geek4Geeks";
		return message1 != message2;//true
	}
	
	public boolean compareByObject(){
		String message1="Java brains";
		String message2=new String("Java brains");
		return message1 == message2;//false
	}
	
	public boolean compareByIntern1(){
		String message1="Java brains";
		String message2=new String("Java brains").intern();
		return message1 == message2;//true
	}
	
	public boolean compareByIntern2(){
		String message1="Java brains".intern();
		String message2=new String("Java brains");
		return message1 == message2;//false
	}
	
	public boolean compareByEquals1(){
		String message1=new String("Java brains");
		String message2=new String("Java brains");
		return message1.equals( message2 );//true
	}
	
	public boolean compareByEquals2(){
		String message1=new String("Java brains");
		String message2=new String("Geek4Geeks");
		return message1.equals( message2 );//false
	}
	
	public boolean compareByConstant(){		
		final String str1 = "Java";
		final String str2 = " brains";
		String append = str1 + str2;
		return append == "Java brains";//true	
	}

	
	public static void main(String[] args)
	{
		StringComparison compare=new StringComparison();
		System.out.println(compare.compareByReference1());
		System.out.println(compare.compareByReference2());
		System.out.println(compare.compareByObject());
		System.out.println(compare.compareByIntern1());
		System.out.println(compare.compareByIntern2());
		System.out.println(compare.compareByEquals1());
		System.out.println(compare.compareByEquals2());
		System.out.println(compare.compareByConstant());
		
		
	}
	
}