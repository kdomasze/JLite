package IR.Flat;

import IR.*;

import java.util.*;
import java.io.*;

public class BuildCode
{
	HashMap<Descriptor, Vector<Descriptor>> TACParent;
	public HashMap<Descriptor, FlatNode> TAC;

	public BuildCode(HashMap<Descriptor, Vector<Descriptor>> tacp, HashMap<Descriptor, FlatNode> tac)
	{
		TACParent = tacp;
		TAC = tac;
	}

	public void buildCode()
	{
//		for (Vector<Descriptor> descriptorVector : TACParent.values())
//		{
//			
//			if(descriptorVector.get(0) instanceof FieldDescriptor)
//			{
//				// Fields Desc
//			}
//			else if(descriptorVector.get(0) instanceof MethodDescriptor)
//			{
//				// Methods Desc
//			}
//			
//			for(Descriptor desc : descriptorVector)
//			{
//				FlatNode flat = TAC.get(desc);
//				
//				if (flat instanceof FlatFieldNode)
//				{			
//					// Field Nodes
//				}
//				else if (flat instanceof FlatMethod)
//				{
//					MethodDescriptor fm = ((FlatMethod) flat).getMethod();
//				
//					for (int i = 0; i < fm.numParameters(); i++)
//					{
//						if (fm.numParameters() == i + 1)
//						{
//							continue;
//						}
//					}
//
//					FlatNode f = flat.getNext(0);
//					while (true)
//					{
//		
//						if (f.next.size() == 0)
//						{
//							break;
//						}
//		
//						f = f.next.get(0);
//					}
//				}
//			}
//		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		/* Create output streams to write to */
		/* Refer to PrintWriter Class in JAVA LIB */
		
		/* Build the virtual dispatch tables */

		/* Output includes */

		/* Output Structures */
		generateClassDefs();
		// Output the C class declarations
		// These could mutually reference each other

		// Output function prototypes and structures for parameters

		/* Build the actual methods */

		/* Generate main method */

		/* Close files */
	}
	
	private void generateClassDefs()
	{
		/* Create output streams to write to */
		/* Refer to PrintWriter Class in JAVA LIB */
		File classDefs = new File("classdefs.h");
		PrintWriter classDefsPW;
		
		int count = 0;
		
		try
		{
			classDefs.createNewFile();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			throw new Error("It's Oracle's fault.");
		}

		try
		{
			classDefsPW = new PrintWriter(classDefs);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new Error("This is how the world ends.");
		}
		
		/* Output Structures */
		
		
		// Output the C class declarations
		// These could mutually reference each other
		
		classDefsPW.append("extern int classsize[];\n");
		classDefsPW.append("extern int supertypes[];\n\n");
		
		for (Descriptor key : TACParent.keySet())
		{
			classDefsPW.append("struct " + key.getSymbol());
			
			classDefsPW.append("\n{\n");
			
			classDefsPW.append("\tint type;\n");
			
			for(Descriptor desc : TACParent.get(key))
			{
				FlatNode flat = TAC.get(desc);
				
				if (flat instanceof FlatFieldNode)
				{			
					// Field Nodes
					FlatFieldNode ffn = (FlatFieldNode)flat;
					
					if(ffn.dst.getType().getSymbol() == "int")
					{
						classDefsPW.append("\t" + ffn.dst.getType().getSymbol() + " " + ffn.dst.getSymbol() + ";\n");
					}
					else
					{
						classDefsPW.append("\tstruct " + ffn.dst.getType().getSymbol() + " *" + ffn.dst.getSymbol() + ";\n");
					}
				}
			}

			classDefsPW.append("};\n\n");
			count++;
		}
		
		classDefsPW.close();
		
		generateStructDefs(count);
	}
	
	private void generateStructDefs(int count)
	{
		File structDefs = new File("structdefs.h");
		PrintWriter structDefsPW;
		
		try
		{
			structDefs.createNewFile();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			throw new Error("It's Oracle's fault.");
		}

		try
		{
			structDefsPW = new PrintWriter(structDefs);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new Error("This is how the world ends.");
		}
		
		structDefsPW.printf("#ifndef STRUCTDEFS_H\n#define STRUCTDEFS_H\n#include \"classdefs.h\"\n#define NUMCLASSES " + count + "\n#endif");
	
		structDefsPW.close();
	}

