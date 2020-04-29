package tests.utils;

import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import utils.Utils;

public class UtilsTest {
  private static final String LINE = "<Foo PlainWord=\"PlainWord\" Symbols=\"!@#$%^&*()_\\<>?\" WithSpaces=\"with spaces\"/>";
  private static final String LINE_MULTI = "<Foo Multi=\"True\">";

  @Test
  public void testGetTagName() {
    Assert.assertEquals("Foo", Utils.getTagName(LINE));
  }

  @Test
  public void testIsSingleLine() {
    Assert.assertTrue(Utils.isSingleLine(LINE));
  }

  @Test
  public void testIsSingleLine_multi() {
    Assert.assertFalse(Utils.isSingleLine(LINE_MULTI));
  }

  @Test
  public void testGetKeysFromLine() {
    Map<String, String> keys = Utils.getKeysFromLine(LINE);
    Assert.assertEquals("PlainWord", keys.get("PlainWord"));
    Assert.assertEquals("!@#$%^&*()_\\<>?", keys.get("Symbols"));
    Assert.assertEquals("with spaces", keys.get("WithSpaces"));
  }

  @Test
  public void testGetRandomWeighted() {
    Integer[] nums = { 1, 2, 3 };
    int[] weights = { 1, 1, 1 };

    Random r = new Random();

    int i = Utils.getRandomWeighted(nums, weights, r);
    Assert.assertTrue(i == 1 || i == 2 || i == 3);
  }

  @Test
  public void testGetRandomWeighted_zeroWeight() {
    Integer[] nums = { 1, 2, 3 };
    int[] weights = { 0, 1, 0 };

    Random r = new Random();

    int i = Utils.getRandomWeighted(nums, weights, r);
    Assert.assertEquals(2, i);
  }

  @Test
  public void testGetRandomWeighted_allZeroWeights() {
    Integer[] nums = { 1, 2, 3 };
    int[] weights = { 0, 0, 0 };

    Random r = new Random();

    Integer i = Utils.getRandomWeighted(nums, weights, r);
    Assert.assertNull(i);
  }
}
