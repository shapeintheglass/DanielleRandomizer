package utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

public class BodyConsts {
  public static final String PREFIX = "objects/characters/humans/";
  public static final String MALE_BODY_TYPE = "objects/characters/humans/genmale/genmale.chr";
  public static final String FEMALE_BODY_TYPE = "objects/characters/humans/genfemale/genfemale.chr";
  public static final String LARGE_MALE_BODY_TYPE = "objects/characters/humans/largemale/largemale.chr";

  public static final String LARGE_MALE_ATTACHMENTS = "objects/characters/humans/largemale/largemale_attachments.cdf";
  public static final String MALE_ATTACHMENTS = "objects/characters/humans/genmale/genmale_attachments.cdf";
  public static final String FEMALE_ATTACHMENTS = "objects/characters/humans/genfemale/genfemale_attachments.cdf";

  public static final String[] LARGE_MALE_MTLS = { "corporate/corporate_largemalebody_variant",
      "corporate/corporate_largemalebody01" };
  public static final String[] LARGE_MALE_HEADS = { "Luka/Luka_LargeMaleHead01",
      "alexlikarl/alexlikarl_largemalehead01", "alexlikarl/alexlikarl_largemalehead01_variant" };
  public static final String[] ALEX_HAIRS = { "AlexLiKarl/AlexLiKarl_LargeMaleHead01_Hair01",
      "alexlikarl_largemalehead01_variant_hair01" };

  public static interface BodyType {
  }

  public static interface HeadType {
  }

  public static interface HairType {
  }

  public static enum FemaleBody implements BodyType {
    CORPORATE, MECHANIC, PLUMBER, SCIENTIST, SECURITY, GLOVELESS
    // TODO: Add labcoat, morgan shift
  }

  public static enum MaleBody implements BodyType {
    CORPORATE, DAHL, LABCOAT, MECHANIC, SCIENTIST, SECURITY, VOLUNTEER, GLOVELESS
    // TODO: Add player models, morgan shift
  }

  // TODO: Add wounded alex variant
  public static enum LargeMaleBody implements BodyType {
    ALEX, ALEX_VARIANT
  }

  public static enum FemaleHead implements HeadType {
    HEAD01, HEAD02, HEAD02_PSYCHOSCOPE, HEAD03, HEAD04, HEAD05, HEAD06, MIKHAILA, MORGAN, SARA, DANIELLE, HELMET, HUSK
    // TODO: Add prototype psychoscope
  }

  public static enum MaleHead implements HeadType {
    DAHL, CALVINO, IGWE, HEAD01, HEAD01_PSYCHOSCOPE, HEAD01_PSYCHO, HEAD02, HEAD03, HEAD03_PSYCHOSCOPE, HEAD04, HEAD05,
    HEAD06, INGRAM, MORGAN, VOLUNTEER01, VOLUNTEER02, VOLUNTEER03, HELMET, HUSK
  }

  public static enum LargeMaleHead implements HeadType {
    ALEX, ALEX_VARIANT, LUKA
  }

  public static enum FemaleHair implements HairType {
    HEAD01_HAIR02, HEAD01_HAIR03, HEAD01_HAIR05, HEAD02_HAIR02, HEAD02_HAIR03, HEAD02_HAIR04, HEAD02_HAIR05,
    HEAD03_HAIR02, HEAD03_HAIR03, HEAD03_HAIR04, HEAD03_HAIR05, HEAD04_HAIR03, HEAD04_HAIR04, HEAD04_HAIR05,
    HEAD05_HAIR03, HEAD05_HAIR05,
    // HEAD06_HAIR02, // Unused
    HEAD06_HAIR03, HEAD06_HAIR05, HEAD06_HAIR06, MIKHAILA, MORGAN, SARA
  }

  public static enum FemaleHairType {
    HAIR02, HAIR03, HAIR04, HAIR05, HAIR06, MIKHAILA, MORGAN, SARA
  }

  public static enum FemaleHairColor {
    // HAIR01_BLACK, // Unused
    HAIR02_BLACK, HAIR02_BLONDE, HAIR02_BROWN, HAIR02_GRAY, HAIR02_RED, HAIR03, HAIR03_BLACK, HAIR03_BLONDE,
    HAIR03_BROWN, HAIR03_RED, HAIR04_BLACK,
    // HAIR04_BLONDE, // Unused
    HAIR04_BROWN, HAIR04_RED, HAIR05_BLACK, HAIR05_BLONDE, HAIR05_BROWN, HAIR05_RED, HAIR06, MIKHAILA, MORGAN, SARA
  }

  public static enum MaleHairType {
    HAIR01, HAIR03, HAIR04, HAIR05, HAIR06, VOLUNTEER, MORGAN, DAHL, IGWE, CALVINO
  }

  public static enum MaleHair implements HairType {
    HEAD01_HAIR01, HEAD01_HAIR03, HEAD01_HAIR05, HEAD02_HAIR01, HEAD02_HAIR03, HEAD02_HAIR05, HEAD03_HAIR03,
    HEAD03_HAIR05, HEAD03_HAIR06, HEAD04_HAIR04, HEAD05_HAIR01, HEAD05_HAIR05, HEAD05_HAIR06, HEAD06_HAIR01,
    HEAD06_HAIR05, HEAD06_HAIR06, VOLUNTEER01, VOLUNTEER02, VOLUNTEER03, MORGAN, DAHL, IGWE, CALVINO
  }
  
