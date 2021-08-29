package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;

public class ZipHelper {
  private static final int BUFFER_SIZE = 1024;

  public static final String DATA_PAK = "data.pak";
  public static final String LEVEL_OUTPUT_PAK = "level.pak";
  public static final String PATCH_OUTPUT_PAK = "patch_randomizer.pak";
  private ZipFile input;

  public static final String DATA_LEVELS = "levels";
  
  public static final String ANIMATIONS_DUAL_WRENCH_PLAYER_1P = "animations/mannequin/adb/arkdualwrenchplayer1p.adb";
  public static final String ANIMATIONS_ARK_PLAYER_DATABASE_3P = "animations/mannequin/adb/arkplayerdatabase3p.adb";

  public static final String APEX_VOLUME_CONFIG = "ark/apexvolumeconfig.xml";

  public static final String AI_TREE_ARMED_HUMANS = "ark/ai/aitrees/armedhumanaitree.xml";
  public static final String AI_TREE_HUMANS = "ark/ai/aitrees/humanaitree.xml";
  public static final String AI_TREE_UNARMED_HUMANS = "ark/ai/aitrees/unarmedhumanaitree.xml";

  public static final String BOOKS_XML = "ark/campaign/books.xml";
  public static final String LOCATIONS_XML = "ark/campaign/locations.xml";

  public static final String VOICES_PATH = "ark/dialog/voices";
  public static final String DIALOGIC_PATH = "ark/dialog/dialoglogic";

  public static final String ARK_ITEMS_XML = "ark/items/arkitems.xml";
  public static final String LOOT_TABLE_FILE = "ark/items/loottables.xml";

  public static final String NPC_GAME_EFFECTS = "ark/npc/npcgameeffects.xml";
  public static final String NIGHTMARE_MANAGER = "ark/npc/nightmaremanager.xml";

  public static final String NEUROMOD_ABILITIES = "ark/player/abilities.xml";
  public static final String NEUROMOD_PDA_LAYOUT = "ark/player/abilitiespdalayout.xml";
  public static final String NEUROMOD_RESEARCH_TOPICS = "ark/player/researchtopics.xml";
  
  public static final String SIGNAL_SYSTEM_PACKAGES = "ark/signalsystem/packages.xml";
  
  public static final String OPTIONS_FILE = "libs/config/gameoptions.xml";

  public static final String ENTITY_ARCHETYPES_SOURCE_DIR = "libs/entityarchetypes/";
  public static final String ARK_PICKUPS_XML = "libs/entityarchetypes/arkpickups.xml";
  public static final String ARK_PROJECTILES_XML = "libs/entityarchetypes/arkprojectiles.xml";

  public static final String MUSIC_XML = "libs/gameaudio/music.xml";

  public static final String GLOBALACTIONS_SELFDESTRUCTTIMER = "libs/globalactions/global_selfdestructsequence.xml";
  
  public static final String PARTICLES_CHARACTERS = "libs/particles/characters.xml";
  
  public static final String LOGO = "libs/ui/textures/danielle_shared_textures/prey_title.dds";

  public static final String HUMANS_FINAL_DIR = "objects/characters/humansfinal";
  public static final String PLAYER_DIR = "objects/characters/player";

  public static final String NATURAL_DAY_2_START_FILE = "flowgraphs/FG_Day2Start.xml";
  public static final String ENABLE_NIGHTMARE_MANAGER = "flowgraphs/enablenightmaremanager.xml";

  private Path tempLevelDir;
  private Path tempPatchDir;

  private Multimap<String, String> dirToFileName;
  private Map<String, ZipOutputStream> levelZips;
  private ZipOutputStream patchZip;

