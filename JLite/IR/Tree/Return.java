package IR.Tree;

public class Return
{
	type returnType;
	expression returnValue;
	
	public Return(type rt, expression rv)
	{
		returnType = rt;
		returnValue = rv;
	}
}