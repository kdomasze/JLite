package IR.Tree;

public class IfStatementNode extends BlockStatementNode
{
	private ExpressionNode condition;
	private BlockNode block;
	private BlockNode blocke;
	
	//Constructor
	IfStatementNode(ExpressionNode condition2, BlockNode block2, BlockNode blocke2)
	{
		condition = condition2;
		block = block2;
		blocke = blocke2;
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
	
	public BlockNode getBlocke()
	{
		return blocke;
	}
	
	public String toString()
	{
		return "[If: " + condition + " -> " + block + "else" + blocke + "]";
	}
}