package IR.Flat;
import java.util.Vector;

import IR.*;
import IR.Tree.OpNode;

public class FlatOpNode extends FlatNode {
  TempDescriptor dest;
  TempDescriptor left;
  TempDescriptor right;
  OpNode op;

  public FlatOpNode(TempDescriptor dest, TempDescriptor left, TempDescriptor right, OpNode op) {
    this.dest=dest;
    this.left=left;
    this.right=right;
    this.op=op;
  }

  public String toString() {
    String str = "FlatOpNode_"+dest.toString();
    if (right!=null)
      str += "="+left.toString()+op.toString()+right.toString();
    else
      str += " "+op.toString() +" "+left.toString();
    return str;
  }
}
