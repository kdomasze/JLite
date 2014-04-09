package IR.Tree;


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
	public String getFieldName()
	{
		return Name.getName();
	}
	
	public String getFieldType()
	{
		return Type.getType();
	}
}
