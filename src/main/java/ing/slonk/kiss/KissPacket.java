package ing.slonk.kiss;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record KissPacket(UUID kisser, UUID kissee) {
	public static void encode(KissPacket packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.kisser);
		buf.writeUUID(packet.kissee);
	}

	public static KissPacket decode(FriendlyByteBuf buf) {
		return new KissPacket(buf.readUUID(), buf.readUUID());
	}

	public static void handle(KissPacket packet, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		context.enqueueWork(() ->
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> KissAFriendClient.handleTransfer(packet))
		);
		context.setPacketHandled(true);
	}
}
