@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "I",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   description = ""
)
public class KillAuraI extends PacketCheck {
   private double lastYawDiff;
   private double lastDeltaXZ;
   private double threshold;
   private Location location;

   public void handle(VPacket packet, long p1) {
      if (packet instanceof VPacketPlayInUseEntity) {
         VPacketPlayInUseEntity useEntity = (VPacketPlayInUseEntity)packet;
         Location lastLocation = this.location;
         this.location = this.player.getLocation();
         if (lastLocation == null || this.location == null) {
            return;
         }

         if (useEntity.getAction().isAttack()) {
            double yaw = (double)(this.location.getYaw() - lastLocation.getYaw());
            double lastYaw = Math.abs(yaw - this.lastYawDiff);
            double deltaXZ = Math.abs(lastLocation.getX() - this.location.getX()) + Math.abs(lastLocation.getZ() - this.location.getZ());
            double difference = Math.abs(deltaXZ - this.lastDeltaXZ);
            if (difference > 0.0D && difference < 0.001D && lastYaw > 7.0D) {
               if (++this.threshold > 2.0D) {
                  this.handleViolation("threshold: " + this.threshold);
               }
            } else {
               this.threshold -= Math.min(this.threshold, 0.25D);
            }

            this.lastDeltaXZ = deltaXZ;
            this.lastYawDiff = yaw;
         }
      } else if (packet instanceof VPacketPlayInFlying && this.playerData.getLastAttackTicks() > 2) {
         this.threshold = 0.0D;
      }

   }
}