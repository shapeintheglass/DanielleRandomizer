package utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

public class KeycardConnectivityConsts {
  public static enum Keycard {
    AR_CREW_QUARTERS,
    AR_EP101,
    AR_LOVERS_KEY,
    BR_BRIEFING_ROOM,
    CB_SHIPPING_AND_RECEIVING,
    CQ_CABIN_ALEX,
    CQ_CABIN_CALVINO,
    CQ_CABIN_FOY,
    CQ_CABIN_KELSTRUP,
    CQ_CABIN_MARKS,
    CQ_CABIN_MITCHELL,
    CQ_CABIN_MORGAN,
    CQ_CABIN_ELAZAR,
    CQ_CABIN_THORSTEIN,
    CQ_KITCHEN,
    CQ_YELLOW_TULIP_STORAGE,
    DS_SECURITY,
    GT_MAINTENANCE_TUNNEL,
    GT_FUEL_STORAGE,
    HL_BALLISTICS,
    HL_BLACKBOX,
    HL_CALVINO_WORKSHOP,
    HL_EMPLOYEE_ENTRANCE,
    LO_GENERAL_ACCESS,
    LO_IT_CLOSET,
    LO_SECURITY,
    LO_TELECONFERENCING,
    LO_SECURED_PHARMACEUTICALS,
    LO_DEVRIES_OFFICE,
    LO_TRAUMA_CENTER,
    LS_ATMOSPHERE_CONTROLS,
    LS_SECURITY,
    ND_GRAVES_OFFICE,
    ND_SIM_LABS,
    PP_COOLANT_CHAMBER,
    PP_REACTOR,
    PY_KELSTRUPS_OFFICE,
    PY_MORGUE,
    SB_CONTROL_ROOM,
    SB_DAHLS_SHUTTLE
  }

  public static enum Location {
    AR_ALEX_SAFE,
    AR_NPC_DAHL,
    AR_LOVERS_KEY,
    AR_NPC_KYRKOS,
    AR_NPC_WEST,
    BR_NPC_GREENE,
    BR_NPC_MARKS,
    BR_NPC_JANUARY,
    BR_NPC_BOLIVAR,
    CB_NPC_ELAZAR,
    CB_NPC_COOL,
    CQ_ALEX_CABIN,
    CQ_FATAL_FORTRESS,
    CQ_NPC_LUKA,
    CQ_NPC_LUKA_QUEST,
    CQ_NPC_SELLERS,
    CQ_NPC_TIZZY,
    DS_DANIELLES_OFFICE,
    GT_NPC_KLINE,
    GT_NPC_LAVALLEY,
    GT_NPC_DALTON,
    HL_NPC_SCHMIDT,
    HL_CALVINOS_SAFE,
    HL_NPC_CALVINO,
    HL_NPC_LARSON,
    HL_NPC_THORSTEIN,
    LO_NPC_DEVRIES,
    LO_IT,
    LO_NPC_JANUARY_GIVEN,
    LO_NPC_JANUARY,
    LO_MORGAN_OFFICE_DESK,
    LO_MORGAN_OFFICE_WORKSHOP,
    LO_GOODWIN_OFFICE,
    LO_RECEPTION,
    LS_NPC_WEBER,
    LS_NPC_DAHL,
    LS_NPC_FOLSON,
    ND_BELLAMY_DESK,
    ND_NPC_DAHL,
    ND_NPC_STEELE,
    PP_NPC_CROAL,
    PP_NPC_FAURE,
    PP_NPC_STILLWATER,
    PP_NPC_BROOKS,
    PY_NPC_KELSTRUP,
    PY_NPC_KELSTRUP_2,
    PY_MORGUE,
    SB_NPC_DAHL,
    SB_PILOT_LOUNGE,
    SB_NPC_PARKER
  }

  public static final ImmutableList<Keycard> KEYCARDS_LOW_PRIORITY = ImmutableList.of(
      Keycard.CQ_CABIN_ELAZAR, Keycard.CB_SHIPPING_AND_RECEIVING, Keycard.DS_SECURITY,
      Keycard.HL_EMPLOYEE_ENTRANCE, Keycard.LO_SECURITY, Keycard.LO_TELECONFERENCING,
      Keycard.ND_SIM_LABS, Keycard.ND_GRAVES_OFFICE, Keycard.PY_MORGUE, Keycard.SB_CONTROL_ROOM);

