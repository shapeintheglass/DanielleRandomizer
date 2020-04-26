package utils;

public class BodyConsts {
  public static final String PREFIX = "objects/characters/humans/";
  public static final String MALE_BODY_TYPE = "genmale/genmale.chr";
  public static final String FEMALE_BODY_TYPE = "genfemale/genfemale.chr";
  public static final String LARGE_MALE_BODY_TYPE = "largemale/largemale.chr";

  public static final String[] FEMALE_BODIES = { "corporate/corporate_genfemalebody01",
      "mechanic/mechanic_genfemalebody01", "plumber/plumber_genfemalebody01", "scientist/scientist_genfemalebody01",
      "security/security_genfemalebody01", "morgankarl/morgankarlgenderselect_genfemale" };
  public static final String[] MALE_BODIES = { "corporate/corporate_genmalebody01", "dahl/dahl_genmalebody01",
      "labcoat/labcoat_genmalebody01", "mechanic/mechanic_genmalebody01", "scientist/scientist_genmalebody01",
      "security/security_genmalebody01", "volunteer/volunteer_genmalebody01",
      "morgankarl/morgankarlgenderselect_genmale" };

  public static final String[] ACCESSORY_COMPATIBLE_BODIES = { "corporate/corporate_genfemalebody01",
      "mechanic/mechanic_genfemalebody01", "scientist/scientist_genfemalebody01", "security/security_genfemalebody01",
      "morgankarl/morgankarlgenderselect_genfemale", "corporate/corporate_genmalebody01", "dahl/dahl_genmalebody01",
      "mechanic/mechanic_genmalebody01", "scientist/scientist_genmalebody01", "security/security_genmalebody01",
      "morgankarl/morgankarlgenderselect_genmale" };

  public static final String[] FEMALE_HEADS = { "genfemale/genfemale_head01", "genfemale/genfemale_head02",
      "genfemale/genfemale_head02_psychoscope", "genfemale/genfemale_head03", "genfemale/genfemale_head04",
      "genfemale/genfemale_head05", "genfemale/genfemale_head06", "mikhailailyushin/mikhailailyushin_genfemalehead01",
      "morgankarl/morgankarl_genfemalehead01", "saraelazar/saraelazar_genfemalehead01",
      "scientist/scientist_genfemalehead01" };
  public static final String FEMALE_HEAD_HELMET = "objects/accessories/breather/breather3p/breather3p_genfemale01";
  public static final String FEMALE_HEAD_HELMET_MTL = "objects/accessories/breather/breather3p/breather1";

  public static final String[] MALE_HEADS = { "aaroningram/aaroningram_genmale_head01", "dahl/dahl_genmalehead01",
      "drcalvino/drcalvino_genmalehead01", "drigwe/igwe_genmalehead01", "genmale/genmale_head01",
      "genmale/genmale_head01_psychoscope", "genmale/genmale_head01_psycho", "genmale/genmale_head02",
      "genmale/genmale_head03", "genmale/genmale_head03_psychoscope", "genmale/genmale_head04",
      "genmale/genmale_head05", "genmale/genmale_head06", "morgankarl/morgankarl_genmalehead01",
      "volunteer/volunteer_genmalehead01", "volunteer/volunteer_genmalehead02", "volunteer/volunteer_genmalehead03",
      "scientist/scientist_genmalehead01" };

  public static final String[] HEADS_NOT_TO_MODIFY = { "husk/husk_genfemalehead01", "husk/husk_genmalehead01", };
  // Used to make sure that hands aren't mismatched with heads
  public static final String[] HEADS_THAT_SHOULD_NOT_HAVE_WHITE_HANDS = { "drigwe/igwe_genmalehead01",
      "genmale/genmale_head04", "husk/husk_genmalehead01", "saraelazar/saraelazar_genfemalehead01",
      "genfemale/genfemale_head04" };
  public static final String[] BODIES_WITH_WHITE_HANDS = { "volunteer/volunteer_genmalebody01",
      "morgankarl/morgankarlgenderselect_genmale", "morgankarl/morgankarlgenderselect_genfemale" };

  // Used to make sure that "complete" heads do not have hair
  public static final String[] HEADS_THAT_SHOULD_NOT_HAVE_HAIR = { "genfemale/genfemale_head02_psychoscope",
      "genmale/genmale_head01_psychoscope", "genmale/genmale_head01_psycho", "husk/husk_genmalehead01",
      "husk/husk_genfemalehead01", "genmale/genmale_head01_husk", "genmale/genmale_head02_husk",
      "genmale/genmale_head03_psychoscope", "scientist/scientist_genmalehead01",
      "scientist/scientist_genfemalehead01" };

