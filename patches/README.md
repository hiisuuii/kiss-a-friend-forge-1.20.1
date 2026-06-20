# MidnightLib dev patch

MidnightLib's published `1.9.2+1.20.1-forge` jar ships its only mixin
(`eu.midnightdust.core.mixin.MixinOptionsScreen`) compiled against **SRG** member
names (`@Inject(method = "m_7856_()V")`) and contains **no refmap**. That works on an
installed (SRG-runtime) Forge instance. But a ForgeGradle dev workspace runs Minecraft
mapped to **official** names, so the mixin can never resolve its target and the game
crashes on startup.

## The fix

For development purposes only, I had to create a patched copy at `libs/midnightlib-patched-1.9.2.jar`. It is the original jar with two changes:

1. `midnightlib.refmap.json` added (see `midnightlib.refmap.json` here), mapping the mixin's
   `m_7856_()V` reference to `Lnet/minecraft/client/gui/screens/OptionsScreen;m_7856_()V`.
2. `midnightlib.mixins.json` replaced with a copy that adds `"refmap": "midnightlib.refmap.json"`.

`build.gradle` pulls this jar from a `flatDir` repo through `fg.deobf(...)`, so ForgeGradle
remaps it to official names. At runtime the two `mixin.env.*` properties in the `runs` block 
remap the **refmap's** SRG reference to the official `init()V`, so the mixin applies.

## Regenerating the jar

```sh
# from a checkout that has resolved MidnightLib once (jar in the Gradle cache):
ORIG=<path to midnightlib-1.9.2+1.20.1-forge.jar in ~/.gradle caches>
rm -rf ext && unzip -q "$ORIG" -d ext
cp patches/midnightlib/midnightlib.mixins.json  ext/midnightlib.mixins.json
cp patches/midnightlib/midnightlib.refmap.json  ext/midnightlib.refmap.json
( cd ext && jar cfM ../libs/midnightlib-patched-1.9.2.jar . ) # cfM keeps the original MANIFEST
```
