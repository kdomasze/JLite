package IR.Tree;

public class TypeDescriptor extends Descriptor
{
	private TypeNode Type;
	
	// Constructor
	public TypeDescriptor(TypeNode t)
	{
		Type = t;
	}
	
	public TypeNode getType()
	{
		return Type;
	}
}
