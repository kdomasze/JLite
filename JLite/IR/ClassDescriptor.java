package IR;

public class ClassDescriptor extends Descriptor
{
	private SymbolTable FieldDescriptorSymbolTable; // Symbol Table for field descriptors
	private SymbolTable MethodDescriptorSymbolTable; // Symbol Table for method descriptors
	
	private ClassDescriptor superClass; // holds reference for super/parent class
	
	// put methods
	public void defineSuperClass(SymbolTable parent, ClassDescriptor sC)
	{
		FieldDescriptorSymbolTable = new SymbolTable(parent);
		MethodDescriptorSymbolTable = new SymbolTable(parent);
		superClass = sC;
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
	
	public ClassDescriptor getSuperClass()
	{
		return superClass;
	}
}
