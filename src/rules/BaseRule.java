package rules;

import utils.XmlEntity;

public abstract class BaseRule {

  // Given an entity config, whether this rule should apply
  public boolean trigger(XmlEntity e, String levelDir) {
    throw new UnsupportedOperationException();
  }

  // Make necessary change to this entity.
  public void apply(XmlEntity e, String levelDir) {
    throw new UnsupportedOperationException();
  }
}
