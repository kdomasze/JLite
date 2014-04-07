package IR.Tree;

public class VarDescriptor extends Descriptor
{
	private NameDescriptor name;
	private TypeDescriptor type;
	private int value;
	
	//constructor:
	public VarDescriptor(NameDescriptor N, TypeDescriptor T, int V)
	{
		name = N;
		type = T;
		value = V;
	}
	
	//getMethods
	public String getName()
	{
		return name.getName();
	}
	
	public String getType()
	{
		return type.getType();
	}
	
	public int getValue()
	{
		return value;
	}
}
