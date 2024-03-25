

@CheckInfo(type=CheckType.KILL_AURA, subType="F", friendlyName="Auto Clicker", version=CheckVersion.RELEASE, maxViolations=10, unsupportedAtleast=ClientVersion.VERSION1_9)
public class KillAuraF extends PacketCheck {
    private boolean dig;
    private boolean place;

    public void handle(VPacket vPacket, long l) {
        if (vPacket instanceof VPacketPlayInFlying) {
            this.dig = false;
            this.place = false;
        } else if (vPacket instanceof VPacketPlayInBlockDig) {
            VPacketPlayInBlockDig vPacketPlayInBlockDig = (VPacketPlayInBlockDig)vPacket;
            if (vPacketPlayInBlockDig.getType() != VPacketPlayInBlockDig.PlayerDigType.DROP_ITEM && vPacketPlayInBlockDig.getType() != VPacketPlayInBlockDig.PlayerDigType.DROP_ALL_ITEMS) {
                this.dig = true;
            }
        } else if (vPacket instanceof VPacketPlayInUseEntity && !this.place && !this.playerData.isVehicle() && this.dig && ((VPacketPlayInUseEntity)vPacket).getAction() == VPacketPlayInUseEntity.EntityUseAction.ATTACK) {
            this.handleViolation("", 1.0);
        } else if (vPacket instanceof VPacketPlayInBlockPlace) {
            this.place = true;
        }
    }
}