  public static enum MaleHairColor {
    HAIR01, HAIR01_BLACK, HAIR01_BLONDE, HAIR01_BROWN, HAIR01_RED, HAIR03, HAIR03_BLACK, HAIR03_BLONDE, HAIR03_BROWN,
    HAIR03_RED, HAIR04, HAIR05, HAIR05_BLACK, HAIR05_BLONDE, HAIR05_BROWN, HAIR05_RED, HAIR06, HAIR06_BLACK,
    HAIR06_BLONDE, MORGAN, DAHL, IGWE, CALVINO, VOLUNTEER
  }

  public static enum LargeMaleHair implements HairType {
    ALEX, ALEX_VARIANT, LUKA
  }

  public static final ImmutableSet<MaleHead> HEADS_THAT_CAN_BE_BALD = ImmutableSet.of(MaleHead.HEAD04, MaleHead.INGRAM,
      MaleHead.VOLUNTEER01, MaleHead.VOLUNTEER02, MaleHead.VOLUNTEER03);

  public static final ImmutableMultimap<MaleHead, MaleHair> MALE_HEAD_HAIR_COMPATIBILITY = new ImmutableMultimap.Builder<MaleHead, MaleHair>()
      .put(MaleHead.HEAD01, MaleHair.HEAD01_HAIR01)
      .put(MaleHead.HEAD01, MaleHair.HEAD01_HAIR03)
      .put(MaleHead.HEAD01, MaleHair.HEAD01_HAIR05)
      .put(MaleHead.HEAD02, MaleHair.HEAD02_HAIR01)
      .put(MaleHead.HEAD02, MaleHair.HEAD02_HAIR03)
      .put(MaleHead.HEAD02, MaleHair.HEAD02_HAIR05)
      .put(MaleHead.HEAD03, MaleHair.HEAD03_HAIR03)
      .put(MaleHead.HEAD03, MaleHair.HEAD03_HAIR05)
      .put(MaleHead.HEAD03, MaleHair.HEAD03_HAIR06)
      .put(MaleHead.HEAD04, MaleHair.HEAD04_HAIR04)
      .put(MaleHead.HEAD05, MaleHair.HEAD05_HAIR01)
      .put(MaleHead.HEAD05, MaleHair.HEAD05_HAIR05)
      .put(MaleHead.HEAD05, MaleHair.HEAD05_HAIR06)
      .put(MaleHead.HEAD06, MaleHair.HEAD06_HAIR01)
      .put(MaleHead.HEAD06, MaleHair.HEAD06_HAIR05)
      .put(MaleHead.HEAD06, MaleHair.HEAD06_HAIR06)
      .put(MaleHead.MORGAN, MaleHair.MORGAN)
      .put(MaleHead.DAHL, MaleHair.DAHL)
      .put(MaleHead.DAHL, MaleHair.MORGAN)
      .put(MaleHead.CALVINO, MaleHair.CALVINO)
      .put(MaleHead.IGWE, MaleHair.IGWE)
      .put(MaleHead.IGWE, MaleHair.HEAD02_HAIR01)
      .put(MaleHead.IGWE, MaleHair.HEAD02_HAIR03)
      .put(MaleHead.IGWE, MaleHair.HEAD02_HAIR05)
      .put(MaleHead.INGRAM, MaleHair.MORGAN)
      .put(MaleHead.VOLUNTEER01, MaleHair.VOLUNTEER01)
      .put(MaleHead.VOLUNTEER02, MaleHair.VOLUNTEER02)
      .put(MaleHead.VOLUNTEER03, MaleHair.VOLUNTEER03)
      .build();

  public static final ImmutableMap<MaleHair, MaleHairType> MALE_HAIR_TO_HAIR_TYPE = new ImmutableMap.Builder<MaleHair, MaleHairType>()
      .put(MaleHair.HEAD01_HAIR01, MaleHairType.HAIR01)
      .put(MaleHair.HEAD02_HAIR01, MaleHairType.HAIR01)
      .put(MaleHair.HEAD05_HAIR01, MaleHairType.HAIR01)
      .put(MaleHair.HEAD06_HAIR01, MaleHairType.HAIR01)
      .put(MaleHair.HEAD01_HAIR03, MaleHairType.HAIR03)
      .put(MaleHair.HEAD02_HAIR03, MaleHairType.HAIR03)
      .put(MaleHair.HEAD03_HAIR03, MaleHairType.HAIR03)
      .put(MaleHair.HEAD04_HAIR04, MaleHairType.HAIR04)
      .put(MaleHair.HEAD01_HAIR05, MaleHairType.HAIR05)
      .put(MaleHair.HEAD02_HAIR05, MaleHairType.HAIR05)
      .put(MaleHair.HEAD03_HAIR05, MaleHairType.HAIR05)
      .put(MaleHair.HEAD05_HAIR05, MaleHairType.HAIR05)
      .put(MaleHair.HEAD06_HAIR05, MaleHairType.HAIR05)
      .put(MaleHair.HEAD03_HAIR06, MaleHairType.HAIR06)
      .put(MaleHair.HEAD05_HAIR06, MaleHairType.HAIR06)
      .put(MaleHair.HEAD06_HAIR06, MaleHairType.HAIR06)
      .put(MaleHair.MORGAN, MaleHairType.MORGAN)
      .put(MaleHair.DAHL, MaleHairType.DAHL)
      .put(MaleHair.IGWE, MaleHairType.IGWE)
      .put(MaleHair.CALVINO, MaleHairType.CALVINO)
      .put(MaleHair.VOLUNTEER01, MaleHairType.VOLUNTEER)
      .build();
  
