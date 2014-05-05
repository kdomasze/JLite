package IR.Flat;

import IR.*;

public class TempDescriptor extends Descriptor
{
	TypeDescriptor type;
	int literal;
	
	private boolean litUndefined = true;

	public TempDescriptor(String name, TypeDescriptor td)
	{
		super(name);
		type = td;
	}

	public TempDescriptor(String name, TypeDescriptor td, int l)
	{
		super(name);
		type = td;
		literal = l;
		litUndefined = false;
	}

	public TypeDescriptor getType()
	{
		return type;
	}

	public int getLiteral()
	{
		return literal;
	}
	
	public String toString()
	{
		if(litUndefined == true)
		{
			return name + "(type: " + type + ")";
		}
		else
		{
			return name + "(type: " + type + ", value: " + literal + ")";
		}
	}
}
