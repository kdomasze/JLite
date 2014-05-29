package IR.Flat;

import IR.*;

import java.util.*;
import java.io.*;

public class BuildCode
{
	HashMap<Descriptor, Vector<Descriptor>> TACParent;
	HashMap<Descriptor, FlatNode> TAC;

	private LinkedHashMap<String, HashMap<String, String>> classNames = new LinkedHashMap<>();

	public static Vector<String> methodVector;
	public static Vector<String> methodVT = new Vector<>();
	Vector<ClassDescriptor> classVector = new Vector<ClassDescriptor>();

	private int maxMethods;

	public BuildCode(HashMap<Descriptor, Vector<Descriptor>> tacp,
			HashMap<Descriptor, FlatNode> tac)
	{
		TACParent = tacp;
		TAC = tac;
	}

	public void buildCode()
	{
		// for (Vector<Descriptor> descriptorVector : TACParent.values())
		// {
		//
		// if(descriptorVector.get(0) instanceof FieldDescriptor)
		// {
		// // Fields Desc
		// }
		// else if(descriptorVector.get(0) instanceof MethodDescriptor)
		// {
		// // Methods Desc
		// }
		//
		// for(Descriptor desc : descriptorVector)
		// {
		// FlatNode flat = TAC.get(desc);
		//
		// if (flat instanceof FlatFieldNode)
		// {
		// // Field Nodes
		// }
		// else if (flat instanceof FlatMethod)
		// {
		// MethodDescriptor fm = ((FlatMethod) flat).getMethod();
		//
		// for (int i = 0; i < fm.numParameters(); i++)
		// {
		// if (fm.numParameters() == i + 1)
		// {
		// continue;
		// }
		// }
		//
		// FlatNode f = flat.getNext(0);
		// while (true)
		// {
		//
		// if (f.next.size() == 0)
		// {
		// break;
		// }
		//
		// f = f.next.get(0);
		// }
		// }
		// }
		// }

		/* Create output streams to write to */
		/* Refer to PrintWriter Class in JAVA LIB */

		/* Build the virtual dispatch tables */

		/* Output includes */

		/* Output Structures */
		fillVector();
		generateClassDefs();
		generateVMT();
		// Output the C class declarations
		// These could mutually reference each other

		// Output function prototypes and structures for parameters
		generateMethods();
		/* Build the actual methods */

		/* Generate main method */

		/* Close files */
	}

