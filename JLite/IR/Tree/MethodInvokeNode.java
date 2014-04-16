package IR.Tree;

import java.util.Set;

public class MethodInvokeNode extends ExpressionNode
{
	private NameNode Name;
	private Set<TreeNode> ArgumentSet;
	
	//constructor
	public MethodInvokeNode(NameNode N)
	{
		Name = N;
	}
	
	public void OverrideArgumentMap(Set<TreeNode> newSet)
	{
		ArgumentSet = newSet;
	}
	
	//accessors
	public NameNode getNameNode()
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