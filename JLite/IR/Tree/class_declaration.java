package IR.Tree;

import java.util.Vector;

public class class_declaration
{
	String name;
	Vector<field> field_list = new Vector<field>(1,1);
	Vector<method> method_list = new Vector<method>(1,1);

	// Constructor
	public class_declaration(String n)
	{
		name = n;
	}

	// add field to field_list
	public void addField(type Type, String name)
	{
		field tempField = new field(Type, name);
		field_list.add(tempField);
	}

	// add method to method_list
	public void addMethod(String name, type returnType)
	{
		method tempMethod = new method(name, returnType);
		method_list.add(tempMethod);
	}
}