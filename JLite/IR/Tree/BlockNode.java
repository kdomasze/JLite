package IR.Tree;

import java.util.Vector;

/*
	NOTE: Commented out all instances of "statement" due to it
	seeming to be unneeded.
*/

public class BlockNode extends TreeNode
{
	// private BlockStatementNode statement;
	private Vector<BlockStatementNode> blockStatementVector = new Vector<BlockStatementNode>(1,1);
	
	//constructor:
	public BlockNode()
	{
		
	}
	/*
	public BlockNode(BlockStatementNode s)
	{
	}
	*/
	
	public void addblockStatement(BlockStatementNode s)
	{
		blockStatementVector.add(s);
	}
	
	//get methods
	/*
	public BlockStatementNode getBlockStatement()
	{
		return statement;
	}
	*/
	
	public Vector<BlockStatementNode> getblockStatementVector()
	{
		return blockStatementVector;
	}
	
	public String toString()
	{
		//return "[Block: " + statement + "]";
		return "[Block: " + blockStatementVector + "]";
	}
}
