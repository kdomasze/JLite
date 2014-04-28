package IR.Flat;
import java.util.Vector;

public class FlatCondBranch extends FlatNode {
  TempDescriptor test_cond;
  FlatNode loopEntrance;

  public FlatCondBranch(TempDescriptor td) {
    test_cond=td;
  }

  public String toString() {
    return "conditional branch("+test_cond.toString()+")";
  }
}
