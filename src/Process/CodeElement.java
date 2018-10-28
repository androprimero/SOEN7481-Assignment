package Process;

public class CodeElement {
	private String owner;// the code that contain this element
	private String name; // name of the component
	private boolean used;   // if the method is called or the variable is used
	public CodeElement() {
		used = false;
	}
	public CodeElement(String name) {
		used = false;
		this.name = name;
	}
	public CodeElement(String name, String owner) {
		this.owner = owner;
		this.name = name;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public boolean getUsed() {
		return used;
	}
	public String getName() {
		return this.name;
	}
	public String getOwner() {
		return this.owner;
	}
	public String toString() {
		String string = "Code Name "+ this.getName() + ", Owner: "+this.getOwner();
		return string;
	}
}
