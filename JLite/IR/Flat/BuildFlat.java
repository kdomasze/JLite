package IR.Flat;

import java.util.*;

import IR.*;
import IR.Tree.*;

public class BuildFlat
{
	State state;
	HashMap<Descriptor, FlatNode> TAC = new HashMap();
	int tempDescCount = 0;
	int labelCount = 0;

	public BuildFlat(State s)
	{
		state = s;
	}
	
	public void flatten()
	{
		// grab symbol table of all classes
		SymbolTable classes = state.getClassSymbolTable();
		Set<ClassDescriptor> classSet = classes.getAllValueSet();

		// iterate over classes
		for (ClassDescriptor Class : classSet)
		{
			if (Class.getClassName().equals("System") == true)
			{
				continue;
			}

			// grab symbol table for all methods in Class
			SymbolTable methods = Class.getMethodTable();
			Set<MethodDescriptor> methodSet = methods.getAllValueSet();

			// iterate over methods
			for (MethodDescriptor Method : methodSet)
			{
				BlockNode bn = state.getMethodBody(Method);

				NodePair np = flattenBlockNode(bn);

				FlatNode fn = np.getBegin();

				FlatMethod fm = new FlatMethod(Method);
				if (!bn.hasNoCode())
				{
					fm.addNext(fn);
				}

				fm.addFormalParameter(Method.getParameterTable());
				Store(Method, fm);
			}
		}
	}

	public NodePair flattenBlockNode(BlockNode bn)
	{
		FlatNode begin = null;
		FlatNode end = null;

		for (int i = 0; i < bn.size(); i++)
		{
			if (bn.get(i) instanceof DeclarationNode)
			{
				if (((DeclarationNode) (bn.get(i))).getExpression() == null)
				{
					continue;
				}
			}
			NodePair np = flattenBlockStatementNode(bn.get(i));
			FlatNode np_begin = null;
			FlatNode np_end = null;
			if (np != null)
			{
				np_begin = np.getBegin();
				np_end = np.getEnd();
			}
			if (begin == null)
			{
				begin = np_begin;
			}
			else
			{
				begin.addNext(np_begin);
			}

			if (end == null)
			{
				end = np_end;
			}
			else
			{
				end.addNext(np_begin);
				end = np_end;
			}
		}

		if (begin == null)
		{
			end = begin = new FlatNop();
		}
		return new NodePair(begin, end);
	}

	public NodePair flattenBlockStatementNode(TreeNode SubTree)
	{
		NodePair np = null;

		if (SubTree instanceof ExpressionNode)
		{
			np = FlattenExpression(SubTree);
		}
		else if (SubTree instanceof BlockExpressionNode)
		{
			ExpressionNode e = ((BlockExpressionNode) SubTree).getExpression();
			np = FlattenExpression(e);
		}
		else if (SubTree instanceof IfStatementNode)
		{
			np = FlattenIf(SubTree);
		}
		else if (SubTree instanceof LoopNode)
		{
			 np = FlattenLoop(SubTree);
		}
		else if (SubTree instanceof ReturnNode)
		{
			np = FlattenReturnNode(SubTree);
		}
		else if (SubTree instanceof DeclarationNode)
		{
			np = FlattenDeclarationNode(SubTree);
		}
		else if (SubTree instanceof SubBlockNode)
		{
			np = flattenBlockNode(((SubBlockNode) SubTree).getBlockNode());
		}
		else
		{
			throw new Error("You dun goof.");
		}

		return np;
	}

	public NodePair FlattenExpression(TreeNode SubTree)
	{
		NodePair np = null;

		if (SubTree instanceof OpNode)
		{
			np = FlattenOpNode(SubTree, null);
		}
		else if (SubTree instanceof AssignmentNode)
		{
			np = FlattenAssignmentNode(SubTree);
		}
		else if (SubTree instanceof ReturnNode)
		{
			np = FlattenReturnNode(SubTree);
		}
		else if (SubTree instanceof CreateObjectNode)
		{
			np = FlattenCreateObjectNode(SubTree, null);
		}
		else if (SubTree instanceof FieldAccessNode)
		{
			np = FlattenFieldAccessNode(SubTree, null);
		}
		else if (SubTree instanceof MethodInvokeNode)
		{
			np = FlattenMethodInvokeNode(SubTree, null);
		}
		else if (SubTree instanceof LiteralNode)
		{
			np = FlattenLiteralNode(SubTree);
		}
		else if (SubTree instanceof NameNode)
		{
			np = FlattenNameNode(SubTree);
		}
		else
		{
			np = FlattenNopNode(SubTree);
		}

		return np;
	}

