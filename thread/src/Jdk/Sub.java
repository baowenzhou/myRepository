package Jdk;

public class Sub extends Base {

	public String baseName = "sub";
	
	public Sub() {
//		callName();
//		super();
	}
//
	public Sub(String baseName) {
		this.baseName = baseName;
		callName();
	}
//	
	public void callName() {
		System.out.println(baseName);
	}
}