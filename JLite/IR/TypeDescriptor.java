package IR;

import IR.Tree.TreeNode;
import IR.Tree.TypeNode;

public class TypeDescriptor extends Descriptor
{
	private TreeNode Type;
	
	// Constructor
	public TypeDescriptor(String name, TreeNode type2)
	{
		super(name);
		Type = type2;
	}

	// get method
	public String getType()
	{
		return ((TypeNode) Type).getType();
	}
}