  public static final String[] FEMALE_HAIRS_01 = { "genfemale/genfemale_head01_hair01",
      "genfemale/genfemale_head01_hair03", "genfemale/genfemale_head01_hair05" };
  public static final String[] FEMALE_HAIRS_02 = { "genfemale/genfemale_head02_hair02",
      "genfemale/genfemale_head02_hair03", "genfemale/genfemale_head02_hair04", "genfemale/genfemale_head02_hair05" };
  public static final String[] FEMALE_HAIRS_03 = { "genfemale/genfemale_head03_hair02",
      "genfemale/genfemale_head03_hair03", "genfemale/genfemale_head03_hair04", "genfemale/genfemale_head03_hair05" };
  public static final String[] FEMALE_HAIRS_04 = { "genfemale/genfemale_head04_hair03",
      "genfemale/genfemale_head04_hair04", "genfemale/genfemale_head04_hair05" };
  public static final String[] FEMALE_HAIRS_05 = { "genfemale/genfemale_head05_hair03",
      "genfemale/genfemale_head05_hair05" };
  public static final String[] FEMALE_HAIRS_06 = { "genfemale/genfemale_head06_hair02",
      "genfemale/genfemale_head06_hair03", "genfemale/genfemale_head06_hair05" };
  // Sarah's hair is compatible with Morgan's and Mikhaila's. Morgan and Mikhaila
  // can use hair type 2.
  public static final String[] FEMALE_HAIR_MIKA = { "mikhailailyushin/mikhailailyushin_genfemalehead01_hair01" };
  public static final String[] FEMALE_HAIR_MORGAN = { "morgankarl/morgankarl_genfemalehead01_hair01" };
  public static final String[] FEMALE_HAIR_SARA = { "saraelazar/saraelazar_genfemalehead01_hair01" };

  public static final String[] FEMALE_HAIR_COLORS_01 = { "genfemale/genfemale_hair01_black" };
  public static final String[] FEMALE_HAIR_COLORS_02 = { "genfemale/genfemale_hair02_black",
      "genfemale/genfemale_hair02_blonde", "genfemale/genfemale_hair02_brown", "genfemale/genfemale_hair02_gray",
      "genfemale/genfemale_hair02_red" };
  public static final String[] FEMALE_HAIR_COLORS_03 = { "genfemale/genfemale_hair03",
      "genfemale/genfemale_hair03_black", "genfemale/genfemale_hair03_blonde", "genfemale/genfemale_hair03_brown",
      "genfemale/genfemale_hair03_red" };
  public static final String[] FEMALE_HAIR_COLORS_04 = { "genfemale/genfemale_hair04_black",
      "genfemale/genfemale_hair04_blonde", "genfemale/genfemale_hair04_brown", "genfemale/genfemale_hair04_red" };
  public static final String[] FEMALE_HAIR_COLORS_05 = { "genfemale/genfemale_hair05_black",
      "genfemale/genfemale_hair05_blonde", "genfemale/genfemale_hair05_brown", "genfemale/genfemale_hair05_red" };
  public static final String[] FEMALE_HAIR_COLORS_06 = { "genfemale/genfemale_hair06" };

  public static final String[] MALE_HAIRS_01 = { "genmale/genmale_head01_hair01", "genmale/genmale_head01_hair03",
      "genmale/genmale_head01_hair05" };
  public static final String[] MALE_HAIRS_02 = { "genmale/genmale_head02_hair01", "genmale/genmale_head02_hair03",
      "genmale/genmale_head02_hair05" };
  public static final String[] MALE_HAIRS_03 = { "genmale/genmale_head03_hair03", "genmale/genmale_head03_hair05",
      "genmale/genmale_head03_hair06" };
  public static final String[] MALE_HAIRS_04 = { "genmale/genmale_head04_hair04" };
  public static final String[] MALE_HAIRS_05 = { "genmale/genmale_head05_hair01", "genmale/genmale_head05_hair05",
      "genmale/genmale_head05_hair06" };
  public static final String[] MALE_HAIRS_06 = { "genmale/genmale_head06_hair01", "genmale/genmale_head06_hair05",
      "genmale/genmale_head06_hair06" };
  public static final String[] MALE_HAIRS_V01 = { "volunteer/volunteer_genmalehead01_hair01" };
  public static final String[] MALE_HAIRS_V02 = { "volunteer/volunteer_genmalehead02_hair01" };
  public static final String[] MALE_HAIRS_V03 = { "volunteer/volunteer_genmalehead03_hair01" };
  public static final String[] MALE_HAIR_CALVINO = { "drcalvino/drcalvino_genmalehead01_hair01" };

