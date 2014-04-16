package IR.Tree;

public class FieldAccessNode extends ExpressionNode
{
	private NameNode Name;
	private TypeNode Type;

	// Constructor
	public FieldAccessNode(NameNode N, TypeNode T)
	{
		Name = N;
		Type = T;
	}

	// get methods
	public String getName()
	{
		return Name.getName();
	}

	public String getType()
	{
		return Type.getType();
	}
	
	public String toString()
	{
		return "[FieldAccess: " + Name + ": " + Type + "]";
	}
}