  public static final ImmutableMultimap<MaleHairType, MaleHairColor> MALE_HAIR_COLOR_COMPATIBILITY = new ImmutableMultimap.Builder<MaleHairType, MaleHairColor>()
      .put(MaleHairType.HAIR01, MaleHairColor.HAIR01)
      .put(MaleHairType.HAIR01, MaleHairColor.HAIR01_BLACK)
      .put(MaleHairType.HAIR01, MaleHairColor.HAIR01_BLONDE)
      .put(MaleHairType.HAIR01, MaleHairColor.HAIR01_BROWN)
      .put(MaleHairType.HAIR01, MaleHairColor.HAIR01_RED)
      .put(MaleHairType.HAIR03, MaleHairColor.HAIR03)
      .put(MaleHairType.HAIR03, MaleHairColor.HAIR03_BLACK)
      .put(MaleHairType.HAIR03, MaleHairColor.HAIR03_BLONDE)
      .put(MaleHairType.HAIR03, MaleHairColor.HAIR03_BROWN)
      .put(MaleHairType.HAIR03, MaleHairColor.HAIR03_RED)
      .put(MaleHairType.HAIR04, MaleHairColor.HAIR04)
      .put(MaleHairType.HAIR05, MaleHairColor.HAIR05)
      .put(MaleHairType.HAIR05, MaleHairColor.HAIR05_BLACK)
      .put(MaleHairType.HAIR05, MaleHairColor.HAIR05_BLONDE)
      .put(MaleHairType.HAIR05, MaleHairColor.HAIR05_BROWN)
      .put(MaleHairType.HAIR05, MaleHairColor.HAIR05_RED)
      .put(MaleHairType.HAIR06, MaleHairColor.HAIR06)
      .put(MaleHairType.HAIR06, MaleHairColor.HAIR06_BLACK)
      .put(MaleHairType.HAIR06, MaleHairColor.HAIR06_BLONDE)
      .put(MaleHairType.MORGAN, MaleHairColor.MORGAN)
      .put(MaleHairType.DAHL, MaleHairColor.DAHL)
      .put(MaleHairType.IGWE, MaleHairColor.IGWE)
      .put(MaleHairType.CALVINO, MaleHairColor.CALVINO)
      .put(MaleHairType.VOLUNTEER, MaleHairColor.VOLUNTEER)
      .build();

  public static final ImmutableMultimap<FemaleHead, FemaleHair> FEMALE_HEAD_HAIR_COMPATIBILITY = new ImmutableMultimap.Builder<FemaleHead, FemaleHair>()
      .put(FemaleHead.HEAD01, FemaleHair.HEAD01_HAIR02)
      .put(FemaleHead.HEAD01, FemaleHair.HEAD01_HAIR03)
      .put(FemaleHead.HEAD01, FemaleHair.HEAD01_HAIR05)
      .put(FemaleHead.HEAD02, FemaleHair.HEAD02_HAIR02)
      .put(FemaleHead.HEAD02, FemaleHair.HEAD02_HAIR03)
      .put(FemaleHead.HEAD02, FemaleHair.HEAD02_HAIR04)
      .put(FemaleHead.HEAD02, FemaleHair.HEAD02_HAIR05)
      .put(FemaleHead.HEAD02, FemaleHair.MORGAN)
      .put(FemaleHead.HEAD03, FemaleHair.HEAD03_HAIR02)
      .put(FemaleHead.HEAD03, FemaleHair.HEAD03_HAIR03)
      .put(FemaleHead.HEAD03, FemaleHair.HEAD03_HAIR04)
      .put(FemaleHead.HEAD03, FemaleHair.HEAD03_HAIR05)
      .put(FemaleHead.HEAD04, FemaleHair.HEAD04_HAIR03)
      .put(FemaleHead.HEAD04, FemaleHair.HEAD04_HAIR04)
      .put(FemaleHead.HEAD04, FemaleHair.HEAD04_HAIR05)
      .put(FemaleHead.HEAD04, FemaleHair.HEAD04_HAIR05)
      .put(FemaleHead.HEAD05, FemaleHair.HEAD05_HAIR03)
      .put(FemaleHead.HEAD05, FemaleHair.HEAD05_HAIR05)
      .put(FemaleHead.HEAD06, FemaleHair.HEAD06_HAIR03)
      .put(FemaleHead.HEAD06, FemaleHair.HEAD06_HAIR05)
      .put(FemaleHead.MIKHAILA, FemaleHair.MIKHAILA)
      .put(FemaleHead.MIKHAILA, FemaleHair.HEAD02_HAIR02)
      .put(FemaleHead.MIKHAILA, FemaleHair.HEAD02_HAIR03)
      .put(FemaleHead.MIKHAILA, FemaleHair.HEAD02_HAIR04)
      .put(FemaleHead.MIKHAILA, FemaleHair.HEAD02_HAIR05)
      .put(FemaleHead.MIKHAILA, FemaleHair.SARA)
      .put(FemaleHead.MORGAN, FemaleHair.MORGAN)
      .put(FemaleHead.MORGAN, FemaleHair.HEAD02_HAIR02)
      .put(FemaleHead.MORGAN, FemaleHair.HEAD02_HAIR03)
      .put(FemaleHead.MORGAN, FemaleHair.HEAD02_HAIR04)
      .put(FemaleHead.MORGAN, FemaleHair.HEAD02_HAIR05)
      .put(FemaleHead.MORGAN, FemaleHair.SARA)
      .put(FemaleHead.SARA, FemaleHair.SARA)
      .put(FemaleHead.SARA, FemaleHair.HEAD02_HAIR02)
      .put(FemaleHead.SARA, FemaleHair.HEAD02_HAIR03)
      .put(FemaleHead.SARA, FemaleHair.HEAD02_HAIR04)
      .put(FemaleHead.SARA, FemaleHair.HEAD02_HAIR05)
      .put(FemaleHead.SARA, FemaleHair.MORGAN)
      .put(FemaleHead.SARA, FemaleHair.MIKHAILA)
      .build();

