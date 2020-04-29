package tests.utils;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import utils.XmlEntity;

public class XmlEntityTest {

  private static String ENTITY_EMPTY = "<Empty />";

  private static String ENTITY_SIMPLE = "<One Two=\"Three\"/>";
  private static String ENTITY_SIMPLE_UPDATED = "<One Two=\"Four\"/>";

  private static String ENTITY_SIMPLE_MULTILINE = "<One Two=\"Three\">\n"
      + " <Four Five=\"Six\"/>\n" + " <Seven Eight=\"Nine\"/>\n"
      + " <Ten Eleven=\"Twelve\"/>\n" + " <Thirteen Fourteen=\"Fifteen\"/>\n"
      + " <Sixteen Seventeen=\"Eighteen\"/>\n" + "</One>";

  private static String ENTITY_SIMPLE_NESTED = "   <One>\n" + "    <Two />\n"
      + "   </One>";

  private static String ENTITY_SIMPLE_NESTED_WITH_KEYS = "   <One>\n"
      + "    <Two Three=\"Four\"/>\n" + "   </One>";
  private static String ENTITY_SIMPLE_NESTED_WITH_KEYS_UPDATED = "   <One>\n"
      + "    <Two Three=\"Five\"/>\n" + "   </One>";

  private static String ENTITY_PROTOTYPE_EXAMPLE = " <EntityPrototype Name=\"Cystoids.Cystoid\" Id=\"{F74EFBB4-E12D-44A5-B4D3-22956A7C7B04}\" Library=\"ArkNpcs\" Class=\"ArkAlienJelly\" Description=\"\" ArchetypeId=\"719\">\n"
      + "  <Properties faction_ArkFaction=\"17591291352619661944\" metaTags_ArkMetaTags=\"9469288860498988665,3149325216945014945,17591291352642463355\" signalGroup_Damage=\"3149325216955293382\" signalpackage_DamagePackage=\"3149325216913727345\" fEnergizedAttackProximity=\"2\" signalpackage_EnergizedDamagePackage=\"3149325216913727345\" nEnergizeDuration=\"5000\" nExplodeDelay=\"750\" fHealth=\"100\" signalmodifier_InboundModifier=\"3149325216937656319\" nInvalidTargetDelay=\"2000\" nLureDelay=\"500\" signal_LureSignal=\"10641886185830929654\" fMaxMovementPeriod=\"5\" fMinMovementPeriod=\"2\" object_Model=\"objects/characters/aliens/cystoid/cystoid.cdf\" signal_NullWave=\"3149325216915549045\" fNullWaveDeaccumulation=\"0.5\" scanningDisplayName=\"@loc_15373294129887561160\" nStunDuration=\"500\" metaTags_TagsToIgnore=\"9469288860498988665,3149325216945013533\" nTargetRefreshDelay=\"5000\" signalGroup_Trigger=\"3149325216963999229\">\n"
      + "   <Effects particleeffect_Active=\"Characters.Aliens.Cystoid.Body.BodyActive_00\" particleeffect_Dormant=\"Characters.Aliens.Cystoid.Body.BodyDormant_00\" particleeffect_Energized=\"Characters.Aliens.Cystoid.Body.BodyEnergized\" particleeffect_Explode=\"Characters.Aliens.Cystoid.Explosion.Explosion\" particleeffect_Proximity=\"Characters.Aliens.Cystoid.Body.ProximityGlow\" particleeffect_SpawnedByWeaver=\"Characters.Aliens.Weaver.ExcreteCystoid.ExcreteCystoid\"/>\n"
      + "   <EnergizeSignals signal_Laser=\"3149325216915549079\"/>\n"
      + "   <OneG fAttackProximity=\"4\" cameraShake_CameraShake=\"10739735956147043737\" fCystoidSpeedThreshold=\"1.5\" fDamageRange=\"0.1\" fDamageRangeOuter=\"4\" fDeactivationProximity=\"15\" fDeceleration=\"3.5\" fExplosionImpulseScale=\"75\" fFlockingTetherRange=\"10\" fHomingProximity=\"12\" fInnerImpulseRange=\"1\" fMaxSpeed=\"2.2\" nMaxTimeOffGround=\"125\" fMinSpeed=\"2\" fOuterImpulseRange=\"5\" fPlayerSpeedThreshold=\"2\" fRandomImpulseMagnitude=\"2\" nRandomImpulseMaxDelay=\"5000\" nRandomImpulseMinDelay=\"2000\" fTargetSpeedThreshold=\"2\"/>\n"
      + "   <Physics signalpackage_collisionDamagePackage=\"3149325216948552760\" Density=\"-1\" Mass=\"1\" bPhysicalize=\"1\" bPushableByAI=\"1\" bPushableByPlayers=\"1\" bResting=\"1\" bRigidBody=\"1\" bRigidBodyActive=\"1\">\n"
      + "    <CollisionFiltering>\n"
      + "     <collisionIgnore bT_collision_class_articulated=\"0\" bT_collision_class_living=\"0\" bT_collision_class_particle=\"0\" bT_collision_class_soft=\"0\" bT_collision_class_terrain=\"0\" bT_collision_class_wheeled=\"0\" bT_gcc_ai=\"0\" bT_gcc_breakable=\"0\" bT_gcc_coral=\"0\" bT_gcc_goo=\"0\" bT_gcc_permeable=\"0\" bT_gcc_permeable_to_AOE=\"0\" bT_gcc_player_body=\"0\" bT_gcc_player_capsule=\"0\" bT_gcc_projectile=\"0\" bT_gcc_ragdoll=\"0\" bT_gcc_rigid=\"0\" bT_gcc_smokeform=\"0\" bT_gcc_transparent=\"0\" bT_gcc_unclimbable=\"0\" bT_gcc_vehicle=\"0\"/>\n"
      + "     <collisionType bT_collision_class_articulated=\"0\" bT_collision_class_living=\"0\" bT_collision_class_particle=\"0\" bT_collision_class_soft=\"0\" bT_collision_class_terrain=\"0\" bT_collision_class_wheeled=\"0\" bT_gcc_ai=\"1\" bT_gcc_breakable=\"0\" bT_gcc_coral=\"0\" bT_gcc_goo=\"0\" bT_gcc_permeable=\"0\" bT_gcc_permeable_to_AOE=\"1\" bT_gcc_player_body=\"0\" bT_gcc_player_capsule=\"0\" bT_gcc_projectile=\"0\" bT_gcc_ragdoll=\"0\" bT_gcc_rigid=\"0\" bT_gcc_smokeform=\"0\" bT_gcc_transparent=\"0\" bT_gcc_unclimbable=\"0\" bT_gcc_vehicle=\"0\"/>\n"
      + "    </CollisionFiltering>\n"
      + "   </Physics>\n"
      + "   <RecycleData exotic=\"5\" mineral=\"0\" organic=\"0\" synthetic=\"0\"/>\n"
      + "   <Sound audioTrigger_AttackStart=\"\" audioTrigger_AttackStop=\"\" audioTrigger_DamagePlayer=\"Play_Cystoid_PlayerDamage\" audioTrigger_ExistStart=\"Play_Cystoid_Idle_Vox\" audioTrigger_ExistStop=\"Stop_Cystoid_Idle_Vox\" audioTrigger_Explode=\"Play_Cystoid_Explode\" audioTrigger_FollowStart=\"Play_Cystoid_FollowPlayer\" audioTrigger_FollowStop=\"Stop_Cystoid_FollowPlayer\"/>\n"
      + "   <StunSignals signal_Explosion=\"3149325216913726733\" signal_Impact=\"3149325216915554009\" signal_RecycleGrenade=\"10641886185820022055\"/>\n"
      + "   <ZeroG fAttackProximity=\"1.5\" cameraShake_CameraShake=\"10739735956147043737\" fCystoidSpeedThreshold=\"0.3\" fDamageRange=\"0.1\" fDamageRangeOuter=\"4\" fDeactivationProximity=\"25\" fDeceleration=\"3.5\" fExplosionImpulseScale=\"10\" fFlockingTetherRange=\"10\" fHomingProximity=\"8\" fInnerImpulseRange=\"1\" fMaxSpeed=\"2.5\" fMinSpeed=\"1.5\" fOuterImpulseRange=\"4\" fPlayerSpeedThreshold=\"1.6\" fRandomImpulseMagnitude=\"0.467\" nRandomImpulseMaxDelay=\"500\" nRandomImpulseMinDelay=\"250\" fTargetSpeedThreshold=\"1.5\"/>\n"
      + "   <AdditionalArchetypeProperties PrototypeMaterial=\"\"/>\n"
      + "  </Properties>\n"
      + "  <ObjectVars OutdoorOnly=\"0\" CastShadow=\"1\" CastShadowMinspec=\"1\" DynamicDistanceShadows=\"0\" ShadowCasterType=\"4\" CastSunShadowMinSpec=\"1\" LodRatio=\"100\" ViewDistRatio=\"255\" CastShadowViewDistRatio=\"20\" HiddenInGame=\"0\" GlobalInSegmentedWorld=\"0\" RecvWind=\"0\" RenderNearest=\"0\" NoStaticDecals=\"1\" CreatedThroughPool=\"0\" SceneIndex=\"0\" HiddenInProbes=\"1\"/>\n"
      + " </EntityPrototype>";

