@CheckInfo(
   type = CheckType.KILL_AURA,
   subType = "A",
   friendlyName = "Kill Aura",
   version = CheckVersion.RELEASE,
   minViolations = -2.0D,
   maxViolations = 8,
   description = "Impossible rotation"
)
public class KillAuraA extends PacketCheck {
   public void handle(VPacket vPacket, long l) {
      if (vPacket instanceof VPacketPlayInFlying && ((VPacketPlayInFlying)vPacket).isLook() && !this.playerData.isTeleporting(5) && this.playerData.isSpawned() && this.playerData.getLastAttackTicks() <= 20 && this.playerData.getLastAttacked() != null) {
         PlayerLocation playerLocation = this.playerData.getLocation();
         Queue<PacketLocation> queue = (Queue)this.playerData.getRecentMoveMap().get(this.playerData.getLastAttackedId());
         if (queue != null) {
            int n2 = this.playerData.getTransactionPing();
            List<PacketLocation> list = (List)queue.stream().map(PacketLocation::clone).collect(Collectors.toList());
            ArrayList<PacketLocation> arrayList = new ArrayList();
            long l2 = l - 125L - (long)n2;
            Iterator<PacketLocation> iterator = list.iterator();

            PacketLocation packetLocation;
            PacketLocation packetLocation2;
            for(packetLocation2 = (PacketLocation)iterator.next(); iterator.hasNext(); packetLocation2 = packetLocation) {
               packetLocation = (PacketLocation)iterator.next();
               long l3 = packetLocation.getTimestamp() - l2;
               if (l3 > 0L) {
                  arrayList.add(packetLocation2);
                  if (l3 > 100L) {
                     packetLocation2 = packetLocation;
                     break;
                  }
               }
            }

            if (arrayList.isEmpty()) {
               arrayList.add(packetLocation2);
            }

            int n;
            if ((n = list.indexOf((PacketLocation)arrayList.stream().min(Comparator.comparingLong(PacketLocation::getTimestamp)).get())) > 0) {
               arrayList.add(list.get(n - 1));
            }

            Iterator var22 = arrayList.iterator();

            while(var22.hasNext()) {
               PacketLocation packetLocation3 = (PacketLocation)var22.next();
               float[] arrf = MathUtil.getRotationFromPosition(playerLocation, packetLocation3);
               double d = MathUtil.getDistanceBetweenAngles(playerLocation.getYaw(), arrf[0]);
               double d2 = MathUtil.getDistanceBetweenAngles(playerLocation.getPitch(), arrf[1]);
               if (d == 0.0D || d2 == 0.0D) {
                  if (d == 0.0D) {
                     this.handleViolation("Yaw", 1.0D);
                  }

                  if (d2 == 0.0D) {
                     this.handleViolation("Pitch", 1.0D);
                  }

                  return;
               }
            }
         }

         this.decreaseVL(1.0E-4D);
      }

   }
}
    