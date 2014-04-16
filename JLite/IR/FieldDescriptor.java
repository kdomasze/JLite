package IR;

public class FieldDescriptor extends Descriptor
{
	private TypeDescriptor Type;
	
	// constructor
	public FieldDescriptor(String name, TypeDescriptor t)
	{
		super(name);
		Type = t;
	}
	
	// get methods
	public TypeDescriptor getFieldType()
	{
		return Type;
	}
	
	public String toString()
	{
		return Type.toString();
	}
}
