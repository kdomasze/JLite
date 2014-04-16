package IR.Tree;

import java.util.Set;

import IR.Descriptor;

public class MethodInvokeNode extends ExpressionNode
{
	private TreeNode Name;
	private Set<TreeNode> ArgumentSet;
	
	//constructor
	public MethodInvokeNode(TreeNode n)
	{
		Name = n;
	}
	
	public void setArgumentSet(Set<TreeNode> newSet)
	{
		ArgumentSet = newSet;
	}
	
	//accessors
	public TreeNode getNameNode()
	{
		return Name;
	}
	
	public Set<TreeNode> getAgruments()
	{
		return ArgumentSet;
	}
	
	public String toString()
	{
		return "[MethodInvoke: " + Name + ": " + ArgumentSet + "]";
	}
}