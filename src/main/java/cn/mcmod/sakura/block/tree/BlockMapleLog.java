package cn.mcmod.sakura.block.tree;

import cn.mcmod.sakura.CommonProxy;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;


public class BlockMapleLog extends BlockLog{
    public BlockMapleLog() {
        super();
        setCreativeTab(CommonProxy.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(LOG_AXIS)) {
            case X:
                return 4;
            case Y:
                return 0;
            case Z:
                return 8;
            case NONE:
            default:
                return 12;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
                return getDefaultState().withProperty(LOG_AXIS, EnumAxis.Y);
            case 4:
                return getDefaultState().withProperty(LOG_AXIS, EnumAxis.X);
            case 8:
                return getDefaultState().withProperty(LOG_AXIS, EnumAxis.Z);
            case 12:
            default:
                return getDefaultState().withProperty(LOG_AXIS, EnumAxis.NONE);
        }
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 5;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 5;
    }
}
