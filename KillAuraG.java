@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "G",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   minViolations = -3.0D,
   maxViolations = 20,
   logData = true,
)
public class KillAuraG extends ReachCheck {
   private int threshold = 0;

   public void handle(ReachData reachData, long n) {
      DistanceData distanceData = reachData.getDistanceData();
      double n2 = ((DistanceData)reachData.getDistanceList().stream().min(Comparator.comparingDouble(DistanceData::getExtra)).get()).getExtra() / distanceData.getHorizontal();
      double n3 = distanceData.getX() * n2;
      double n4 = distanceData.getZ() * n2;
      if (Math.max(Math.abs(n3), Math.abs(n4)) > 0.5D) {
         if (this.threshold++ > 2 + this.playerData.getPingTicks() / 3) {
            this.handleViolation(String.format("SPOOFED", n3, n4, distanceData.getExtra()), 1.0D, true);
         }
      } else {
         this.decreaseVL(0.2D);
         this.threshold = 0;
      }

   }
}