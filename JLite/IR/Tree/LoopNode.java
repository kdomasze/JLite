package IR.Tree;

public class LoopNode extends BlockStatementNode {
  ExpressionNode condition;
  BlockNode body;

  public LoopNode(ExpressionNode condition, BlockNode body) {
    this.condition=condition;
    this.body=body;
  }

  public ExpressionNode getCondition() {
    return condition;
  }

  public BlockNode getBody() {
    return body;
  }

  public String printNode(int indent) {
    return "while("+condition.printNode(0)+") "+body.printNode(indent+INDENT)+"\n";
  }

  public int kind() {
    return Kind.LoopNode;
  }
}