	public NodePair FlattenDeclarationNode(TreeNode SubTree)
	{
		DeclarationNode dn = (DeclarationNode) SubTree;

		NameNode nn = new NameNode(new NameDescriptor(dn.getVarDescriptor()
				.getName()));
		AssignmentNode as = new AssignmentNode(nn, dn.getExpression());

		NodePair fanp = FlattenAssignmentNode((TreeNode) as);

		return fanp;
	}

	public void Store(Descriptor desc, FlatNode fn)
	{
		TAC.put(desc, fn);
	}

	public NodePair FlattenOpNode(TreeNode SubTree, TempDescriptor out)
	{
		TempDescriptor tmp = null;
		Operation op = null;

		NodePair leftNodePair = null;
		NodePair rightNodePair = null;

		ExpressionNode eRight = null;

		if (!(SubTree instanceof AssignmentNode))
		{
			if (SubTree instanceof OpNode)
			{
				if (out == null)
				{
					tmp = getTempDescriptor(((OpNode) SubTree).getType());
				}
				else
				{
					tmp = out;
				}
				op = ((OpNode) SubTree).getOp();
			}
			else
			{
				throw new Error("Live free or die hard.");
			}

			leftNodePair = FlattenExpression(((OpNode) SubTree).getLeft());
			eRight = ((OpNode) SubTree).getRight();
		}
		else
		{
			if (((AssignmentNode) SubTree).getSrc() instanceof LiteralNode)
			{
				AssignmentNode as = ((AssignmentNode) SubTree);
				if (out == null)
				{
					tmp = new TempDescriptor(((NameNode) as.getDest())
							.getName().getSymbol(), (as.getSrc()).getType());
				}
				else
				{
					tmp = out;
				}
				op = new Operation(Operation.ASSIGN);
			}
			else if (((AssignmentNode) SubTree).getSrc() instanceof NameNode)
			{
				AssignmentNode as = ((AssignmentNode) SubTree);
				NameNode nn = ((NameNode) as.getDest());
				TypeDescriptor type = ((AssignmentNode) SubTree).getSrc()
						.getType();
				if (out == null)
				{
					tmp = new TempDescriptor(nn.getName().getSymbol(), type);
				}
				else
				{
					tmp = out;
				}
				op = new Operation(Operation.ASSIGN);
			}
			else
			{
				throw new Error("This is the end of the world as we know it.");
			}

			leftNodePair = FlattenExpression(((AssignmentNode) SubTree)
					.getSrc());
		}

		FlatNode last = leftNodePair.end;

		if (eRight != null)
		{
			rightNodePair = FlattenExpression(((OpNode) SubTree).getRight());
			leftNodePair.end.next.add(rightNodePair.begin);
			rightNodePair.begin.prev.add(leftNodePair.end);
			last = rightNodePair.end;
		}

		FlatOpNode fon = new FlatOpNode(tmp, leftNodePair.tmp,
				(rightNodePair != null) ? rightNodePair.tmp : null, op);
		last.next.add(fon);
		fon.prev.add(last);

		return new NodePair(leftNodePair.begin, fon, tmp);
	}

	public NodePair FlattenAssignmentNode(TreeNode SubTree)
	{
		AssignmentNode as = ((AssignmentNode) SubTree);
		TempDescriptor out = null;

		if (as.getDest() instanceof NameNode)
		{
			String name = ((NameNode) as.getDest()).getName().toString();

			
			
			out = new TempDescriptor(name, as.getSrc().getType());
		}

		if (as.getSrc() instanceof NameNode)
		{
			if (((NameNode) as.getSrc()).getExpression() instanceof FieldAccessNode)
			{
				return FlattenFieldAccessNode(
						((NameNode) as.getSrc()).getExpression(), out);
			}

			return FlattenOpNode(SubTree, out);
		}
		else if (as.getSrc() instanceof LiteralNode)
		{
			return FlattenOpNode(SubTree, out);
		}
		else if (as.getSrc() instanceof OpNode)
		{
			OpNode on = ((OpNode) as.getSrc());

			return FlattenOpNode((TreeNode) on, out);
		}
		else if (as.getSrc() instanceof CreateObjectNode)
		{
			return FlattenCreateObjectNode(SubTree, out);
		}
		else if (as.getSrc() instanceof MethodInvokeNode)
		{
			return FlattenMethodInvokeNode(as.getSrc(), out);
		}
		else if (as.getSrc() instanceof CastNode)
		{
			return FlattenCastNode(as.getSrc(), as.getDest());
		}
		else
		{
			throw new Error("I can't let you do that, Dave.");
		}
	}
	
