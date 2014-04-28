package IR;

/**
 * Descriptor
 *
 * represents a symbol in the language (var name, function name, etc).
 */

public abstract class Descriptor {
  protected String name;

  public Descriptor(String name) {
    this.name = name;
  }

  protected Descriptor(String name, String safename) {
    this.name = name;
  }

  public String toString() {
    return name;
  }

  public String getSymbol() {
    return name;
  }
}
