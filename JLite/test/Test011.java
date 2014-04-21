/*
 * The expression in a return statement must have the same type as the declared result type 
 * of the enclosing method definition
 */
class tar {
	int y;
}
 
class bar extends tar{
	int a;
	int b;
	
	int sumthis(int x, int y)
	{
		return x+y;
	}
}

class Test007 {
	int x;
	bar foo (tar here) {  
		x = a + b+ c;
		return x;
	}
	
	void main() {
		  int g;
		  int b = 1;
		  
		  bar mine = new bar();
		  tar nope = new tar();
		  g = (int) b;
		  nope =(tar) mine;
		  mine =(bar) nope;
		  g = mine.sumthis(g,b);
		 foo(nope) ;
		 return;
		 
	  }
}