  public static final ImmutableMap<FemaleHair, FemaleHairType> FEMALE_HAIR_TO_HAIR_TYPE = new ImmutableMap.Builder<FemaleHair, FemaleHairType>()
      .put(FemaleHair.HEAD01_HAIR02, FemaleHairType.HAIR02)
      .put(FemaleHair.HEAD03_HAIR02, FemaleHairType.HAIR02)
      .put(FemaleHair.HEAD01_HAIR03, FemaleHairType.HAIR03)
      .put(FemaleHair.HEAD02_HAIR03, FemaleHairType.HAIR03)
      .put(FemaleHair.HEAD03_HAIR03, FemaleHairType.HAIR03)
      .put(FemaleHair.HEAD04_HAIR03, FemaleHairType.HAIR03)
      .put(FemaleHair.HEAD05_HAIR03, FemaleHairType.HAIR03)
      .put(FemaleHair.HEAD06_HAIR03, FemaleHairType.HAIR03)
      .put(FemaleHair.HEAD02_HAIR04, FemaleHairType.HAIR04)
      .put(FemaleHair.HEAD03_HAIR04, FemaleHairType.HAIR04)
      .put(FemaleHair.HEAD04_HAIR04, FemaleHairType.HAIR04)
      .put(FemaleHair.HEAD01_HAIR05, FemaleHairType.HAIR05)
      .put(FemaleHair.HEAD02_HAIR05, FemaleHairType.HAIR05)
      .put(FemaleHair.HEAD03_HAIR05, FemaleHairType.HAIR05)
      .put(FemaleHair.HEAD04_HAIR05, FemaleHairType.HAIR05)
      .put(FemaleHair.HEAD05_HAIR05, FemaleHairType.HAIR05)
      .put(FemaleHair.HEAD06_HAIR05, FemaleHairType.HAIR05)
      .put(FemaleHair.MIKHAILA, FemaleHairType.MIKHAILA)
      .put(FemaleHair.MORGAN, FemaleHairType.MORGAN)
      .put(FemaleHair.SARA, FemaleHairType.SARA)
      .build();

  public static final ImmutableMultimap<FemaleHairType, FemaleHairColor> FEMALE_HAIR_COLOR_COMPATIBILITY = new ImmutableMultimap.Builder<FemaleHairType, FemaleHairColor>()
      .put(FemaleHairType.HAIR02, FemaleHairColor.HAIR02_BLACK)
      .put(FemaleHairType.HAIR02, FemaleHairColor.HAIR02_BLONDE)
      .put(FemaleHairType.HAIR02, FemaleHairColor.HAIR02_BROWN)
      .put(FemaleHairType.HAIR02, FemaleHairColor.HAIR02_GRAY)
      .put(FemaleHairType.HAIR02, FemaleHairColor.HAIR02_RED)
      .put(FemaleHairType.HAIR03, FemaleHairColor.HAIR03)
      .put(FemaleHairType.HAIR03, FemaleHairColor.HAIR03_BLACK)
      .put(FemaleHairType.HAIR03, FemaleHairColor.HAIR03_BLONDE)
      .put(FemaleHairType.HAIR03, FemaleHairColor.HAIR03_BROWN)
      .put(FemaleHairType.HAIR03, FemaleHairColor.HAIR03_RED)
      .put(FemaleHairType.HAIR04, FemaleHairColor.HAIR04_BLACK)
      .put(FemaleHairType.HAIR04, FemaleHairColor.HAIR04_BROWN)
      .put(FemaleHairType.HAIR04, FemaleHairColor.HAIR04_RED)
      .put(FemaleHairType.HAIR05, FemaleHairColor.HAIR05_BLACK)
      .put(FemaleHairType.HAIR05, FemaleHairColor.HAIR05_BLONDE)
      .put(FemaleHairType.HAIR05, FemaleHairColor.HAIR05_BROWN)
      .put(FemaleHairType.HAIR05, FemaleHairColor.HAIR05_RED)
      .put(FemaleHairType.HAIR06, FemaleHairColor.HAIR06)
      .put(FemaleHairType.MIKHAILA, FemaleHairColor.MIKHAILA)
      .put(FemaleHairType.MORGAN, FemaleHairColor.MORGAN)
      .put(FemaleHairType.SARA, FemaleHairColor.SARA)
      .build();

  public static final ImmutableMap<FemaleBody, String> FEMALE_BODIES_MAP = new ImmutableMap.Builder<FemaleBody, String>()
      .put(FemaleBody.CORPORATE, "corporate/corporate_genfemalebody01")
      .put(FemaleBody.MECHANIC, "mechanic/mechanic_genfemalebody01")
      .put(FemaleBody.SCIENTIST, "scientist/scientist_genfemalebody01")
      .put(FemaleBody.SECURITY, "security/security_genfemalebody01")
      .put(FemaleBody.GLOVELESS, "morgankarl/morgankarlgenderselect_genfemale")
      .build();

