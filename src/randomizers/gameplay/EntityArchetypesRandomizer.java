package randomizers.gameplay;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import com.google.common.collect.ImmutableSet;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import randomizers.gameplay.filters.BaseFilter;
import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.ZipHelper;

/**
 * 
 * Randomizes content in the entity archetypes folder.
 *
 */
public class EntityArchetypesRandomizer extends BaseRandomizer {

  private List<BaseFilter> filterList;

  private TaggedDatabase database;

  private static final ImmutableSet<Archetype> SUPPORTED_ARCHETYPES = ImmutableSet.of(Archetype.PICKUPS, Archetype.GAMEPLAY_ARCHITECTURE);

  public EntityArchetypesRandomizer(Settings s, TaggedDatabase database, ZipHelper zipHelper) {
    super(s, zipHelper);
    filterList = new LinkedList<>();
    this.database = database;
  }

  public EntityArchetypesRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  @Override
  public void randomize() {
    for (Archetype a : SUPPORTED_ARCHETYPES) {
      String out = ZipHelper.ENTITY_ARCHETYPES_SOURCE_DIR + FileConsts.ARCHETYPE_TO_FILENAME.get(a);

      Element root = new Element("EntityPrototypeLibrary");
      String libraryName = FileConsts.ARCHETYPE_TO_LIBRARY_NAME.get(a);
      root.setAttribute("Name", libraryName);

      List<String> allEntities = database.getAllForTag(libraryName);

      for (String name : allEntities) {
        Element e = database.getEntityByName(name).clone();

        for (BaseFilter filter : filterList) {
          filter.filterEntity(e, r, a.toString());
        }

        root.addContent(e);
      }

      Document document = new Document(root);

      try {
        zipHelper.copyToPatch(document, out);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
