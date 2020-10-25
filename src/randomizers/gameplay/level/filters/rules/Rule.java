package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import org.jdom2.Element;

public interface Rule {
  // Given an entity config, whether this rule should apply
  public abstract boolean trigger(Element e, Random r, String filename);

  // Make necessary change to this entity.
  public abstract void apply(Element e, Random r, String filename);
}