	public NodePair FlattenCastNode(TreeNode SubTree, TreeNode SubTree2)
	{
		TypeDescriptor type = ((CastNode) SubTree).getType();
		NodePair np = FlattenExpression(((CastNode)SubTree).getExpression());
		
		TempDescriptor src = np.tmp;
		TempDescriptor dest = new TempDescriptor(((NameNode)SubTree2).getName().getIdentifier(), type);
		
		FlatCastNode fcn = new FlatCastNode(type, src, dest);
		
		return new NodePair(fcn, fcn);
	}

	public NodePair FlattenCreateObjectNode(TreeNode SubTree, TempDescriptor out)
	{
		TypeDescriptor type = ((AssignmentNode) SubTree).getSrc().getType();
		TempDescriptor tmp = null;
		
		if(out == null)
		{
			tmp = getTempDescriptor(type);
		}
		else
		{
			tmp = out;
		}
			
		FlatNew fn = new FlatNew(type, tmp);

		return new NodePair(fn, fn, tmp);
	}

	public NodePair FlattenReturnNode(TreeNode SubTree)
	{
		ExpressionNode e = ((ReturnNode) SubTree).getReturnExpression();
		NodePair np = FlattenExpression(e);
		TempDescriptor tmp = np.tmp;

		FlatReturnNode frn = new FlatReturnNode(tmp);
		np.end.addNext(frn);
		return new NodePair(np.begin,frn,  tmp);
	}

	public NodePair FlattenFieldAccessNode(TreeNode SubTree, TempDescriptor out)
	{
		System.out.println("If you see this, check FlattenFieldAccessNode");

		FieldAccessNode FieldNode = (FieldAccessNode) SubTree;

		FieldDescriptor fd = FieldNode.getField();
		TempDescriptor dst = null;
		NodePair np = FlattenExpression(FieldNode.getExpression());

		TempDescriptor src = null;

		src = np.tmp;

		if (out == null)
		{
			dst = getTempDescriptor(fd.getType());
		}
		else
		{
			dst = out;
		}

		FlatFieldNode ffn = new FlatFieldNode(fd, src, dst);

		return new NodePair(ffn, ffn, dst);
	}

	public NodePair FlattenMethodInvokeNode(TreeNode SubTree, TempDescriptor out) // ALMOST
																					// DONE.
																					// MISSING
																					// THIS_TEMP
	{
		MethodInvokeNode min = (MethodInvokeNode) SubTree;

		TempDescriptor tmp = null;

		MethodDescriptor md = min.getMethod();
		
			TempDescriptor[] argArray = new TempDescriptor[min.getArgVector().size()];
		

		for (int i = 0; i < min.getArgVector().size(); i++)
		{
			argArray[i] = FlattenExpression(min.getArg(i)).tmp;
		}

		if ((md.getReturnType().getSymbol().equals("void")))
		{
			tmp = null;
		}
		else
		{
			tmp = getTempDescriptor(md.getReturnType());
		}
		FlatCall fc = new FlatCall(md, tmp, null, argArray); // should not be
																// null. Need to
																// fix.

		return new NodePair(fc, fc, tmp);
	}

	public NodePair FlattenLiteralNode(TreeNode SubTree)
	{
		int val = ((Integer) ((LiteralNode) SubTree).getValue());
		TypeDescriptor type = ((LiteralNode) SubTree).getType();
		TempDescriptor tmp = getTempDescriptor(type);

		FlatLiteralNode fln = new FlatLiteralNode(type, val, tmp);

		return new NodePair(fln, fln, tmp);
	}

	public NodePair FlattenNameNode(TreeNode SubTree)
	{
		TypeDescriptor type = ((NameNode) SubTree).getType();
		String name = ((NameNode) SubTree).getName().getSymbol();
		TempDescriptor tmp = new TempDescriptor(name, type);

		FlatNameNode fnn = new FlatNameNode(type, tmp);

		return new NodePair(fnn, fnn, tmp);
	}

