package randomizers.gameplay.filters.rules;

import java.util.Random;
import org.jdom2.Element;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import utils.FileConsts.Archetype;
import utils.Utils;

public class WeaponProjectileRule implements Rule {

  private static final ImmutableSet<String> SUPPORTED_WEAPONS =
      ImmutableSet.of("Weapons.Shotgun", "Weapons.GooGun", "Weapons.Pistol", "Weapons.ToyGun");

  private static final String PROJECTILE_LIBRARY_NAME = "ArkProjectiles";

  private static final ImmutableList<String> EXOTIC_PROJECTILES =
      ImmutableList.of("AlienPowers.PhantomProjectile_Default", "AlienPowers.TelepathProjectile",
          "AlienPowers.NightmareProjectile", "AlienPowers.PhantomProjectile_EMP",
          "AlienPowers.TechnopathElectricalHazard_1g", "AlienPowers.TechnopathElectricalHazard_0g");

  private static final ImmutableList<String> REGULAR_PROJECTILES =
      ImmutableList.of("Gloo.GlooShot", "Charges.Lure", "Charges.Nullwave",
          "Bullets.PistolRound_Default", "Charges.Recycler", "Bullets.ShotgunPellet_Default",
          "Bullets.ToygunDart_Default", "Bullets.TurretRound_Default", "Bullets.Tracer_Default");

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    /*if (filename.equals(Archetype.PICKUPS.toString())) {
      String name = e.getAttributeValue("Name");
      return SUPPORTED_WEAPONS.contains(name);
    }*/
    return false;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    String newProjectile = Utils.getRandom(EXOTIC_PROJECTILES, r);
    e.getChild("Properties")
        .getChild("Weapon")
        .setAttribute("archetype_ammo", PROJECTILE_LIBRARY_NAME + "." + newProjectile);
  }
}
