package IR.Tree;

public class CreateObjectNode extends BlockStatementNode
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
