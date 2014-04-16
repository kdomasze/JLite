package IR.Tree;

public class WhileStatementNode extends BlockStatementNode
{
	private TreeNode condition;
	private TreeNode block;

	//Constructor
	WhileStatementNode(TreeNode condition2, TreeNode block2)
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
		return "[While: " + condition + ": " + block + "]";
	}
}