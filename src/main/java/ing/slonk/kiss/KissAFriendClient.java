package ing.slonk.kiss;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class KissAFriendClient {
	public static void handleTransfer(KissPacket packet) {
		Minecraft client = Minecraft.getInstance();
		if (client.level == null) {
			KissAFriend.LOGGER.error("world doesn't exist yet");
			return;
		}

		Player kisser = client.level.getPlayerByUUID(packet.kisser());
		Player kissee = client.level.getPlayerByUUID(packet.kissee());
		if (kisser == null || kissee == null) {
			KissAFriend.LOGGER.debug("player doesn't exist, ignoring");
			return;
		}

		Vec3 midpoint = kisser.position().add(kissee.position()).scale(0.5).add(0, 1, 0);

		KissAFriend.LOGGER.debug("spawning particles at {}", midpoint);
		for (int i = 0; i < Math.ceil(Math.random() * 3); i++) {
			client.level.addParticle(
				ParticleTypes.HEART,
				midpoint.x() + Math.random() * 0.25,
				midpoint.y() + Math.random() * 0.25,
				midpoint.z() + Math.random() * 0.25,
				0.0D,
				0.5D,
				0.0D
			);
		}
	}
}
