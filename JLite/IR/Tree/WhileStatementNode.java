package IR.Tree;

public class WhileStatementNode extends BlockStatementNode
{
	private ExpressionNode condition;
	private BlockNode block;

	//Constructor
	WhileStatementNode(ExpressionNode condition2, BlockNode block2)
	{
		condition = condition2;
		block = block2;
	}

	//Get methods
	public ExpressionNode getOperation()
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