package IR.Tree;

public class CreateObjectNode extends ExpressionNode
{
	TypeNode Type;

	//constructor
	public CreateObjectNode(TypeNode T)
	{
		Type = T;
	}

	public String toString()
	{
		return Type.toString();
	}	
}
