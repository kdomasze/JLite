package IR.Tree;

import java.util.HashMap;
import java.util.Map;

public class ClassDescriptor extends Descriptor
{
	private Map<String, FieldDescriptor> FieldDescriptorMap = new HashMap<String, FieldDescriptor>(); // hashMap for field descriptors
	private Map<String, MethodDescriptor> MethodDescriptorMap = new HashMap<String, MethodDescriptor>(); // hashMap for method descriptors
	
	private ClassDescriptor superClass; // holds reference for super/parent class
	
	// put methods
	public void addFieldDescriptor(String identifier, FieldDescriptor fD)
	{
		FieldDescriptorMap.put(identifier, fD);
	}
	
	public void addMethodDescriptor(String identifier, MethodDescriptor mD)
	{
		methodDescriptorMap.put(identifier, mD);
	}
	
	public void defineSuperClass(ClassDescriptor sC)
	{
		superClass = sC;
	}
	
	// get methods
	public FieldDescriptor getFieldDescriptor(String identifier)
	{
		return FieldDescriptorMap.get(identifier);
	}
	
	public MethodDescriptor getMethodDescriptor(String identifier)
	{
		return MethodDescriptorMap.get(identifier);
	}
	
	public ClassDescriptor getSuperClass()
	{
		return superClass;
	}
}
