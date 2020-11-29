package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import com.google.common.collect.ImmutableSet;
import databases.EntityDatabase;
import databases.TaggedDatabase;
import json.SettingsJson;
import randomizers.BaseRandomizer;
import randomizers.gameplay.filters.BaseFilter;
import utils.FileConsts;
import utils.FileConsts.Archetype;

/**
 * 
 * Randomizes content in the entity archetypes folder.
 *
 */
public class EntityArchetypesRandomizer extends BaseRandomizer {

  private static final String ARK_ITEMS_SOURCE = "data/ark";
  private static final String ARK_ITEMS_DEST = "ark/items";
  private static final String ARK_ITEMS_FILE = "arkitems.xml";

  private List<BaseFilter> filterList;
  private Random r;

  private Path tempPatchDir;
  private TaggedDatabase database;

  private static final ImmutableSet<Archetype> SUPPORTED_ARCHETYPES =
      ImmutableSet.of(Archetype.PICKUPS);

  public EntityArchetypesRandomizer(SettingsJson s, Path tempPatchDir) {
    super(s);
    filterList = new LinkedList<>();
    r = new Random(s.getSeed());
    this.tempPatchDir = tempPatchDir;
    this.database = EntityDatabase.getInstance();
  }

  public EntityArchetypesRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  private void copyArkItemsFile() throws IOException {
    // Copy ArkItems file over as-is
    tempPatchDir.resolve(ARK_ITEMS_DEST)
        .toFile()
        .mkdirs();
    File out = tempPatchDir.resolve(ARK_ITEMS_DEST)
        .resolve(ARK_ITEMS_FILE)
        .toFile();


    out.createNewFile();

    Files.copy(Paths.get(ARK_ITEMS_SOURCE)
        .resolve(ARK_ITEMS_FILE), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void randomize() {
    try {
      copyArkItemsFile();
    } catch (IOException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
      return;
    }

    tempPatchDir.resolve(FileConsts.ENTITY_ARCHETYPES_DEST_DIR)
        .toFile()
        .mkdirs();
    for (Archetype a : SUPPORTED_ARCHETYPES) {
      File out = tempPatchDir.resolve(FileConsts.ENTITY_ARCHETYPES_DEST_DIR)
          .resolve(FileConsts.ARCHETYPE_TO_FILENAME.get(a))
          .toFile();
      try {
        out.createNewFile();
      } catch (IOException e1) {
        e1.printStackTrace();
        return;
      }

      Element root = new Element("EntityPrototypeLibrary");
      String libraryName = FileConsts.ARCHETYPE_TO_LIBRARY_NAME.get(a);
      root.setAttribute("Name", libraryName);

      List<String> allEntities = database.getAllForTag(libraryName);

      for (String name : allEntities) {
        Element e = database.getEntityByName(name)
            .clone();

        for (BaseFilter filter : filterList) {
          filter.filterEntity(e, r, a.toString());
        }

        root.addContent(e);
      }

      Document document = new Document(root);
      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      try {
        xmlOutput.output(document, new FileOutputStream(out));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
