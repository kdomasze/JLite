package IR;

import IR.Tree.ExpressionNode;

public class VarDescriptor extends Descriptor
{
	private TypeDescriptor type;
	private ExpressionNode value;
	
	//constructor:
	public VarDescriptor(String name, TypeDescriptor T, ExpressionNode V)
	{
		super(name);
		type = T;
		value = V;
	}
	
	//getMethods
	
	public TypeDescriptor getType()
	{
		return type;
	}
	
	public ExpressionNode getValueNode()
	{
		return value;
	}
}
