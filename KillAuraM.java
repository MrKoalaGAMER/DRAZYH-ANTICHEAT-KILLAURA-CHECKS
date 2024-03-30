@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "M",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   description = "Invalid block place",
   unsupportedServers = {ServerVersion.v1_11_R1, ServerVersion.v1_12_R1}
)
public class KillAuraM extends PacketCheck {
   private boolean place = false;
   private boolean attack = false;

   public void handle(VPacket vPacket, long n) {
      if (vPacket instanceof VPacketPlayInFlying) {
         if (this.place && this.attack) {
            this.handleViolation("", 1.0D, true);
         }

         this.place = false;
         this.attack = false;
      } else if (vPacket instanceof VPacketPlayInUseEntity) {
         if (((VPacketPlayInUseEntity)vPacket).getAction() == VPacketPlayInUseEntity.EntityUseAction.ATTACK) {
            this.attack = true;
         }
      } else if (vPacket instanceof VPacketPlayInBlockPlace) {
         BlockPosition position = ((VPacketPlayInBlockPlace)vPacket).getPosition();
         if (position.getX() != -1 && position.getY() != -1 && position.getZ() != -1) {
            this.place = true;
         }
      }

   }
}