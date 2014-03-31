/*
 * Test Case 5 :
 * A return statement must not have a return value unless it appears in the body 
 * of a method that is declared to return a value
 */
class Test006 {
	int x;
	
	void foo (int a, int b, int c) {  
		x = a + b+ c;
		return x;
	}
	
	
	void main(System s) {
		  int b = 1;
		 
		 foo(b, b+1, b-1) ;
		 return;
		 
	  }
}
