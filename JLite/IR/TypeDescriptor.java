package IR;

import IR.Tree.TypeNode;

public class TypeDescriptor extends Descriptor
{
	private TypeNode Type;
	
	// Constructor
	public TypeDescriptor(String name, TypeNode t)
	{
		super(name);
		Type = t;
	}

	// get method
	public String getType()
	{
		return Type.getType();
	}
}
