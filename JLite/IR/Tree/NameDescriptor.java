package IR.Tree;

public class NameDescriptor extends Descriptor
{
	private NameNode name;
	
	// Constructor
	public NameDescriptor(NameNode n)
	{
		name = n;
	}
	
	// get method
	public NameNode getName()
	{
		return name;
	}
}
