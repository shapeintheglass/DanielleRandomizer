package randomizers.gameplay;

import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.primitives.ImmutableIntArray;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

/**
 * Randomizes the neuromod upgrade tree.
 */
public class NeuromodTreeRandomizer extends BaseRandomizer {
  private static final String FALSE = "false";
  private static final String REQUIRE_SCANNER = "RequireScanner";
  private static final int NUM_COLUMNS = 4;
  private static final int NUM_ROWS = 7;
  private static final int NUM_CATEGORIES = 6;

  private boolean unlockAllScans;

  private Map<String, Ability> abilityIdToAbility;
  private Map<String, Element> abilityIdToElement;
  private Multimap<String, String> categoryToAbilityId;
  private List<String> categoryOrder;

  private static final boolean[][] LAYOUT_ZERO = { { false, false, false, false, false, false, false } };

  private static final boolean[][] LAYOUT_ONE = { { true, false, false, false, false, false, false }, { false, true,
      false, false, false, false, false }, { false, false, true, false, false, false, false }, { false, false, false,
          true, false, false, false }, { false, false, false, false, true, false, false }, { false, false, false, false,
              false, true, false }, { false, false, false, false, false, false, true }, };

  private static final boolean[][] LAYOUT_TWO = { { true, false, false, true, false, false, false }, { false, false,
      true, true, false, false, false }, { false, true, false, false, true, false, false }, { false, false, true, false,
          true, false, false } };

  private static final boolean[][] LAYOUT_THREE = { { true, false, true, false, true, false, false }, { false, true,
      false, true, false, true, false }, { false, true, true, false, true, false, false }, { false, true, false, true,
          true, false, false } };

  private static final boolean[][] LAYOUT_FOUR = { { false, true, true, true, true, false, false }, { true, true, true,
      true, false, false, false }, { true, false, true, true, false, true, false } };

  private static final boolean[][] LAYOUT_FIVE = { { true, true, true, true, true, false, false }, { false, true, true,
      true, true, true, false } };

  private static final boolean[][] LAYOUT_SIX = { { true, true, true, true, true, true, false }, { false, true, true,
      true, true, true, true } };

  private static final boolean[][] LAYOUT_SEVEN = { { true, true, true, true, true, true, true } };

  private static final ImmutableList<String> HUMAN_PSI_IDS = ImmutableList.of("3149325216929347148",
      "3149325216929347150", "3149325216929347152", "3149325216929347147", "3149325216977824648",
      "3149325216929347153", "3149325216929347155", "3149325216929347156");

  // If we recalculate neuromod costs, pull a random int from the appropriate set.
  private static final ImmutableIntArray COLUMN_ONE_COSTS = ImmutableIntArray.of(1, 1, 2, 2, 3);
  private static final ImmutableIntArray COLUMN_TWO_COSTS = ImmutableIntArray.of(3, 4, 5);
  private static final ImmutableIntArray COLUMN_THREE_COSTS = ImmutableIntArray.of(5, 6, 7);
  private static final ImmutableIntArray COLUMN_FOUR_COSTS = ImmutableIntArray.of(8, 9, 10);
  private static final ImmutableList<ImmutableIntArray> COLUMN_COSTS = ImmutableList.of(COLUMN_ONE_COSTS,
      COLUMN_TWO_COSTS, COLUMN_THREE_COSTS, COLUMN_FOUR_COSTS);

  private Document abilitiesDoc;
  private Document layoutDoc;