  public static final ImmutableList<Keycard> KEYCARDS_MEDIUM_PRIORITY =
      ImmutableList.of(Keycard.CQ_CABIN_ALEX, Keycard.AR_LOVERS_KEY, Keycard.CQ_CABIN_MARKS,
          Keycard.BR_BRIEFING_ROOM, Keycard.CQ_KITCHEN, Keycard.LO_TRAUMA_CENTER,
          Keycard.CQ_YELLOW_TULIP_STORAGE, Keycard.HL_BLACKBOX, Keycard.HL_BALLISTICS,
          Keycard.CQ_CABIN_CALVINO, Keycard.HL_CALVINO_WORKSHOP, Keycard.CQ_CABIN_THORSTEIN,
          Keycard.AR_EP101, Keycard.LO_SECURED_PHARMACEUTICALS, Keycard.CQ_CABIN_MORGAN,
          Keycard.LO_DEVRIES_OFFICE, Keycard.LS_SECURITY, Keycard.LO_IT_CLOSET,
          Keycard.SB_DAHLS_SHUTTLE, Keycard.LS_ATMOSPHERE_CONTROLS, Keycard.CQ_CABIN_KELSTRUP);

  public static final ImmutableList<Keycard> KEYCARDS_HIGH_PRIORITY = ImmutableList.of(
      Keycard.AR_CREW_QUARTERS, Keycard.GT_FUEL_STORAGE, Keycard.GT_MAINTENANCE_TUNNEL,
      Keycard.LO_GENERAL_ACCESS, Keycard.PP_REACTOR, Keycard.PP_COOLANT_CHAMBER,
      Keycard.CQ_CABIN_FOY, Keycard.CQ_CABIN_MITCHELL, Keycard.PY_KELSTRUPS_OFFICE);

  public static final ImmutableList<Location> LOCATIONS_DUPE_ONLY =
      ImmutableList.of(Location.AR_NPC_DAHL, Location.BR_NPC_JANUARY, Location.CQ_NPC_LUKA,
          Location.CQ_NPC_LUKA_QUEST, Location.LO_NPC_JANUARY, Location.LO_NPC_JANUARY_GIVEN,
          Location.LS_NPC_DAHL, Location.ND_NPC_DAHL, Location.SB_NPC_DAHL);

  public static final ImmutableList<Location> LOCATIONS_LOW_ACCESSIBILITY =
      ImmutableList.of(Location.CB_NPC_ELAZAR, Location.CB_NPC_COOL, Location.CQ_ALEX_CABIN,
          Location.HL_NPC_CALVINO, Location.LS_NPC_FOLSON);

  public static final ImmutableList<Location> LOCATIONS_MEDIUM_ACCESSIBILITY = ImmutableList.of(
      Location.AR_ALEX_SAFE, Location.AR_LOVERS_KEY, Location.CQ_FATAL_FORTRESS,
      Location.CQ_NPC_SELLERS, Location.CQ_NPC_TIZZY, Location.DS_DANIELLES_OFFICE,
      Location.GT_NPC_KLINE, Location.GT_NPC_DALTON, Location.HL_NPC_SCHMIDT,
      Location.HL_CALVINOS_SAFE, Location.HL_NPC_LARSON, Location.HL_NPC_THORSTEIN,
      Location.LO_NPC_DEVRIES, Location.LO_IT, Location.ND_BELLAMY_DESK, Location.ND_NPC_STEELE,
      Location.PP_NPC_CROAL, Location.PP_NPC_FAURE, Location.PP_NPC_STILLWATER, Location.PY_MORGUE);

  public static final ImmutableList<Location> LOCATIONS_HIGH_ACCESSIBILITY = ImmutableList.of(
      Location.AR_NPC_KYRKOS, Location.AR_NPC_WEST, Location.BR_NPC_GREENE, Location.BR_NPC_MARKS,
      Location.BR_NPC_BOLIVAR, Location.GT_NPC_LAVALLEY, Location.LO_MORGAN_OFFICE_DESK,
      Location.LO_MORGAN_OFFICE_WORKSHOP, Location.LO_RECEPTION, Location.LO_GOODWIN_OFFICE,
      Location.LS_NPC_WEBER, Location.PP_NPC_BROOKS, Location.PY_NPC_KELSTRUP,
      Location.PY_NPC_KELSTRUP_2, Location.SB_NPC_PARKER, Location.SB_PILOT_LOUNGE);

