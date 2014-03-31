/*
 * Test Case 5:
 * If a method call is used as an expression, the method must return a result
 */
class Test005 {
	int x;
	
	void foo (int a, int b, int c) {  
		x = a + b+ c;
	}
	void main(System s) {
		  int b = 1;
		 
		 b = foo(b, b+1, b-1) * 10;
		 
	  }

}
