package IR.Tree;

public class IfStatementNode extends BlockStatementNode
{
	private OpNode condition;
	private BlockNode block;
	
	//Constructor
	IfStatementNode(OpNode c, BlockNode b)
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
		return "[If: " + condition + " -> " + block + "]";
	}
}