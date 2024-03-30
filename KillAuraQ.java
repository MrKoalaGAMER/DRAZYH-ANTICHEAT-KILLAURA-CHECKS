@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "Q",
   friendlyName = "Kill Aura",
   version = CheckVersion.DEVELOPMENT,
   minViolations = -5.0D,
   maxViolations = 30
)
public class KillAuraQ extends AimCheck {
   private final Queue<Double> values = new ConcurrentLinkedQueue();

   public void handle(PlayerLocation playerLocation, PlayerLocation playerLocation2, long n2) {
      if (this.playerData.getLastAttackTicks() <= 1 && this.playerData.getLastAttacked() != null && (double)Math.min(Math.abs(playerLocation.getYaw() - playerLocation2.getYaw()), Math.abs(playerLocation.getPitch() - playerLocation2.getPitch())) > 0.5D) {
         Deque<PacketLocation> deque = (Deque)this.playerData.getRecentMoveMap().get(this.playerData.getLastAttackedId());
         if (deque != null && deque.size() > 5) {
            ArrayList<PacketLocation> list = new ArrayList();
            long n3 = n2 - 125L - (long)this.playerData.getTransactionPing();
            Iterator<PacketLocation> iterator = deque.iterator();

            PacketLocation packetLocation2;
            PacketLocation packetLocation3;
            for(packetLocation2 = (PacketLocation)iterator.next(); iterator.hasNext(); packetLocation2 = packetLocation3) {
               packetLocation3 = (PacketLocation)iterator.next();
               long n4 = packetLocation3.getTimestamp() - n3;
               if (n4 > 0L) {
                  list.add(packetLocation2);
                  if (n4 > 75L) {
                     packetLocation2 = packetLocation3;
                     break;
                  }
               }
            }

            if (list.isEmpty()) {
               list.add(packetLocation2);
            }

            Stream<Double> map = list.stream().map((packetLocation) -> {
               return MathUtil.getLuckyAura(playerLocation2, packetLocation);
            });
            if (this.values.size() > 10) {
               double average = MathUtil.average((Iterable)this.values);
               Double n5 = (Double)map.min(Comparator.comparingDouble((n) -> {
                  return Math.abs(n - average) + Math.abs(n / 2.0D);
               })).orElse((Object)null);
               if (n5 != null) {
                  this.values.add(n5);
                  double deviation = MathUtil.deviation(this.values);
                  if (deviation < 0.1D && average < 0.3D) {
                     this.handleViolation(String.format("AVG: %.2f DEV: %.3f", average, deviation), 1.25D - deviation * 10.0D);
                  } else {
                     this.decreaseVL(0.1D);
                  }

                  this.values.poll();
               }
            } else {
               this.values.add(MathUtil.average((Iterable)map.collect(Collectors.toList())));
            }
         }
      }

   }
}