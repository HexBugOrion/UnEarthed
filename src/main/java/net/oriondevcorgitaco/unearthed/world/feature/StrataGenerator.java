package net.oriondevcorgitaco.unearthed.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoise;

import java.util.Random;

public class StrataGenerator extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> UNDERGROUND_STONE = RegistrationHelper.registerFeature("strata_gen1", new StrataGenerator(DefaultFeatureConfig.CODEC));

    public StrataGenerator(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    FastNoise fastNoise3D = null;

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig featureConfig) {
        setSeed(world.getSeed());


        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                for (int y = 0; y < topY; y++) {
                    //Noise range is between -1 and 1.
                    double noise3D = fastNoise3D.GetNoise(mutable.getX(), mutable.getY(), mutable.getZ());

                    if (world.getBlockState(mutable).getBlock().isIn(BlockTags.BASE_STONE_OVERWORLD)) {
                        if (world.getBiome(mutable).getCategory() == Biome.Category.ICY) {
                            if (noise3D > 0.5)
                                world.setBlockState(mutable, BlockGeneratorReference.RHYOLITE.getBlock().getDefaultState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlockState(mutable, Blocks.DIORITE.getDefaultState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlockState(mutable, Blocks.BLUE_ICE.getDefaultState(), 2);
                            else
                                world.setBlockState(mutable, Blocks.SNOW_BLOCK.getDefaultState(), 2);

                        } else if (world.getBiome(mutable).getCategory() == Biome.Category.MESA) {
                            if (noise3D > 0.5)
                                world.setBlockState(mutable, Blocks.RED_TERRACOTTA.getDefaultState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlockState(mutable, Blocks.BLACK_TERRACOTTA.getDefaultState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlockState(mutable, Blocks.YELLOW_TERRACOTTA.getDefaultState(), 2);
                            else
                                world.setBlockState(mutable, Blocks.ORANGE_TERRACOTTA.getDefaultState(), 2);

                        } else if (world.getBiome(mutable).getCategory() == Biome.Category.EXTREME_HILLS) {
                            if (noise3D > 0.5)
                                world.setBlockState(mutable, BlockGeneratorReference.PUMICE.getBlock().getDefaultState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlockState(mutable, Blocks.GRANITE.getDefaultState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlockState(mutable, Blocks.ANDESITE.getDefaultState(), 2);
                            else
                                world.setBlockState(mutable, BlockGeneratorReference.KIMBERLITE.getBlock().getDefaultState(), 2);

                        } else if (world.getBiome(mutable).getCategory() == Biome.Category.DESERT) {
                            if (noise3D > 0.5)
                                world.setBlockState(mutable, Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlockState(mutable, Blocks.SMOOTH_SANDSTONE.getDefaultState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlockState(mutable, Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), 2);
                            else
                                world.setBlockState(mutable, Blocks.SMOOTH_SANDSTONE.getDefaultState(), 2);

                        } else {
                            if (noise3D > 0.5)
                                world.setBlockState(mutable, BlockGeneratorReference.LIMESTONE.getBlock().getDefaultState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlockState(mutable, BlockGeneratorReference.MARBLE.getBlock().getDefaultState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlockState(mutable, BlockGeneratorReference.SLATE.getBlock().getDefaultState(), 2);
                            else
                                world.setBlockState(mutable, Blocks.POLISHED_BLACKSTONE.getDefaultState(), 2);
                        }
                    }
                    mutable.move(Direction.UP);
                }
            }
        }
        return true;
    }


    private void setSeed(long seed) {
        if (fastNoise3D == null) {
            fastNoise3D = new FastNoise((int) seed);
            fastNoise3D.SetNoiseType(FastNoise.NoiseType.Simplex);
            fastNoise3D.SetFrequency(0.004F);
        }
    }
}
