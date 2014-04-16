package IR;

import IR.Tree.TreeNode;
import IR.Tree.TypeNode;

public class TypeDescriptor extends Descriptor
{
	private TypeNode Type;
	
	// Constructor
	public TypeDescriptor(String name, TypeNode type2)
	{
		super(name);
		Type = type2;
	}

	// get method
	public String getType()
	{
		return Type.getType();
	}
	
	public String toString()
	{
		return Type.toString();
	}
}
