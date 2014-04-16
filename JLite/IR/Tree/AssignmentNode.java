package IR.Tree;

public class AssignmentNode extends ExpressionNode
{
	private TreeNode leftHandSide;
	private TreeNode rightHandSide;
	
	public AssignmentNode(TreeNode left, TreeNode right)
	{
		leftHandSide = left;
		rightHandSide = right;
	}
	
	// get methods
	public TreeNode getLeftHandSide()
	{
		return leftHandSide;
	}
	
	public TreeNode getRightHandSide()
	{
		return rightHandSide;
	}
	
	public String toString()
	{
		return "[Assignment: " + leftHandSide + " = " + rightHandSide + "]";
	}
}
