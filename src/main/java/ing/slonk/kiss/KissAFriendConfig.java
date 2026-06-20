package ing.slonk.kiss;

import eu.midnightdust.lib.config.MidnightConfig;

public class KissAFriendConfig extends MidnightConfig {
	public static final String GENERAL = "general";

	@Entry(category = GENERAL, min = 0)
	public static double kissRange = 2.0;

	@Entry(category = GENERAL, isSlider = true, min = 0.0, max = 180.0)
	public static double kissMaxAngle = 40.0;
}
