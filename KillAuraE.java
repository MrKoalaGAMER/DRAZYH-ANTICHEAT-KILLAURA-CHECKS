@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "E",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   disableHigherVersions = true,
   description = "Invalid sword blocking"
)
public class KillAuraE extends PacketCheck {
   private boolean place = false;

   public void handle(VPacket vPacket, long n) {
      if (vPacket instanceof VPacketPlayInFlying) {
         this.place = false;
      } else if (vPacket instanceof VPacketPlayInBlockPlace) {
         this.place = true;
      } else if (vPacket instanceof VPacketPlayInUseEntity && this.place && ((VPacketPlayInUseEntity)vPacket).getAction() == VPacketPlayInUseEntity.EntityUseAction.ATTACK) {
         this.handleViolation("", 1.0D, true);
      }

   }
}