  public static final ImmutableMap<MaleBody, String> MALE_BODIES_MAP = new ImmutableMap.Builder<MaleBody, String>().put(
      MaleBody.CORPORATE, "corporate/corporate_genmalebody01")
      .put(MaleBody.DAHL, "dahl/dahl_genmalebody01")
      .put(MaleBody.LABCOAT, "labcoat/labcoat_genmalebody01")
      .put(MaleBody.MECHANIC, "mechanic/mechanic_genmalebody01")
      .put(MaleBody.SCIENTIST, "scientist/scientist_genmalebody01")
      .put(MaleBody.SECURITY, "security/security_genmalebody01")
      .put(MaleBody.VOLUNTEER, "volunteer/volunteer_genmalebody01")
      .put(MaleBody.GLOVELESS, "morgankarl/morgankarlgenderselect_genmale")
      .build();

  public static final String LARGE_MALE_BODY_MODEL = "corporate/corporate_largemalebody01";

  public static final ImmutableMap<FemaleHead, String> FEMALE_HEADS_MAP = new ImmutableMap.Builder<FemaleHead, String>()
      .put(FemaleHead.HEAD01, "genfemale/genfemale_head01")
      .put(FemaleHead.HEAD02, "genfemale/genfemale_head02")
      .put(FemaleHead.HEAD02_PSYCHOSCOPE, "genfemale/genfemale_head02_psychoscope")
      .put(FemaleHead.HEAD03, "genfemale/genfemale_head03")
      .put(FemaleHead.HEAD04, "genfemale/genfemale_head04")
      .put(FemaleHead.HEAD05, "genfemale/genfemale_head05")
      .put(FemaleHead.HEAD06, "genfemale/genfemale_head06")
      .put(FemaleHead.MIKHAILA, "mikhailailyushin/mikhailailyushin_genfemalehead01")
      .put(FemaleHead.MORGAN, "morgankarl/morgankarl_genfemalehead01")
      .put(FemaleHead.SARA, "saraelazar/saraelazar_genfemalehead01")
      .put(FemaleHead.DANIELLE, "objects/accessories/breather/breather3p/breather3p_genfemale01")
      .put(FemaleHead.HELMET, "scientist/scientist_genfemalehead01")
      .put(FemaleHead.HUSK, "husk/husk_genfemalehead01")
      .build();

  public static final ImmutableMap<MaleHead, String> MALE_HEADS_MAP = new ImmutableMap.Builder<MaleHead, String>().put(
      MaleHead.DAHL, "dahl/dahl_genmalehead01")
      .put(MaleHead.CALVINO, "drcalvino/drcalvino_genmalehead01")
      .put(MaleHead.IGWE, "drigwe/igwe_genmalehead01")
      .put(MaleHead.HEAD01, "genmale/genmale_head01")
      .put(MaleHead.HEAD01_PSYCHOSCOPE, "genmale/genmale_head01_psychoscope")
      .put(MaleHead.HEAD01_PSYCHO, "genmale/genmale_head01_psycho")
      .put(MaleHead.HEAD02, "genmale/genmale_head02")
      .put(MaleHead.HEAD03, "genmale/genmale_head03")
      .put(MaleHead.HEAD03_PSYCHOSCOPE, "genmale/genmale_head03_psychoscope")
      .put(MaleHead.HEAD04, "genmale/genmale_head04")
      .put(MaleHead.HEAD05, "genmale/genmale_head05")
      .put(MaleHead.HEAD06, "genmale/genmale_head06")
      .put(MaleHead.MORGAN, "morgankarl/morgankarl_genmalehead01")
      .put(MaleHead.VOLUNTEER01, "volunteer/volunteer_genmalehead01")
      .put(MaleHead.VOLUNTEER02, "volunteer/volunteer_genmalehead02")
      .put(MaleHead.VOLUNTEER03, "volunteer/volunteer_genmalehead03")
      .put(MaleHead.HELMET, "scientist/scientist_genmalehead01")
      .put(MaleHead.HUSK, "husk/husk_genmalehead01")
      .put(MaleHead.INGRAM, "aaroningram/aaroningram_genmale_head01")
      .build();
  
  public static final ImmutableMap<LargeMaleHead, String> LARGE_MALE_HEADS_MAP = new ImmutableMap.Builder<LargeMaleHead, String>()
      .put(LargeMaleHead.ALEX, "alexlikarl/alexlikarl_largemalehead01")
      .put(LargeMaleHead.ALEX_VARIANT, "alexlikarl/alexlikarl_largemalehead01_variant")
      .put(LargeMaleHead.LUKA, "luka/luka_largemalehead01")
      .build();

