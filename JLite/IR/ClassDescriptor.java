package IR;

import java.util.HashMap;
import java.util.Map;

public class ClassDescriptor extends Descriptor
{
	private SymbolTable FieldDescriptorSymbolTable = new SymbolTable(); // Symbol Table for field descriptors
	private SymbolTable MethodDescriptorSymbolTable = new SymbolTable(); // Symbol Table for method descriptors
	
	private ClassDescriptor superClass; // holds reference for super/parent class
	
	// put methods
	public void defineSuperClass(ClassDescriptor sC)
	{
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