  private static String ENTITY_INVOCATION_EXAMPLE = "  <Entity Name=\"ArkNpcSpawner14\" Pos=\"301.65375,708.33429,495.19998\" Rotate=\"4.3711388e-08,0,0,1\" EntityClass=\"ArkNpcSpawner\" EntityId=\"1353\" EntityGuid=\"4642C5D512522300\" CastShadowViewDistRatio=\"30\" CastShadowMinSpec=\"1\" CastSunShadowMinSpec=\"1\" ShadowCasterType=\"4\" Layer=\"Lobby_PsychCodeQuest\" HiddenInNodes=\"1\">\n"
      + "   <Properties sNpcArchetype=\"ArkNpcs.Mimics.Mimic\" pose_PoseAnim=\"\" bRigorMortis=\"0\" bSpawnAlwaysUpdate=\"0\" bSpawnBroken=\"0\" bSpawnCorpse=\"0\" bSpawnDormant=\"0\" bSpawnOnGameStart=\"0\"/>\n"
      + "   <Properties2 roomContainer_wanderRoomsContainer=\"4953167328235826769\">\n"
      + "    <ArkDialogOverride fPlayerApproachCDFar=\"-1\" fPlayerApproachCDMedium=\"-1\" fPlayerApproachCDNear=\"-1\" fPlayerApproachDistanceFar=\"-1\" fPlayerApproachDistanceMedium=\"-1\" fPlayerApproachDistanceNear=\"-1\" fPlayerLoiterCD=\"-1\" fPlayerLoiterDistance=\"-1\"/>\n"
      + "   </Properties2>\n" + "   <RenderProxy />\n" + "  </Entity>";

