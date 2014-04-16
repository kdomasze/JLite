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
	
	// override symbol table functions
	public void overrideFieldDescriptorSymbolTable(SymbolTable newSymbolTable)
	{
		FieldDescriptorSymbolTable = newSymbolTable;
	}
	
	public void overrideMethodDescriptorSymbolTable(SymbolTable newSymbolTable)
	{
		MethodDescriptorSymbolTable = newSymbolTable;
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
}
