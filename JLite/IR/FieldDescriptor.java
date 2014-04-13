package IR;

public class FieldDescriptor extends Descriptor
{
	private TypeDescriptor Type;
	private ClassDescriptor ParentClass;
	
	// constructor
	public FieldDescriptor(TypeDescriptor t, ClassDescriptor pc)
	{
		Type = t;
		ParentClass = pc;
	}
	
	// get methods
	public TypeDescriptor getFieldType()
	{
		return Type;
	}
	
	public ClassDescriptor getParentClass()
	{
		return ParentClass;
	}
}
