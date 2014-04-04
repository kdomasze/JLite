package IR.Tree;

import java.util.Vector;

public class method
{
    String name;
    type returnType;
	
    Vector<formal_parameter> formal_parameter_list = new Vector<formal_parameter>(1,1);
	Vector<block_statement> block_statement_list = new Vector<block_statement>(1,1);
	
	public method(String n, type rt)
	{
		name = n;
		returnType = rt;
	}
}
