package IR.Flat;

import java.util.HashMap;
import java.util.Set;

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
		for(ClassDescriptor Class : classSet)
		{			
			// grab symbol table for all methods in Class
			SymbolTable methods = Class.getMethodTable();
			Set<MethodDescriptor> methodSet = methods.getAllValueSet();

			// iterate over methods
			for(MethodDescriptor Method : methodSet)
			{
				BlockNode bn = state.getMethodBody(Method);
				
				NodePair np = flattenBlockNode(bn);
				
				FlatNode fn = np.getBegin();
				
				FlatMethod fm = new FlatMethod(Method);
				if(!bn.hasNoCode())
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
		
		for(int i = 0; i<bn.size(); i++)
		{			
			if(bn.get(i) instanceof DeclarationNode)
			{
				if(((DeclarationNode)(bn.get(i))).getExpression() == null)
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
			
			if(end == null)
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
		return new NodePair(begin,end);
	}
	
	public NodePair flattenBlockStatementNode(TreeNode SubTree)
	{
		NodePair np = null;
		
		if(SubTree instanceof ExpressionNode)
		{
			np = FlattenExpression(SubTree);
		}
		else if(SubTree instanceof BlockExpressionNode)
		{
			ExpressionNode e = ((BlockExpressionNode)SubTree).getExpression();
			np = FlattenExpression(e);
		}
		else if(SubTree instanceof IfStatementNode)
		{
			np = FlattenIf(SubTree);
		}
		else if(SubTree instanceof LoopNode)
		{
			//np = FlattenLoop(SubTree, out);
		}
		else if(SubTree instanceof ReturnNode)
		{
			np = FlattenReturnNode(SubTree);
		}
		else if(SubTree instanceof DeclarationNode)
		{
			np = FlattenDeclarationNode(SubTree);
		}
		else if(SubTree instanceof SubBlockNode)
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
		
		if(SubTree instanceof OpNode)
		{
			np = FlattenOpNode(SubTree);
		}
		else if(SubTree instanceof AssignmentNode)
		{
			np = FlattenAssignmentNode(SubTree);
		}
		else if(SubTree instanceof ReturnNode)
		{
			np = FlattenReturnNode(SubTree);
		}
		else if(SubTree instanceof CreateObjectNode)
		{
			np = FlattenCreateObjectNode(SubTree);
		}
		else if(SubTree instanceof FieldAccessNode)
		{
			np = FlattenFieldAccessNode(SubTree);
		}
		else if(SubTree instanceof MethodInvokeNode)
		{
			np = FlattenMethodInvokeNode(SubTree);
		}
		else if(SubTree instanceof LiteralNode)
		{
			np = FlattenLiteralNode(SubTree);
		}
		else if(SubTree instanceof NameNode)
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
		DeclarationNode dn = (DeclarationNode)SubTree;
		
		NameNode nn = new NameNode(new NameDescriptor(dn.getVarDescriptor().getName()));
		AssignmentNode as = new AssignmentNode(nn, dn.getExpression());
		
		NodePair fanp = FlattenAssignmentNode((TreeNode)as);
		
		return fanp;
	}
	
	public void Store(Descriptor desc, FlatNode fn)
	{
		TAC.put(desc, fn);
	}
	
	public NodePair FlattenOpNode(TreeNode SubTree)
	{
		TempDescriptor tmp = null;
		Operation op = null;
		
		NodePair leftNodePair = null;
		NodePair rightNodePair = null;
		
		ExpressionNode eRight = null;
		
		if(!(SubTree instanceof AssignmentNode))
		{
			if(SubTree instanceof OpNode)
			{
				tmp = getTempDescriptor(((OpNode)SubTree).getType());
				op = ((OpNode)SubTree).getOp();
			}
			else
			{
				throw new Error("Live free or die hard.");
			}
			
			leftNodePair = FlattenExpression(((OpNode)SubTree).getLeft());
			eRight = ((OpNode)SubTree).getRight();
		}
		else
		{
			if(((AssignmentNode)SubTree).getSrc() instanceof LiteralNode)
			{
				AssignmentNode as = ((AssignmentNode)SubTree);
				tmp = new TempDescriptor(((NameNode)as.getDest()).getName().getSymbol(), (as.getSrc()).getType());
				op = new Operation(Operation.ASSIGN);
			}
			else if(((AssignmentNode)SubTree).getSrc() instanceof NameNode)
			{
				AssignmentNode as = ((AssignmentNode)SubTree);
				NameNode nn = ((NameNode)as.getDest());
				TypeDescriptor type = ((AssignmentNode)SubTree).getSrc().getType();
				tmp = new TempDescriptor(nn.getName().getSymbol(), type);
				op = new Operation(Operation.ASSIGN);
			}
			else
			{
				throw new Error("This is the end of the world as we know it.");
			}
			
			leftNodePair = FlattenExpression(((AssignmentNode)SubTree).getSrc());
		}
		
		FlatNode last = leftNodePair.end;
		
		if(eRight != null)
		{
			rightNodePair = FlattenExpression(((OpNode)SubTree).getRight());
			leftNodePair.end.next.add(rightNodePair.begin);
			rightNodePair.begin.prev.add(leftNodePair.end);
			last = rightNodePair.end;
		}
		
		FlatOpNode fon = new FlatOpNode(tmp, leftNodePair.tmp, (rightNodePair!=null)?rightNodePair.tmp:null, op);
		last.next.add(fon);
		fon.prev.add(last);		
		
		return new NodePair(leftNodePair.begin, fon, tmp);	
	}
	
	public NodePair FlattenAssignmentNode(TreeNode SubTree)
	{
		AssignmentNode as = ((AssignmentNode)SubTree);
		if(as.getSrc() instanceof NameNode)
		{			
			return FlattenOpNode(SubTree);
		}
		else if(as.getSrc() instanceof LiteralNode)
		{
			return FlattenOpNode(SubTree);
		}
		else if(as.getSrc() instanceof OpNode)
		{
			OpNode on = ((OpNode)as.getSrc());
			
			return FlattenOpNode((TreeNode)on);
		}
		else if(as.getSrc() instanceof CreateObjectNode)
		{
			return FlattenCreateObjectNode(SubTree);
		}
		else
		{
			throw new Error("I can't let you do that, Dave.");
		}
	}
	
	public NodePair FlattenCreateObjectNode(TreeNode SubTree)
	{
		TypeDescriptor type = ((AssignmentNode)SubTree).getSrc().getType();
		TempDescriptor tmp = getTempDescriptor(type);
		
		FlatNew fn = new FlatNew(type, tmp);
		
		return new NodePair (fn, fn, tmp);
	}
	
	public NodePair FlattenReturnNode(TreeNode SubTree)
	{
		ExpressionNode e = ((ReturnNode)SubTree).getReturnExpression();
		TempDescriptor tmp = (FlattenExpression(e)).tmp;
		
		FlatReturnNode frn = new FlatReturnNode(tmp);
		
		return new NodePair(frn, frn, tmp);
	}
	
	public NodePair FlattenFieldAccessNode(TreeNode SubTree) // MAYBE WORKING
	{
		System.out.println("If you see this, check FlattenFieldAccessNode");
		
		FieldAccessNode FieldNode = (FieldAccessNode)SubTree;
		
		FieldDescriptor fd = FieldNode.getField();
		TempDescriptor dst = getTempDescriptor(fd.getType());
		NodePair np = FlattenExpression(FieldNode.getExpression());
		
		TempDescriptor src = np.tmp;
		
		FlatFieldNode ffn = new FlatFieldNode(fd, src, dst);
		
		return new NodePair(ffn, ffn, dst);
	}
	
	public NodePair FlattenMethodInvokeNode(TreeNode SubTree)
	{
		NodePair np = null;
		return np;
	}
	
	public NodePair FlattenLiteralNode(TreeNode SubTree)
	{
		int val = ((Integer)((LiteralNode)SubTree).getValue());
		TypeDescriptor type = ((LiteralNode)SubTree).getType();
		TempDescriptor tmp = getTempDescriptor(type);
		
		FlatLiteralNode fln = new FlatLiteralNode(type, val, tmp);
		
		return new NodePair(fln, fln, tmp);
	}
	
	public NodePair FlattenNameNode(TreeNode SubTree)
	{
		TypeDescriptor type = ((NameNode)SubTree).getType();
		String name = ((NameNode)SubTree).getName().getSymbol();
		TempDescriptor tmp = new TempDescriptor(name, type);
		
		FlatNameNode fnn = new FlatNameNode(type, tmp);
		
		return new NodePair(fnn, fnn, tmp);
	}
	
	public NodePair FlattenIf(TreeNode SubTree) // NOT WORKING
	{
		ExpressionNode condition = ((IfStatementNode)SubTree).getCondition();
		BlockNode trueStatement = ((IfStatementNode)SubTree).getTrueBlock();
		BlockNode elseStatement = ((IfStatementNode)SubTree).getFalseBlock();
		
		NodePair testFlatCond = FlattenExpression(condition);
		
		
		
		NodePair trueFlatBlock = flattenBlockNode(trueStatement);
		NodePair elseFlatBlock = null;
		
		if(elseStatement != null)
		{
			elseFlatBlock = flattenBlockNode(elseStatement);
		}
		else
		{
			elseFlatBlock = FlattenNopNode(null);
		}
		
		TempDescriptor tmp = testFlatCond.tmp;
		
		FlatCondBranch ifStatement = new FlatCondBranch(tmp);
		
		if(elseFlatBlock != null)
		{
			return new NodePair(ifStatement.loopEntrance = elseFlatBlock.getBegin(), trueFlatBlock.getBegin(), tmp);
		}
		else
		{
			return new NodePair(ifStatement.loopEntrance = null, trueFlatBlock.begin, tmp);
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
		
		for(FlatNode flat : TAC.values())
		{
			returnString += ((FlatMethod)flat).getMethod().getSymbol() + "()\n{\n";
			FlatNode f = flat.getNext(0);
			while(true)
			{
				returnString += "\t" + f.toString() + "\n";
				
				if(f.next.size()==0)
				{
					returnString += "}\n";
					return returnString;
				}
				
				f = f.next.get(0);
			}
		}
		
		return returnString;
	}
}
