@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "D",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   minViolations = -1.0D,
   maxViolations = 5,
   logData = true,
   description = "Unlikely rotations"
)
public class KillAuraD extends PacketCheck {
   private final Queue<Double> pitchList = new ConcurrentLinkedQueue();
   private Float lastPitch = null;

   public void handle(VPacket vPacket, long n) {
      if (vPacket instanceof VPacketPlayInFlying) {
         VPacketPlayInFlying vPacketPlayInFlying = (VPacketPlayInFlying)vPacket;
         if (vPacketPlayInFlying.isLook()) {
            if (this.lastPitch != null && this.playerData.getLastAttackTicks() <= 3 && this.playerData.getLastAttacked() != null) {
               this.pitchList.add((double)Math.abs(vPacketPlayInFlying.getPitch() - this.lastPitch));
               if (this.pitchList.size() >= 20) {
                  double deviation = MathUtil.deviation(this.pitchList);
                  double average = MathUtil.average((Iterable)this.pitchList);
                  if ((!(average > 17.5D) || !(deviation > 15.0D)) && (!(average > 22.5D) || !(deviation > 12.5D))) {
                     this.decreaseVL(0.025D);
                  } else {
                     this.handleViolation(String.format("AVG: %s DEV: %s", average, deviation));
                  }

                  this.pitchList.clear();
               }
            }

            this.lastPitch = vPacketPlayInFlying.getPitch();
         }
      }

   }
}