package crazypants.enderio.powertools.config;

import javax.annotation.ParametersAreNonnullByDefault;

import crazypants.enderio.base.config.Config.Section;
import crazypants.enderio.base.config.ValueFactory;
import crazypants.enderio.powertools.EnderIOPowerTools;

@ParametersAreNonnullByDefault // Not the right one, but eclipse knows only 3 null annotations anyway, so it's ok
public final class Config {

  public static final Section sectionCapacitor = new Section("", "capacitor");

  public static final ValueFactory F = new ValueFactory(EnderIOPowerTools.MODID);

  //

  public static void load() {
    // force sub-configs to be classloaded with the main config
    CapBankConfig.F.getClass();
    ConduitConfig.F.getClass();
    PersonalConfig.F.getClass();
  }
}