	private void generateClassDefs()
	{
		/* Create output streams to write to */
		File classDefs = new File("classdefs.h");
		PrintWriter classDefsPW;
		boolean generateSystem = false;
		Vector<String> outString = new Vector<>();
		Vector<String> fields;

		int classCount = 0;

		// create new file
		try
		{
			classDefs.createNewFile();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			throw new Error("It's Oracle's fault.");
		}

		// create print writer
		try
		{
			classDefsPW = new PrintWriter(classDefs);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new Error("This is how the world ends.");
		}

		// begin generating classdefs.h

		/*
		 * generate structure prototypes
		 */
		for (Descriptor key : classVector)
		{
			outString.add("struct " + key.getSymbol() + ";\n");
		}

		outString.add("\n");

		/*
		 * generate arrays for class size and super types
		 */
		outString.add("extern int classsize[];\n");
		outString.add("extern int supertypes[];\n\n");

		/*
		 * generate actual structures for classes
		 */
		for (Descriptor key : classVector)
		{
			fields = new Vector<>();
			HashMap<String, String> superInfo = new HashMap<>();
			String superClass;

			if (((ClassDescriptor) key).getSuperDesc() != null)
			{
				superClass = ((ClassDescriptor) key).getSuperDesc().getSymbol();
			}
			else
			{
				superClass = "null";
			}

			if (!superInfo.containsKey(key.getSymbol()))
			{
				superInfo.put(superClass, Integer.toString(classVector
						.indexOf(((ClassDescriptor) key).getSuperDesc())));

				classNames.put(key.getSymbol(), superInfo);
			}

			outString.add("struct " + key.getSymbol() + "\n{\n");

			outString.add("\tint type;\n");

			// loop though all of the fields in each class
			for (Descriptor desc : TACParent.get(key))
			{
				FlatNode fn = TAC.get(desc);

				if (fn instanceof FlatFieldNode)
				{
					boolean duplicateField = false;
					FlatFieldNode ffn = (FlatFieldNode) fn;

					// check if the field is already defined
					for (String str : fields)
					{
						if (str.equals(ffn.dst.getSymbol()))
						{
							duplicateField = true;
						}
					}

					// don't add the field if it's defined already
					if (!duplicateField)
					{
						// if the type of the field is an int, we don't need
						// to worry about it's super class
						if (ffn.dst.getType().getSymbol() == "int")
						{
							outString.add("\tint " + ffn.dst.getSymbol()
									+ ";\n");
						}
						// if the type is anything else, we need to create
						// a pointer reference to the structure
						else
						{
							outString.add("\tstruct "
									+ ffn.dst.getType().getSymbol() + " *"
									+ ffn.dst.getSymbol() + ";\n");
						}
						fields.add(ffn.dst.getSymbol());
					}
				}

				// check FlatMethods contain system
				/*
				 * if (fn instanceof FlatMethod && generateSystem == false) {
				 * FlatMethod fm = (FlatMethod) fn;
				 * 
				 * for (int i = 0; i < fm.method.numParameters(); i++) { if
				 * (fm.method.getParamType(i).getSymbol() .equals("System")) {
				 * HashMap<String, String> superInfoSystem = new HashMap<>();
				 * 
				 * superInfoSystem.put("null", Integer.toString(classCount));
				 * 
				 * classNames.put("System", superInfoSystem);
				 * 
				 * 
				 * generateSystem = true; } } }
				 */
			}

			outString.add("};\n\n");

			// classCount++;
		}

		// put output in the file
		for (String str : outString)
		{
			classDefsPW.append(str);
		}

		classDefsPW.close();

		generateStructDefs(classVector.size());
	}

	private void generateStructDefs(int classCount)
	{
		File structDefs = new File("structdefs.h");
		PrintWriter structDefsPW;

		// create new file
		try
		{
			structDefs.createNewFile();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			throw new Error("It's Oracle's fault.");
		}

		// create new print writer
		try
		{
			structDefsPW = new PrintWriter(structDefs);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new Error("This is how the world ends.");
		}

		// add the file contents to the file
		structDefsPW
				.printf("#ifndef STRUCTDEFS_H\n#define STRUCTDEFS_H\n#include \"classdefs.h\"\n#define NUMCLASSES "
						+ classCount + "\n#endif");

		structDefsPW.close();
	}

