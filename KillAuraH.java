@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "H",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   minViolations = -1.0D,
   maxViolations = 5,
   description = "Post use entity packet"
)
public class KillAuraH extends PacketCheck {
   private boolean attack;

   public void handle(VPacket vPacket, long n) {
      if (vPacket instanceof VPacketPlayInFlying) {
         if (this.attack) {
            if (n - this.playerData.getLastLastFlying() > 40L) {
               this.handleViolation("", 0.5D);
            } else {
               this.decreaseVL(0.05D);
            }

            this.attack = false;
         }
      } else if (vPacket instanceof VPacketPlayInUseEntity && ((VPacketPlayInUseEntity)vPacket).getAction().isAttack()) {
         if (n - this.playerData.getLastFlying() < 5L) {
            this.attack = true;
         } else {
            this.decreaseVL(0.1D);
         }
      }

   }
}