  public static final ImmutableMap<FemaleHair, String> FEMALE_HAIRS_MAP = new ImmutableMap.Builder<FemaleHair, String>()
      .put(FemaleHair.HEAD01_HAIR02, "genfemale/genfemale_head01_hair02")
      .put(FemaleHair.HEAD01_HAIR03, "genfemale/genfemale_head01_hair03")
      .put(FemaleHair.HEAD01_HAIR05, "genfemale/genfemale_head01_hair05")
      .put(FemaleHair.HEAD02_HAIR02, "genfemale/genfemale_head02_hair02")
      .put(FemaleHair.HEAD02_HAIR03, "genfemale/genfemale_head02_hair03")
      .put(FemaleHair.HEAD02_HAIR04, "genfemale/genfemale_head02_hair04")
      .put(FemaleHair.HEAD02_HAIR05, "genfemale/genfemale_head02_hair05")
      .put(FemaleHair.HEAD03_HAIR02, "genfemale/genfemale_head03_hair02")
      .put(FemaleHair.HEAD03_HAIR03, "genfemale/genfemale_head03_hair03")
      .put(FemaleHair.HEAD03_HAIR04, "genfemale/genfemale_head03_hair04")
      .put(FemaleHair.HEAD03_HAIR05, "genfemale/genfemale_head03_hair05")
      .put(FemaleHair.HEAD04_HAIR03, "genfemale/genfemale_head04_hair03")
      .put(FemaleHair.HEAD04_HAIR04, "genfemale/genfemale_head04_hair04")
      .put(FemaleHair.HEAD04_HAIR05, "genfemale/genfemale_head04_hair05")
      .put(FemaleHair.HEAD05_HAIR03, "genfemale/genfemale_head05_hair03")
      .put(FemaleHair.HEAD05_HAIR05, "genfemale/genfemale_head05_hair05")
      // .put(FemaleHair.HEAD06_HAIR02, "genfemale/genfemale_head06_hair02")
      .put(FemaleHair.HEAD06_HAIR03, "genfemale/genfemale_head06_hair03")
      .put(FemaleHair.HEAD06_HAIR05, "genfemale/genfemale_head06_hair05")
      .put(FemaleHair.HEAD06_HAIR06, "genfemale/genfemale_head06_hair06")
      .put(FemaleHair.MIKHAILA, "mikhailailyushin/mikhailailyushin_genfemalehead01_hair01")
      .put(FemaleHair.MORGAN, "morgankarl/morgankarl_genfemalehead01_hair01")
      .put(FemaleHair.SARA, "saraelazar/saraelazar_genfemalehead01_hair01")
      .build();

  public static final ImmutableMap<FemaleHairColor, String> FEMALE_HAIR_COLORS_MAP = new ImmutableMap.Builder<FemaleHairColor, String>()
      // .put(FemaleHairColor.HAIR01_BLACK, "genfemale/genfemale_hair01_black")
      .put(FemaleHairColor.HAIR02_BLACK, "genfemale/genfemale_hair02_black")
      .put(FemaleHairColor.HAIR02_BLONDE, "genfemale/genfemale_hair02_blonde")
      .put(FemaleHairColor.HAIR02_BROWN, "genfemale/genfemale_hair02_brown")
      .put(FemaleHairColor.HAIR02_GRAY, "genfemale/genfemale_hair02_gray")
      .put(FemaleHairColor.HAIR02_RED, "genfemale/genfemale_hair02_red")
      .put(FemaleHairColor.HAIR03, "genfemale/genfemale_hair03")
      .put(FemaleHairColor.HAIR03_BLACK, "genfemale/genfemale_hair03_black")
      .put(FemaleHairColor.HAIR03_BLONDE, "genfemale/genfemale_hair03_blonde")
      .put(FemaleHairColor.HAIR03_BROWN, "genfemale/genfemale_hair03_brown")
      .put(FemaleHairColor.HAIR03_RED, "genfemale/genfemale_hair03_red")
      .put(FemaleHairColor.HAIR04_BLACK, "genfemale/genfemale_hair04_black")
      // .put(FemaleHairColor.HAIR04_BLONDE, "genfemale/genfemale_hair04_blonde")
      .put(FemaleHairColor.HAIR04_BROWN, "genfemale/genfemale_hair04_brown")
      .put(FemaleHairColor.HAIR04_RED, "genfemale/genfemale_hair04_red")
      .put(FemaleHairColor.HAIR04_RED, "genfemale/genfemale_hair04_red")
      .put(FemaleHairColor.HAIR05_BLACK, "genfemale/genfemale_hair05_black")
      .put(FemaleHairColor.HAIR05_BLONDE, "genfemale/genfemale_hair05_blonde")
      .put(FemaleHairColor.HAIR05_BROWN, "genfemale/genfemale_hair05_brown")
      .put(FemaleHairColor.HAIR05_RED, "genfemale/genfemale_hair05_red")
      .put(FemaleHairColor.HAIR06, "genfemale/genfemale_hair06")
      .put(FemaleHairColor.MIKHAILA, "mikhailailyushin/mikhailailyushin_genfemalehead01_hair01")
      .put(FemaleHairColor.MORGAN, "morgankarl/morgankarl_genfemalehead01_hair01")
      .put(FemaleHairColor.SARA, "saraelazar/saraelazar_genfemalehead01_hair01")
      .build();

  public static final ImmutableMap<MaleHair, String> MALE_HAIRS_MAP = new ImmutableMap.Builder<MaleHair, String>().put(
      MaleHair.HEAD01_HAIR01, "genmale/genmale_head01_hair01")
      .put(MaleHair.HEAD01_HAIR03, "genmale/genmale_head01_hair03")
      .put(MaleHair.HEAD01_HAIR05, "genmale/genmale_head01_hair05")
      .put(MaleHair.HEAD02_HAIR01, "genmale/genmale_head02_hair01")
      .put(MaleHair.HEAD02_HAIR03, "genmale/genmale_head02_hair03")
      .put(MaleHair.HEAD02_HAIR05, "genmale/genmale_head02_hair05")
      .put(MaleHair.HEAD03_HAIR03, "genmale/genmale_head03_hair03")
      .put(MaleHair.HEAD03_HAIR05, "genmale/genmale_head03_hair05")
      .put(MaleHair.HEAD03_HAIR06, "genmale/genmale_head03_hair06")
      .put(MaleHair.HEAD04_HAIR04, "genmale/genmale_head04_hair04")
      .put(MaleHair.HEAD05_HAIR01, "genmale/genmale_head05_hair01")
      .put(MaleHair.HEAD05_HAIR05, "genmale/genmale_head05_hair05")
      .put(MaleHair.HEAD05_HAIR06, "genmale/genmale_head05_hair06")
      .put(MaleHair.HEAD06_HAIR01, "genmale/genmale_head06_hair01")
      .put(MaleHair.HEAD06_HAIR05, "genmale/genmale_head06_hair05")
      .put(MaleHair.HEAD06_HAIR06, "genmale/genmale_head06_hair06")
      .put(MaleHair.VOLUNTEER01, "volunteer/volunteer_genmalehead01_hair01")
      .put(MaleHair.VOLUNTEER02, "volunteer/volunteer_genmalehead02_hair01")
      .put(MaleHair.VOLUNTEER03, "volunteer/volunteer_genmalehead03_hair01")
      .put(MaleHair.MORGAN, "morgankarl/morgankarl_genmalehead01_hair01")
      .put(MaleHair.DAHL, "dahl/dahl_genmalehead01_hair01")
      .put(MaleHair.IGWE, "drigwe/igwe_genmalehead01_hair01")
      .put(MaleHair.CALVINO, "drcalvino/drcalvino_genmalehead01_hair01")
      .build();