	private void generateMethods()
	{
		File methodsF = new File("methods.c");
		PrintWriter methodsPW;
		int tempVarCount = 0;

		String systemNum = "";
		String mainClassNum = "";
		String mainClass = "";

		Vector<String> classSize = new Vector<>();
		Vector<String> superTypes = new Vector<>();
		Vector<String> methodDecs = new Vector<>();

		try
		{
			methodsF.createNewFile();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			throw new Error("It's Oracle's fault.");
		}

		try
		{
			methodsPW = new PrintWriter(methodsF);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new Error("This is how the world ends.");
		}

		/*
		 * print includes
		 */
		methodsPW
				.append("#include \"methodheaders.h\"\n#include \"virtualtable.h\"\n#include <runtime.h>\n\n");

		/*
		 * get the info necessary for classsize and supertypes arrays
		 */
		for (Descriptor desc : classVector)
		{
			String name = desc.getSymbol();
			// add each class name to the classSize vector
			classSize.add(name);

			// check if the super is null or not. Will default to -1 if null;
			String superName = classNames.get(name).keySet().toArray()[0]
					.toString();
			String superValue = "-1";
			if (!superName.equals("null"))
			{
				superValue = classNames.get(superName).values().toArray()[0]
						.toString();
			}

			// add super value to supertypes vector
			superTypes.add(superValue);
		}

		/*
		 * print the classsize array
		 */
		methodsPW.append("int classsize[] = {");

		for (int i = 0; i < classSize.size(); i++)
		{
			methodsPW.append("sizeof(struct " + classSize.get(i) + ")");

			if (i < classSize.size() - 1)
			{
				methodsPW.append(", ");
			}
		}

		methodsPW.append("};\n");

		/*
		 * print supertypes array
		 */
		methodsPW.append("int supertypes[] = {");

		for (int i = 0; i < superTypes.size(); i++)
		{
			methodsPW.append(superTypes.get(i));

			if (i < superTypes.size() - 1)
			{
				methodsPW.append(", ");
			}
		}

		methodsPW.append("};\n\n");

		HashMap<String, HashMap<String, String>> fieldsPerClass = new HashMap<>();

		/*
		 * get fields to exclude them from generating in method body
		 */
		for (Descriptor Class : classVector)
		{
			Vector<Descriptor> MethodVector = TACParent.get(Class);

			HashMap<String, String> Fields = new HashMap<>();
			for (Descriptor desc : MethodVector)
			{
				FlatNode flatNode = TAC.get(desc);

				if (flatNode instanceof FlatFieldNode)
				{
					FlatFieldNode flatFieldNode = (FlatFieldNode) flatNode;
					Fields.put(flatFieldNode.dst.getSymbol(), flatFieldNode.dst
							.getType().getSymbol());
				}
			}
			fieldsPerClass.put(Class.getSymbol(), Fields);
		}

		/*
		 * print methods
		 */
		for (Descriptor Class : classVector)
		{
			Vector<Descriptor> MethodVector = TACParent.get(Class);

			HashMap<String, String> Fields = new HashMap<>();
			for (Descriptor desc : MethodVector)
			{
				FlatNode flatNode = TAC.get(desc);

				if (flatNode instanceof FlatMethod)
				{
					LinkedHashMap<String, String> thisVarClassVector = new LinkedHashMap<>();
					MethodDescriptor method = (MethodDescriptor) desc;
					FlatMethod flatMethod = (FlatMethod) flatNode;

					HashMap<String, String> MethodArgs = new HashMap<>();

					// get all arguments for method
					for (int i = 0; i < method.numParameters(); i++)
					{
						MethodArgs.put(method.getParamName(i), method
								.getParamType(i).getSymbol());
					}

					String methodAppend = method.getReturnType().getSymbol()
							+ " " + Class.getSymbol() + "_"
							+ method.getSymbol() + "(struct "
							+ Class.getSymbol() + " *this" + tempVarCount;

					// print any arguments that are apart of the method
					for (String name : MethodArgs.keySet())
					{
						String type = MethodArgs.get(name) + " ";

						if (!type.equals("int "))
						{
							type = "struct " + type + "*";
						}
						methodAppend += ", " + type + name;
					}

					methodDecs.add(methodAppend + ");");
					
					// print main structure of method declaration
					if (Class.getSymbol().equals("System")
							&& (method.getSymbol().equals("input") || method
									.getSymbol().equals("output")))
					{
						continue;
					}
					
					methodsPW.append(methodAppend);

					// close the method declaration and open the block body
					methodsPW.append(")\n{\n");

					/*
					 * get all var declarations
					 */
					Vector<String> methodBlockVector = new Vector<>();

					FlatNode f = flatMethod.getNext(0);
					while (true)
					{
						String output = "";

						if (f instanceof FlatLiteralNode)
						{
							FlatLiteralNode fln = (FlatLiteralNode) f;
							boolean instanceOfField = false;
							for (String args : MethodArgs.keySet())
							{
								if (fln.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							for (String args : fieldsPerClass.get(
									((MethodDescriptor) desc).getClassDesc()
											.getSymbol()).keySet())
							{
								if (fln.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							if (!instanceOfField)
							{
								output = fln.dst.type.getSymbol() + " "
										+ fln.dst.getSymbol() + ";\n";
							}
						}
						else if (f instanceof FlatOpNode)
						{
							FlatOpNode fon = (FlatOpNode) f;

							boolean instanceOfField = false;
							for (String args : MethodArgs.keySet())
							{
								if (fon.dest.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							for (String args : fieldsPerClass.get(
									((MethodDescriptor) desc).getClassDesc()
											.getSymbol()).keySet())
							{
								if (fon.dest.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							if (!instanceOfField)
							{
								output = fon.dest.type.getSymbol() + " "
										+ fon.dest.getSymbol() + ";\n";
							}
						}
						else if (f instanceof FlatNameNode)
						{
							FlatNameNode fnn = (FlatNameNode) f;
							boolean instanceOfField = false;
							for (String args : MethodArgs.keySet())
							{
								if (fnn.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							for (String args : fieldsPerClass.get(
									((MethodDescriptor) desc).getClassDesc()
											.getSymbol()).keySet())
							{
								if (fnn.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							if (!instanceOfField)
							{
								output = fnn.dst.type.getSymbol() + " "
										+ fnn.dst.getSymbol() + ";\n";
							}
						}
						else if (f instanceof FlatCastNode)
						{
							FlatCastNode fcn = (FlatCastNode) f;
							boolean instanceOfField = false;
							for (String args : MethodArgs.keySet())
							{
								if (fcn.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							for (String args : fieldsPerClass.get(
									((MethodDescriptor) desc).getClassDesc()
											.getSymbol()).keySet())
							{
								if (fcn.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							if (!instanceOfField)
							{
								output = fcn.dst.type.getSymbol() + " "
										+ fcn.dst.getSymbol() + ";\n";
							}
						}
						else if (f instanceof FlatNew)
						{
							FlatNew fn = (FlatNew) f;
							String type = ((FlatNew) fn).type.getSymbol();
							String dst = ((FlatNew) fn).dst.getSymbol();

							boolean instanceOfField = false;
							for (String args : MethodArgs.keySet())
							{
								if (fn.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							for (String args : fieldsPerClass.get(
									((MethodDescriptor) desc).getClassDesc()
											.getSymbol()).keySet())
							{
								if (fn.dst.getSymbol().equals(args))
								{
									instanceOfField = true;
									break;
								}
							}

							if (!instanceOfField)
							{
								output = "struct " + type + " *" + dst + ";\n";

								thisVarClassVector
										.put(fn.type.getSymbol(), dst);
							}
						}
						else if (f instanceof FlatCall)
						{
							FlatCall fc = (FlatCall) f;
							String dst = "";
							
							if (fc.dst != null)
							{
								dst = fc.dst.type.getSymbol() + " "
										+ fc.dst.getSymbol() + ";\n";

								boolean instanceOfField = false;
								for (String args : MethodArgs.keySet())
								{
									if (fc.dst.getSymbol().equals(args))
									{
										instanceOfField = true;
										break;
									}
								}

								for (String args : fieldsPerClass.get(
										((MethodDescriptor) desc)
												.getClassDesc().getSymbol())
										.keySet())
								{
									if (fc.dst.getSymbol().equals(args))
									{
										instanceOfField = true;
										break;
									}
								}

								if (!instanceOfField)
								{
									output = dst;
								}
							}
							else
							{
								output = dst;
							}
						}

						String outputTemp = "\t" + output;
						output = outputTemp;

						if (!methodBlockVector.contains(output))
						{
							methodBlockVector.add(output);
						}

						if (f.next.size() == 0)
						{
							methodBlockVector.add("\n");
							break;
						}

						f = f.next.get(0);
					}

					/*
					 * begin loop for method body
					 */
					f = flatMethod.getNext(0);
					while (true)
					{
						String output = formatFlatPrint(f,
								fieldsPerClass.get(((MethodDescriptor) desc)
										.getClassDesc().getSymbol()), "this"
										+ tempVarCount, thisVarClassVector,
								(MethodDescriptor) desc);

						methodBlockVector.add(output);

						if (f.next.size() == 0)
						{
							if (!(f instanceof FlatReturnNode))
							{
								methodBlockVector.add("\t\treturn;\n");
							}
							break;
						}

						f = f.next.get(0);
					}

					for (String out : methodBlockVector)
					{
						methodsPW.append(out);
					}

					// close the method
					methodsPW.append("}\n\n");
				}
			}
		}

		for (Descriptor Class : classVector)
		{
			if (generation((ClassDescriptor) Class) == 0)
			{
				Vector<Descriptor> MethodVector = TACParent.get(Class);

				for (Descriptor desc : MethodVector)
				{
					if (desc.getSymbol().equals("main"))
					{
						mainClass = Class.getSymbol();
						mainClassNum = Integer.toString(classVector
								.indexOf(Class));
					}
				}
			}
			if (Class.getSymbol().equals("System"))
			{
				systemNum = Integer.toString(classVector.indexOf(Class));
			}
		}

		/*
		 * int count = 0; for (String name : classNames.keySet()) { if
		 * (name.equals("System")) { systemNum = Integer.toString(count); } if
		 * (name.equals(mainClass)) { mainClassNum = Integer.toString(count); }
		 * 
		 * count++; }
		 */

		methodsPW
				.append("int main(int argc, const char *argv[])\n{\n\tint i;\n");
		methodsPW.append("\tvoid *systemptr = allocate_new(");
		methodsPW.append(systemNum);
		methodsPW.append(");\n");
		methodsPW.append("\tvoid *baseptr = allocate_new(");
		methodsPW.append(mainClassNum);
		methodsPW.append(");\n");
		methodsPW.append("\t" + mainClass + "_main(baseptr, systemptr);\n");
		methodsPW.append("}\n\n");

		methodsPW.close();

		generateMethodHeader(methodDecs);
	}

	private void generateMethodHeader(Vector<String> methodDecs)
	{
		File methodHeadersF = new File("methodheaders.h");
		PrintWriter methodHeadersPW;

		try
		{
			methodHeadersF.createNewFile();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			throw new Error("It's Oracle's fault.");
		}

		try
		{
			methodHeadersPW = new PrintWriter(methodHeadersF);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new Error("This is how the world ends.");
		}

		methodHeadersPW
				.append("#ifndef METHODHEADERS_H\n#define METHODHEADERS_H\n#include \"structdefs.h\"\n\n");

		for (String str : methodDecs)
		{
			methodHeadersPW.append(str + "\n\n");
		}

		methodHeadersPW.append("#endif\n");

		methodHeadersPW.close();
	}

	private String formatFlatPrint(FlatNode fn,
			HashMap<String, String> fieldsMap, String thisVar,
			LinkedHashMap<String, String> thisVarClassVector,
			MethodDescriptor thisMethod)
	{
		String returnString = "";

		if (fn instanceof FlatLiteralNode)
		{
			String dst = ((FlatLiteralNode) fn).dst.getSymbol();
			String value = ((FlatLiteralNode) fn).value.toString();

			for (String str : fieldsMap.keySet())
			{
				if (dst.equals(str))
				{
					dst = thisVar + "->" + dst;
				}
			}

			returnString = dst + " = " + value + ";";
		}
		else if (fn instanceof FlatCastNode)
		{
			String dst = ((FlatCastNode) fn).dst.getSymbol();
			String src = ((FlatCastNode) fn).src.getSymbol();
			String type = ((FlatCastNode) fn).type.getSymbol();

			returnString = dst + " = " + "(" + type + ")" + src + ";";
		}
		else if (fn instanceof FlatOpNode)
		{
			String dst = ((FlatOpNode) fn).dest.getSymbol();
			String valueLeft = ((FlatOpNode) fn).left.getSymbol();
			String op = "";
			String valueRight = "";

			if (((FlatOpNode) fn).right != null)
			{
				valueRight = ((FlatOpNode) fn).right.getSymbol();
				op = ((FlatOpNode) fn).op.toString();
			}

			for (String str : fieldsMap.keySet())
			{
				if (dst.equals(str))
				{
					dst = thisVar + "->" + dst;
				}

				if (valueLeft.equals(str))
				{
					valueLeft = thisVar + "->" + valueLeft;
				}

				if (valueRight.equals(str))
				{
					valueRight = thisVar + "->" + valueRight;
				}
			}

			if (valueRight != "")
			{
				returnString = dst + " = " + valueLeft + " " + op + " "
						+ valueRight + ";";
			}
			else
			{
				returnString = dst + " = " + valueLeft + ";";
			}
		}
		else if (fn instanceof FlatNameNode)
		{
			return "";
		}
		else if (fn instanceof FlatCall)
		{
			FlatCall fc = (FlatCall) fn;
			String className = fc.method.getClassDesc().getClassName();
			String returnType = fc.method.getReturnType().getSymbol();
			String dst = "";

			String thisClass = "";
			String paramClass = "";
			String argsTypes = "";

			if (thisVarClassVector.get(className) == null)
			{
				thisClass = thisVar;
				paramClass = thisClass;
				for (int i = 0; i < thisMethod.numParameters(); i++)
				{
					System.out.println(thisMethod.getParamType(i).getSymbol()
							+ "    " + className);
					if (thisMethod.getParamType(i).getSymbol()
							.equals(className))
					{
						thisClass = thisMethod.getParamName(i);
					}
				}
			}
			else
			{
				thisClass = thisVarClassVector.get(className);
				paramClass = thisClass;
			}

			String methodNum = null;

			int classNum = classVector.indexOf(fc.method.getClassDesc());

			/*
			 * for (Descriptor desc : classVector) {
			 * System.out.println(classNum); String str = desc.getSymbol(); if
			 * (str.equals(className)) { break; } else { classNum++; } }
			 */

			for (int i = 0; i < maxMethods; i++)
			{
				String classCheck1 = methodVT.elementAt(classNum * maxMethods
						+ i);
				String classCheck2 = className + "_" + fc.method.getSymbol();
				if (classCheck1.equals(classCheck2))
				{
					methodNum = Integer.toString(i);
				}
			}

			if (methodNum == null)
			{
				throw new Error(
						"The British are coming! The British are coming!");
			}

			String args = "";

			for (int i = 0; i < fc.method.numParameters(); i++)
			{
				args += ", " + fc.args[i].getSymbol();
			}

			for (int i = 0; i < fc.method.numParameters(); i++)
			{
				argsTypes += ", " + fc.args[i].type.getSymbol();
			}

			if (fc.dst != null)
			{
				dst = fc.dst.getSymbol() + " = ";
			}

			returnString = dst + "((" + returnType + " (*)(struct " + className
					+ " *" + argsTypes + "))virtualtable[" + thisClass
					+ "->type*" + maxMethods + "+" + methodNum + "])((struct "
					+ className + "*) " + thisClass + args + ");";
		}
		else if (fn instanceof FlatLabel)
		{
			returnString = ((FlatLabel) fn).toString() + ':';
		}
		else if (fn instanceof GoFlatLabel)
		{
			returnString = ((GoFlatLabel) fn).toString() + ';';
		}
		else if (fn instanceof FlatCondBranch)
		{
			GoFlatLabel gfl = ((GoFlatLabel) fn.next.firstElement());
			if (!fn.prev.isEmpty()
					&& fn.prev.firstElement() instanceof GoFlatLabel)
			{
				gfl = ((GoFlatLabel) fn.prev.firstElement());
				returnString = "if (!"
						+ ((FlatCondBranch) fn).test_cond.getSymbol() + ")";// +
																			// gfl.toString();
			}
			else
			{
				returnString = "if (!"
						+ ((FlatCondBranch) fn).test_cond.getSymbol() + ") ";// +
																				// gfl.toString();
			}
			return returnString;
		}
		else if (fn instanceof FlatNew)
		{
			String dst = ((FlatNew) fn).dst.getSymbol();
			String classNum = "";
			/*
			 * for (String name : classNames.keySet()) {
			 * 
			 * if (name.equals(((FlatNew) fn).type.getSymbol())) { classNum =
			 * classNames.get(name).values().toArray()[0] .toString(); } }
			 */

			classNum = Integer.toString(classVector.indexOf(((FlatNew) fn).type
					.getClassDesc()));

			returnString = dst + " = allocate_new(" + classNum + ");";
		}
		else if (fn instanceof FlatReturnNode)
		{
			FlatReturnNode frn = (FlatReturnNode) fn;
			if (frn.tempdesc == null)
			{
				returnString = "return;";
			}
			else
			{
				returnString = "return " + frn.tempdesc.getSymbol() + ";";
			}

		}

		else
		{
			// throw new Error("Oh, god, what have we done?");
		}

		return "\t" + returnString + "\n";
	}

	private void generateVMT()
	{
		int numClasses = 0;
		int numMethods = 0;
		boolean copy;

		methodVector = new Vector<String>();

		HashMap<String, Integer> classGen = new HashMap<>();

		classGen = new HashMap<>();
		// Find max number of methods and fill vector with method names
		for (Descriptor key : classVector)
		{
			numClasses++;
			numMethods = 0;
			for (Descriptor desc : TACParent.get(key))
			{
				FlatNode flat = TAC.get(desc);
				if (flat instanceof FlatMethod)
				{
					FlatMethod fm = (FlatMethod) flat;
					copy = false;
					if (methodVector.isEmpty())
					{
						methodVector.add(fm.method.getSymbol());
						numMethods++;
					}
					for (String src : methodVector)
					{
						if (src.equals(fm.method.getSymbol()))
						{
							// numMethods ++;
							copy = true;
							break;
						}
					}
					if (!copy)
					{
						methodVector.add(fm.method.getSymbol());
						numMethods++;
					}
				}
			}
			classGen.put(key.getSymbol(), numMethods);
			if (((ClassDescriptor) key).getSuper() != null)
			{
				numMethods += classGen.get(((ClassDescriptor) key).getSuper());
				classGen.remove(key.getSymbol());
				classGen.put(key.getSymbol(), numMethods);
			}
			if (maxMethods < numMethods)
			{
				maxMethods = numMethods;
			}
		}
		/*
		 * System.out.println("numClasses = " + numClasses + "  maxMethods = " +
		 * maxMethods);
		 */

		String vmtString = "void * virtualtable[]={";
		String vmtAdd;
		int counter = 0;

		// Prepare virtual Table String
		for (Descriptor key : classVector)
		{
			numMethods = 0;
			for (String src : methodVector)
			{
				for (Descriptor desc : TACParent.get(key))
				{
					FlatNode flat = TAC.get(desc);
					if (flat instanceof FlatMethod)
					{
						FlatMethod fm = (FlatMethod) flat;
						if (fm.method.getSymbol().equals(src))
						{
							vmtAdd = fm.getMethod().getClassDesc().getSymbol()
									+ "_" + fm.method.getSymbol();
							vmtString += " &" + vmtAdd;
							numMethods += 1;
							methodVT.add(vmtAdd);
							if (counter < maxMethods * numClasses - 1)
							{
								vmtString += ",";
								counter++;
							}
							if (counter % 5 == 0)
							{
								vmtString += "\n\t";
							}
							break;
						}
					}
				}
			}
			while (numMethods < maxMethods)
			{
				vmtString += " 0";
				if (counter < maxMethods * numClasses - 1)
				{
					vmtString += ",";
					counter++;
				}
				if (counter % 5 == 0)
				{
					vmtString += "\n\t";
				}
				methodVT.add("0");
				numMethods++;
			}
		}
		/*
		 * for(String s:methodVT) { System.out.println(s); }
		 */
		vmtString += "};\n";
		// System.out.println(vmtString);

		// prepare file for opening and write virtual table string to file
		File virtualTable = new File("virtualtable.h");
		PrintWriter virtualTablePW;
		try
		{
			virtualTable.createNewFile();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			throw new Error("The table is fubar");
		}
		try
		{
			virtualTablePW = new PrintWriter(virtualTable);
		}
		catch (FileNotFoundException e2)
		{
			e2.printStackTrace();
			throw new Error("It was Christian's fault!!!");
		}
		virtualTablePW.append(vmtString);
		virtualTablePW.close();
	}

	private int generation(ClassDescriptor cd)
	{
		int generation = 0;
		if (cd.getSuper() == null)
		{
			return generation;
		}
		else
		{
			return generation(cd.getSuperDesc()) + 1;
		}
	}

	private void fillVector()
	{
		int maxGen = 0;
		HashMap<String, Integer> classGen = new HashMap<>();
		// Make Table of class names matched to generation value
		for (Descriptor key : TACParent.keySet())
		{
			ClassDescriptor cd = (ClassDescriptor) key;
			int thisgen = generation(cd);
			classGen.put(cd.getSymbol(), thisgen);
			if (generation(cd) > maxGen)
			{
				maxGen = thisgen;
			}
		}

		// Fill Vector with classes in generation order starting from 0
		for (int count = 0; count <= maxGen; count++)
		{
			for (Descriptor key : TACParent.keySet())
			{
				if (classGen.get(key.getSymbol()) == count)
				{
					classVector.add((ClassDescriptor) key);
				}
			}
		}
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
