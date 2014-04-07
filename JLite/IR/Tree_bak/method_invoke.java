package IR.Tree;

import java.util.Vector;

public class method_invoke extends expression
{
	String identifier;
	Vector<argument> argument_list = new Vector<argument>(1,1); // contains all of the arguments
	
	public method_invoke(String i)
	{
		identifier = i;
	}
	
	public void addArgument(argument a)
	{
		argument_list.add(a);
	}
}