  public static final ImmutableMap<MaleHairColor, String> MALE_HAIR_COLORS_MAP = new ImmutableMap.Builder<MaleHairColor, String>()
      .put(MaleHairColor.HAIR01, "genmale/genmale_hair01")
      .put(MaleHairColor.HAIR01_BLACK, "genmale/genmale_hair01_black")
      .put(MaleHairColor.HAIR01_BLONDE, "genmale/genmale_hair01_blonde")
      .put(MaleHairColor.HAIR01_BROWN, "genmale/genmale_hair01_brown")
      .put(MaleHairColor.HAIR01_RED, "genmale/genmale_hair01_red")
      .put(MaleHairColor.HAIR03, "genmale/genmale_hair03")
      .put(MaleHairColor.HAIR03_BLACK, "genmale/genmale_hair03_black")
      .put(MaleHairColor.HAIR03_BLONDE, "genmale/genmale_hair03_blonde")
      .put(MaleHairColor.HAIR03_BROWN, "genmale/genmale_hair03_brown")
      .put(MaleHairColor.HAIR03_RED, "genmale/genmale_hair03_red")
      .put(MaleHairColor.HAIR04, "genmale/genmale_hair04")
      .put(MaleHairColor.HAIR05, "genmale/genmale_hair05")
      .put(MaleHairColor.HAIR05_BLACK, "genmale/genmale_hair05_black")
      .put(MaleHairColor.HAIR05_BLONDE, "genmale/genmale_hair05_blonde")
      .put(MaleHairColor.HAIR05_BROWN, "genmale/genmale_hair05_brown")
      .put(MaleHairColor.HAIR05_RED, "genmale/genmale_hair05_red")
      .put(MaleHairColor.HAIR06, "genmale/genmale_hair06")
      .put(MaleHairColor.HAIR06, "genmale/genmale_hair06")
      .put(MaleHairColor.HAIR06_BLACK, "genmale/genmale_hair06_black")
      .put(MaleHairColor.HAIR06_BLONDE, "genmale/genmale_hair06_blonde")
      .put(MaleHairColor.MORGAN, "morgankarl/morgankarl_genmalehead01_hair01")
      .put(MaleHairColor.DAHL, "dahl/dahl_genmalehead01_hair01")
      .put(MaleHairColor.IGWE, "drigwe/igwe_genmalehead01_hair01")
      .put(MaleHairColor.CALVINO, "drcalvino/dr_calvino_genmalehead01_hair")
      .put(MaleHairColor.VOLUNTEER, "volunteer/volunteer_hair01_brown")
      .build();
  
  public static final ImmutableMap<LargeMaleHair, String> LARGE_MALE_HAIRS_MAP = new ImmutableMap.Builder<LargeMaleHair, String>()
      .put(LargeMaleHair.ALEX, "alexlikarl/alexlikarl_largemalehead01_hair01")
      .put(LargeMaleHair.ALEX_VARIANT, "alexlikarl/alexlikarl_largemalehead01_variant_hair01")
      .put(LargeMaleHair.LUKA, "luka/luka_largemalehead01_hair01")
      .build();

  public static final FemaleBody[] SUPPORTED_FEMALE_BODIES = { FemaleBody.CORPORATE, FemaleBody.MECHANIC,
      FemaleBody.PLUMBER, FemaleBody.SCIENTIST, FemaleBody.SECURITY, FemaleBody.GLOVELESS };
  public static final Integer[] FEMALE_BODIES_WEIGHTS = { 5, 5, 1, 5, 5, 1 };

  public static final MaleBody[] SUPPORTED_MALE_BODIES = { MaleBody.CORPORATE, MaleBody.DAHL, MaleBody.LABCOAT,
      MaleBody.MECHANIC, MaleBody.SCIENTIST, MaleBody.SECURITY, MaleBody.VOLUNTEER, MaleBody.GLOVELESS };
  public static final Integer[] MALE_BODIES_WEIGHTS = { 5, 1, 5, 5, 5, 5, 5, 1 };

  public static final LargeMaleBody[] SUPPORTED_LARGE_MALE_BODIES = { LargeMaleBody.ALEX, LargeMaleBody.ALEX_VARIANT };

  public static final ImmutableSet<FemaleBody> ACCESSORY_COMPATIBLE_FEMALE_BODIES = ImmutableSet.of(
      FemaleBody.CORPORATE, FemaleBody.MECHANIC, FemaleBody.SCIENTIST, FemaleBody.SECURITY, FemaleBody.GLOVELESS);
  public static final ImmutableSet<MaleBody> ACCESSORY_COMPATIBLE_MALE_BODIES = ImmutableSet.of(MaleBody.CORPORATE,
      MaleBody.DAHL, MaleBody.MECHANIC, MaleBody.SCIENTIST, MaleBody.SECURITY, MaleBody.GLOVELESS);

