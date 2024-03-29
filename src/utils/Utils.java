package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jdom2.Element;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Utils {

  private static final ImmutableSet<String> PROGRESSION_ITEMS = ImmutableSet.of("Player.ZeroGSuit",
      "Player.Psychoscope", "Mods.Psychoscope.CanScanCoral", "MissionItems.BigNullwaveTransmitter",
      "MissionItems.BigNullwave_Alex_Cinematic", "MissionItems.SelfDestructKey_Alex",
      "MissionItems.SelfDestructKey_Morgan", "MissionItems.SelfDestructKey_Morgan_Cinematic",
      "Crafting.FabricationPlans.PropulsionSystemFabPlan",
      "Crafting.FabricationPlans.BigNullwaveTransmitterFabPlan",
      "Crafting.FabricationPlans.BigNullwaveTransmitterFabPlan_Cinematic",
      "Crafting.FabricationPlans.AlexStationKeyFabPlan",
      "Crafting.FabricationPlans.MorganStationKeyFabPlan",
      "Crafting.FabricationPlans.CoralScanModFabPlan");

  private static final ImmutableSet<String> LIBRARY_PREFIXES =
      ImmutableSet.of("ArkGameplayArchitecture", "ArkGameplayObjects", "ArkHumans", "ArkNpcs",
          "ArkPhysicsProps", "ArkPickups", "ArkProjectiles", "ArkRobots", "ArkSpecialWeapons");

  public static Path createTempDir(Path workingDir, String name) {
    long now = new Date().getTime();
    Path newTempDir = workingDir.resolve(String.format("temp_%8d_%s", now, name));
    newTempDir.toFile()
        .mkdirs();
    return newTempDir;
  }

  public static int getRandomBetween(int lowerBound, int upperBound, Random r) {
    if (lowerBound >= upperBound) {
      return lowerBound;
    }

    int diff = upperBound - lowerBound;
    return r.nextInt(diff) + lowerBound;
  }

  public static <T extends Comparable<T>> T getRandomFromCollection(ImmutableCollection<T> arr,
      Random r) {
    List<T> tList = Lists.newArrayList();
    tList.addAll(arr);
    Collections.sort(tList);
    return getRandom(tList, r);
  }

  public static <T> T getRandom(List<T> arr, Random r) {
    int index = r.nextInt(arr.size());
    return arr.get(index);
  }

  public static <T> T getRandom(T[] arr, Random r) {
    int index = r.nextInt(arr.length);
    return arr[index];
  }

  public static <T> T getRandom(T[] arr1, T[] arr2, Random r) {
    int index = r.nextInt(arr1.length + arr2.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    return arr2[index - arr1.length];
  }

  public static <T> T getRandom(T[] arr1, T[] arr2, T[] arr3, Random r) {
    int index = r.nextInt(arr1.length + arr2.length + arr3.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    if (index < arr1.length + arr2.length) {
      return arr2[index - arr1.length];
    }
    return arr3[index - arr1.length - arr2.length];
  }

  public static <T> T getRandom(T[] arr1, T[] arr2, T[] arr3, T[] arr4, Random r) {
    int index = r.nextInt(arr1.length + arr2.length + arr3.length + arr4.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    if (index < arr1.length + arr2.length) {
      return arr2[index - arr1.length];
    }
    if (index < arr1.length + arr2.length + arr3.length) {
      return arr3[index - arr1.length - arr2.length];
    }
    return arr3[index - arr1.length - arr2.length - arr3.length];
  }

  public static <T> T getRandomWeighted(T[] arr, Integer[] weights, Random r) {
    int sum = Arrays.stream(weights)
        .reduce(0, Integer::sum);
    if (sum == 0) {
      return null;
    }

    int index = r.nextInt(sum);

    int prev = 0;
    for (int i = 0; i < arr.length; i++) {
      if (index >= prev && index < prev + weights[i]) {
        return arr[i];
      }
      prev += weights[i];
    }
    return arr[arr.length - 1];
  }

  public static <T> T getRandomWeighted(List<T> arr, List<Integer> weights, Random r) {
    if (weights == null || weights.size() != arr.size()) {
      weights = new ArrayList<>(arr.size());
      for (int i = 0; i < arr.size(); i++) {
        weights.add(1);
      }
    }

    int sum = weights.stream()
        .reduce(0, Integer::sum);
    if (sum == 0) {
      return null;
    }

    int index = r.nextInt(sum);

    int prev = 0;
    for (int i = 0; i < arr.size(); i++) {
      if (index >= prev && index < prev + weights.get(i)) {
        return arr.get(i);
      }
      prev += weights.get(i);
    }
    return arr.get(arr.size() - 1);
  }

  public static String getCommonElement(Collection<String> s1, Collection<String> s2) {
    if (s1 == null || s2 == null) {
      return null;
    }
    for (String s : s1) {
      if (s2.contains(s)) {
        return s;
      }
    }
    return null;
  }

  public static void copyDirectory(File fromDir, Path toDir) throws IOException {
    if (fromDir.isDirectory()) {
      for (File f : fromDir.listFiles()) {
        toDir.resolve(f.getName())
            .toFile()
            .mkdir();
        copyDirectory(f, toDir.resolve(f.getName()));
      }
    } else {
      Files.copy(fromDir.toPath(), toDir, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  public static void deleteDirectory(File dir) {
    if (dir.isDirectory()) {
      for (File f : dir.listFiles()) {
        deleteDirectory(f);
      }
    }
    dir.delete();
  }

  public static Set<String> getTags(Element e) {
    Set<String> tags = new HashSet<>();

    if (e == null) {
      return tags;
    }

    // Split name on dots
    String name = e.getAttributeValue("Name");
    String[] nameTags = name.split("\\.");
    Arrays.stream(nameTags)
        .forEach(s -> tags.add(s));
    tags.add(e.getAttributeValue("Class"));
    tags.add(e.getAttributeValue("Library"));

    Element properties = e.getChild("Properties");
    addTagForBoolean(tags, properties, "bIsCarryable", "_CARRYABLE");
    addTagForBoolean(tags, properties, "bIsMimicable", "_MIMICABLE");
    addTagForBoolean(tags, properties, "bUsable", "_USABLE");
    addTagForBoolean(tags, properties, "bConsumable", "_CONSUMABLE");
    addTagForBoolean(tags, properties, "bConsumable", "_CONSUMABLE");
    addTagForBoolean(tags, properties, "bIsTrash", "_TRASH");
    addTagForBoolean(tags, properties, "bImportant", "_IMPORTANT");
    addTagForBoolean(tags, properties, "bIsPlotCritical", "_PLOT_CRITICAL");
    addTagForBoolean(tags, properties, "bIsRandom", "_RANDOM");
    addTagForBoolean(tags, properties, "bSurvivalMode", "_SURVIVAL");
    addTagForBoolean(tags, properties, "bCanTriggerAreas", "_CAN_TRIGGER_AREAS");
    addCarryRequirementTag(tags, properties);

    if (PROGRESSION_ITEMS.contains(name)) {
      tags.add("_PROGRESSION");
    }

    return tags;
  }

  private static void addTagForBoolean(Set<String> tags, Element properties, String propertyName,
      String tagName) {
    if (properties == null || properties.getAttributeValue(propertyName) == null) {
      return;
    }

    if (properties.getAttributeValue(propertyName)
        .equals("1")) {
      tags.add(tagName);
    }
  }

  private static void addCarryRequirementTag(Set<String> tags, Element properties) {
    if (properties == null) {
      return;
    }
    String carryReq = properties.getAttributeValue("ability_CarryRequirement");

    if (carryReq == null) {
      return;
    }

    switch (carryReq) {
      case "3149325216928599448":
        tags.add("_LEVERAGE_I");
        return;
      case "3149325216928599824":
        tags.add("_LEVERAGE_II");
        return;
      case "3149325216933436173":
        tags.add("_LEVERAGE_III");
        return;
    }
  }

  public static String stripLibraryPrefixForEntity(String e) {
    if (e.contains(".") && LIBRARY_PREFIXES.contains(e.substring(0, e.indexOf('.')))) {
      return e.substring(e.indexOf('.') + 1);
    }
    return e;
  }

  public static String getNameForEntity(Element e) {
    return String.format("%s.%s", e.getAttributeValue("Library"), e.getAttributeValue("Name"));
  }

  public static String stripPrefix(String s) {
    int dotIndex = s.indexOf('.');
    return s.substring(dotIndex + 1);
  }

  public static long getNewSeed() {
    return new Random().nextLong();
  }

  public static boolean listAContainsListB(List<String> listA, List<String> listB) {
    if (listA == null || listA.isEmpty()) {
      return false;
    }
    Set<String> listASet = Sets.newHashSet(listA);
    for (String s : listB) {
      if (!listASet.contains(s)) {
        return false;
      }
    }
    return true;
  }

  public static long stringToLong(String s) {
    try {
      return Long.parseLong(s);
    } catch (NumberFormatException e) {
      return s.hashCode();
    }
  }
  
  public static <T> List<T> intersectLists(List<T> a, List<T> b) {
    List<T> result = Lists.newArrayList();
    Set<T> setA = Sets.newHashSet(a);
    for (T t : b) {
      if (setA.contains(t)) {
        result.add(t);
      }
    }
    return result;
  }
}
