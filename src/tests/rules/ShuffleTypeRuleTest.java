package tests.rules;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import databases.Database;
import rules.ShuffleWithinTypeRule;
import tests.databases.FakeDatabase;
import utils.XmlEntity;

public class ShuffleTypeRuleTest {

  ShuffleWithinTypeRule r;
  Database d;

  @Before
  public void setup() {
    d = new FakeDatabase();
    r = new ShuffleWithinTypeRule(d);
  }

  @Test
  public void testGetSupportedTag_noSettings() {
    XmlEntity x = new XmlEntity("RecyclerJunk.1", new HashMap<>(), new ArrayList<>(), 0);
    x.setValue("Archetype", "ArkPickups.RecyclerJunk.1");
    Assert.assertNull(r.getSupportedTag(x));
  }

  @Test
  public void testGetSupportedTag_whitelisted() {
    XmlEntity x = new XmlEntity("RecyclerJunk.1", new HashMap<>(), new ArrayList<>(), 0);
    x.setValue("Archetype", "ArkPickups.RecyclerJunk.1");
    r.setTagsToShuffle("JUNK");
    Assert.assertEquals("JUNK", r.getSupportedTag(x));
  }

  @Test
  public void testGetSupportedTag_priority() {
    XmlEntity x = new XmlEntity("RecyclerJunk.1", new HashMap<>(), new ArrayList<>(), 0);
    x.setValue("Archetype", "ArkPickups.RecyclerJunk.1");
    r.setTagsToShuffle("JUNK", "GLOBAL");
    Assert.assertEquals("JUNK", r.getSupportedTag(x));
  }
  
  @Test
  public void testGetSupportedTag_priorityReverse() {
    XmlEntity x = new XmlEntity("RecyclerJunk.1", new HashMap<>(), new ArrayList<>(), 0);
    x.setValue("Archetype", "ArkPickups.RecyclerJunk.1");
    r.setTagsToShuffle("GLOBAL", "JUNK");
    Assert.assertEquals("GLOBAL", r.getSupportedTag(x));
  }

  @Test
  public void testGetSupportedTag_blacklisted() {
    XmlEntity x = new XmlEntity("RecyclerJunk.1", new HashMap<>(), new ArrayList<>(), 0);
    x.setValue("Archetype", "ArkPickups.RecyclerJunk.1");
    r.setTagsToShuffle("JUNK");
    r.setTagsToIgnore("JUNK");
    Assert.assertNull(r.getSupportedTag(x));
  }
}
