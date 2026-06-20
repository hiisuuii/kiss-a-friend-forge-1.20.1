package ing.slonk.kiss;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(KissAFriend.MOD_ID)
public class KissAFriend {
	public static final String MOD_ID = "kiss_a_friend";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceLocation PACKET_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "kiss");
	public static final ResourceLocation PACKET_ID_LEGACY = ResourceLocation.fromNamespaceAndPath("e-transfer", "etransfer");

	private static final String PROTOCOL_VERSION = "1";

	public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
		.named(PACKET_ID)
		.networkProtocolVersion(() -> PROTOCOL_VERSION)
		.clientAcceptedVersions(PROTOCOL_VERSION::equals)
		.serverAcceptedVersions(PROTOCOL_VERSION::equals)
		.simpleChannel();

	public static final SimpleChannel NETWORK_LEGACY = NetworkRegistry.ChannelBuilder
		.named(PACKET_ID_LEGACY)
		.networkProtocolVersion(() -> PROTOCOL_VERSION)
		.clientAcceptedVersions(PROTOCOL_VERSION::equals)
		.serverAcceptedVersions(PROTOCOL_VERSION::equals)
		.simpleChannel();

	public KissAFriend(FMLJavaModLoadingContext context) {
		MidnightConfig.init(MOD_ID, KissAFriendConfig.class);

		NETWORK.registerMessage(0, KissPacket.class, KissPacket::encode, KissPacket::decode, KissPacket::handle);
		NETWORK_LEGACY.registerMessage(0, KissPacket.class, KissPacket::encode, KissPacket::decode, KissPacket::handle);
	}

	public static void initiateKiss(Player player1, Player player2) {
		KissPacket packet = new KissPacket(player1.getUUID(), player2.getUUID());
		NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player1), packet);
	}
}
