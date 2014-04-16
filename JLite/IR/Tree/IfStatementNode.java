package IR.Tree;

public class IfStatementNode extends BlockStatementNode
{
	private TreeNode condition;
	private TreeNode block;
	
	//Constructor
	IfStatementNode(TreeNode condition2, TreeNode block2)
	{
		condition = condition2;
		block = block2;
	}
	
	//Get methods
	public TreeNode getOperation()
	{
		return condition;
	}
	
	public TreeNode getBlock()
	{
		return block;
	}
	
	public String toString()
	{
		return "[If: " + condition + " -> " + block + "]";
	}
}