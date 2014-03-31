/*
 * The expression in a return statement must have the same type as the declared result type 
 * of the enclosing method definition
 */

class bar {
	int a;
	int b;
}

class Test007 {
	int x;
	bar foo (int a, int b, int c) {  
		x = a + b+ c;
		return x;
	}
	
	void main() {
		  int b = 1;
		 
		 foo(b, b+1, b-1) ;
		 return;
		 
	  }
}
