package net.oriondevcorgitaco.unearthed;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.world.feature.StrataGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.StrataGenerator2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Unearthed implements ModInitializer {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        BlockGeneratorReference.init();
        strataGeneratorForAllBiomes();

        BlockGeneratorHelper.blockIdList.forEach(id -> LOGGER.info(MOD_ID + ":" + id));
    }

    public static final ConfiguredFeature<?, ?> STRATA_GENERATOR = RegistrationHelper.newConfiguredFeature("strata_generator", StrataGenerator.UNDERGROUND_STONE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));
    public static final ConfiguredFeature<?, ?> STRATA_GENERATOR2 = RegistrationHelper.newConfiguredFeature("strata_generator2", StrataGenerator2.UNDERGROUND_STONE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));


    public static void strataGeneratorForAllBiomes() {
        for (Biome biome : BuiltinRegistries.BIOME) {
            if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE) {
                addFeatureToBiome(biome, GenerationStep.Feature.UNDERGROUND_ORES, STRATA_GENERATOR);
            }
        }
    }

    public static void addFeatureToBiome(Biome biome, GenerationStep.Feature feature, ConfiguredFeature<?, ?> configuredFeature) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.getGenerationSettings().features;
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).add(() -> configuredFeature);

    }

    private static void ConvertImmutableFeatures(Biome biome) {
        if (biome.getGenerationSettings().features instanceof ImmutableList) {
            biome.getGenerationSettings().features = biome.getGenerationSettings().features.stream().map(Lists::newArrayList).collect(Collectors.toList());
        }
    }
}