package IR.Tree;

public class IfStatementNode extends BlockStatementNode
{
	private OpNode condition;
	private BlockNode block;
	
	//Constructor
	IfStatementNode(OpNode condition2, BlockNode block2)
	{
		condition = condition2;
		block = block2;
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
		return "[If: " + condition + " -> " + block + "]";
	}
}