package IR;

public class VarDescriptor extends Descriptor
{

	// MIGHT NEED TO REDO
	private NameDescriptor name;
	private TypeDescriptor type;
	private ExpressionNode value;
	
	//constructor:
	public VarDescriptor(NameDescriptor N, TypeDescriptor T, ExpressionNode V)
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
	
	public ExpressionNode getValue()
	{
		return value;
	}
}
