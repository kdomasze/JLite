package IR;

import Tree.*;

public class VarDescriptor extends Descriptor
{
	private TypeDescriptor type;
	private ExpressionNode value;
	
	//constructor:
	public VarDescriptor(TypeDescriptor T, ExpressionNode V)
	{
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
