package IR.Tree;

public class TypeDescriptor extends Descriptor
{
	private String Type;
	
	// Constructor
	public TypeDescriptor(String t)
	{
		Type = t;
	}
	
	public String getType()
	{
		return Type;
	}
}