  public ZipHelper(Path tempLevelDir, Path tempPatchDir) throws IOException {
    this.tempLevelDir = tempLevelDir;
    this.tempPatchDir = tempPatchDir;
    levelZips = Maps.newHashMap();
    input = new ZipFile(new File(DATA_PAK).getCanonicalPath());
    dirToFileName = ArrayListMultimap.create();
    Enumeration<? extends ZipEntry> zipEntries = input.entries();
    while (zipEntries.hasMoreElements()) {
      ZipEntry entry = (ZipEntry) zipEntries.nextElement();
      String name = entry.getName();
      String parentDir = getParentDirectory(name);
      dirToFileName.put(parentDir, name);
    }
  }

  public boolean closeOutputZips() {
    for (String level : levelZips.keySet()) {
      if (levelZips.get(level) != null) {
        try {
          levelZips.get(level).close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    try {
      if (patchZip != null) {
        patchZip.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean close() {
    try {
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return true;
  }
  
  private ZipOutputStream getZipOutputStreamForLevel(String level) throws FileNotFoundException {
    if (!levelZips.containsKey(level)) {
      Path levelPak = tempLevelDir.resolve(level).resolve(LEVEL_OUTPUT_PAK);
      levelPak.toFile().getParentFile().mkdirs();
      ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(levelPak.toString()));
      levelZips.put(level, zos);
    }
    return levelZips.get(level);
  }

  private ZipOutputStream getZipOutputStreamForPatch() throws FileNotFoundException {
    if (patchZip == null) {
      Path patchPak = tempPatchDir.resolve(PATCH_OUTPUT_PAK);
      patchZip = new ZipOutputStream(new FileOutputStream(patchPak.toString()));
    }
    return patchZip;
  }

  private String getParentDirectory(String entryName) {
    Path p = Paths.get(entryName).getParent();
    if (p == null) {
      return "";
    }
    return p.toString();
  }

  public boolean isDirectory(String entryName) {
    return input.getEntry(entryName).isDirectory();
  }

  public static String getFileName(String entryName) {
    return Paths.get(entryName).getFileName().toString();
  }

  public InputStream getInputStream(String path) throws IOException {
    ZipEntry entry = input.getEntry(path);
    if (entry == null) {
      throw new IOException("Could not find path " + path);
    }
    return input.getInputStream(entry);
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
    byte[] buffer = new byte[BUFFER_SIZE];
    is.read(buffer);

    File targetFile = new File(fileOut);
    Files.write(buffer, targetFile);
  }

  public void copyToLevelPak(String pathInInputZip, String pathInOutputZip, String level) throws IOException {
    ZipOutputStream zos = getZipOutputStreamForLevel(level);
    InputStream in = getInputStream(pathInInputZip);

    zos.putNextEntry(new ZipEntry(pathInOutputZip));
    copyStreams(in, zos);
    zos.closeEntry();
  }

  public void copyToLevelPak(Document d, String pathInOutputZip, String level) throws IOException {
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());

    ZipOutputStream zos = getZipOutputStreamForLevel(level);
    zos.putNextEntry(new ZipEntry(pathInOutputZip));
    xmlOutput.output(d, zos);
    zos.closeEntry();
  }
  
  public void copyToPatch(String path) throws IOException {
    copyToPatch(path, path);
  }

  public void copyToPatch(String pathInInputZip, String pathInOutputZip) throws IOException {
    ZipOutputStream zos = getZipOutputStreamForPatch();
    InputStream in = getInputStream(pathInInputZip);

    zos.putNextEntry(new ZipEntry(pathInOutputZip));
    copyStreams(in, zos);
    zos.closeEntry();
  }

  public void copyToPatch(Document d, String pathInOutputZip) throws IOException {
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());

    ZipOutputStream zos = getZipOutputStreamForPatch();
    zos.putNextEntry(new ZipEntry(pathInOutputZip));
    xmlOutput.output(d, zos);
    zos.closeEntry();
  }

  // Copies contents of the input stream to the output stream
  private void copyStreams(InputStream zin, ZipOutputStream out) throws IOException {
    byte[] buf = new byte[1024];
    int len;
    while ((len = zin.read(buf)) > 0) {
      out.write(buf, 0, len);
    }
  }
}
