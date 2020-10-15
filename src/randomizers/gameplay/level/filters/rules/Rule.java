package randomizers.gameplay.level.filters.rules;

import javax.xml.stream.events.StartElement;

import utils.XmlEntity;

public interface Rule {
  // Given an entity config, whether this rule should apply
  public abstract boolean trigger(StartElement e);

  // Make necessary change to this entity.
  public abstract void apply(StartElement e);
}
