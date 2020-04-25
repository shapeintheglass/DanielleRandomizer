
<CharacterDefinition>
 <Model File="objects/characters/humans/genmale/genmale.chr" Material="Objects/characters/Humans/Scientist/Scientist_GenMaleBody01.mtl"/>
 <AdditionalChrparams File="Animations/Characters/Humans/Dialogue/BDDB548E54DBAF56/BDDB548E54DBAF56.chrparams"/>
 <AttachmentList>
  <Attachment Inheritable="1" Type="CA_CDF" AName="Inherit_CDF" CDF="objects/characters/humans/genmale/genmale_attachments.cdf" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="body_skin" Binding="Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.skin" Material="Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.mtl" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="head_skin" Binding="Objects/characters/Humans/GenMale/GenMale_Head03.skin" Material="Objects/characters/Humans/GenMale/GenMale_Head03.mtl" SkinJointsOverrideSkeleton="1" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_SKIN" AName="legs" Binding="objects/characters/humans/labcoat/labcoat_genmalelegs01.skin" Material="objects/characters/humans/scientist/scientist_genmalebody01.mtl" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_SKIN" AName="hands" Binding="objects/characters/humans/labcoat/labcoat_genmalehands01.skin" Material="objects/characters/humans/scientist/scientist_genmalebody01.mtl" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="r_leg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="r_upperLegTwist_jnt" SA_SpringType="4" SA_Radius="5" SA_Redirect="1" SA_Damping="5" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="l_leg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="l_upperLegTwist_jnt" SA_SpringType="4" SA_Radius="5" SA_Redirect="1" SA_Damping="5" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="r_UpperLeg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="r_upperLegTop_jnt" SA_SpringType="4" SA_FPS="30" SA_Radius="1" SA_Redirect="1" SA_Damping="20" SA_Stiffness="1" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="l_UpperLeg_sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="l_upperLegTop_jnt" SA_SpringType="4" SA_FPS="30" SA_Radius="1" SA_Redirect="1" SA_Damping="20" SA_Stiffness="1" Flags="0"/>
 </AttachmentList>
</CharacterDefinition>