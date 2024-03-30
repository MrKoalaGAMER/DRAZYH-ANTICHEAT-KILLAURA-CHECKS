@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "F",
   friendlyName = "AutoClicker",
   version = CheckVersion.RELEASE,
   maxViolations = 10,
   disableHigherVersions = true,
   description = "Invalid sword blocking"
)
public class KillAuraF extends PacketCheck {
   private boolean dig;
   private boolean place;

   public void handle(VPacket vPacket, long n) {
      if (vPacket instanceof VPacketPlayInFlying) {
         this.dig = false;
         this.place = false;
      } else if (vPacket instanceof VPacketPlayInBlockDig) {
         VPacketPlayInBlockDig vPacketPlayInBlockDig = (VPacketPlayInBlockDig)vPacket;
         if (vPacketPlayInBlockDig.getType() != VPacketPlayInBlockDig.PlayerDigType.DROP_ITEM && vPacketPlayInBlockDig.getType() != VPacketPlayInBlockDig.PlayerDigType.DROP_ALL_ITEMS) {
            this.dig = true;
         }
      } else if (vPacket instanceof VPacketPlayInUseEntity && !this.place && !this.playerData.isVehicle() && this.dig && ((VPacketPlayInUseEntity)vPacket).getAction() == VPacketPlayInUseEntity.EntityUseAction.ATTACK) {
         this.handleViolation("", 1.0D);
      } else if (vPacket instanceof VPacketPlayInBlockPlace) {
         this.place = true;
      }

   }
}