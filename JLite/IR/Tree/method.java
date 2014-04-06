package IR.Tree;

import java.util.Vector;

public class method
{
    String name;
    
	Return returnStatement;
	
    Vector<formal_parameter> formal_parameter_list = new Vector<formal_parameter>(1,1); // contains all of the method header components
	Vector<block_statement> block_statement_list = new Vector<block_statement>(1,1); // contains all of the block statements within the method
	
	// Constructor
	public method(String n, Return r)
	{
		name = n;
		returnStatement = r;
	}
	
	// adds a parameter into the formal_parameter_list
	public void addParameter(type t, String n)
	{
		formal_parameter tempParameter = new formal_parameter(t, n);
		formal_parameter_list.add(tempParameter);
	}
	
	// adds an if-statement to the block_statement_list
	public void addIfStatement(String condition, String identifier, int literal)
	{
		block_statement tempBlockStatement = new if_statement(condition, identifier,literal);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds a variable declaration to the block_statement_list
	public void addLocalVarDeclaration(type Type, String identifier, int literal)
	{
		block_statement tempBlockStatement = new local_variable_declaration(Type, identifier, literal);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds an assignment to the block_statement_list
	public void addAssignment(String identifier, int literal)
	{
		block_statement tempBlockStatement = new assignment(identifier, literal);
		block_statement_list.add(tempBlockStatement);
	}
}
