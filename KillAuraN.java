@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "N",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   minViolations = -2.5D,
   maxViolations = 25,
   logData = true,
   disableHigherVersions = true,
   description = "Invalid sprint motion (keep sprint)"
)
public class KillAuraN extends MovementCheck {
   private static final double SPEED = 0.02D;
   private static final double NON_SPRINT = 0.2325D;
   private int threshold = 0;
   private boolean lastOnGround = false;
   private Double lastValue = null;

   public void handle(PlayerLocation playerLocation, PlayerLocation currentLocation, long currentTime) {
      if (playerLocation.getGround() && currentLocation.getGround() && this.lastOnGround && this.playerData.isSprinting(true) && this.player.getGameMode() != GameMode.CREATIVE) {
         double hypot = MathUtil.hypot(currentLocation.getX() - playerLocation.getX(), currentLocation.getZ() - playerLocation.getZ());
         if (this.lastValue != null && this.playerData.getLastAttackTicks() <= 1 && this.playerData.getLastAttacked() != null) {
            double n2 = 0.2325D + (double)BukkitUtil.getPotionLevel(this.player, PotionEffectType.SPEED) * 0.02D;
            if ((double)this.player.getWalkSpeed() > 0.2D) {
               n2 *= (double)this.player.getWalkSpeed() / 0.2D;
            }

            if (hypot >= this.lastValue * 0.99D && hypot > n2) {
               if (this.threshold++ > 3) {
                  this.handleViolation(String.format("D: %s", hypot - n2), 1.0D, true);
                  this.threshold = 0;
               }
            } else {
               this.threshold = 0;
               this.decreaseVL(0.05D);
            }
         }

         this.lastValue = hypot;
      } else {
         this.lastValue = null;
      }

      this.lastOnGround = playerLocation.getGround();
   }
}