  public NeuromodTreeRandomizer(Settings s, ZipHelper zipHelper) {
    super(s, zipHelper);
    unlockAllScans = s.getCheatSettings().getUnlockAllScans();
    abilityIdToAbility = new HashMap<>();
    abilityIdToElement = new HashMap<>();
    categoryToAbilityId = HashMultimap.create();
    categoryOrder = Lists.newArrayList();

    try {
      populateAbilitiesMap();
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }

  public void populateAbilitiesMap() throws JDOMException, IOException {
    abilitiesDoc = zipHelper.getDocument(ZipHelper.NEUROMOD_ABILITIES);
    Element abilitiesRoot = abilitiesDoc.getRootElement();
    List<Element> allAbilities = abilitiesRoot.getChild("Abilities").getChildren();
    for (Element ability : allAbilities) {
      String name = ability.getAttributeValue("Name");
      String id = ability.getAttributeValue("ID");
      abilityIdToAbility.put(id, new Ability(name, id));
      abilityIdToElement.put(id, ability);
    }

    layoutDoc = zipHelper.getDocument(ZipHelper.NEUROMOD_PDA_LAYOUT);
    Element layoutRoot = layoutDoc.getRootElement();
    List<Element> categories = layoutRoot.getChild("Categories").getChildren();
    for (Element arkAbilityCategory : categories) {
      String categoryName = arkAbilityCategory.getAttributeValue("Name");
      categoryOrder.add(categoryName);
      List<Element> rows = arkAbilityCategory.getChild("Rows").getChildren();
      for (Element abilityRow : rows) {
        for (Element ability : abilityRow.getChild("Abilities").getChildren()) {
          String abilityValue = ability.getAttributeValue("Value");
          if (abilityValue != null) {
            categoryToAbilityId.put(categoryName, abilityValue);
          }
        }
      }
    }
  }

  @Override
  public void randomize() {
    if (unlockAllScans) {
      try {
        removeScanRequirementInResearchTopics();
      } catch (JDOMException | IOException e) {
        e.printStackTrace();
      }
    } else {
      removePsychoscopeRequirementForPsiAptitudes();
    }

    try {
      Element layoutRoot = layoutDoc.getRootElement();
      List<Element> allCategories = layoutRoot.getChild("Categories").getChildren();
      for (int categoryIndex = 0; categoryIndex < NUM_CATEGORIES; categoryIndex++) {
        // Generate a new layout using the next category's ability IDs
        String categoryName = categoryOrder.get(categoryIndex);
        List<String> abilityIdsInCategory = new ArrayList<String>(categoryToAbilityId.get(categoryName));
        // Sort to ensure consistency when randomizing
        Collections.sort(abilityIdsInCategory);
        Collections.shuffle(abilityIdsInCategory, r);
        Ability[][] layout = createLayout(abilityIdsInCategory, r);

        // Delete all existing rows
        Element rowsElement = allCategories.get(categoryIndex).getChild("Rows");
        rowsElement.removeContent();
        // Add 6 rows, 4 columns per row
        for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
          Element abilityRow = new Element("ArkAbilityRow");
          Element columns = new Element("Abilities");
          abilityRow.addContent(columns);
          for (int columnIndex = 0; columnIndex < NUM_COLUMNS; columnIndex++) {
            columns.addContent(new Element("Ability"));
          }
          rowsElement.addContent(abilityRow);
        }

        List<Element> rows = allCategories.get(categoryIndex).getChild("Rows").getChildren();
        for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
          List<Element> columns = rows.get(rowIndex).getChild("Abilities").getChildren();
          for (int columnIndex = 0; columnIndex < NUM_COLUMNS; columnIndex++) {
            Element ability = columns.get(columnIndex);

            // Use the generated layout to set the value here
            if (layout[rowIndex][columnIndex] == null) {
              // Intentionally leave this cell blank
              ability.removeAttribute("Value");
            } else {
              // Set ability id
              Ability a = layout[rowIndex][columnIndex];
              ability.setAttribute("Value", a.getId());
            }
          }
        }
      }

      // Update prereqs and other requirements for all abilities
      for (Ability a : abilityIdToAbility.values()) {
        Element e = abilityIdToElement.get(a.getId());
        // Remove any pre-existing prereqs
        e.getChild("Prereqs").removeChildren("Prereq");
        if (a.getPrereq() != null) {
          // Generate and add new prereq
          Element prereqElement = new Element("Prereq");
          prereqElement.setAttribute("value", a.getPrereq());
          e.getChild("Prereqs").addContent(prereqElement);
        }
        if (unlockAllScans) {
          e.setAttribute(REQUIRE_SCANNER, FALSE);
        }
        e.setAttribute("Cost", Integer.toString(a.getCost()));
      }

      zipHelper.copyToPatch(abilitiesDoc, ZipHelper.NEUROMOD_ABILITIES);
      zipHelper.copyToPatch(layoutDoc, ZipHelper.NEUROMOD_PDA_LAYOUT);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
  }

  public void unlockAllScans() {
    // Update prereqs and other requirements for all abilities
    try {
      for (Ability a : abilityIdToAbility.values()) {
        Element e = abilityIdToElement.get(a.getId());
        e.setAttribute(REQUIRE_SCANNER, FALSE);
      }

      zipHelper.copyToPatch(abilitiesDoc, ZipHelper.NEUROMOD_ABILITIES);
      removeScanRequirementInResearchTopics();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JDOMException e) {
      e.printStackTrace();
    }
  }

  private void removePsychoscopeRequirementForPsiAptitudes() {
    for (String id : HUMAN_PSI_IDS) {
      Element e = abilityIdToElement.get(id);
      e.setAttribute(REQUIRE_SCANNER, FALSE); 
    }
  }

  private void removeScanRequirementInResearchTopics() throws JDOMException, IOException {
    // Read in research topics file
    Document researchDoc = zipHelper.getDocument(ZipHelper.NEUROMOD_RESEARCH_TOPICS);
    Element researchRoot = researchDoc.getRootElement();
    // Iterate over every research topic
    for (Element topic : researchRoot.getChild("Topics").getChildren()) {
      // Iterate over every ability group
      for (Element abilityGroup : topic.getChild("AbilityGroups").getChildren()) {
        // Set required scans to 0
        abilityGroup.setAttribute("scansRequired", "0");
      }
    }
    zipHelper.copyToPatch(researchDoc, ZipHelper.NEUROMOD_RESEARCH_TOPICS);
  }

