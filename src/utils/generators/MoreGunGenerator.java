package utils.generators;

import java.io.File;
import java.io.FileOutputStream;
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
import com.google.common.collect.Maps;

public class MoreGunGenerator {

  private static final String SOURCE = "data/entityarchetypes/arkpickups_backup.xml";
  private static final String DEST = "data/entityarchetypes/arkpickups.xml";

  private static final ImmutableMap<String, String> WEAPON_NAME_TO_READABLE_NAME =
      new ImmutableMap.Builder<String, String>().put("Weapons.Shotgun", "Shotgun")
          .put("Weapons.GooGun", "Gloo Gun")
          .put("Weapons.Pistol", "Pistol")
          .put("Weapons.ToyGun", "Huntress Boltcaster")
          .build();


  private static final ImmutableMap<String, String> PROJECTILE_NAME_TO_READABLE_NAME =
      new ImmutableMap.Builder<String, String>()
          .put("ArkProjectiles.AlienPowers.PhantomProjectile_Default", "Phantasmic")
          .put("ArkProjectiles.AlienPowers.TelepathProjectile", "Telepathic")
          .put("ArkProjectiles.AlienPowers.NightmareProjectile", "Nightmarish")
          .put("ArkProjectiles.AlienPowers.PhantomProjectile_EMP", "Voltaic")
          .put("ArkProjectiles.Charges.Lure", "Lure")
          .put("ArkProjectiles.Charges.Nullwave", "Nullwave")
          .put("ArkProjectiles.Charges.Recycler", "Recycler")
          .put("ArkProjectiles.Charges.EMP", "EMP")
          .put("ArkProjectiles.Gloo.GlooShot", "GLOO")
          .put("ArkProjectiles.Bullets.PistolRound_Default", "9mm")
          .put("ArkProjectiles.Bullets.ToygunDart_Default", "Foam")
          .build();

  private static final ImmutableMap<String, String> PROJECTILE_NAME_TO_MATERIAL =
      new ImmutableMap.Builder<String, String>()
          .put("ArkProjectiles.AlienPowers.PhantomProjectile_Default",
              "Objects\\characters\\Aliens\\AlienCorpses\\AlienCorpsePiece")
          .put("ArkProjectiles.AlienPowers.TelepathProjectile",
              "Objects\\characters\\Aliens\\Telepath\\Telepath01")
          .put("ArkProjectiles.AlienPowers.NightmareProjectile",
              "Objects\\characters\\Aliens\\Phantom\\PhantomElite01")
          .put("ArkProjectiles.AlienPowers.PhantomProjectile_EMP",
              "Objects\\characters\\Player\\EtherDuplicate_00")
          .build();

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

    Map<String, Element> weapons = getSupportedWeapons(originalRoot);
    for (String weaponName : WEAPON_NAME_TO_READABLE_NAME.keySet()) {
      String readableWeaponName = WEAPON_NAME_TO_READABLE_NAME.get(weaponName);
      for (String projectileName : PROJECTILE_NAME_TO_READABLE_NAME.keySet()) {
        Element clone = weapons.get(weaponName)
            .clone();
        String readableProjectileName = PROJECTILE_NAME_TO_READABLE_NAME.get(projectileName);
        String newName = weaponName + readableProjectileName;
        String newReadableName = readableProjectileName + " " + readableWeaponName;
        String newDescription =
            String.format("A %s with %s projectiles.", readableWeaponName, readableProjectileName);
        long newIdLong = r.nextLong();
        if (newIdLong < 0L) {
          newIdLong = -1 * newIdLong;
        }
        String newUUID = String.format("{%s}", UUID.randomUUID())
            .toUpperCase();
        clone.setAttribute("Name", newName);
        clone.setAttribute("ArchetypeId", Long.toString(newIdLong));
        clone.setAttribute("Id", newUUID);
        clone.getChild("Properties")
            .setAttribute("textDisplayName", newReadableName);
        clone.getChild("Properties")
            .setAttribute("textDescription", newDescription);
        clone.getChild("Properties")
            .getChild("Weapon")
            .setAttribute("archetype_ammo", projectileName);
        if (PROJECTILE_NAME_TO_MATERIAL.containsKey(projectileName)) {
          String material = PROJECTILE_NAME_TO_MATERIAL.get(projectileName);
          clone.getChild("Properties")
              .setAttribute("material_MaterialFP", material);
          clone.getChild("Properties")
              .setAttribute("material_MaterialTP", material);
        }
        originalRoot.addContent(clone);
      }
    }

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
