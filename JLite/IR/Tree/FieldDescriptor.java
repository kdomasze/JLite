package IR.Tree;

import java.util.HashMap;
import java.util.Map;

public class FieldDescriptor extends Descriptor
{
	private NameDescriptor Name;
	private TypeDescriptor Type;
	
	// constructor
	public FieldDescriptor(NameDescriptor n, TypeDescriptor t)
	{
		Name = n;
		Type = t;
	}
	
	// get methods
	public NameDescriptor getFieldName()
	{
		return Name.getName();
	}
	
	public TypeDescriptor getFieldType()
	{
		return Type.getType();
	}
}