  public static final String[] MALE_HAIR_COLORS_01 = { "genmale/genmale_hair01", "genmale/genmale_hair01_black",
      "genmale/genmale_hair01_blonde", "genmale/genmale_hair01_brown", "genmale/genmale_hair01_red" };
  public static final String[] MALE_HAIR_COLORS_03 = { "genmale/genmale_hair03", "genmale/genmale_hair03_black",
      "genmale/genmale_hair03_blonde", "genmale/genmale_hair03_brown", "genmale/genmale_hair03_red" };
  public static final String[] MALE_HAIR_COLORS_04 = { "genmale/genmale_hair04" };
  public static final String[] MALE_HAIR_COLORS_05 = { "genmale/genmale_hair05", "genmale/genmale_hair05_black",
      "genmale/genmale_hair05_blonde", "genmale/genmale_hair05_brown", "genmale/genmale_hair05_red" };
  public static final String[] MALE_HAIR_COLORS_06 = { "genmale/genmale_hair06", "genmale/genmale_hair06_black",
      "genmale/genmale_hair06_blonde" };
  public static final String[] MALE_HAIR_MORGAN = { "morgankarl/morgankarl_genmalehead01_hair01" };
  public static final String[] MALE_HAIR_DAHL = { "dahl/dahl_genmalehead01_hair01" };
  public static final String[] MALE_HAIR_IGWE = { "drigwe/igwe_genmalehead01_hair01" };
  public static final String[] MALE_HAIR_CALVINO_COLOR = { "drcalvino/dr_calvino_genmalehead01_hair" };
  public static final String[] MALE_HAIR_VOLUNTEER = { "volunteer/volunteer_hair01_brown" };

  // TODO: Bellamy's labcoat seems to be broken, female limbs as well
  public static final String MALE_LAB_COAT_LIMBS = "  <Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"legs\" Binding=\"objects/characters/humans/labcoat/labcoat_genmalelegs01.skin\" Material=\"objects/characters/humans/scientist/scientist_genmalebody01.mtl\" Flags=\"0\"/>\n"
      + "  <Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"hands\" Binding=\"objects/characters/humans/labcoat/labcoat_genmalehands01.skin\" Material=\"objects/characters/humans/scientist/scientist_genmalebody01.mtl\" Flags=\"0\"/>";
  public static final String FEMALE_LAB_COAT_LIMBS = "<Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"legs_arms\" Binding=\"objects/characters/humans/labcoat/labcoat_genfemalelegsarms01.skin\" Material=\"objects/characters/humans/scientist/scientist_genfemalebody01.mtl\" Flags=\"0\"/>";

  // (body_skin | head_skin | hair_skin), binding.skin, material.mtl, (0 | 1)
  public static final String SKIN_PATTERN = "  <Attachment Inheritable=\"1\" Type=\"CA_SKIN\" AName=\"%s\" Binding=\"%s.skin\" Material=\"%s.mtl\" SkinJointsOverrideSkeleton=\"%d\" Flags=\"0\"/>";

  public static final String PLUMBER_HAT = "objects/characters/humans/plumber/plumber_genfemalehat01";
  public static final String PLUMBER_HAT_MTL = "objects/characters/humans/plumber/plumber_genfemalebody01";

  public static final String[] ACCESSORIES_FEMALE = {
      "objects/accessories/propulsionpack/propulsionpack3p_genfemale01" };
  public static final String[] ACCESSORIES_FEMALE_MTL = { "objects/accessories/propulsionpack/propulsionpack_3p_01" };
  public static final String[] ACCESSORIES_MALE = { "objects/characters/humans/dahl/dahl_genmalebackpack01.skin",
      "objects/accessories/propulsionpack/propulsionpack3p_genmale01" };
  public static final String[] ACCESSORIES_MALE_MTL = { "objects/characters/humans/dahl/dahl_genmalebody01.mtl",
      "objects/accessories/propulsionpack/propulsionpack_3p_01" };
}
