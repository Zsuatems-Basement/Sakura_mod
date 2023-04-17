package cn.mcmod.sakura.level.tree;

import java.util.Random;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class SakuraTreeGrower extends AbstractTreeGrower {

    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean hasBees) {
        if (random.nextInt(10) == 0) {
            return SakuraTreeFeatures.FANCY_SAKURA.getHolder().get();
        } else {
            return SakuraTreeFeatures.SAKURA.getHolder().get();
        }
    }

}
