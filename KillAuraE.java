

@CheckInfo(
        type = CheckType.KILL_AURA,
        subType = "E",
        version = CheckVersion.RELEASE,
        minViolations = -1.0D,
        unsupportedAtleast = ClientVersion.V1_9,
        unsupportedServerAtleast = ServerVersion.v1_17_R1
)
public class KillAuraO2 extends Check implements PacketHandler {
    private int entityId, ticks;

  
    }

    public void handle(VPacketPlayInUseEntity vPacket) {
        if (vPacket.getAction().isAttack() && vPacket.isPlayer()) {
            int packetId = vPacket.getId();
            if (this.entityId == packetId) {
                this.decreaseVL(0.05D);
            } else {
                if (this.ticks <= 1 && this.playerData.isSurvival()) {
                    this.handleViolation();
                } else {
                    this.decreaseVL(0.05D);
                }
                this.ticks = 0;
            }
            this.entityId = packetId;
        }
    }

    public void handle(VPacketPlayInFlying vPacket) {
        this.ticks += 1;
    }
}
