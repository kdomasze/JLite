package IR.Tree;

import java.util.Vector;

public class while_statement extends block_statement
{
	String condition; 
	String identifier;
	int literal;
	
	Vector<block_statement> block_statement_list = new Vector<block_statement>(1,1);
	
	public while_statement (String c, String id, int lit)
	{
		condition = c;
		identifier = id;
		literal = lit;
	}
}
