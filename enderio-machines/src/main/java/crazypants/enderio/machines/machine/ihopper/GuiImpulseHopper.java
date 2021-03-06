package crazypants.enderio.machines.machine.ihopper;

import javax.annotation.Nonnull;

import com.enderio.core.client.gui.button.ToggleButton;

import crazypants.enderio.base.gui.GuiContainerBaseEIO;
import crazypants.enderio.base.gui.IconEIO;
import crazypants.enderio.base.gui.RedstoneModeButton;
import crazypants.enderio.base.machine.gui.PowerBar;
import crazypants.enderio.base.machine.gui.GuiButtonIoConfig;
import crazypants.enderio.base.machine.gui.GuiOverlayIoConfig;
import crazypants.enderio.machines.lang.Lang;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;

// TODO we need a GuiMachineBase for cap machines
public class GuiImpulseHopper extends GuiContainerBaseEIO implements ImpulseHopperRemoteExec.GUI {

  private static final int ID_REDSTONE_BUTTON = 139;
  private static final int ID_IO_MODE_BUTTON = 140;
  private static final int ID_LOCK_OUTPUT_BUTTON = 141;

  private static final int REDSTONE_MODE_LEFT = 153;
  private static final int REDSTONE_MODE_TOP = 8;

  private static final int POWERX = 15;
  private static final int POWERY = 9;
  private static final int POWER_HEIGHT = 47;

  private static final int D = 18;
  private static final int ROW1 = 9;
  private static final int ROW2 = ROW1 + D + D / 2;
  private static final int COL = 44;

  private final @Nonnull RedstoneModeButton<TileImpulseHopper> rsB;
  private final @Nonnull ToggleButton lockOutputB;
  private final @Nonnull GuiOverlayIoConfig<TileImpulseHopper> configOverlay;
  private final @Nonnull GuiButtonIoConfig<TileImpulseHopper> configB;

  private final @Nonnull TileImpulseHopper te;

  public GuiImpulseHopper(@Nonnull InventoryPlayer playerInv, @Nonnull TileImpulseHopper te) {
    super(new ContainerImpulseHopper(playerInv, te), "impulse_hopper");
    this.te = te;

    xSize = 176;
    ySize = 166;

    int x = REDSTONE_MODE_LEFT;
    int y = REDSTONE_MODE_TOP;

    rsB = new RedstoneModeButton<TileImpulseHopper>(this, ID_REDSTONE_BUTTON, x, y, te);

    y += 20;
    configOverlay = new GuiOverlayIoConfig<TileImpulseHopper>(te);
    addOverlay(configOverlay);

    configB = new GuiButtonIoConfig<TileImpulseHopper>(this, ID_IO_MODE_BUTTON, x, y, te, configOverlay);

    y += 35;

    lockOutputB = new ToggleButton(this, ID_LOCK_OUTPUT_BUTTON, x, y, IconEIO.LOCK_UNLOCKED, IconEIO.LOCK_LOCKED);
    lockOutputB.setSelectedToolTip(Lang.GUI_IMPULSE_HOPPER_LOCKED.get(), Lang.GUI_IMPULSE_HOPPER_LOCKED_TOOLTIP.get());
    lockOutputB.setUnselectedToolTip(Lang.GUI_IMPULSE_HOPPER_UNLOCKED.get());

    addDrawingElement(new PowerBar(te.getEnergy(), this, POWERX, POWERY, POWER_HEIGHT));
  }

  @Override
  public void initGui() {
    super.initGui();
    rsB.onGuiInit();
    configB.onGuiInit();
    lockOutputB.onGuiInit();
    lockOutputB.setSelected(te.isOutputLocked());

    ((ContainerImpulseHopper) inventorySlots).createGhostSlots(getGhostSlotHandler().getGhostSlots());
  }

  @Override
  public void actionPerformed(@Nonnull GuiButton guiButton) {
    if (guiButton.id == ID_LOCK_OUTPUT_BUTTON) {
      doOpenFilterGui(lockOutputB.isSelected());
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture();
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

    for (int slot = 0; slot < TileImpulseHopper.SLOTS; slot++) {
      if (te.checkGhostSlot(slot)) {
        if (te.checkInputSlot(slot)) {
          drawTexturedModalRect(guiLeft + COL + slot * D - 1, guiTop + ROW1 + D - 1, 200, D / 2, D, D / 2);
        } else {
          drawTexturedModalRect(guiLeft + COL + slot * D - 1, guiTop + ROW1 + D - 1, 200, 0, D, D / 2);
        }
        if (te.checkOutputSlot(slot)) {
          drawTexturedModalRect(guiLeft + COL + slot * D - 1, guiTop + ROW2 + D - 1, 200, D / 2, D, D / 2);
        } else {
          drawTexturedModalRect(guiLeft + COL + slot * D - 1, guiTop + ROW2 + D - 1, 200, 0, D, D / 2);
        }
      }
    }

    super.drawGuiContainerBackgroundLayer(par1, par2, par3);
  }

}
