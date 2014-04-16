package IR.Tree;

import java.util.Set;

import IR.*;

/**
 * It is the super class of any other type of Abstract Semantic Tree Node.
 * 
 */
public class TreeNode
{
	public static final int INDENT = 2;
	int numLine = -1;
	
	ClassDescriptor Class;
	FieldDescriptor Field;
	MethodDescriptor Method;
	VarDescriptor Var;
	TypeDescriptor Type;
	Set<TreeNode> Tree;

	public String printNode(int indent)
	{
		return null;
	}

	public static String printSpace(int x)
	{
		String sp = "";
		for (int i = 0; i < x; i++)
			sp += " ";
		return sp;
	}

	public int kind()
	{
		throw new Error();
	}

	public void setNumLine(int numLine)
	{
		this.numLine = numLine;
	}

	public int getNumLine()
	{
		return this.numLine;
	}

}