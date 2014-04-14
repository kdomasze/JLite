package IR;

import Tree.*;

public class TypeDescriptor extends Descriptor
{
	private TypeNode Type;
	
	// Constructor
	public TypeDescriptor(TypeNode t)
	{
		Type = t;
	}

	// get method
	public String getType()
	{
		return Type.getType();
	}
}
