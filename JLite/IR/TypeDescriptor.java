package IR;

import Tree.*;

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
