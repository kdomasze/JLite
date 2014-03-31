/*
 * Test case 1:
 * No identifier is declared twice in the same scope. 
 */
class Test001 {
	int x;
	int y; 
	  
	  void main(System s) {
		y = 0;
	    int x = y;  
	    if (y == 1) {
	    	int x = 1; /* Declared twice */
	    	y = x;
	    }
	  }
	  
}
