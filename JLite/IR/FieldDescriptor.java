package IR;

public class FieldDescriptor extends Descriptor
{
	private TypeDescriptor Type;
	
	// constructor
	public FieldDescriptor(TypeDescriptor t)
	{
		Type = t;
	}
	
	// get methods
	public TypeDescriptor getFieldType()
	{
		return Type;
	}
}