  public static final ImmutableMultimap<Keycard, Location> DEFAULT_CONNECTIVITY =
      new ImmutableMultimap.Builder<Keycard, Location>()
          .put(Keycard.AR_CREW_QUARTERS, Location.AR_NPC_WEST)
          .put(Keycard.AR_EP101, Location.CQ_ALEX_CABIN)
          .put(Keycard.AR_EP101, Location.BR_NPC_JANUARY)
          .put(Keycard.AR_LOVERS_KEY, Location.AR_LOVERS_KEY)
          .put(Keycard.BR_BRIEFING_ROOM, Location.BR_NPC_BOLIVAR)
          .put(Keycard.BR_BRIEFING_ROOM, Location.BR_NPC_GREENE)
          .put(Keycard.CB_SHIPPING_AND_RECEIVING, Location.CB_NPC_COOL)
          .put(Keycard.CQ_CABIN_ALEX, Location.AR_ALEX_SAFE)
          .put(Keycard.CQ_CABIN_CALVINO, Location.HL_CALVINOS_SAFE)
          .put(Keycard.CQ_CABIN_FOY, Location.CQ_FATAL_FORTRESS)
          .put(Keycard.CQ_CABIN_KELSTRUP, Location.PY_NPC_KELSTRUP)
          .put(Keycard.CQ_KITCHEN, Location.CQ_NPC_LUKA)
          .put(Keycard.CQ_CABIN_MARKS, Location.AR_NPC_KYRKOS)
          .put(Keycard.CQ_CABIN_MARKS, Location.BR_NPC_MARKS)
          .put(Keycard.CQ_CABIN_MITCHELL, Location.CQ_NPC_LUKA_QUEST)
          .put(Keycard.CQ_CABIN_MORGAN, Location.LO_MORGAN_OFFICE_WORKSHOP)
          .put(Keycard.CQ_CABIN_ELAZAR, Location.CB_NPC_ELAZAR)
          .put(Keycard.CQ_CABIN_THORSTEIN, Location.HL_NPC_THORSTEIN)
          .put(Keycard.CQ_YELLOW_TULIP_STORAGE, Location.CQ_NPC_TIZZY)
          .put(Keycard.DS_SECURITY, Location.DS_DANIELLES_OFFICE)
          .put(Keycard.GT_MAINTENANCE_TUNNEL, Location.GT_NPC_KLINE)
          .put(Keycard.GT_FUEL_STORAGE, Location.GT_NPC_LAVALLEY)
          .put(Keycard.HL_BALLISTICS, Location.HL_NPC_SCHMIDT)
          .put(Keycard.HL_BLACKBOX, Location.GT_NPC_DALTON)
          .put(Keycard.HL_CALVINO_WORKSHOP, Location.HL_NPC_CALVINO)
          .put(Keycard.HL_EMPLOYEE_ENTRANCE, Location.HL_NPC_LARSON)
          .put(Keycard.LO_IT_CLOSET, Location.LS_NPC_FOLSON)
          .put(Keycard.LO_GENERAL_ACCESS, Location.LO_NPC_JANUARY)
          .put(Keycard.LO_GENERAL_ACCESS, Location.LO_NPC_JANUARY_GIVEN)
          .put(Keycard.LO_SECURITY, Location.LO_IT)
          .put(Keycard.LO_TELECONFERENCING, Location.LO_MORGAN_OFFICE_DESK)
          .put(Keycard.LO_SECURED_PHARMACEUTICALS, Location.CQ_NPC_SELLERS)
          .put(Keycard.LO_DEVRIES_OFFICE, Location.LO_NPC_DEVRIES)
          .put(Keycard.LO_TRAUMA_CENTER, Location.LO_RECEPTION)
          .put(Keycard.LO_TRAUMA_CENTER, Location.LO_GOODWIN_OFFICE)
          .put(Keycard.LS_ATMOSPHERE_CONTROLS, Location.PP_NPC_FAURE)
          .put(Keycard.LS_SECURITY, Location.LS_NPC_WEBER)
          .put(Keycard.ND_GRAVES_OFFICE, Location.ND_NPC_STEELE)
          .put(Keycard.ND_SIM_LABS, Location.ND_BELLAMY_DESK)
          .put(Keycard.PP_COOLANT_CHAMBER, Location.PP_NPC_BROOKS)
          .put(Keycard.PP_REACTOR, Location.PP_NPC_STILLWATER)
          .put(Keycard.PP_REACTOR, Location.PP_NPC_CROAL)
          .put(Keycard.PY_KELSTRUPS_OFFICE, Location.PY_NPC_KELSTRUP_2)
          .put(Keycard.PY_MORGUE, Location.PY_MORGUE)
          .put(Keycard.SB_CONTROL_ROOM, Location.SB_PILOT_LOUNGE)
          .put(Keycard.SB_CONTROL_ROOM, Location.SB_NPC_PARKER)
          .put(Keycard.SB_DAHLS_SHUTTLE, Location.AR_NPC_DAHL)
          .put(Keycard.SB_DAHLS_SHUTTLE, Location.LS_NPC_DAHL)
          .put(Keycard.SB_DAHLS_SHUTTLE, Location.ND_NPC_DAHL)
          .put(Keycard.SB_DAHLS_SHUTTLE, Location.SB_NPC_DAHL)
          .build();

