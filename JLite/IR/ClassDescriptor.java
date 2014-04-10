package IR;

import java.util.HashMap;
import java.util.Map;

public class ClassDescriptor extends Descriptor
{
	private Map<NameDescriptor, FieldDescriptor> FieldDescriptorMap = new HashMap<NameDescriptor, FieldDescriptor>(); // hashMap for field descriptors
	private Map<NameDescriptor, MethodDescriptor> MethodDescriptorMap = new HashMap<NameDescriptor, MethodDescriptor>(); // hashMap for method descriptors
	
	private ClassDescriptor superClass; // holds reference for super/parent class
	
	// put methods
	public void addFieldDescriptor(NameDescriptor identifier, FieldDescriptor fD)
	{
		FieldDescriptorMap.put(identifier, fD);
	}
	
	public void addMethodDescriptor(NameDescriptor identifier, MethodDescriptor mD)
	{
		MethodDescriptorMap.put(identifier, mD);
	}
	
	public void defineSuperClass(ClassDescriptor sC)
	{
		superClass = sC;
	}
	
	// get methods
	public FieldDescriptor getFieldDescriptor(NameDescriptor identifier)
	{
		return FieldDescriptorMap.get(identifier);
	}
	
	public MethodDescriptor getMethodDescriptor(NameDescriptor identifier)
	{
		return MethodDescriptorMap.get(identifier);
	}
	
	public ClassDescriptor getSuperClass()
	{
		return superClass;
	}
}
