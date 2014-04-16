package IR.Tree;

public class NameNode extends ExpressionNode
{
	private String name;
	
	// constructor
	public NameNode(String n)
	{
		name = n;
	}
	
	// get method
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}
}