  public static final ImmutableMap<Keycard, String> KEYCARD_TO_ID =
      new ImmutableMap.Builder<Keycard, String>().put(Keycard.AR_CREW_QUARTERS, "844024417269161838")
          .put(Keycard.AR_EP101, "14667999412535817654")
          .put(Keycard.AR_LOVERS_KEY, "14667999412531426939")
          .put(Keycard.BR_BRIEFING_ROOM, "4349723564935470793")
          .put(Keycard.CB_SHIPPING_AND_RECEIVING, "4349723564917331725")
          .put(Keycard.CQ_CABIN_ALEX, "13680621263398555661")
          .put(Keycard.CQ_CABIN_CALVINO, "761057047959608375")
          .put(Keycard.CQ_CABIN_FOY, "844024417273225818")
          .put(Keycard.CQ_CABIN_KELSTRUP, "844024417289636866")
          .put(Keycard.CQ_CABIN_MARKS, "844024417289132055")
          .put(Keycard.CQ_CABIN_MITCHELL, "844024417296040047")
          .put(Keycard.CQ_CABIN_MORGAN, "13680621263398566320")
          .put(Keycard.CQ_CABIN_ELAZAR, "844024417289133282")
          .put(Keycard.CQ_CABIN_THORSTEIN, "844024417264850195")
          .put(Keycard.CQ_KITCHEN, "844024417287725050")
          .put(Keycard.CQ_YELLOW_TULIP_STORAGE, "844024417289724089")
          .put(Keycard.DS_SECURITY, "1713490239430510631")
          .put(Keycard.GT_MAINTENANCE_TUNNEL, "761057047955726988")
          .put(Keycard.GT_FUEL_STORAGE, "761057047955908816")
          .put(Keycard.HL_BALLISTICS, "761057047957716142")
          .put(Keycard.HL_BLACKBOX, "761057047996404704")
          .put(Keycard.HL_CALVINO_WORKSHOP, "844024417264850262")
          .put(Keycard.HL_EMPLOYEE_ENTRANCE, "844024417264848561")
          .put(Keycard.LO_GENERAL_ACCESS, "15659330456309530410")
          .put(Keycard.LO_IT_CLOSET, "13856881013202738023")
          .put(Keycard.LO_SECURITY, "844024417314372118")
          .put(Keycard.LO_TELECONFERENCING, "1713490239448502258")
          .put(Keycard.LO_SECURED_PHARMACEUTICALS, "12889009724999938970")
          .put(Keycard.LO_DEVRIES_OFFICE, "13680621263411175870")
          .put(Keycard.LO_TRAUMA_CENTER, "1713490239391479434")
          .put(Keycard.LS_ATMOSPHERE_CONTROLS, "11824555372655840193")
          .put(Keycard.LS_SECURITY, "13856881013199799876")
          .put(Keycard.ND_GRAVES_OFFICE, "12889009724998311610")
          .put(Keycard.ND_SIM_LABS, "12889009724996576042")
          .put(Keycard.PP_COOLANT_CHAMBER, "11824555372665894444")
          .put(Keycard.PP_REACTOR, "11824555372644363968")
          .put(Keycard.PY_KELSTRUPS_OFFICE, "6732635291222790585")
          .put(Keycard.PY_MORGUE, "11824555372636319847")
          .put(Keycard.SB_CONTROL_ROOM, "14667999412539377155")
          .put(Keycard.SB_DAHLS_SHUTTLE, "14667999412528404640")
          .build();

