
class fibonacci 
{	
	int fibNum;

	void setNum(int num)
	{
		fibNum = num;
	}
	
	int toFibonacci(int x)
	{
		if(x < 2)
		{
			if(x<0)
				return 0;
			else
				return 1;
		}
		else
		{
			return toFibonacci(x-1) + toFibonacci(x-2);
		}
	}
	
	void main(System x)
	{
		fibonacci f = new fibonacci();
		int i = x.input();
		int d = 0;	
		while(d < i)
		{
			x.output(f.toFibonacci(d));
			d = d + 1;
		}
	}
}
