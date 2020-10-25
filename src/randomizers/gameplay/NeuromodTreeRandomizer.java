package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
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
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import json.SettingsJson;
import randomizers.BaseRandomizer;

/**
 * Randomizes the neuromod upgrade tree.
 * 
 * Notes - Preserve the original structure of the tree - Ensure that neuromods
 * must still be unlocked in order
 * 
 */
public class NeuromodTreeRandomizer extends BaseRandomizer {
  private static final String FALSE = "false";
  private static final String REQUIRE_SCANNER = "RequireScanner";
  private static final int NUM_COLUMNS = 4;
  private static final int NUM_ROWS = 6;
  private static final int NUM_CATEGORIES = 6;
  private static final String FILES_DIR = "data/ark";
  private static final String ABILITIES_FILE = "abilities.xml";
  private static final String PDA_LAYOUT_FILE = "abilitiespdalayout.xml";
  private static final String RESEARCH_TOPICS_FILE = "researchtopics.xml";

  private static final String OUTPUT_PATH = "ark/player";

  private File abilitiesFileIn;
  private File layoutFileIn;
  private File researchFileIn;
  private File abilitiesFileOut;
  private File layoutFileOut;
  private File researchFileOut;

  private boolean unlockAllScans;

  private static final int ABILITIES_PER_CATEGORY = 16;

  private List<Ability> abilityIds;
  private Map<String, Element> abilityIdToElement;
  private static final String ID_PREREQ_FORMAT = "%s;%s";

  private static final boolean[][] LAYOUT_ONE = { { true, false, false, false, false, false },
      { false, true, false, false, false, false }, { false, false, true, false, false, false },
      { false, false, false, true, false, false }, { false, false, false, false, true, false },
      { false, false, false, false, false, true }, };

  private static final boolean[][] LAYOUT_TWO = { { true, false, false, true, false, false },
      { false, false, true, true, false, false }, { false, true, false, false, true, false },
      { false, false, true, false, true, false } };

  private static final boolean[][] LAYOUT_THREE = { { true, false, true, false, true, false },
      { false, true, false, true, false, true }, { false, true, true, false, true, false },
      { false, true, false, true, true, false } };

  private static final boolean[][] LAYOUT_FOUR = { { false, true, true, true, true, false },
      { true, true, true, true, false, false }, { true, false, true, true, false, true } };

  private static final boolean[][] LAYOUT_FIVE = { { true, true, true, true, true, false } };

  private static final boolean[][] LAYOUT_SIX = { { true, true, true, true, true, true } };
  private Document abilitiesDoc;

