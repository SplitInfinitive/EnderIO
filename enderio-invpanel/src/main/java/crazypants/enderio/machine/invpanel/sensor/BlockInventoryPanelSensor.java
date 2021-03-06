package crazypants.enderio.machine.invpanel.sensor;

import java.util.Random;

import javax.annotation.Nonnull;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;

import crazypants.enderio.base.init.IModObject;
import crazypants.enderio.base.machine.base.block.AbstractMachineBlock;
import crazypants.enderio.base.machine.modes.IoMode;
import crazypants.enderio.base.network.PacketHandler;
import crazypants.enderio.base.paint.IPaintable;
import crazypants.enderio.base.render.IBlockStateWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockInventoryPanelSensor extends AbstractMachineBlock<TileInventoryPanelSensor> implements IResourceTooltipProvider, IPaintable.ISolidBlockPaintableBlock,
IPaintable.IWrenchHideablePaint {

  
  public static BlockInventoryPanelSensor create(@Nonnull IModObject modObject) {
    
    PacketHandler.INSTANCE.registerMessage(PacketActive.Handler.class, PacketActive.class, PacketHandler.nextID(), Side.CLIENT);
    PacketHandler.INSTANCE.registerMessage(PacketItemToCheck.Handler.class, PacketItemToCheck.class, PacketHandler.nextID(), Side.SERVER);
    PacketHandler.INSTANCE.registerMessage(PacketItemCount.Handler.class, PacketItemCount.class, PacketHandler.nextID(), Side.SERVER);
    
    
    BlockInventoryPanelSensor result = new BlockInventoryPanelSensor(modObject);
    result.init();
    return result;
  }
  
  public BlockInventoryPanelSensor(@Nonnull IModObject modObject) {
    super(modObject);
  }
  
  @Override
  public Container getServerGuiElement(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, int param1, TileInventoryPanelSensor te) {
      return new ContainerSensor(player.inventory, te);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiScreen getClientGuiElement(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, int param1, TileInventoryPanelSensor te) {
      return new GuiSensor(player.inventory, te);
  }

  @Override
  public boolean isOpaqueCube(IBlockState bs) {
    return false;
  }

  @Override
  protected void setBlockStateWrapperCache(@Nonnull IBlockStateWrapper blockStateWrapper, @Nonnull IBlockAccess world, @Nonnull BlockPos pos,
      @Nonnull TileInventoryPanelSensor tileEntity) {
    blockStateWrapper.addCacheKey(tileEntity.getFacing());
    blockStateWrapper.addCacheKey(tileEntity.isActive());
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(IBlockState bs, World world, BlockPos pos, Random rand) {
  }

  @Deprecated
  @Override
  public int getWeakPower(IBlockState blockStateIn, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    TileInventoryPanelSensor te = getTileEntity(blockAccess, pos);
    if (te != null) {
      int res = te.getIoMode(side.getOpposite()) != IoMode.DISABLED ? te.getRedstoneLevel() : 0;
      return res;
    }
    return super.getWeakPower(blockStateIn, blockAccess, pos, side);
  }

  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }

  @Override
  public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    TileInventoryPanelSensor te = getTileEntitySafe(world, pos);
    if (te != null && side != null) {
      return te.getIoMode(side.getOpposite()) != IoMode.DISABLED;
    }
    return super.canConnectRedstone(state, world, pos, side);
  }
}
