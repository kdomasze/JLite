/*
 * Test Case 11:
 * The operands of (arith op)s and (rel op)s must have type int
 */

class bar {
	int a;
	int b;
}

class Test011 {

	int x;
	
	bar foo (int a, int b, int c) {  
		x = a + b+ c;
		return new bar();
	}
	
	void main(System s) {
		 int b = 1;
		 
		 while(foo(b, b+1, b-1) > 0)  {
			 b = foo(b, b+1, b-1) +100;
		 }
		 
		 return;
		 
	  }
}