	// /** Example code to generate code for FlatMethod fm. */
	// /** DFS algorithm **/
	// private void generateFlatMethod(FlatMethod fm, PrintWriter output)
	// {
	// MethodDescriptor md = fm.getMethod();
	// if (fm.numNext() == 0)
	// return;
	//
	// /*
	// * EECS40 Implement: You need some code to print out the method
	// * declaration, local variable declarations and stuff like that...
	// */
	//
	// /* Assign labels to FlatNode's if necessary. */
	// Hashtable<FlatNode, Integer> nodetolabel = assignLabels(fm);
	// /* Do the actual code generation */
	// FlatNode current_node = null;
	// HashSet tovisit = new HashSet();
	// HashSet visited = new HashSet();
	// tovisit.add(fm.getNext(0));
	// while (current_node != null || !tovisit.isEmpty())
	// {
	// if (current_node == null)
	// {
	// current_node = (FlatNode) tovisit.iterator().next();
	// tovisit.remove(current_node);
	// }
	// visited.add(current_node);
	// if (nodetolabel.containsKey(current_node))
	// output.println("L" + nodetolabel.get(current_node) + ":");
	// if (current_node.numNext() == 0)
	// {
	// output.print("   ");
	// /* EECS40 Implement : */
	// generateFlatNode(fm, current_node, output);
	// if (current_node.kind() != FKind.FlatReturnNode)
	// {
	// output.println("   return;");
	// }
	// current_node = null;
	// }
	// else if (current_node.numNext() == 1)
	// {
	// output.print("   ");
	// /* EECS40 Implement : */
	// generateFlatNode(fm, current_node, output);
	// FlatNode nextnode = current_node.getNext(0);
	// if (visited.contains(nextnode))
	// {
	// output.println("goto L" + nodetolabel.get(nextnode) + ";");
	// current_node = null;
	// }
	// else
	// current_node = nextnode;
	// }
	// else if (current_node.numNext() == 2)
	// {
	// /* Branch */
	// output.print("   ");
	// /* EECS40 Implement : */
	// generateFlatCondBranch(fm, (FlatCondBranch) current_node, "L"
	// + nodetolabel.get(current_node.getNext(1)), output);
	// if (!visited.contains(current_node.getNext(1)))
	// tovisit.add(current_node.getNext(1));
	// if (visited.contains(current_node.getNext(0)))
	// {
	// output.println("goto L"
	// + nodetolabel.get(current_node.getNext(0)) + ";");
	// current_node = null;
	// }
	// else
	// current_node = current_node.getNext(0);
	// }
	// else
	// throw new Error();
	// }
	// /** Close out the method. */
	// output.println("}\n\n");
	// }
	//
	// /** This method assigns labels to FlatNodes */
	//
	// private Hashtable<FlatNode, Integer> assignLabels(FlatMethod fm)
	// {
	// HashSet tovisit = new HashSet();
	// HashSet visited = new HashSet();
	// int labelindex = 0;
	// Hashtable<FlatNode, Integer> nodetolabel = new Hashtable<FlatNode,
	// Integer>();
	// tovisit.add(fm.getNext(0));
	//
	// /*
	// * Assign labels first. A node needs a label if the previous node has
	// * two exits or this node is a join point.
	// */
	//
	// while (!tovisit.isEmpty())
	// {
	// FlatNode fn = (FlatNode) tovisit.iterator().next();
	// tovisit.remove(fn);
	// visited.add(fn);
	// for (int i = 0; i < fn.numNext(); i++)
	// {
	// FlatNode nn = fn.getNext(i);
	// if (i > 0)
	// {
	// // 1) Edge >1 of node
	// nodetolabel.put(nn, new Integer(labelindex++));
	// }
	// if (!visited.contains(nn) && !tovisit.contains(nn))
	// {
	// tovisit.add(nn);
	// }
	// else
	// {
	// // 2) Join point
	// nodetolabel.put(nn, new Integer(labelindex++));
	// }
	// }
	// }
	// return nodetolabel;
	// }

}
