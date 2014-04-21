package IR.Tree;

public class IfStatementNode extends BlockStatementNode
{
	private ExpressionNode condition;
	private BlockNode block;
	
	//Constructor
	IfStatementNode(ExpressionNode condition2, BlockNode block2)
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
		return "[If: " + condition + " -> " + block + "]";
	}
}