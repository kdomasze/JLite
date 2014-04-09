package IR.Tree;

public class MethodInvokeNode extends BlockStatementNode
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