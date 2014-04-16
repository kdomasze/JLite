package IR;

public class ClassDescriptor extends Descriptor
{
	private SymbolTable FieldDescriptorSymbolTable; // Symbol Table for field descriptors
	private SymbolTable MethodDescriptorSymbolTable; // Symbol Table for method descriptors
	
	private String superClass; // holds reference for super/parent class
	
	// constructor
	public ClassDescriptor(String name, SymbolTable parent, String sC)
	{
		super(name);
		FieldDescriptorSymbolTable = new SymbolTable(parent);
		MethodDescriptorSymbolTable = new SymbolTable(parent);
		superClass = sC;
	}
	
	// put methods
	public void addFieldDescriptor(FieldDescriptor field)
	{
		FieldDescriptorSymbolTable.add(field);
	}
	
	public void addMethodDescriptor(MethodDescriptor method)
	{
		MethodDescriptorSymbolTable.add(method);
	}
	
	// get methods
	public SymbolTable getFieldDescriptorTable()
	{
		return FieldDescriptorSymbolTable;
	}
	
	public SymbolTable getMethodDescriptorTable()
	{
		return MethodDescriptorSymbolTable;
	}
	
	public String getSuperClass()
	{
		return superClass;
	}
	
	public String toString()
	{
		return "\n\tFields: " + FieldDescriptorSymbolTable.toString() + "\n\tMethods: " + MethodDescriptorSymbolTable.toString() + "\n\tSuper Class: [" + superClass + "]\n";
	}
}
