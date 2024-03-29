package utils;

public class ProgressionConsts {
  public interface ProgressionNode {}
  
  // Non-keycard type progression milestones
  public enum ProgressionMilestone implements ProgressionNode {
    ND_START,
    ND_KASPAR_NEUTRALIZED,
    LO_LG_VIDEO_PART_1,
    LO_LG_VIDEO_PART_2,
    LO_TRAUMA_CENTER_RECORDING_20_PERCENT,
    LO_UNLOCKED_LIFT,
    LO_NEUROMOD_DIVISION_PASSWORD,
    HL_VISITED_HARDWARE_LABS,
    HL_ARTAX_JETPACK,
    HL_FIXED_LG_VIDEO,
    HL_KASPAR_NEUTRALIZED,
    HL_AIRLOCK,
    EX_KASPAR_NEUTRALIZED,
    EX_CORAL_SCANS_COMPLETED,
    PY_PSYCHOSCOPE,
    PY_KASPAR_NEUTRALIZED,
    PY_NULLWAVE_PRIMED,
    PY_AIRLOCK,
    SB_KASPAR_IN_ND,
    SB_KASPAR_IN_HL,
    SB_KASPAR_IN_EX,
    SB_KASPAR_IN_PY,
    SB_AIRLOCK,
    AR_WATER_PRESSURE_REGULATOR,
    AR_ALEX_LG_VIDEO,
    AR_CORAL_SCAN_CHIPSET,
    AR_DAHL_ARRIVAL,
    AR_ALEX_ARMING_KEY,
    AR_PROTOTYPE_NULLWAVE,
    AR_APEX_IS_HERE,
    AR_AIRLOCK,
    CQ_CAFETERIA_TELEPATH_NEUTRALIZED,
    CQ_MITCHELLS_AWARD,
    CQ_KITCHEN_ACCESS,
    CQ_FREEZER_ACCESS,
    CQ_RECORDINGS_90_PERCENT,
    CQ_MITCHELL_CABIN_RECORDING_10_PERCENT,
    CQ_ABIGAIL_CABIN_RECORDING_10_PERCENT,
    CQ_FREEZER_RECORDING_20_PERCENT,
    CQ_VOICE_OVERRIDE,
    DS_DIRECTOR_LOCKDOWN,
    DS_MORGANS_ARMING_KEY,
    LS_WATER_TREATMENT_TECHNOPATH,
    PP_REBOOT_COMPLETED,
    PP_ARMING_KEYS_PRIMED,
    PP_AIRLOCK,
    BR_GOAL,
  }

}
