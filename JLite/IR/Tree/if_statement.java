package IR.Tree;

import java.util.Vector;

public class if_statement extends block_statement
{
	String condition;
	String identifier;
	int literal;
	
	Vector<block_statement> block_statement_list = new Vector<block_statement>(1,1); // contains all of the block statements in the if-statement
	Vector<block_statement> else_block_statement_list = new Vector<block_statement>(1,1); // contains all of the block statements in the if-statement
	
	// Constructor
	public if_statement(String c, String id, int lit)
	{
		condition = c;
		identifier = id;
		literal = lit;
	}
	
	// adds an if-statement to the block_statement_list
	public void addIfStatement(String condition, String identifier, int literal, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new if_statement(condition, identifier, literal);
		block_list.add(tempBlockStatement);
	}
	
	// adds a while-statement to the block_statement_list
	public void addWhileStatement(String condition, String identifier, int literal, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new while_statement(condition, identifier, literal);
		block_list.add(tempBlockStatement);
		
	}
	
	// adds a variable declaration to the block_statement_list
	public void addLocalVarDeclaration(type Type, String identifier, int literal, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new local_variable_declaration(Type, identifier, literal);
		block_list.add(tempBlockStatement);
	}
	
	// adds an assignment to the block_statement_list
	public void addAssignment(String identifier, expression Expression, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new assignment(identifier, Expression);
		block_list.add(tempBlockStatement);
	}
	
	// adds an add to the block_statement list
	public void addAdd(expression Expression, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new Add(Expression);
		block_list.add(tempBlockStatement);
	}
	
	// adds a multiply to the block_statement list
	public void addMultiply(expression Expression, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new multiply(Expression);
		block_list.add(tempBlockStatement);
	}
	
	// adds a subtract to the block_statement list
	public void addSubtract(expression Expression, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new subtract(Expression);
		block_list.add(tempBlockStatement);
	}
	
	// adds a create_object to the block_statement_list
	public void addcreateObject(type Type, Vector <block_statement>block_list)
	{
		block_statement tempBlockStatement = new create_object(Type);
		block_list.add(tempBlockStatement);
	}
}