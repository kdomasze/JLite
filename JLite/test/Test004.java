/*
 * Test Case  4:
 * The number and types of arguments in a method call must be the same as the number
 *  and types of the formals, i.e., the signatures must be identical
 */

class Test004 {
	int x;
	
	int foo (int a, int b, int c) {  
		x = a + b+ c;
		return (x-10);
	}
	void main(System s) {
		  int b = 1;
		 
		  foo(b, b+1);
		  
	  } 

}
