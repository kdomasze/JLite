package IR.Flat;
import java.util.Vector;
import IR.*;

public class FlatOpNode extends FlatNode {
  TempDescriptor dest;
  TempDescriptor left;
  TempDescriptor right;
  Operation op;

  public FlatOpNode(TempDescriptor dest, TempDescriptor left, TempDescriptor right, Operation op) {
    this.dest=dest;
    this.left=left;
    this.right=right;
    this.op=op;
  }

  public String toString() {
    String str = "FlatOpNode_"+dest.toString();
    if (right!=null)
      str += "="+left.toString()+op.toString()+right.toString();
    else if (op.getOp()==Operation.ASSIGN)
      str += " = "+left.toString();
    else
      str += " "+op.toString() +" "+left.toString();
    return str;
  }
}