  public static final ImmutableMap<Location, String> LOCATION_TO_LEVEL_NAME =
      new ImmutableMap.Builder<Location, String>()
          .put(Location.AR_ALEX_SAFE, "executive/arboretum;Data.Keycard_AlexRoomKey")
          .put(Location.AR_NPC_DAHL, "executive/arboretum;Data.Keycard8")
          .put(Location.AR_LOVERS_KEY, "executive/arboretum;Data.Keycard7")
          .put(Location.AR_NPC_KYRKOS, "executive/arboretum;Data.Keycard6")
          .put(Location.AR_NPC_WEST, "executive/arboretum;Data.Keycard1")
          .put(Location.BR_NPC_GREENE, "executive/bridge;Data.Keycard6")
          .put(Location.BR_NPC_MARKS, "executive/bridge;Data.Keycard2")
          .put(Location.BR_NPC_JANUARY, "executive/bridge;Data.Keycard1")
          .put(Location.BR_NPC_BOLIVAR, "executive/bridge;Data.Keycard5")
          .put(Location.CB_NPC_ELAZAR, "engineering/cargobay;Data.Keycard4")
          .put(Location.CB_NPC_COOL, "engineering/cargobay;Data.Keycard2")
          .put(Location.CQ_ALEX_CABIN, "executive/crewfacilities;Data.Keycard_AlexEscapePodKey")
          .put(Location.CQ_FATAL_FORTRESS, "executive/crewfacilities;SamsKeycard")
          .put(Location.CQ_NPC_LUKA, "executive/crewfacilities;Data.Keycard_KitchenKey")
          .put(Location.CQ_NPC_LUKA_QUEST, "executive/crewfacilities;Data.Keycard_MitchellCabin2")
          .put(Location.CQ_NPC_SELLERS, "executive/crewfacilities;Data.Keycard_Pharmacy")
          .put(Location.CQ_NPC_TIZZY, "executive/crewfacilities;Data.Keycard1")
          .put(Location.DS_DANIELLES_OFFICE, "executive/corporateit;Data.Keycard2")
          .put(Location.GT_NPC_KLINE, "research/zerog_utilitytunnels;Data.Keycard3")
          .put(Location.GT_NPC_LAVALLEY, "research/zerog_utilitytunnels;Data.Keycard2")
          .put(Location.GT_NPC_DALTON, "research/zerog_utilitytunnels;Data.Keycard4")
          .put(Location.HL_NPC_SCHMIDT, "research/prototype;Data.Keycard6")
          .put(Location.HL_CALVINOS_SAFE, "research/prototype;Data.Keycard1")
          .put(Location.HL_NPC_CALVINO, "research/prototype;Data.Keycard3")
          .put(Location.HL_NPC_LARSON, "research/prototype;HL_EntryKey")
          .put(Location.HL_NPC_THORSTEIN, "research/prototype;Data.Keycard4")
          .put(Location.LO_NPC_DEVRIES, "research/lobby;Data.Keycard3")
          .put(Location.LO_IT, "research/lobby;Data.Keycard4")
          .put(Location.LO_NPC_JANUARY_GIVEN, "research/lobby;LobbyKeyCardTwo")
          .put(Location.LO_NPC_JANUARY, "research/lobby;PsychotronicsCard1")
          .put(Location.LO_MORGAN_OFFICE_DESK, "research/lobby;Data.Keycard1")
          .put(Location.LO_MORGAN_OFFICE_WORKSHOP, "research/lobby;Data.Keycard2")
          .put(Location.LO_GOODWIN_OFFICE, "research/lobby;Data.Keycard6")
          .put(Location.LO_RECEPTION, "research/lobby;Data.Keycard5")
          .put(Location.LS_NPC_WEBER, "engineering/lifesupport;Data.Keycard1")
          .put(Location.LS_NPC_DAHL, "engineering/lifesupport;Data.Keycard3")
          .put(Location.LS_NPC_FOLSON, "engineering/lifesupport;Data.Keycard2")
          .put(Location.ND_BELLAMY_DESK, "research/simulationlabs;Data.Keycard1")
          .put(Location.ND_NPC_DAHL, "research/simulationlabs;Data.Keycard3")
          .put(Location.ND_NPC_STEELE, "research/simulationlabs;Data.Keycard5")
          .put(Location.PP_NPC_CROAL, "engineering/powersource;Data.Keycard5")
          .put(Location.PP_NPC_FAURE, "engineering/powersource;Data.Keycard4")
          .put(Location.PP_NPC_STILLWATER, "engineering/powersource;Data.Keycard2")
          .put(Location.PP_NPC_BROOKS, "engineering/powersource;Data.Keycard3")
          .put(Location.PY_NPC_KELSTRUP, "research/psychotronics;Data.Keycard5")
          .put(Location.PY_NPC_KELSTRUP_2, "research/psychotronics;Data.Keycard6")
          .put(Location.PY_MORGUE, "research/psychotronics;Data.Keycard2")
          .put(Location.SB_NPC_DAHL, "research/shuttlebay;Data.Keycard4")
          .put(Location.SB_PILOT_LOUNGE, "research/shuttlebay;Data.Keycard3")
          .put(Location.SB_NPC_PARKER, "research/shuttlebay;Data.Keycard1")
          .build();
}
