package IR.Tree;

public class local_variable_declaration extends block_statement
{
    type Type;
	String identifier;
	int literal;

	// Constructor
	public local_variable_declaration(type t, String i, int l)
	{
		Type = t;
		identifier = i;
		literal = l;
	}
}
