package IR.Flat;

public class GoFlatLabel extends FlatNode
{
	String label;
	int numL;
	
	public GoFlatLabel(String l, int num)
	{
		numL = num;
		label = l;
	}
	
	public String toString()
	{
		return label + numL;
	}
}
