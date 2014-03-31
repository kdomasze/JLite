/*
 * Test Case 10 :
 * The (expr) in an while statement must have type int
 */

class bar {
	int a;
	int b;
}

class Test010 {
	int x;
	
	bar foo (int a, int b, int c) {  
		x = a + b+ c;
		return new bar();
	}
	
	void main(System s) {
		 int b = 1;
		 
		 while(foo(b, b+1, b-1))  {
			 b = b+1;
		 }
		 
		 return;
		 
	  }

}