  // Creates a single page layout
  private Ability[][] createLayout(List<String> ids, Random r) throws UnexpectedException {
    Ability[][] layout = new Ability[NUM_ROWS][NUM_COLUMNS];

    int[] elementsPerColumn = new int[NUM_COLUMNS];
    // Decide how many abilities will be in each column
    // First column: 3-4
    elementsPerColumn[0] = 3 + r.nextInt(2);
    // Second column: 1st + 0-2
    elementsPerColumn[1] = elementsPerColumn[0] + 1 + r.nextInt(3);
    int remainder = ids.size() - elementsPerColumn[0] - elementsPerColumn[1];
    // Third column: 4-7
    elementsPerColumn[2] = Math.min(remainder, 4 + r.nextInt(4));
    // Fourth column- remainder
    remainder = ids.size() - elementsPerColumn[0] - elementsPerColumn[1] - elementsPerColumn[2];
    elementsPerColumn[3] = Math.max(remainder, 0);
    int idsIndex = 0;
    for (int columnIndex = 0; columnIndex < NUM_COLUMNS; columnIndex++) {
      // Decide column layout depending on number of elements
      boolean[] columnLayout = new boolean[NUM_COLUMNS];
      switch (elementsPerColumn[columnIndex]) {
        case 0:
          columnLayout = LAYOUT_ZERO[r.nextInt(LAYOUT_ZERO.length)];
          break;
        case 1:
          columnLayout = LAYOUT_ONE[r.nextInt(LAYOUT_ONE.length)];
          break;
        case 2:
          columnLayout = LAYOUT_TWO[r.nextInt(LAYOUT_TWO.length)];
          break;
        case 3:
          columnLayout = LAYOUT_THREE[r.nextInt(LAYOUT_THREE.length)];
          break;
        case 4:
          columnLayout = LAYOUT_FOUR[r.nextInt(LAYOUT_FOUR.length)];
          break;
        case 5:
          columnLayout = LAYOUT_FIVE[r.nextInt(LAYOUT_FIVE.length)];
          break;
        case 6:
          columnLayout = LAYOUT_SIX[r.nextInt(LAYOUT_SIX.length)];
          break;
        case 7:
          columnLayout = LAYOUT_SEVEN[r.nextInt(LAYOUT_SEVEN.length)];
          break;
        default:
          throw new UnexpectedException(String.format("Unhandled number of abilities in column %d: %d", columnIndex,
              elementsPerColumn[columnIndex]));
      }

      // Populate the column depending on layout
      for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
        if (columnLayout[rowIndex]) {
          // Decide prereqs for everything after the first column
          String nextId = ids.get(idsIndex++);
          Ability nextAbility = abilityIdToAbility.get(nextId);
          if (columnIndex != 0) {
            // Scan upwards until the first prereq is found
            boolean prereqFound = false;
            for (int prereqRow = rowIndex; prereqRow >= 0; prereqRow--) {
              if (layout[prereqRow][columnIndex - 1] != null) {
                nextAbility.setPrereq(layout[prereqRow][columnIndex - 1].getId());
                prereqFound = true;
                break;
              }
            }
            if (!prereqFound) {
              // Scan downwards the first prereq is found
              for (int prereqRow = rowIndex + 1; prereqRow < NUM_ROWS; prereqRow++) {
                if (layout[prereqRow][columnIndex - 1] != null) {
                  nextAbility.setPrereq(layout[prereqRow][columnIndex - 1].getId());
                  break;
                }
              }
            }
          }
          // Generate a new cost for this ability based on its new column
          ImmutableIntArray columnCosts = COLUMN_COSTS.get(columnIndex);
          int newCost = columnCosts.get(r.nextInt(columnCosts.length()));
          nextAbility.setCost(newCost);

          layout[rowIndex][columnIndex] = nextAbility;
        } else {
          layout[rowIndex][columnIndex] = null;
        }
      }
    }
    return layout;
  }

  private static class Ability {
    private String name;
    private String id;
    private String prereq;
    private int cost;

    public Ability(String name, String id) {
      this.name = name;
      this.id = id;
      this.prereq = null;
      this.cost = 0;
    }

    public String getId() {
      return id;
    }

    public String getPrereq() {
      return prereq;
    }

    public void setPrereq(String prereq) {
      this.prereq = prereq;
    }

    public int getCost() {
      return cost;
    }

    public void setCost(int cost) {
      this.cost = cost;
    }

    public String toString() {
      return name;
    }
  }
}
