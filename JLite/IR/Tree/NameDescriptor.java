package IR.Tree;

public class NameDescriptor extends Descriptor
{
	private String name;
	
	// Constructor
	public NameDescriptor(String n)
	{
		name = n;
	}
	
	// get method
	public String getName()
	{
		return name;
	}
}