  public NeuromodTreeRandomizer(SettingsJson s, Path tempPatchDir) {
    super(s);
    unlockAllScans = s.getGameplaySettings()
                      .isUnlockAllScans();
    abilityIds = new ArrayList<>();
    abilityIdToElement = new HashMap<>();
    Path arkDir = new File(FILES_DIR).toPath();
    abilitiesFileIn = arkDir.resolve(ABILITIES_FILE)
                            .toFile();
    layoutFileIn = arkDir.resolve(PDA_LAYOUT_FILE)
                         .toFile();
    researchFileIn = arkDir.resolve(RESEARCH_TOPICS_FILE)
                           .toFile();
    Path outDir = tempPatchDir.resolve(OUTPUT_PATH);
    outDir.toFile()
          .mkdirs();
    abilitiesFileOut = outDir.resolve(ABILITIES_FILE)
                             .toFile();
    layoutFileOut = outDir.resolve(PDA_LAYOUT_FILE)
                          .toFile();
    researchFileOut = outDir.resolve(RESEARCH_TOPICS_FILE)
                            .toFile();

    try {
      populateAbilitiesMap();
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }

  public void populateAbilitiesMap() throws JDOMException, IOException {
    SAXBuilder saxBuilder = new SAXBuilder();
    abilitiesDoc = saxBuilder.build(abilitiesFileIn);
    Element abilitiesRoot = abilitiesDoc.getRootElement();
    List<Element> allAbilities = abilitiesRoot.getChild("Abilities")
                                              .getChildren();
    for (Element ability : allAbilities) {
      String id = ability.getAttributeValue("ID");
      abilityIds.add(new Ability(id));
      abilityIdToElement.put(id, ability);
    }
  }

  @Override
  public void randomize() {
    if (unlockAllScans) {
      try {
        removeScanRequirementInResearchTopics();
      } catch (JDOMException | IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    Collections.shuffle(abilityIds, r);

    try {
      SAXBuilder saxBuilder = new SAXBuilder();

      Document layoutDoc = saxBuilder.build(layoutFileIn);

      Element layoutRoot = layoutDoc.getRootElement();
      List<Element> allCategories = layoutRoot.getChild("Categories")
                                              .getChildren();
      int abilityIdIndex = 0;
      for (int categoryIndex = 0; categoryIndex < NUM_CATEGORIES; categoryIndex++) {
        // Generate a new layout using the next N ability IDs
        List<Ability> abilitiesToAdd = abilityIds.subList(abilityIdIndex, abilityIdIndex + ABILITIES_PER_CATEGORY);
        abilityIdIndex += ABILITIES_PER_CATEGORY;
        Ability[][] layout = createLayout(abilitiesToAdd, r);

        // Delete all existing rows
        Element rowsElement = allCategories.get(categoryIndex)
                                           .getChild("Rows");
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

        List<Element> rows = allCategories.get(categoryIndex)
                                          .getChild("Rows")
                                          .getChildren();
        for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
          List<Element> columns = rows.get(rowIndex)
                                      .getChild("Abilities")
                                      .getChildren();
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
      for (Ability a : abilityIds) {
        Element e = abilityIdToElement.get(a.getId());
        // Remove any pre-existing prereqs
        e.getChild("Prereqs")
         .removeChildren("Prereq");
        if (a.getPrereq() != null) {
          // Generate and add new prereq
          Element prereqElement = new Element("Prereq");
          prereqElement.setAttribute("value", a.getPrereq());
          e.getChild("Prereqs")
           .addContent(prereqElement);
        }
        if (unlockAllScans) {
          e.setAttribute(REQUIRE_SCANNER, FALSE);
        }
      }

      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(abilitiesDoc, new FileOutputStream(abilitiesFileOut));
      xmlOutput.output(layoutDoc, new FileOutputStream(layoutFileOut));

    } catch (JDOMException | IOException e) {
      e.printStackTrace();
      return;
    }
  }

  public void unlockAllScans()  {
    // Update prereqs and other requirements for all abilities

    try {
      for (Ability a : abilityIds) {
        Element e = abilityIdToElement.get(a.getId());
        e.setAttribute(REQUIRE_SCANNER, FALSE);
      }

      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(abilitiesDoc, new FileOutputStream(abilitiesFileOut));
      removeScanRequirementInResearchTopics();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  private void removeScanRequirementInResearchTopics() throws JDOMException, IOException {
    // Read in research topics file
    SAXBuilder saxBuilder = new SAXBuilder();
    Document researchDoc = saxBuilder.build(researchFileIn);
    Element researchRoot = researchDoc.getRootElement();
    // Iterate over every research topic
    for (Element topic : researchRoot.getChild("Topics")
                                     .getChildren()) {
      // Iterate over every ability group
      for (Element abilityGroup : topic.getChild("AbilityGroups")
                                       .getChildren()) {
        // Set required scans to 0
        abilityGroup.setAttribute("scansRequired", "0");
      }
    }

    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    try {
      xmlOutput.output(researchDoc, new FileOutputStream(researchFileOut));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Creates a single page layout
  private Ability[][] createLayout(List<Ability> ids, Random r) throws UnexpectedException {
    Ability[][] layout = new Ability[NUM_ROWS][NUM_COLUMNS];
    int idsIndex = 0;

    int[] elementsPerColumn = new int[NUM_COLUMNS];
    // Decide how many abilities will be in each column
    // First column- 2-4
    elementsPerColumn[0] = 2 + r.nextInt(3);
    // Second column- 6
    elementsPerColumn[1] = 6;
    // Third column- 4-5
    elementsPerColumn[2] = 4 + r.nextInt(2);
    // Fourth column- remainder
    elementsPerColumn[3] = ABILITIES_PER_CATEGORY - elementsPerColumn[0] - elementsPerColumn[1] - elementsPerColumn[2];

    for (int columnIndex = 0; columnIndex < NUM_COLUMNS; columnIndex++) {
      // Decide column layout depending on number of elements
      boolean[] columnLayout = new boolean[6];
      switch (elementsPerColumn[columnIndex]) {
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
        default:
          throw new UnexpectedException(String.format("Unhandled number of abilities in column %d: %d", columnIndex,
              elementsPerColumn[columnIndex]));
      }

      // Populate the column depending on layout
      for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
        if (columnLayout[rowIndex]) {
          // Decide prereqs for everything after the first column
          Ability nextAbility = ids.get(idsIndex++);
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
          layout[rowIndex][columnIndex] = nextAbility;
        } else {
          layout[rowIndex][columnIndex] = null;
        }
      }
    }
    return layout;
  }

  private static class Ability {
    private String id;

    private String prereq;

    public Ability(String id) {
      this.id = id;
      this.prereq = null;
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

    public String toString() {
      return String.format(ID_PREREQ_FORMAT, id, prereq);
    }
  }
}
