package IR.Tree;

import java.util.HashMap;
import java.util.Map;

public class Descriptor 
{
	private Map<String, ClassDescriptor> ClassDescriptorMap = new HashMap<String, ClassDescriptor>(); // hashMap for class descriptors
	
	// put methods
	public void addClassDescriptor(String identifier, ClassDescriptor cD)
	{
		ClassDescriptorMap.put(identifier, cD);
	}
	
	// get methods
	public ClassDescriptor getClassDescriptor(String identifier)
	{
		return ClassDescriptorMap.get(identifier);
	}
}