  private static String ENTITY_FLOWGRAPH_EXAMPLE = "  <Entity Name=\"AudioTriggerSpot1\" Pos=\"32.45591,28.016764,15.435433\" EntityClass=\"AudioTriggerSpot\" EntityId=\"51\" EntityGuid=\"469D818BDA436D19\" CastShadowViewDistRatio=\"0\" CastShadowMinSpec=\"1\" CastSunShadowMinSpec=\"8\" ShadowCasterType=\"0\" Layer=\"Audio\">\n"
      + "   <Properties bEnabled=\"1\" fMaxDelay=\"2\" fMinDelay=\"1\" bPlayOnX=\"0\" bPlayOnY=\"0\" bPlayOnZ=\"0\" bPlayRandom=\"0\" audioTriggerPlayTriggerName=\"\" fRadiusRandom=\"10\" bSerializePlayState=\"1\" eiSoundObstructionType=\"1\" audioTriggerStopTriggerName=\"\"/>\n"
      + "   <FlowGraph Description=\"\" Group=\"EndGameConvos\" enabled=\"1\" MultiPlayer=\"ClientServer\">\n"
      + "    <Nodes>\n"
      + "     <Node Id=\"2\" Class=\"entity:AreaTrigger\" pos=\"-494.51129,-94.38253,0\" EntityGUID=\"{3729C7A2-BAB8-4A1F-86A5-189863728FBF}\" EntityGUID_64=\"4A1FBAB83729C7A2\">\n"
      + "      <Inputs entityId=\"0\" Disable=\"0\" Enable=\"0\" Enter=\"0\" Leave=\"0\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"3\" Class=\"Audio:Trigger\" pos=\"-150,-120,0\" GraphEntity=\"0\">\n"
      + "      <Inputs entityId=\"0\" audioTrigger_PlayTrigger=\"Play_OBJ_Tech_Console_11_LP\" audioTrigger_StopTrigger=\"Stop_OBJ_Tech_Console_11_LP\" ForceStopOnTriggerChange=\"1\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"4\" Class=\"Audio:Trigger\" pos=\"-150,50,0\" EntityGUID=\"{B5E0769F-84CD-4042-873A-B0BCB9D31E27}\" EntityGUID_64=\"404284CDB5E0769F\">\n"
      + "      <Inputs entityId=\"0\" audioTrigger_PlayTrigger=\"Play_OBJ_Tech_Console_06_LP\" audioTrigger_StopTrigger=\"Stop_OBJ_Tech_Console_06_LP\" ForceStopOnTriggerChange=\"1\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"5\" Class=\"Audio:Trigger\" pos=\"-150,210,0\" EntityGUID=\"{5D61F63D-9AF8-473B-ACF1-5FACE2F0BB4C}\" EntityGUID_64=\"473B9AF85D61F63D\">\n"
      + "      <Inputs entityId=\"0\" audioTrigger_PlayTrigger=\"Play_OBJ_Hum_General_05_LP\" audioTrigger_StopTrigger=\"Stop_OBJ_Hum_General_05_LP\" ForceStopOnTriggerChange=\"1\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"6\" Class=\"Audio:Trigger\" pos=\"-150,-290,0\" EntityGUID=\"{4B04B482-BE17-43E1-9D4F-BDDDFE395741}\" EntityGUID_64=\"43E1BE174B04B482\">\n"
      + "      <Inputs entityId=\"0\" audioTrigger_PlayTrigger=\"Play_OBJ_Display_Tone_01_LP\" audioTrigger_StopTrigger=\"Stop_OBJ_Display_Tone_01_LP\" ForceStopOnTriggerChange=\"1\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"7\" Name=\"3D_ENV_Emmiters\" Class=\"_commentbox\" pos=\"-520,-364,0\">\n"
      + "      <Inputs TextSize=\"1\" Color=\"0.27451,0.352941,0.705882\" DisplayFilled=\"1\" DisplayBox=\"1\" SortPriority=\"16\"/>\n"
      + "      <CommentBox Width=\"760\" Height=\"780\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"10\" Class=\"Game:Start\" pos=\"-1180,-780,0\">\n"
      + "      <Inputs InGame=\"1\" InEditor=\"1\" InEditorPlayFromHere=\"1\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"12\" Class=\"Audio:Trigger\" pos=\"-460,-1090,0\" EntityGUID=\"{4B04B482-BE17-43E1-9D4F-BDDDFE395741}\" EntityGUID_64=\"43E1BE174B04B482\">\n"
      + "      <Inputs entityId=\"0\" audioTrigger_PlayTrigger=\"Play_MX_Core\" audioTrigger_StopTrigger=\"\" ForceStopOnTriggerChange=\"1\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"14\" Class=\"Time:Delay\" pos=\"-679.90985,-659.87982,0\">\n"
      + "      <Inputs delay=\"0.5\" resetOnInput=\"0\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"16\" Class=\"Audio:Trigger\" pos=\"-130,-860,0\" EntityGUID=\"{4B04B482-BE17-43E1-9D4F-BDDDFE395741}\" EntityGUID_64=\"43E1BE174B04B482\">\n"
      + "      <Inputs entityId=\"0\" audioTrigger_PlayTrigger=\"Play_Amb_2D_Main\" audioTrigger_StopTrigger=\"\" ForceStopOnTriggerChange=\"1\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"17\" Class=\"Audio:Switch\" pos=\"70,-660,0\">\n"
      + "      <Inputs entityId=\"0\" audioSwitch_SwitchName=\"MusicState\" audioSwitchState_SwitchStateName1=\"Unsettled\" audioSwitchState_SwitchStateName2=\"\" audioSwitchState_SwitchStateName3=\"\" audioSwitchState_SwitchStateName4=\"\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"18\" Class=\"Time:Delay\" pos=\"-132.44591,-669.89484,0\">\n"
      + "      <Inputs delay=\"7\" resetOnInput=\"0\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"19\" Class=\"Audio:Switch\" pos=\"-110,-1180,0\">\n"
      + "      <Inputs entityId=\"0\" audioSwitch_SwitchName=\"MusicState\" audioSwitchState_SwitchStateName1=\"Silence\" audioSwitchState_SwitchStateName2=\"\" audioSwitchState_SwitchStateName3=\"\" audioSwitchState_SwitchStateName4=\"\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"21\" Class=\"Time:Delay\" pos=\"-770,-520,0\">\n"
      + "      <Inputs delay=\"1\" resetOnInput=\"0\"/>\n"
      + "     </Node>\n"
      + "     <Node Id=\"22\" Class=\"Audio:Switch\" pos=\"-550,-540,0\">\n"
      + "      <Inputs entityId=\"0\" audioSwitch_SwitchName=\"Mix_States\" audioSwitchState_SwitchStateName1=\"EndGame\" audioSwitchState_SwitchStateName2=\"\" audioSwitchState_SwitchStateName3=\"\" audioSwitchState_SwitchStateName4=\"\"/>\n"
      + "     </Node>\n"
      + "    </Nodes>\n"
      + "    <Edges>\n"
      + "     <Edge nodeIn=\"3\" nodeOut=\"2\" portIn=\"Play\" portOut=\"Enter\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"3\" nodeOut=\"2\" portIn=\"Stop\" portOut=\"Leave\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"4\" nodeOut=\"2\" portIn=\"Play\" portOut=\"Enter\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"4\" nodeOut=\"2\" portIn=\"Stop\" portOut=\"Leave\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"5\" nodeOut=\"2\" portIn=\"Play\" portOut=\"Enter\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"5\" nodeOut=\"2\" portIn=\"Stop\" portOut=\"Leave\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"6\" nodeOut=\"2\" portIn=\"Play\" portOut=\"Enter\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"6\" nodeOut=\"2\" portIn=\"Stop\" portOut=\"Leave\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"18\" nodeOut=\"2\" portIn=\"in\" portOut=\"Enter\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"14\" nodeOut=\"10\" portIn=\"in\" portOut=\"LevelStateRestored\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"14\" nodeOut=\"10\" portIn=\"in\" portOut=\"output\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"21\" nodeOut=\"10\" portIn=\"in\" portOut=\"LevelStateRestored\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"21\" nodeOut=\"10\" portIn=\"in\" portOut=\"output\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"19\" nodeOut=\"12\" portIn=\"audioSwitchState_SetState1\" portOut=\"Out\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"12\" nodeOut=\"14\" portIn=\"Play\" portOut=\"out\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"16\" nodeOut=\"14\" portIn=\"Play\" portOut=\"out\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"17\" nodeOut=\"18\" portIn=\"audioSwitchState_SetState1\" portOut=\"out\" enabled=\"1\"/>\n"
      + "     <Edge nodeIn=\"22\" nodeOut=\"21\" portIn=\"audioSwitchState_SetState1\" portOut=\"out\" enabled=\"1\"/>\n"
      + "    </Edges>\n"
      + "    <GraphTokens />\n"
      + "   </FlowGraph>\n"
      + "  </Entity>";

