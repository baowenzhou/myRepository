package Jdk;

public class Base{

	public String baseName = "base";
	
	public Base() {
		callName();
	}
	
	public Base(String baseName) {
		this.baseName = baseName;
		callName();
	}

	public void callName() {
		System.out.println(baseName);
	}
	
//	class Sub extends Base {
//		
//		public String baseName = "sub";
//		
//		public Sub() {
////			callName();
//			super();
//		}
//
//		public Sub(String baseName) {
//			this.baseName = baseName;
//			callName();
//		}
//		
//		public void callName() {
//			System.out.println(baseName);
//		}
//	}
	
	public static void main(String[] args) {
		
//		Base a = new Base();
//		Base.Sub b = a.new Sub("2222");
		
		
		Sub b = new Sub("2222");
		
//		Sub b = new Sub();
		
	}
}