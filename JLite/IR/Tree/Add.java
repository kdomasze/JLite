package IR.Tree;

import java.util.Vector;

public class Add extends expression
{
	Vector<expression>Identifier = new Vector<expression>(2,1);
	
	public Add(expression Expression)
	{
		Identifier.add(Expression);
	}
	
	public void addLiteral(int Literal)
	{
		expression tempExpression = new literal(Literal);
		Identifier.add(tempExpression);
		
	}
	public void addVariable(String identifier)
	{
		expression tempExpression = new variable_call(identifier);
		Identifier.add(tempExpression);
		
	}
	public void addMethodInvoke(String identifier)
	{
		expression tempExpression = new method_invoke(identifier);
		Identifier.add(tempExpression);
		
	}
}