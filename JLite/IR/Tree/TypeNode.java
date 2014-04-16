package IR.Tree;

public class TypeNode extends ExpressionNode
{
	private String Type;

	// Constructor
	public TypeNode(String t)
	{
		Type = t;
	}

	// get method
	public String getType()
	{
		return Type;
	}
	
	public String toString()
	{
		return Type;
	}
}
