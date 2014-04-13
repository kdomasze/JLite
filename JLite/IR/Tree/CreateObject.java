package IR.Tree;

public class CreateObject extends BlockStatementNode
{
	TypeNode Type;
	
	//constructor
	public CreateObject(TypeNode T)
	{
		Type = T;
	}
}
