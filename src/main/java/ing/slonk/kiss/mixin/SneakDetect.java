package ing.slonk.kiss.mixin;

import ing.slonk.kiss.KissAFriendConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ing.slonk.kiss.KissAFriend.initiateKiss;

@Mixin(Entity.class)
public abstract class SneakDetect {
	@Inject(at = @At("HEAD"), method = "setShiftKeyDown")
	private void init(boolean sneaking, CallbackInfo info) {
		Entity entity = (Entity) (Object) this;
		if (!(entity instanceof ServerPlayer player)) return;

		// just started sneaking
		if (sneaking && !player.isShiftKeyDown()) {
			double maxAngle = KissAFriendConfig.kissMaxAngle / 180.0 * Math.PI;

			Player closestPlayer = player.serverLevel().getNearestPlayer(
				TargetingConditions
					.forNonCombat()
					.range(KissAFriendConfig.kissRange)
					.selector((t) -> {
						if (t.equals(player)) return false;
						if (!(t instanceof Player otherPlayer)) return false;

						Vec3 diff = otherPlayer.position().subtract(player.position());
						Vec3 lookAngle = player.getLookAngle();
						double angle1 = Math.acos(diff.dot(lookAngle) / (diff.horizontalDistance() * lookAngle.horizontalDistance()));
						diff = diff.reverse();
						lookAngle = otherPlayer.getLookAngle();
						double angle2 = Math.acos(diff.dot(lookAngle) / (diff.horizontalDistance() * lookAngle.horizontalDistance()));

						return angle1 < maxAngle && angle2 < maxAngle;
					}),
				player);

			if (closestPlayer != null && closestPlayer.isShiftKeyDown()) {
				initiateKiss(player, closestPlayer);
			}
		}
	}
}
