package utils.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public class MoreGunGenerator {

  private static final String SOURCE = "_data/libs/entityarchetypes/arkpickups_backup.xml";
  private static final String DEST = "_data/libs/entityarchetypes/arkpickups_new.xml";

  private static final String SOURCE_PROJECTILES = "_data/libs/entityarchetypes/arkprojectiles.xml";
  private static final String DEST_PROJECTILES = "_data/libs/entityarchetypes/arkprojectiles_new.xml";

  private static final String FILE_LIST_DEST = "_data/libs/ui/textures/icons_inventory/filelist.txt";

  private static final ImmutableMap<String, String> WEAPON_NAME_TO_READABLE_NAME = new ImmutableMap.Builder<String, String>()
      .put("Weapons.Shotgun", "Shotgun")
      .put("Weapons.GooGun", "Gloo Gun")
      .put("Weapons.Pistol", "Pistol")
      .put("Weapons.ToyGun", "Huntress Boltcaster")
      .build();

  private static final ImmutableMap<String, String> WEAPON_NAME_TO_UI_NAME = new ImmutableMap.Builder<String, String>()
      .put("Weapons.Shotgun", "shotgun")
      .put("Weapons.GooGun", "googun")
      .put("Weapons.Pistol", "pistol")
      .put("Weapons.ToyGun", "toygun")
      .build();

  private static final ImmutableMap<String, String> PROJECTILE_NAME_TO_READABLE_NAME = new ImmutableMap.Builder<String, String>()
      .put("ArkProjectiles.Bullets.MoreGuns.Phantom", "Phantasmic")
      .put("ArkProjectiles.Bullets.MoreGuns.Telepath", "Telepathic")
      .put("ArkProjectiles.Bullets.MoreGuns.Nightmare", "Nightmarish")
      .put("ArkProjectiles.Bullets.MoreGuns.Voltaic", "Voltaic")
      //.put("ArkProjectiles.Charges.Lure", "Lure")
      //.put("ArkProjectiles.Charges.Nullwave", "Nullwave")
      //.put("ArkProjectiles.Charges.Recycler", "Recycler")
      //.put("ArkProjectiles.Charges.EMP", "EMP")
      .put("ArkProjectiles.Gloo.GlooShot", "GLOO")
      .put("ArkProjectiles.Bullets.PistolRound_Default", "9mm")
      .put("ArkProjectiles.Bullets.ToygunDart_Default", "Toy")
      .build();

  private static final ImmutableMap<String, String> PROJECTILE_NAME_TO_UI_NAME = new ImmutableMap.Builder<String, String>()
      .put("ArkProjectiles.Bullets.MoreGuns.Phantom", "phantom")
      .put("ArkProjectiles.Bullets.MoreGuns.Telepath", "telepath")
      .put("ArkProjectiles.Bullets.MoreGuns.Nightmare", "nightmare")
      .put("ArkProjectiles.Bullets.MoreGuns.Voltaic", "voltaic")
      //.put("ArkProjectiles.Charges.Lure", "lure")
      //.put("ArkProjectiles.Charges.Nullwave", "nullwave")
      //.put("ArkProjectiles.Charges.Recycler", "recycler")
      //.put("ArkProjectiles.Charges.EMP", "emp")
      .put("ArkProjectiles.Gloo.GlooShot", "gloo")
      .put("ArkProjectiles.Bullets.PistolRound_Default", "9mm")
      .put("ArkProjectiles.Bullets.ToygunDart_Default", "foam")
      .build();

  private static final ImmutableMap<String, String> REDUNDANT_PROJECTILE_TYPES = new ImmutableMap.Builder<String, String>()
      .put("Weapons.Shotgun", "ArkProjectiles.Bullets.PistolRound_Default")
      .put("Weapons.GooGun", "ArkProjectiles.Gloo.GlooShot")
      .put("Weapons.Pistol", "ArkProjectiles.Bullets.PistolRound_Default")
      .put("Weapons.ToyGun", "ArkProjectiles.Bullets.ToygunDart_Default")
      .build();

  private static final ImmutableSet<String> EXOTIC_PROJECTILE_TYPES = ImmutableSet.of("phantom", "nightmare", "voltaic",
      "telepath");

  private static final String MATERIALS_DIR = "objects\\weapons\\";

  private static final String FIRST_PARTY_MTL = "%s\\1p\\%s1p_%s01";
  private static final String THIRD_PARTY_MTL = "%s\\3p\\%s3p_%s01";

  private static Map<String, Element> getSupportedWeapons(Element root) {
    Map<String, Element> toReturn = Maps.newHashMap();
    List<Element> entities = root.getChildren("EntityPrototype");

    for (Element e : entities) {
      String name = e.getAttributeValue("Name");
      if (WEAPON_NAME_TO_READABLE_NAME.containsKey(name)) {
        toReturn.put(name, e.clone());
      }
    }
    return toReturn;
  }
  
  private static void addMoreGuns(Element originalRoot, Random r) {
    Map<String, Element> weapons = getSupportedWeapons(originalRoot);
    try (FileWriter fw = new FileWriter(new File(FILE_LIST_DEST));) {
      for (String weaponName : WEAPON_NAME_TO_READABLE_NAME.keySet()) {
        String readableWeaponName = WEAPON_NAME_TO_READABLE_NAME.get(weaponName);
        for (String projectileName : PROJECTILE_NAME_TO_READABLE_NAME.keySet()) {

          if (REDUNDANT_PROJECTILE_TYPES.containsKey(weaponName) && REDUNDANT_PROJECTILE_TYPES.get(weaponName)
              .equals(projectileName)) {
            continue;
          }

          Element clone = weapons.get(weaponName).clone();
          String readableProjectileName = PROJECTILE_NAME_TO_READABLE_NAME.get(projectileName);
          String newName = weaponName + readableProjectileName + ".Randomizer";
          String newReadableName = readableProjectileName + " " + readableWeaponName;
          String newDescription = String.format("A %s with %s projectiles.", readableWeaponName,
              readableProjectileName);
          
          long newIdLong = r.nextLong();
          if (newIdLong < 0L) {
            newIdLong = -1 * newIdLong;
          }
          String newUUID = String.format("{%s}", UUID.randomUUID()).toUpperCase();

          String uiName = WEAPON_NAME_TO_UI_NAME.get(weaponName);
          String projectileUiName = PROJECTILE_NAME_TO_UI_NAME.get(projectileName);
          String iconName = String.format("ui_icon_inv_%s_%s", projectileUiName, uiName);
          String hudName = iconName + "_HUD";
          boolean isExotic = EXOTIC_PROJECTILE_TYPES.contains(projectileUiName);
          if (isExotic) {
            newDescription += " Use exotic ammunition to fire this weapon.";
          }

          String mtl1p = String.format(MATERIALS_DIR + FIRST_PARTY_MTL, uiName, uiName, projectileUiName).toLowerCase();
          String mtl3p = String.format(MATERIALS_DIR + THIRD_PARTY_MTL, uiName, uiName, projectileUiName).toLowerCase();

          fw.write(iconName + ".dds\n");
          fw.write(hudName + ".dds\n");

          clone.setAttribute("Name", newName);
          clone.setAttribute("ArchetypeId", Long.toString(newIdLong));
          clone.setAttribute("Id", newUUID);
          Element properties = clone.getChild("Properties");
          properties.setAttribute("textDisplayName", newReadableName)
              .setAttribute("sStylizedIcon", hudName)
              .setAttribute("sHUDIcon", hudName)
              .setAttribute("textDescription", newDescription)
              .setAttribute("bAvailableForRandom", "1");

          if (isExotic) {
            properties.setAttribute("material_MaterialFP", mtl1p).setAttribute("material_MaterialTP", mtl3p);
            System.out.printf("\"%s.mtl\", \"%s.mtl\", ", mtl1p.replace("\\", "/"), mtl3p.replace("\\", "/"));
          }

          properties.getChild("Weapon").setAttribute("archetype_ammo", projectileName);
          properties.getChild("Inventory").setAttribute("sIcon", iconName);
          originalRoot.addContent(clone);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    File file = new File(SOURCE);
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document;
    try {
      document = saxBuilder.build(file);
    } catch (JDOMException | IOException e1) {
      e1.printStackTrace();
      return;
    }
    Element originalRoot = document.getRootElement();

    Random r = new Random();
    addMoreGuns(originalRoot, r);

    File out = new File(DEST);
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    try {
      xmlOutput.output(document, new FileOutputStream(out));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
