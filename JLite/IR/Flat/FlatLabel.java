package IR.Flat;

public class FlatLabel extends FlatNode 
{
	int numL;
	
	public FlatLabel(int num)
	{
		numL = num;
	}

	
	public String toString()
	{
		return "L"+numL;
	}
}
