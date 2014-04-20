package IR.Tree;

import java.util.Vector;

public class BlockNode extends TreeNode
{
	private BlockStatementNode statement;
	private Vector<BlockStatementNode> blockStatementVector = new Vector<BlockStatementNode>(1,1);
	
	//constructor:
	public BlockNode()
	{
	}
	
	public void addblockStatement(BlockStatementNode s)
	{
		blockStatementVector.add(s);
	}
	
	//get methods
	public BlockStatementNode getBlockStatement()
	{
		return statement;
	}
	
	public Vector<BlockStatementNode> getblockStatementVector()
	{
		return blockStatementVector;
	}
	
	public String toString()
	{
		return "[Block: " + blockStatementVector +  "]";
	}
}
