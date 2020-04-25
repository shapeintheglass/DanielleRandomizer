
<CharacterDefinition>
 <Model File="Objects/characters/Humans/GenFemale/GenFemale.chr" Material="Objects/characters/Humans/Security/Security_GenFemaleBody01.mtl"/>
 <AdditionalChrparams File="Animations/Characters/Humans/Dialogue/BDDB548E54DBAF6A/BDDB548E54DBAF6A.chrparams"/>
 <AttachmentList>
  <Attachment Inheritable="1" Type="CA_CDF" AName="Inherit_CDF" CDF="objects/characters/humans/genfemale/genfemale_attachments.cdf" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="body_skin" Binding="Objects/characters/Humans/Labcoat/Labcoat_GenFemaleBody01.skin" Material="Objects/characters/Humans/Labcoat/Labcoat_GenFemaleBody01.mtl" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="head_skin" Binding="Objects/characters/Humans/GenFemale/GenFemale_Head01.skin" Material="Objects/characters/Humans/GenFemale/GenFemale_Head01.mtl" SkinJointsOverrideSkeleton="1" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_SKIN" AName="legs_arms" Binding="objects/characters/humans/labcoat/labcoat_genfemalelegsarms01.skin" Material="objects/characters/humans/scientist/scientist_genfemalebody01.mtl" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="r_leg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="r_upperLegTwist_jnt" SA_SpringType="4" SA_FPS="30" SA_Radius="4" SA_Redirect="1" SA_Damping="0.5" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="l_leg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="l_upperLegTwist_jnt" SA_SpringType="4" SA_FPS="30" SA_Radius="5" SA_Redirect="1" SA_Damping="0.5" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="r_UpperLeg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="r_upperLegTop_jnt" SA_SpringType="4" SA_FPS="30" SA_Radius="1" SA_Redirect="1" SA_Damping="10" SA_Stiffness="1" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="l_UpperLeg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="l_upperLegTop_jnt" SA_SpringType="4" SA_FPS="30" SA_Radius="1" SA_Redirect="1" SA_Damping="10" SA_Stiffness="1" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="hair_skin" Binding="Objects/characters/Humans/GenFemale/GenFemale_Head01_Hair02.skin" Material="Objects/characters/Humans/GenFemale/GenFemale_Hair02_Black.mtl" Flags="0"/>
 </AttachmentList>
 <Modifiers>
  <Element>
   <enabled value="true"/>
   <guidHiPart value="9D07DEEB5408413D"/>
   <guidLoPart value="AD471FABC571F964"/>
   <draw value="false"/>
   <version value="1"/>
   <drivenNode>
    <name value="r_upperLegTop_jnt"/>
    <crc32 value="692443482"/>
   </drivenNode>
   <aimVector>
    <Element value="1"/>
    <Element value="3.6500072e-008"/>
    <Element value="1.7763527e-015"/>
   </aimVector>
   <upVector>
    <Element value="-3.6500072e-008"/>
    <Element value="1"/>
    <Element value="-4.2104551e-015"/>
   </upVector>
   <targetNode>
    <name value="r_lowerLeg_jnt"/>
    <crc32 value="3005600464"/>
   </targetNode>
   <targetOffset>
    <Element value="0"/>
    <Element value="0"/>
    <Element value="0"/>
   </targetOffset>
   <upNode>
    <name value="r_upperLeg_jnt"/>
    <crc32 value="4249235145"/>
   </upNode>
   <upOffset>
    <Element value="0"/>
    <Element value="0.40255958"/>
    <Element value="0"/>
   </upOffset>
   <weightNode>
    <name value=""/>
    <crc32 value="0"/>
   </weightNode>
   <weight value="1"/>
  </Element>
 </Modifiers>
</CharacterDefinition>