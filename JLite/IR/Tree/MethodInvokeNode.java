package IR.Tree;

import IR.FieldDescriptor;
import IR.SymbolTable;

public class MethodInvokeNode extends ExpressionNode
{
	private TreeNode Name;
	private SymbolTable ArgumentSymbolTable;//SymbolTable for Arguments
	
	//constructor
	public MethodInvokeNode(TreeNode n, SymbolTable parent)
	{
		Name = n;
		ArgumentSymbolTable = new SymbolTable(parent); 
	}
	
	public void addArgument(FieldDescriptor field)
	{
		ArgumentSymbolTable.add(field);
	}
	
	//accessors
	public TreeNode getNameNode()
	{
		return Name;
	}
	
	public SymbolTable getAgrumentSymbolTable()
	{
		return ArgumentSymbolTable;
	}
	
	public String toString()
	{
		return "[MethodInvoke: " + Name + ": " + ArgumentSymbolTable.toString() + "]";
	}
}