
<CharacterDefinition>
 <Model File="objects/characters/humans/genmale/genmale.chr" Material="Objects/characters/Humans/Corporate/Corporate_GenMaleBody01.mtl"/>
 <AdditionalChrparams File="Animations/Characters/Humans/Dialogue/BDDB548E5550F706/BDDB548E5550F706.chrparams"/>
 <AttachmentList>
  <Attachment Inheritable="1" Type="CA_CDF" AName="Inherit_CDF" CDF="objects/characters/humans/genmale/genmale_attachments.cdf" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="body_skin" Binding="Objects/characters/Humans/Dahl/Dahl_GenMaleBody01.skin" Material="Objects/characters/Humans/Dahl/Dahl_GenMaleBody01.mtl" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="head_skin" Binding="Objects/characters/Humans/Dahl/Dahl_GenMaleHead01.skin" Material="Objects/characters/Humans/Dahl/Dahl_GenMaleHead01.mtl" SkinJointsOverrideSkeleton="1" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_SKIN" AName="hair_skin" Binding="Objects/characters/Humans/Dahl/Dahl_GenMaleHead01_Hair01.skin" Material="Objects/characters/Humans/Dahl/Dahl_GenMaleHead01_Hair01.mtl" Flags="0"/>
  <Attachment Inheritable="1" Type="CA_BONE" AName="FX_hand_lt" RelRotation="1,0,0,0" RelPosition="0.034061432,2.9802322e-008,-2.9802322e-008" BoneName="l_handProp_jnt" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="Backpack_Sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="backpack_jnt" PA_PendulumType="1" PA_MaxAngle="20" PA_Redirect="1" PA_Mass="10" PA_Damping="10" PA_SimulationAxis="0.97042608,-1,-0.75462341" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="r_legPouchA_Sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="r_legPouchA_jnt" PA_PendulumType="3" PA_MaxAngle="13.827585" PA_HRotation="63.665024" PA_Redirect="1" PA_Mass="5" PA_Damping="10" PA_SimulationAxis="0,-1,0.067877047" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="l_legPouchA_Sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="l_legPouchA_jnt" PA_PendulumType="3" PA_MaxAngle="27.054186" PA_HRotation="90.192108" PA_Redirect="1" PA_Mass="5" PA_Damping="10" PA_SimulationAxis="0,-1,0" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="l_legPouchB_Sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="l_legPouchB_jnt" PA_PendulumType="3" PA_MaxAngle="27.054186" PA_HRotation="90.192108" PA_Redirect="1" PA_Mass="5" PA_Damping="10" PA_SimulationAxis="0,-1,0" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="spine2Pack_Sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="spine2Pack_jnt" SA_SpringType="4" SA_Radius="1" SA_Redirect="1" SA_Mass="5" SA_Damping="50" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_BONE" AName="backpackFlap_Sim" RelRotation="1,0,0,0" RelPosition="0,0,0" BoneName="backpackFlap_jnt" PA_PendulumType="3" PA_MaxAngle="10" PA_HRotation="70.738914" PA_Redirect="1" PA_Damping="10" PA_SimulationAxis="0,-1,0" Flags="0"/>
  <Attachment Inheritable="0" Type="CA_SKIN" AName="backpack_skin" Binding="objects/characters/humans/dahl/dahl_genmalebackpack01.skin" Material="objects/characters/humans/dahl/dahl_genmalebody01.mtl" Flags="0"/>
 </AttachmentList>
 <Modifiers>
  <Element>
   <enabled value="true"/>
   <guidHiPart value="9D07DEEB5408413D"/>
   <guidLoPart value="AD471FABC571F964"/>
   <draw value="false"/>
   <version value="1"/>
   <drivenNode>
    <name value="l_legPouchB_jnt"/>
    <crc32 value="2864734968"/>
   </drivenNode>
   <aimVector>
    <Element value="-1.7881393e-007"/>
    <Element value="-0.99999988"/>
    <Element value="-2.9802314e-008"/>
   </aimVector>
   <upVector>
    <Element value="0.99999988"/>
    <Element value="-1.7881393e-007"/>
    <Element value="5.3290705e-015"/>
   </upVector>
   <targetNode>
    <name value="l_lowerLeg_jnt"/>
    <crc32 value="504150844"/>
   </targetNode>
   <targetOffset>
    <Element value="0"/>
    <Element value="0"/>
    <Element value="0"/>
   </targetOffset>
   <upNode>
    <name value="l_lowerLeg_jnt"/>
    <crc32 value="504150844"/>
   </upNode>
   <upOffset>
    <Element value="0"/>
    <Element value="0.1"/>
    <Element value="-0.343036"/>
   </upOffset>
   <weightNode>
    <name value=""/>
    <crc32 value="0"/>
   </weightNode>
   <weight value="1"/>
  </Element>
  <Element>
   <enabled value="true"/>
   <guidHiPart value="9D07DEEB5408413D"/>
   <guidLoPart value="AD471FABC571F964"/>
   <draw value="false"/>
   <version value="1"/>
   <drivenNode>
    <name value="l_legPouchA_jnt"/>
    <crc32 value="3982497832"/>
   </drivenNode>
   <aimVector>
    <Element value="-1.7881393e-007"/>
    <Element value="-0.99999994"/>
    <Element value="-0"/>
   </aimVector>
   <upVector>
    <Element value="0.99999994"/>
    <Element value="-1.7881393e-007"/>
    <Element value="0"/>
   </upVector>
   <targetNode>
    <name value="l_lowerLeg_jnt"/>
    <crc32 value="504150844"/>
   </targetNode>
   <targetOffset>
    <Element value="-0.031516861"/>
    <Element value="0.19323821"/>
    <Element value="0.067460261"/>
   </targetOffset>
   <upNode>
    <name value="l_lowerLeg_jnt"/>
    <crc32 value="504150844"/>
   </upNode>
   <upOffset>
    <Element value="-0.083455704"/>
    <Element value="0.19187753"/>
    <Element value="0.042227406"/>
   </upOffset>
   <weightNode>
    <name value=""/>
    <crc32 value="0"/>
   </weightNode>
   <weight value="1"/>
  </Element>
  <Element>
   <enabled value="true"/>
   <guidHiPart value="9D07DEEB5408413D"/>
   <guidLoPart value="AD471FABC571F964"/>
   <draw value="false"/>
   <version value="1"/>
   <drivenNode>
    <name value="r_legPouchA_jnt"/>
    <crc32 value="1148296978"/>
   </drivenNode>
   <aimVector>
    <Element value="-3.5762787e-007"/>
    <Element value="-0.99999988"/>
    <Element value="2.9802319e-008"/>
   </aimVector>
   <upVector>
    <Element value="0.99999988"/>
    <Element value="-3.5762787e-007"/>
    <Element value="0"/>
   </upVector>
   <targetNode>
    <name value="r_lowerLeg_jnt"/>
    <crc32 value="3005600464"/>
   </targetNode>
   <targetOffset>
    <Element value="-1.5258789e-005"/>
    <Element value="0.077087402"/>
    <Element value="0.087112427"/>
   </targetOffset>
   <upNode>
    <name value="r_upperLeg_jnt"/>
    <crc32 value="4249235145"/>
   </upNode>
   <upOffset>
    <Element value="0.014087677"/>
    <Element value="0.21355438"/>
    <Element value="0.18483806"/>
   </upOffset>
   <weightNode>
    <name value=""/>
    <crc32 value="0"/>
   </weightNode>
   <weight value="1"/>
  </Element>
 </Modifiers>
</CharacterDefinition>