  public static final FemaleHead[] SUPPORTED_FEMALE_HEADS = { FemaleHead.HEAD01, FemaleHead.HEAD02,
      FemaleHead.HEAD02_PSYCHOSCOPE, FemaleHead.HEAD03, FemaleHead.HEAD04, FemaleHead.HEAD05, FemaleHead.HEAD06,
      FemaleHead.MIKHAILA, FemaleHead.MORGAN, FemaleHead.SARA, FemaleHead.HELMET };

  public static final MaleHead[] SUPPORTED_MALE_HEADS = { MaleHead.DAHL, MaleHead.CALVINO, MaleHead.IGWE,
      MaleHead.HEAD01, MaleHead.HEAD01_PSYCHOSCOPE, MaleHead.HEAD01_PSYCHO, MaleHead.HEAD02, MaleHead.HEAD03,
      MaleHead.HEAD03_PSYCHOSCOPE, MaleHead.HEAD04, MaleHead.HEAD05, MaleHead.HEAD06, MaleHead.MORGAN,
      MaleHead.VOLUNTEER01, MaleHead.VOLUNTEER02, MaleHead.VOLUNTEER03, MaleHead.HELMET };

  public static final LargeMaleHead[] SUPPORTED_LARGE_MALE_HEADS = { LargeMaleHead.ALEX, LargeMaleHead.ALEX_VARIANT,
      LargeMaleHead.LUKA };
  
  public static final LargeMaleHair[] SUPPORTED_ALEX_HAIRS = { LargeMaleHair.ALEX, LargeMaleHair.ALEX_VARIANT };

  public static final ImmutableSet<HeadType> HEADS_THAT_SHOULD_NOT_HAVE_BARE_HANDS = ImmutableSet.of(FemaleHead.SARA,
      FemaleHead.HEAD01, FemaleHead.HUSK, MaleHead.IGWE, MaleHead.HEAD04, MaleHead.HUSK);

  public static final ImmutableSet<BodyType> BODIES_WITH_BARE_HANDS = ImmutableSet.of(FemaleBody.GLOVELESS,
      MaleBody.GLOVELESS, MaleBody.VOLUNTEER);

  // Used to make sure that "complete" heads do not have hair
  // TODO: Add husk heads?
  public static final ImmutableSet<HeadType> HEADS_THAT_SHOULD_NOT_HAVE_HAIR = ImmutableSet.of(
      FemaleHead.HEAD02_PSYCHOSCOPE, FemaleHead.HELMET, MaleHead.HEAD01_PSYCHOSCOPE, 
      MaleHead.HEAD01_PSYCHO, MaleHead.HEAD01_PSYCHOSCOPE, MaleHead.HELMET);

  
  public static final String FEMALE_HEAD_HELMET = "objects/accessories/breather/breather3p/breather3p_genfemale01";
  public static final String FEMALE_HEAD_HELMET_MTL = "objects/accessories/breather/breather3p/breather1";
  
  public static final String MALE_LABCOAT_LEGS_MODEL = "labcoat/labcoat_genmalelegs01";
  public static final String MALE_LABCOAT_HANDS_MODEL = "labcoat/labcoat_genmalehands01";
  public static final String MALE_LABCOAT_LIMBS_MTL = "scientist/scientist_genmalebody01";
  
  public static final String MORGAN_FEMALE_GLOVELESS_MTL = "morgankarl/morgan_genfemalebody01_cut_scene";
  public static final String MORGAN_MALE_GLOVELESS_MTL = "morgankarl/morgan_genmalebody01_cut_scene";
  
  // TODO: Bellamy's labcoat seems to be broken, female limbs as well
  public static final String MALE_LAB_COAT_LIMBS = "  <Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"legs\" Binding=\"objects/characters/humans/labcoat/labcoat_genmalelegs01.skin\" Material=\"objects/characters/humans/scientist/scientist_genmalebody01.mtl\" Flags=\"0\"/>\n"
      + "  <Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"hands\" Binding=\"objects/characters/humans/labcoat/labcoat_genmalehands01.skin\" Material=\"objects/characters/humans/scientist/scientist_genmalebody01.mtl\" Flags=\"0\"/>";
  public static final String FEMALE_LAB_COAT_LIMBS = "<Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"legs_arms\" Binding=\"objects/characters/humans/labcoat/labcoat_genfemalelegsarms01.skin\" Material=\"objects/characters/humans/scientist/scientist_genfemalebody01.mtl\" Flags=\"0\"/>";

  public static final String PLUMBER_HAT = "plumber/plumber_genfemalehat01";
  public static final String PLUMBER_HAT_MTL = "plumber/plumber_genfemalebody01";

  public static final String[] ACCESSORIES_FEMALE = {
      "objects/accessories/propulsionpack/propulsionpack3p_genfemale01" };
  public static final String[] ACCESSORIES_FEMALE_MTL = { "objects/accessories/propulsionpack/propulsionpack_3p_01" };
  public static final String[] ACCESSORIES_MALE = { "objects/characters/humans/dahl/dahl_genmalebackpack01.skin",
      "objects/accessories/propulsionpack/propulsionpack3p_genmale01" };
  public static final String[] ACCESSORIES_MALE_MTL = { "objects/characters/humans/dahl/dahl_genmalebody01.mtl",
      "objects/accessories/propulsionpack/propulsionpack_3p_01" };
}
