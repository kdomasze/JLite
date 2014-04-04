package IR.Tree;

public class ifstatement extends block_statement
{
	String condition;
	String identifier;
	int literal;
	
	Vector<block_statement> block_statement_list = new Vector<block_statement>(1,1);
}
