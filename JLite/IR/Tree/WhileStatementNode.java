package IR.Tree;

public class WhileStatementNode extends BlockStatementNode
{
	private OpNode condition;
	private BlockNode block;

	//Constructor
	WhileStatementNode(OpNode c, BlockNode b)
	{
		condition = c;
		block = b;
	}

	//Get methods
	public OpNode getOperation()
	{
		return condition;
	}

	public BlockNode getBlock()
	{
		return block;
	}
	
	public String toString()
	{
		return "[While: " + condition + ": " + block + "]";
	}
}