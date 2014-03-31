/*
 * Test Case 9:
 * The (expr) in an if statement must have type int
 */

class bar {
	int a;
	int b;
}

class Test009 {
	int x;
	
	bar foo (int a, int b, int c) {  
		x = a + b+ c;
		return new bar();
	}
	
	
	void main(System s) {
		  int b = 1;
		  bar a = new bar();
		 if (foo(b, b+1, b-1) ) {
			 b = b+1;
		 }
		 
		 return;
		 
	  }
}