  @Test
  public void testParseXml_empty() throws Exception {
    parseXmlHelper(ENTITY_EMPTY);
  }

  @Test
  public void testParseXml_simple() throws Exception {
    parseXmlHelper(ENTITY_SIMPLE);
  }

  @Test
  public void testParseXml_simpleNested() throws Exception {
    parseXmlHelper(ENTITY_SIMPLE_NESTED);
  }

  @Test
  public void testParseXml_multiline() throws Exception {
    parseXmlHelper(ENTITY_SIMPLE_MULTILINE);
  }

  @Test
  public void testParseXml_definition() throws Exception {
    parseXmlHelper(ENTITY_PROTOTYPE_EXAMPLE);
  }

  @Test
  public void testParseXml_invocation() throws Exception {
    parseXmlHelper(ENTITY_INVOCATION_EXAMPLE);
  }
  
  @Test
  public void testParseXml_flowgraph() throws Exception {
    parseXmlHelper(ENTITY_FLOWGRAPH_EXAMPLE);
  }

  @Test
  public void testSetKey() throws Exception {
    BufferedReader br = new BufferedReader(new CharArrayReader(
        ENTITY_SIMPLE.toCharArray()));
    XmlEntity x = new XmlEntity(br);
    x.setKey("Two", "Four");
    Assert.assertEquals(ENTITY_SIMPLE_UPDATED, x.toString());
  }

  @Test
  public void testSetKey_nested() throws Exception {
    BufferedReader br = new BufferedReader(new CharArrayReader(
        ENTITY_SIMPLE_NESTED_WITH_KEYS.toCharArray()));
    XmlEntity x = new XmlEntity(br);
    x.getSubEntityByTagName("Two").setKey("Three", "Five");
    Assert.assertEquals(ENTITY_SIMPLE_NESTED_WITH_KEYS_UPDATED, x.toString());
  }

  private void parseXmlHelper(String xml) throws IOException {
    BufferedReader br = new BufferedReader(new CharArrayReader(
        xml.toCharArray()));
    XmlEntity x = new XmlEntity(br);
    Assert.assertEquals(xml, x.toString());
  }
}
