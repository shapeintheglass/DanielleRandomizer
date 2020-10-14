package randomizers.gameplay.level.filters.rules;

import utils.XmlEntity;

public interface Rule {
  // Given an entity config, whether this rule should apply
  public abstract boolean trigger(XmlEntity e);

  // Make necessary change to this entity.
  public abstract void apply(XmlEntity e);
}
