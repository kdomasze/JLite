
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
		fact fa=new fact();
		factorial fac = new factorial();
		
		i = x.input();
		
		fa.setNumber(i);
		fac.setNumber(i);
		x.output(fa.factRec(i));
		x.output(fac.factRec(i));
		

	}
}
class factorial
{
	int n;
	void setNumber(int num) 
	{
		n = num;
	}

	int factRec(int num) 
	{
		if (num == 1) 
		{
			return 1;
		}
		return (factRec(num-1) * num);
	}
}
class fact extends factorial
{

	

	int factRec(int num) 
	{
		if (num == 1) 
		{
			return 1;
		}
		return (num);
	}

	int factIter() 
	{
		int i;
		int f;

		i=1;
		f=1;

		while (i < (n +1)) 
		{
			f = f*i;
			i = i +1;
		}

		return f;
	}
}
