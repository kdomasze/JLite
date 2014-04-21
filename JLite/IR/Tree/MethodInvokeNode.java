package IR.Tree;

import java.util.Vector;

import IR.FieldDescriptor;
import IR.SymbolTable;

public class MethodInvokeNode extends ExpressionNode
{
	private ExpressionNode Name;
	private Vector<ExpressionNode> ArgumentVector = new Vector<ExpressionNode>();//SymbolTable for Arguments
	
	//constructor
	public MethodInvokeNode(ExpressionNode n)
	{
		Name = n;
	}
	
	public void addArgument(ExpressionNode expression)
	{
		ArgumentVector.add(expression);
	}
	
	//accessors
	public ExpressionNode getNameNode()
	{
		return Name;
	}
	
	public Vector<ExpressionNode> getArgumentVector()
	{
		return ArgumentVector;
	}
	
	public String toString()
	{
		return "[MethodInvoke: " + Name + ": " + ArgumentVector + "]";
	}
}