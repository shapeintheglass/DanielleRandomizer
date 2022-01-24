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
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
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

  public static final String ANIMATIONS_IMG = "animations/animations.img";
  public static final String ANIMATIONS_ADB_DIR = "animations/mannequin/adb";
  public static final String ANIMATIONS_DUAL_WRENCH_PLAYER_1P = "animations/mannequin/adb/arkdualwrenchplayer1p.adb";
  public static final String ANIMATIONS_ARK_FLAMETHROWER_TURRET = "animations/mannequin/adb/arkflamethrowerturretentity.adb";
  public static final String ANIMATIONS_ARK_LASER_TURRET = "animations/mannequin/adb/arklaserturretentity.adb";
  public static final String ANIMATIONS_ARK_ITEM_TAGS = "animations/mannequin/adb/arkitemtags.xml";
  public static final String ANIMATIONS_ARK_PLAYER_DATABASE_1P = "animations/mannequin/adb/arkplayerdatabase1p.adb";
  public static final String ANIMATIONS_ARK_PLAYER_DATABASE_3P = "animations/mannequin/adb/arkplayerdatabase3p.adb";
  public static final String ANIMATIONS_ARK_SLOW_SHOTGUN_PLAYER_1P = "animations/mannequin/adb/arkslowshotgunplayer1p.adb";
  public static final String ANIMATIONS_ARK_SLOW_SHOTGUN_WEAPON = "animations/mannequin/adb/arkslowshotgunweapon.adb";
  public static final String ANIMATIONS_ARK_UNSILENCED_PISTOL_PLAYER_1P = "animations/mannequin/adb/arkunsilencedpistolplayer1p.adb";
  public static final String ANIMATIONS_ARK_UNSILENCED_PISTOL_WEAPON = "animations/mannequin/adb/arkunsilencedpistolweapon.adb";
  public static final String ANIMATIONS_ARK_DUAL_PISTOL_PLAYER_1P = "animations/mannequin/adb/arkdualpistolplayer1p.adb";
  public static final String ANIMATIONS_ARK_DUAL_PISTOL_WEAPON = "animations/mannequin/adb/arkdualpistolweapon.adb";

  public static final String ARK_FACTIONS = "ark/arkfactions.xml";
  public static final String APEX_VOLUME_CONFIG = "ark/apexvolumeconfig.xml";
  public static final String PROJECTILE_POOLS = "ark/arkprojectilepooldefinitions.xml";
  public static final String ARK_META_TAGS = "ark/arkmetatags.xml";
  public static final String ARK_TIP_LIBRARY = "ark/tiplibrary.xml";

  public static final String AI_TREE_ABSOLUTE_NIGHTMARE = "ark/ai/aitrees/absolutenightmareaitree.xml";
  public static final String AI_TREE_ARMED_HUMANS = "ark/ai/aitrees/armedhumanaitree.xml";
  public static final String AI_TREE_HUMANS = "ark/ai/aitrees/humanaitree.xml";
  public static final String AI_TREE_UNARMED_HUMANS = "ark/ai/aitrees/unarmedhumanaitree.xml";
  public static final String AI_SIGNAL_RECEIVER_THERMAL_MIMIC = "ark/ai/aisignalreceiverconfigs/thermalmimicsignalreceiverconfig.xml";

  public static final String BOOKS_XML = "ark/campaign/books.xml";
  public static final String LOCATIONS_XML = "ark/campaign/locations.xml";
  public static final String LORE_LIBRARY = "ark/campaign/lorelibrary.xml";

  public static final String VOICES_PATH = "ark/dialog/voices";
  public static final String PREPROCESSED_VOICES_PATH = "ark/dialog/voices/voices.json";
  public static final String DIALOGIC_PATH = "ark/dialog/dialoglogic";
  public static final String BINK_SUBTITLES_PATH = "ark/dialog/binksubtitles";

  public static final String ARK_ITEMS_XML = "ark/items/arkitems.xml";
  public static final String LOOT_TABLE_FILE = "ark/items/loottables.xml";

  public static final String NPC_ABILITIES = "ark/npc/npcabilities.xml";
  public static final String NPC_ABILITY_CONTEXTS = "ark/npc/npcabilitycontexts.xml";
  public static final String NPC_ABILITY_CONTEXT_PROFILES = "ark/npc/npcabilitycontextprofiles.xml";
  public static final String NPC_GAME_EFFECTS = "ark/npc/npcgameeffects.xml";
  public static final String NIGHTMARE_MANAGER = "ark/npc/nightmaremanager.xml";

  public static final String NEUROMOD_ABILITIES = "ark/player/abilities.xml";
  public static final String NEUROMOD_PDA_LAYOUT = "ark/player/abilitiespdalayout.xml";
  public static final String NEUROMOD_RESEARCH_TOPICS = "ark/player/researchtopics.xml";

  public static final String SIGNAL_SYSTEM_MODIFIERS = "ark/signalsystem/modifiers.xml";
  public static final String SIGNAL_SYSTEM_PACKAGES = "ark/signalsystem/packages.xml";

  public static final String CHARACTER_ATTACHMENT_EFFECTS = "ark/visualeffects/characterattachmenteffects.xml";
  
  public static final String OPTIONS_FILE = "libs/config/gameoptions.xml";

  public static final String ENTITY_ARCHETYPES_SOURCE_DIR = "libs/entityarchetypes/";
  public static final String ARK_NPCS_XML = "libs/entityarchetypes/arknpcs.xml";
  public static final String ARK_PICKUPS_XML = "libs/entityarchetypes/arkpickups.xml";
  public static final String ARK_PROJECTILES_XML = "libs/entityarchetypes/arkprojectiles.xml";
  public static final String ARK_ROBOTS_XML = "libs/entityarchetypes/arkrobots.xml";

  public static final String MUSIC_XML = "libs/gameaudio/music.xml";

  public static final String GLOBALACTIONS_SELFDESTRUCTTIMER = "libs/globalactions/global_selfdestructsequence.xml";

  public static final String PARTICLES_CHARACTERS = "libs/particles/characters.xml";
  public static final String PARTICLES_PLAYER_WEAPONS = "libs/particles/playerweapons.xml";

  public static final String LOGO = "libs/ui/textures/danielle_shared_textures/prey_title.dds";
  public static final String ICONS_INVENTORY_DIR = "libs/ui/textures/icons_inventory/";

  public static final String PHANTOM_BLOOD_PROJECTILE_ROUND = "objects/arkeffects/characters/aliens/phantom/bloodprojectileround.mtl";
  public static final String PHANTOM_SOLAR_PROJECTILE_ROUND = "objects/arkeffects/characters/aliens/phantom/solarprojectileround.mtl";

  public static final String PHANTOM_SKINS_DIR = "objects/characters/aliens/phantom";
  public static final String WEAVER_SKINS_DIR = "objects/characters/aliens/weaver";
  public static final String HUMANS_FINAL_DIR = "objects/characters/humansfinal";
  public static final String PLAYER_DIR = "objects/characters/player";
  public static final String PLAYER_TYPHON_SKIN_INNER = "objects/characters/player/etherskin_inner.mtl";
  public static final String PLAYER_TYPHON_SKIN_OUTER = "objects/characters/player/etherskin_outer.mtl";
  public static final String PLAYER_TYPHON_SKIN_SCROLL = "objects/characters/player/etherskin_particlescroll.mtl";
  public static final String PLAYER_CDF = "objects/characters/player/player.cdf";
  public static final String PLAYER_FEMALE_CDF = "objects/characters/player/playerfemale.cdf";
  public static final String PLAYER_MALE_PAJAMA_CDF = "objects/characters/player/playermale_pajamas.cdf";
  public static final String PLAYER_FEMALE_PAJAMA_CDF = "objects/characters/player/playerfemale_pajamas.cdf";
  public static final String PLAYER_ALIEN_CDF = "objects/characters/player/playermalealien.cdf";
  public static final String ZOMBIE_CDF = "objects/characters/humansfinal/zombie.cdf";

  public static final String DUAL_PISTOL_CDF = "objects/weapons/pistol/1p/dualpistol1p.cdf";

  public static final String NATURAL_DAY_2_START_FILE = "flowgraphs/FG_Day2Start.xml";
  public static final String ENABLE_NIGHTMARE_MANAGER = "flowgraphs/enablenightmaremanager.xml";

  // Psy cutter-specific files (other than arkpickups, packages, playerdatabase,
  // items, playerweapons)
  public static final String PSY_CUTTER_ANIMATIONS_FF_EVENTS = "animations/mannequin/adb/arkswordffevents.adb";
  public static final String PSY_CUTTER_ANIMATIONS_PLAYER_1P = "animations/mannequin/adb/arkswordplayer1p.adb";
  public static final String PSY_CUTTER_ANIMATIONS_SOUNDS = "animations/mannequin/adb/arkswordsounds.adb";
  public static final String PSY_CUTTER_ANIMATIONS_WEAPON = "animations/mannequin/adb/arkswordweapon.adb";
  public static final String PSY_CUTTER_ANIMATIONS_WRENCH_WEAPON = "animations/mannequin/adb/arkwrenchweapon.adb";
  public static final String PSY_CUTTER_INVENTORY_ICON_HUD = "libs/ui/textures/icons_inventory/ui_icon_inv_sword_HUD.dds";
  public static final String PSY_CUTTER_INVENTORY_ICON_PICKUP = "libs/ui/textures/icons_inventory/ui_icon_psycutter_1x1.dds";
  public static final String PSY_CUTTER_INVENTORY_ICON_INVENTORY = "libs/ui/textures/icons_inventory/ui_icon_psycutter_2x1.dds";

  // Psy cutter-specific directories
  public static final String PSY_CUTTER_PLAYER_ANIMATIONS_DIR = "animations/characters/player/male";
  public static final String PSY_CUTTER_ANIMATIONS_DIR = "animations/weapons/sword";
  public static final String PSY_CUTTER_OBJECTS_ARKEFFECTS_DIR = "objects/arkeffects/weapons/psycutter";
  public static final String PSY_CUTTER_OBJECTS_PICKUPS_DIR = "objects/pickups/ammo/psycutter";
  public static final String PSY_CUTTER_OBJECTS_DIR = "objects/weapons/psycutter";
  public static final String PSY_CUTTER_MATERIALS_ARKEFFECTS_DIR = "materials/arkeffects/weapon/psycutter";
  public static final String PSY_CUTTER_TEXTURES_ARKEFFECTS_DIR = "textures/arkeffects/weapon/psycutter";
  public static final String PSY_CUTTER_STUNGUN_TEXTURES_DIR = "textures/arkeffects/weapon/stungun";

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

    zos.putNextEntry(new ZipEntry(pathInOutputZip.toLowerCase()));
    copyStreams(in, zos);
    zos.closeEntry();
  }

  public void copyToLevelPak(Document d, String pathInOutputZip, String level) throws IOException {
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());

    ZipOutputStream zos = getZipOutputStreamForLevel(level);
    zos.putNextEntry(new ZipEntry(pathInOutputZip.toLowerCase()));
    xmlOutput.output(d, zos);
    zos.closeEntry();
  }

  // Copies over all files within the given directories (works by matching the
  // prefix)
  public void copyDirsToPatch(List<String> dirsToCopy) throws IOException {
    Enumeration<? extends ZipEntry> entries = input.entries();
    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      for (String s : dirsToCopy) {
        if (entry.getName().startsWith(s)) {
          copyToPatch(entry.getName());
        }
      }
    }
  }

  public void copyToPatch(String path) throws IOException {
    copyToPatch(path, path);
  }

  public void copyToPatch(String pathInInputZip, String pathInOutputZip) throws IOException {
    ZipOutputStream zos = getZipOutputStreamForPatch();
    InputStream in = getInputStream(pathInInputZip);

    try {
      zos.putNextEntry(new ZipEntry(pathInOutputZip.toLowerCase()));
      copyStreams(in, zos);
      zos.closeEntry();
    } catch (ZipException e) {
      Logger.getGlobal().info("Unable to copy file " + pathInInputZip + " file may already exist in zip");
    }
  }

  public void copyToPatch(Document d, String pathInOutputZip) throws IOException {
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());

    ZipOutputStream zos = getZipOutputStreamForPatch();
    zos.putNextEntry(new ZipEntry(pathInOutputZip.toLowerCase()));
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
