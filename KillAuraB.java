@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "B",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   minViolations = -1.0D,
   maxViolations = 4,
   logData = true,
   description = ""
)
public class KillAuraB extends PacketCheck {
   private int invalidTicks;
   private int lastTicks;
   private int totalTicks;
   private int ticks;

   public void handle(VPacket vPacket, long n) {
      if (vPacket instanceof VPacketPlayInFlying) {
         ++this.ticks;
      } else if (vPacket instanceof VPacketPlayInUseEntity && ((VPacketPlayInUseEntity)vPacket).getAction().isAttack()) {
         if (this.ticks <= 8) {
            if (this.lastTicks == this.ticks) {
               ++this.invalidTicks;
            }

            if (this.totalTicks++ >= 25) {
               if (this.invalidTicks > 22) {
                  this.handleViolation(String.format("%s/%s", this.invalidTicks, this.totalTicks), 1.0D + (double)(this.invalidTicks - 22) / 6.0D);
               } else {
                  this.violations -= Math.min(this.violations + 1.0D, 1.0D);
               }

               this.totalTicks = 0;
               this.invalidTicks = 0;
            }

            this.lastTicks = this.ticks;
         }

         this.ticks = 0;
      }

   }
}