	public NodePair FlattenLoop(TreeNode SubTree)
	{
		ExpressionNode condition = ((LoopNode) SubTree).getCondition();
		BlockNode loopBody = ((LoopNode) SubTree).getBody();
		
		NodePair testFlatCond = FlattenExpression(condition);
		
		NodePair FlatLoopBody = flattenBlockNode(loopBody);
		FlatLabel L1 = new FlatLabel(labelCount++); 
		GoFlatLabel L2 = new GoFlatLabel("FlatCondBranch_if(!"+ ((FlatOpNode)(testFlatCond.end)).dest.getSymbol() + ") Goto L", labelCount++);
		
		L1.addNext(testFlatCond.begin);
		testFlatCond.end.addNext(L2);
		L2.addNext(FlatLoopBody.begin);
		GoFlatLabel L3 = new GoFlatLabel("Goto L", L1.numL);
		FlatLoopBody.end.addNext(L3);
		FlatLabel L4 = new FlatLabel(L2.numL);
		L3.addNext(L4);
		
		return new NodePair(L1,L4);
	}
	
	public NodePair FlattenIf(TreeNode SubTree) 
	{
		ExpressionNode condition = ((IfStatementNode) SubTree).getCondition();
		BlockNode trueStatement = ((IfStatementNode) SubTree).getTrueBlock();
		BlockNode elseStatement = ((IfStatementNode) SubTree).getFalseBlock();

		NodePair testFlatCond = FlattenExpression(condition);

		NodePair trueFlatBlock = flattenBlockNode(trueStatement);
		
		GoFlatLabel L1 = new GoFlatLabel("FlatCondBranch_if(!"+ ((FlatOpNode)(testFlatCond.end)).dest.getSymbol() +  ") Goto L", labelCount++);
		
		testFlatCond.end.addNext(L1);
		L1.addNext(trueFlatBlock.begin);
		NodePair elseFlatBlock = null;

		if (elseStatement != null)
		{
			elseFlatBlock = flattenBlockNode(elseStatement);
		}
		else
		{
			elseFlatBlock = FlattenNopNode(null);
		}
		
		GoFlatLabel L2 = new GoFlatLabel("Goto L", labelCount++);
		trueFlatBlock.end.addNext(L2);
		FlatLabel L3 = new FlatLabel(L1.numL);
		L2.addNext(L3);
		L3.addNext(elseFlatBlock.begin);
		FlatLabel L4 = new FlatLabel(L2.numL);
		elseFlatBlock.end.addNext(L4);
		TempDescriptor tmp = testFlatCond.tmp;

		FlatCondBranch ifStatement = new FlatCondBranch(tmp);

		if (elseFlatBlock != null)
		{
			ifStatement.loopEntrance = elseFlatBlock.getBegin();
			return new NodePair(testFlatCond.begin, L4, tmp);
		}
		else
		{
			return new NodePair(ifStatement.loopEntrance = null,
					trueFlatBlock.begin, tmp);
		}
	}

	public NodePair FlattenNopNode(TreeNode SubTree)
	{
		FlatNop fn = new FlatNop();
		return new NodePair(fn, fn);
	}

	private TempDescriptor getTempDescriptor(TypeDescriptor type)
	{
		tempDescCount++;
		return new TempDescriptor("t" + tempDescCount, type);
	}

	private String getLabel()
	{
		labelCount++;
		return "Label" + labelCount;
	}

	public String toString()
	{
		String returnString = "";

		for (FlatNode flat : TAC.values())
		{
			
			MethodDescriptor fm = ((FlatMethod) flat).getMethod();
			returnString += fm.getClassDesc().getClassName() + "." + fm.getSymbol() + "(";
			for (int i = 0; i < fm.numParameters(); i++)
			{
				returnString += fm.getParameter(i);
				if (fm.numParameters() == i+1)
				{
					continue;
				}
				returnString += ", ";
			}
			returnString += ")\n{\n";
			FlatNode f = flat.getNext(0);
			while (true)
			{
				returnString += "\t" + f.toString() + "\n";

				if (f.next.size() == 0)
				{
					returnString += "}\n\n";
					break;
				}

				f = f.next.get(0);
			}
		}

		return returnString;
	}
}
