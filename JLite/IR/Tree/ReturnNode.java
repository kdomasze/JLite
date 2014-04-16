package IR.Tree;

public class ReturnNode extends BlockStatementNode
{
	private TreeNode ReturnStatement;
	
	// constructor
	public ReturnNode(TreeNode rs)
	{
		ReturnStatement = rs;
	}
	
	// get method
	public TreeNode getReturnStatement()
	{
		return ReturnStatement;
	}
	
	public String toString()
	{
		return "[Return: " + ReturnStatement + "]";
	}
}
