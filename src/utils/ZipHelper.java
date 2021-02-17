package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;

public class ZipHelper {
  public static final String DATA_PAK = "data.pak";
  private ZipFile data;

  public static final String DATA_LEVELS = "levels";

  public static final String APEX_VOLUME_CONFIG = "ark/apexvolumeconfig.xml";

  public static final String AI_TREE_ARMED_HUMANS = "ark/ai/aitrees/armedhumanaitree.xml";
  public static final String AI_TREE_HUMANS = "ark/ai/aitrees/humanaitree.xml";
  public static final String AI_TREE_UNARMED_HUMANS = "ark/ai/aitrees/unarmedhumanaitree.xml";

  public static final String BOOKS_XML = "ark/campaign/books.xml";

  public static final String VOICES_PATH = "ark/dialog/voices";
  public static final String DIALOGIC_PATH = "ark/dialog/dialoglogic";

  public static final String ARK_ITEMS_XML = "ark/items/arkitems.xml";
  public static final String LOOT_TABLE_FILE = "ark/items/loottables.xml";

  public static final String NPC_GAME_EFFECTS = "ark/npc/npcgameeffects.xml";

  public static final String NEUROMOD_ABILITIES = "ark/player/abilities.xml";
  public static final String NEUROMOD_PDA_LAYOUT = "ark/player/abilitiespdalayout.xml";
  public static final String NEUROMOD_RESEARCH_TOPICS = "ark/player/researchtopics.xml";

  public static final String ENTITY_ARCHETYPES_SOURCE_DIR = "libs/entityarchetypes/";
  public static final String ARK_PICKUPS_XML = "libs/entityarchetypes/arkpickups.xml";
  public static final String ARK_PROJECTILES_XML = "libs/entityarchetypes/arkprojectiles.xml";

  public static final String MUSIC_XML = "libs/gameaudio/music.xml";

  public static final String GLOBALACTIONS_SELFDESTRUCTTIMER = "libs/globalactions/global_selfdestructsequence.xml";

  public static final String HUMANS_FINAL_DIR = "objects/characters/humansfinal";
  public static final String PLAYER_DIR = "objects/characters/player";

  public static final String NATURAL_DAY_2_START_FILE = "naturalday2start/FG_Day2Start.xml";
  public static final String ENABLE_NIGHTMARE_MANAGER = "flowgraphs/enablenightmaremanager.xml";

  Multimap<String, String> dirToFileName;

  public ZipHelper() throws IOException {
    data = new ZipFile(new File(DATA_PAK).getCanonicalPath());
    dirToFileName = ArrayListMultimap.create();
    Enumeration<? extends ZipEntry> zipEntries = data.entries();
    while (zipEntries.hasMoreElements()) {
      ZipEntry entry = (ZipEntry) zipEntries.nextElement();
      String name = entry.getName();
      String parentDir = getParentDirectory(name);
      dirToFileName.put(parentDir, name);
    }
  }

  private String getParentDirectory(String entryName) {
    Path p = Paths.get(entryName).getParent();
    if (p == null) {
      return "";
    }
    return p.toString();
  }

  public boolean isDirectory(String entryName) {
    return data.getEntry(entryName).isDirectory();
  }

  public static String getFileName(String entryName) {
    return Paths.get(entryName).getFileName().toString();
  }

  public boolean close() {
    try {
      data.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public InputStream getInputStream(String path) throws IOException {
    ZipEntry entry = data.getEntry(path);
    if (entry == null) {
      throw new IOException("Could not find path " + path);
    }
    return data.getInputStream(entry);
  }

  public Collection<String> listFiles(String path) {
    String normalizedName = Paths.get(path).toString();
    if (!dirToFileName.containsKey(normalizedName)) {
      return Lists.newArrayList();
    }
    return dirToFileName.get(normalizedName);
  }

  public Document getDocument(String path) throws IOException, JDOMException {
    InputStream is = getInputStream(path);
    SAXBuilder saxBuilder = new SAXBuilder();
    return saxBuilder.build(is);
  }

  public void copy(String fileIn, String fileOut) throws IOException {
    InputStream is = getInputStream(fileIn);
    byte[] buffer = new byte[is.available()];
    is.read(buffer);

    File targetFile = new File(fileOut);
    Files.write(buffer, targetFile);
  }
}
