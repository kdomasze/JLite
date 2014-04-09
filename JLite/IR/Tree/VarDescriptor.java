package IR.Tree;

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
	public NameNode getName()
	{
		return name.getName();
	}
	
	public TypeNode getType()
	{
		return type.getType();
	}
	
	public ExpressionNode getValue()
	{
		return value;
	}
}
