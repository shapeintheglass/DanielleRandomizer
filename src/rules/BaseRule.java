package rules;

public abstract class BaseRule {
  public BaseRule() {

  }
  
  // Given an entity config, whether this rule should apply
  public boolean trigger() {
    return false;
  }
  
  public void apply() {
    
  }
}
