package IR.Tree;

public class MethodInvokeNode extends ExpressionNode
{
	private NameDescriptor name;
	private ExpressionNode args;
	
	//constructor
	public MethodInvokeNode(NameDescriptor N, ExpressionNode A)
	{
		name = N;
		args = A;

	}
	
}