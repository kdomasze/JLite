package IR.Tree;

public class CreateObjectNode extends ExpressionNode
{
	TypeNode Type;

	//constructor
	public CreateObjectNode(TypeNode T)
	{
		Type = T;
	}
	
	public TypeNode getType()
	{
		return Type;
	}

	public String toString()
	{
		return Type.toString();
	}	
}
