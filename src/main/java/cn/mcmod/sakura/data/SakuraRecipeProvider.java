package cn.mcmod.sakura.data;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import cn.mcmod.sakura.SakuraMod;
import cn.mcmod.sakura.block.BlockItemRegistry;
import cn.mcmod.sakura.block.BlockRegistry;
import cn.mcmod.sakura.data.builder.ChoppingBoardRecipeBuilder;
import cn.mcmod.sakura.data.builder.CookingPotRecipeBuilder;
import cn.mcmod.sakura.data.builder.DistillerRecipeBuilder;
import cn.mcmod.sakura.data.builder.FermenterRecipeBuilder;
import cn.mcmod.sakura.data.builder.StoneMortarRecipeBuilder;
import cn.mcmod.sakura.fluid.BucketItemRegistry;
import cn.mcmod.sakura.fluid.FluidRegistry;
import cn.mcmod.sakura.item.FoodRegistry;
import cn.mcmod.sakura.item.ItemRegistry;
import cn.mcmod.sakura.item.enums.SakuraCuisineSet;
import cn.mcmod.sakura.item.enums.SakuraFoodSet;
import cn.mcmod.sakura.item.enums.SakuraNormalItemSet;
import cn.mcmod.sakura.tags.SakuraFluidTags;
import cn.mcmod.sakura.tags.SakuraItemTags;
import cn.mcmod_mmf.mmlib.data.AbstractRecipeProvider;
import cn.mcmod_mmf.mmlib.fluid.FluidIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fluids.FluidStack;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;

public class SakuraRecipeProvider extends AbstractRecipeProvider {

    public SakuraRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        registerCraftingRecipe(consumer);
        registerMortarRecipe(consumer);
        registerCookingRecipe(consumer);
        registerFermenterRecipe(consumer);
        registerDistillerRecipe(consumer);
        registerChoppingRecipes(consumer);
    }

    private void registerCraftingRecipe(Consumer<FinishedRecipe> consumer) {

        makeSlab(BlockRegistry.TATAMI_SLAB, BlockRegistry.TATAMI).save(consumer);
        makeSlab(BlockRegistry.TATAMI_SLAB_SUNBURNT, BlockRegistry.TATAMI_SUNBURNT).save(consumer);

        ShapedRecipeBuilder.shaped(BlockRegistry.STRAW_BLOCK.get(), 4).pattern("LLL").pattern("LLL").pattern("LLL")
                .define('L', SakuraItemTags.STRAW).unlockedBy("has_item", has(SakuraItemTags.STRAW)).save(consumer);

        ShapedRecipeBuilder.shaped(ItemRegistry.IRON_FISH_KNIFE.get()).pattern("  I").pattern(" I ").pattern("L  ")
                .define('I', Tags.Items.INGOTS_IRON).define('L', SakuraItemTags.LUMBER)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);

        ShapedRecipeBuilder.shaped(BlockRegistry.TATAMI.get(), 6).pattern("LLL").pattern("L#L").pattern("LLL")
                .define('#', SakuraItemTags.LUMBER).define('L', SakuraItemTags.STRAW)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);

        ShapedRecipeBuilder.shaped(Items.TORCH, 4).pattern("C").pattern("#")
                .define('C', ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL).get())
                .define('#', Tags.Items.RODS_WOODEN).unlockedBy("has_item", has(Tags.Items.RODS_WOODEN))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "torchs_from_charcoal"));

        ShapedRecipeBuilder.shaped(Items.STICK, 4).pattern("#").pattern("#").define('#', SakuraItemTags.LUMBER)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "sticks_from_lumbers"));

        ShapedRecipeBuilder.shaped(BlockRegistry.OBON.get()).pattern("LLL").pattern("L#L")
                .define('#', BlockRegistry.SAKURA_LEAVES.get()).define('L', SakuraItemTags.LUMBER)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);

        ShapedRecipeBuilder.shaped(Items.PAPER, 4).pattern("###").define('#', SakuraItemTags.LUMBER)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "papers_from_lumbers"));

        ShapedRecipeBuilder.shaped(BlockItemRegistry.CHOPPING_BOARD.get()).pattern("###").pattern("I I")
                .define('#', SakuraItemTags.LUMBER).define('I', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "chopping_board"));

        ShapedRecipeBuilder.shaped(BlockItemRegistry.FERMENTER.get()).pattern("SSS").pattern("PPP").pattern("SSS")
                .define('S', SakuraItemTags.LUMBER).define('P', ItemTags.LOGS)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "fermenter"));

        ShapedRecipeBuilder.shaped(BlockItemRegistry.DISTILLER.get()).pattern("ISI").pattern("PPP").pattern("III")
                .define('S', SakuraItemTags.LUMBER).define('P', ItemTags.LOGS).define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "distiller"));

        registerFarmerDelightRecipes(consumer);

        ShapedRecipeBuilder.shaped(BlockRegistry.COOKING_POT.get()).pattern("#L#").pattern("###")
                .define('#', Tags.Items.INGOTS_IRON).define('L', SakuraItemTags.LUMBER)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);

        ShapedRecipeBuilder.shaped(BlockRegistry.STONE_MORTAR.get()).pattern("L  ").pattern("###").pattern("###")
                .define('#', Tags.Items.COBBLESTONE).define('L', SakuraItemTags.LUMBER)
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);

        foodSmeltingRecipes("eggplant_bake", FoodRegistry.FOODSET.get(SakuraFoodSet.EGGPLANT).get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.EGGPLANT_BAKED).get(), 0.5F, consumer);
        foodSmeltingRecipes("taro_bake", ItemRegistry.TARO.get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.TARO_BAKED).get(), 0.5F, consumer);
        foodSmeltingRecipes("burger", FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER_RAW).get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER).get(), 0.5F, consumer);

        foodSmeltingRecipes("chikuwa", FoodRegistry.FOODSET.get(SakuraFoodSet.CHIKUWA_RAW).get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.CHIKUWA).get(), 0.5F, consumer);

        foodSmeltingRecipes("bun", ItemRegistry.MATERIALS.get(SakuraNormalItemSet.DOUGH).get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.BUN).get(), 0.5F, consumer);
        foodSmeltingRecipes("buckwheat_bread", ItemRegistry.MATERIALS.get(SakuraNormalItemSet.DOUGH_BUCKWHEAT).get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.BUCKWHEAT_BREAD).get(), 0.5F, consumer);
        foodSmeltingRecipes("rice_bread", ItemRegistry.MATERIALS.get(SakuraNormalItemSet.DOUGH_RICE).get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_BREAD).get(), 0.5F, consumer);

        ShapelessRecipeBuilder.shapeless(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.DOUGH).get(), 3)
                .requires(SakuraItemTags.FLOUR_WHEAT).requires(SakuraItemTags.FLOUR_WHEAT)
                .requires(SakuraItemTags.FLOUR_WHEAT).requires(SakuraItemTags.WATER)
                .unlockedBy("has_flour", has(SakuraItemTags.FLOUR_WHEAT)).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockItemRegistry.NABE_SUKIYAKI.get())
                .requires(BlockItemRegistry.COOKING_POT.get()).requires(SakuraItemTags.SOYSAUCE)
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get()).requires(SakuraItemTags.RAW_BEEF)
                .requires(Tags.Items.CROPS_CARROT).requires(SakuraItemTags.MUSHROOMS)
                .requires(SakuraItemTags.VEGETABLES).requires(SakuraItemTags.VEGETABLES)
                .unlockedBy("has_pot", has(BlockItemRegistry.COOKING_POT.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockItemRegistry.NABE_ODEN.get())
                .requires(BlockItemRegistry.COOKING_POT.get()).requires(SakuraItemTags.FISHCAKE)
                .requires(SakuraItemTags.FISHCAKE).requires(SakuraItemTags.FISHCAKE).requires(SakuraItemTags.FISHCAKE)
                .requires(SakuraItemTags.EGGS).requires(SakuraItemTags.DASHI).requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
                .unlockedBy("has_pot", has(BlockItemRegistry.COOKING_POT.get())).save(consumer);

        makeItemToBucket(BucketItemRegistry.FOOD_OIL_BUCKET, Ingredient.of(SakuraItemTags.SEEDS_RAPESEED))
                .unlockedBy("has_seeds", has(SakuraItemTags.SEEDS_RAPESEED)).save(consumer);

        ShapelessRecipeBuilder.shapeless(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.DOUGH_BUCKWHEAT).get(), 3)
                .requires(SakuraItemTags.FLOUR_BUCKWHEAT).requires(SakuraItemTags.FLOUR_BUCKWHEAT)
                .requires(SakuraItemTags.FLOUR_BUCKWHEAT).requires(SakuraItemTags.WATER)
                .unlockedBy("has_flour", has(SakuraItemTags.FLOUR_BUCKWHEAT)).save(consumer);

        ShapelessRecipeBuilder.shapeless(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.DOUGH_RICE).get(), 3)
                .requires(SakuraItemTags.FLOUR_RICE).requires(SakuraItemTags.FLOUR_RICE)
                .requires(SakuraItemTags.FLOUR_RICE).requires(SakuraItemTags.WATER)
                .unlockedBy("has_flour", has(SakuraItemTags.FLOUR_RICE)).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockRegistry.TEISHOKO_TAMAGOYAKI.get()).requires(SakuraItemTags.SOUPS)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(BlockRegistry.OBON.get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.TAMAGOYAKI).get())
                .unlockedBy("has_obon", has(BlockRegistry.OBON.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockRegistry.TEISHOUKU_FISH_COOKED.get()).requires(SakuraItemTags.SOUPS)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(BlockRegistry.OBON.get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.FISH_BAKE).get())
                .unlockedBy("has_obon", has(BlockRegistry.OBON.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockRegistry.TEISHOUKU_FISH_SALT.get()).requires(SakuraItemTags.SOUPS)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(BlockRegistry.OBON.get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.FISH_BAKE_SALT).get())
                .unlockedBy("has_obon", has(BlockRegistry.OBON.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockRegistry.TEISHOUKU_FISH_RAW.get()).requires(SakuraItemTags.SOUPS)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(BlockRegistry.OBON.get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.SASHIMI).get())
                .unlockedBy("has_obon", has(BlockRegistry.OBON.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockRegistry.TEISHOKO_YAKINIKU.get()).requires(SakuraItemTags.SOUPS)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(BlockRegistry.OBON.get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.YAKINIKU).get())
                .unlockedBy("has_obon", has(BlockRegistry.OBON.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.SASHIMI).get())
                .requires(SakuraItemTags.SLICES_RAW_FISHES).requires(SakuraItemTags.SLICES_RAW_FISHES)
                .requires(SakuraItemTags.SOYSAUCE).unlockedBy("has_fish", has(SakuraItemTags.SLICES_RAW_FISHES))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.CHIKUWA_RAW).get(), 2)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get()).requires(SakuraItemTags.SALT)
                .unlockedBy("has_fish", has(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(BlockRegistry.SAKURA_SAPLING.get()).requires(ItemTags.SAPLINGS)
                .requires(Tags.Items.DYES_PINK).unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);
        ShapelessRecipeBuilder.shapeless(BlockRegistry.MAPLE_SAPLING_RED.get()).requires(ItemTags.SAPLINGS)
                .requires(Tags.Items.DYES_RED).unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);
        ShapelessRecipeBuilder.shapeless(BlockRegistry.MAPLE_SAPLING_GREEN.get()).requires(ItemTags.SAPLINGS)
                .requires(Tags.Items.DYES_GREEN).unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);
        ShapelessRecipeBuilder.shapeless(BlockRegistry.MAPLE_SAPLING_YELLOW.get()).requires(ItemTags.SAPLINGS)
                .requires(Tags.Items.DYES_YELLOW).unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);
        ShapelessRecipeBuilder.shapeless(BlockRegistry.MAPLE_SAPLING_ORANGE.get()).requires(ItemTags.SAPLINGS)
                .requires(Tags.Items.DYES_ORANGE).unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.ONIGIRI).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(Items.DRIED_KELP)
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.ONIGIRI_BAMBOO).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(Items.DRIED_KELP)
                .requires(BlockRegistry.BAMBOOSHOOT.get())
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.ONIGIRI_SEAWEED).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(Items.DRIED_KELP)
                .requires(Items.DRIED_KELP)
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.ONIGIRI_MUSHROOM).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(Items.DRIED_KELP)
                .requires(SakuraItemTags.MUSHROOMS)
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.ONIGIRI_TEMPURA).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(Items.DRIED_KELP)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.TEMPURA).get())
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.SUSHI).get(), 2)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())
                .requires(SakuraItemTags.SLICES_RAW_FISHES)
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.SUSHI_SHRIMP).get(), 2)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(SakuraItemTags.SHRIMP)
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.SUSHI_TAMAGO).get(), 3)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.TAMAGOYAKI).get()).requires(Items.DRIED_KELP)
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.TEMPURA_BATTER).get(), 8)
                .requires(SakuraItemTags.FLOUR).requires(SakuraItemTags.SALT).requires(SakuraItemTags.EGGS)
                .requires(SakuraItemTags.EGGS).requires(SakuraItemTags.WATER)
                .unlockedBy("has_flour", has(SakuraItemTags.FLOUR)).save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.HAMBURGER).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BUN).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER).get()).requires(SakuraItemTags.TOMATOSAUCE)
                .unlockedBy("has_bun", has(FoodRegistry.FOODSET.get(SakuraFoodSet.BUN).get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.CHEESE).get())
                .requires(SakuraItemTags.MILK).requires(SakuraItemTags.SALT)
                .unlockedBy("has_salt", has(SakuraItemTags.SALT)).save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER_DISH).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER).get())
                .requires(SakuraItemTags.SALAD_INGREDIENTS_CABBAGE)
                .unlockedBy("has_burger", has(FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER).get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.CHEESE_BURGER).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BUN).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.TOMATO_SAUCE).get()).requires(SakuraItemTags.CHEESE)
                .unlockedBy("has_bun", has(FoodRegistry.FOODSET.get(SakuraFoodSet.BUN).get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.CHEESE_BURGER).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.HAMBURGER).get()).requires(SakuraItemTags.CHEESE)
                .unlockedBy("has_bun", has(FoodRegistry.FOODSET.get(SakuraFoodSet.BUN).get()))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "cheese_burger_from_hamburger"));

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.MOCHI).get(), 8)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())
                .unlockedBy("has_rice", has(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())).save(consumer);

        foodSmeltingRecipes("mochi_toasted", FoodRegistry.FOODSET.get(SakuraFoodSet.MOCHI).get(),
                FoodRegistry.FOODSET.get(SakuraFoodSet.MOCHI_TOASTED).get(), 0.5F, consumer);

        ShapelessRecipeBuilder.shapeless(FoodRegistry.FOODSET.get(SakuraFoodSet.MOCHI_SAKURA).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.MOCHI).get())
                .requires(BlockRegistry.SAKURA_LEAVES.get())
                .unlockedBy("has_mochi", has(FoodRegistry.FOODSET.get(SakuraFoodSet.MOCHI).get())).save(consumer);

        makeIngotToBlock(BlockRegistry.BAMBOO_BLOCK, ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO))
                .unlockedBy("has_item", has(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO).get()))
                .save(consumer);
        makeIngotToBlock(BlockRegistry.BAMBOO_BLOCK, () -> Items.BAMBOO).unlockedBy("has_item", has(Items.BAMBOO))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "bamboo_block_from_vanilla_bamboo"));
        makeIngotToBlock(BlockRegistry.BAMBOO_BLOCK_SUNBURNT,
                ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_SUNBURNT))
                        .unlockedBy("has_item",
                                has(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_SUNBURNT).get()))
                        .save(consumer);
        makeIngotToBlock(BlockRegistry.BAMBOO_CHARCOAL_BLOCK,
                ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL))
                        .unlockedBy("has_item",
                                has(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL).get()))
                        .save(consumer);

        makeBlockToIngot(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO), BlockRegistry.BAMBOO_BLOCK)
                .save(consumer);
        makeBlockToIngot(() -> Items.BAMBOO, BlockRegistry.BAMBOO_BLOCK).save(consumer,
                new ResourceLocation(SakuraMod.MODID, "bamboo_block_to_vanilla_bamboo"));
        makeBlockToIngot(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL),
                BlockRegistry.BAMBOO_CHARCOAL_BLOCK).save(consumer);
        makeBlockToIngot(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_SUNBURNT),
                BlockRegistry.BAMBOO_BLOCK_SUNBURNT).save(consumer);

        makeLumber(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.LUMBER_BAMBOO), Ingredient.of(SakuraItemTags.BAMBOO))
                .unlockedBy("has_item", has(SakuraItemTags.BAMBOO)).save(consumer);
        makeLumber(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.LUMBER_MAPLE),
                Ingredient.of(BlockRegistry.MAPLE_LOG.get()))
                        .unlockedBy("has_item", has(BlockItemRegistry.MAPLE_LOG.get())).save(consumer);
        makeLumber(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.LUMBER_SAKURA),
                Ingredient.of(BlockRegistry.SAKURA_LOG.get()))
                        .unlockedBy("has_item", has(BlockItemRegistry.SAKURA_LOG.get())).save(consumer);

        makeLumber(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.LUMBER_MAPLE),
                Ingredient.of(BlockRegistry.MAPLE_WOOD.get()))
                        .unlockedBy("has_item", has(BlockItemRegistry.MAPLE_LOG.get()))
                        .save(consumer, new ResourceLocation(SakuraMod.MODID, "maple_lumber_from_wood"));
        makeLumber(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.LUMBER_SAKURA),
                Ingredient.of(BlockRegistry.SAKURA_WOOD.get()))
                        .unlockedBy("has_item", has(BlockItemRegistry.SAKURA_LOG.get()))
                        .save(consumer, new ResourceLocation(SakuraMod.MODID, "sakura_lumber_from_wood"));

        makeLumber(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.LUMBER_MAPLE),
                Ingredient.of(BlockRegistry.STRIPPED_MAPLE_LOG.get()))
                        .unlockedBy("has_item", has(BlockItemRegistry.MAPLE_LOG.get()))
                        .save(consumer, new ResourceLocation(SakuraMod.MODID, "maple_lumber_from_stripped"));
        makeLumber(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.LUMBER_SAKURA),
                Ingredient.of(BlockRegistry.STRIPPED_SAKURA_LOG.get()))
                        .unlockedBy("has_item", has(BlockItemRegistry.SAKURA_LOG.get()))
                        .save(consumer, new ResourceLocation(SakuraMod.MODID, "sakura_lumber_from_stripped"));

        makeLumberToPlank(BlockRegistry.BAMBOO_PLANK, Ingredient.of(SakuraItemTags.LUMBER_BAMBOO))
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);
        makeLumberToPlank(BlockRegistry.MAPLE_PLANK, Ingredient.of(SakuraItemTags.LUMBER_MAPLE))
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);
        makeLumberToPlank(BlockRegistry.SAKURA_PLANK, Ingredient.of(SakuraItemTags.LUMBER_SAKURA))
                .unlockedBy("has_item", has(SakuraItemTags.LUMBER)).save(consumer);

        smeltingRecipe(BlockRegistry.BAMBOO_CHARCOAL_BLOCK.get(), BlockRegistry.BAMBOO_BLOCK.get(), 0.5F).save(consumer,
                new ResourceLocation(SakuraMod.MODID, "bamboo_charcoal_block_from_smelt"));

        smeltingRecipe(BlockRegistry.BAMBOO_CHARCOAL_BLOCK.get(), BlockRegistry.BAMBOO_BLOCK_SUNBURNT.get(), 0.5F)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "bamboo_charcoal_block_from_sunburnt_smelt"));

        smeltingRecipe(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL).get(),
                ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO).get(), 0.5F).save(consumer,
                        new ResourceLocation(SakuraMod.MODID, "bamboo_charcoal_from_smelt"));

        smeltingRecipe(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL).get(),
                ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_SUNBURNT).get(), 0.5F).save(consumer,
                        new ResourceLocation(SakuraMod.MODID, "bamboo_charcoal_from_sunburnt_smelt"));
    }

    private void registerMortarRecipe(Consumer<FinishedRecipe> consumer) {
        StoneMortarRecipeBuilder.mortar(Items.BONE_MEAL, 3).addResult(Items.BONE_MEAL, 3).requires(Tags.Items.BONES)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "bonemeal_from_mortar"));

        StoneMortarRecipeBuilder.mortar(Items.SAND).addResult(Items.FLINT).requires(Tags.Items.GRAVEL).save(consumer,
                new ResourceLocation(SakuraMod.MODID, "flint_from_mortar"));

        StoneMortarRecipeBuilder.mortar(Items.GRAVEL)
                .addResult(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.SALT).get(), 2)
                .requires(Tags.Items.COBBLESTONE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "salt_from_mortar"));

        StoneMortarRecipeBuilder.mortar(Items.COBBLESTONE)
                .addResult(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.ALKALINE).get(), 2).requires(Tags.Items.STONE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "alkaline_from_mortar"));

        StoneMortarRecipeBuilder.mortar(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.CHARCOAL_POWDER).get(), 1)
                .requires(Ingredient.of(Items.CHARCOAL,
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL).get()))
                .requires(Ingredient.of(Items.CHARCOAL,
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BAMBOO_CHARCOAL).get()))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "charcoal_powder"));

        StoneMortarRecipeBuilder.mortar(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BROWN_RICE).get(), 1)
                .addResult(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.BROWN_RICE).get(), 1)
                .requires(SakuraItemTags.SEEDS_RICE).requires(SakuraItemTags.SEEDS_RICE)
                .requires(SakuraItemTags.SEEDS_RICE).requires(SakuraItemTags.SEEDS_RICE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "brown_rice_from_mortar"));
        StoneMortarRecipeBuilder.mortar(Items.GREEN_DYE, 1).addResult(Items.GREEN_DYE, 1).requires(ItemTags.LEAVES)
                .requires(ItemTags.LEAVES).requires(ItemTags.LEAVES).requires(ItemTags.LEAVES)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "dye_green_from_leaves"));

        StoneMortarRecipeBuilder.mortar(FoodRegistry.FOODSET.get(SakuraFoodSet.MINCED_MEAT).get(), 2)
                .addResult(FoodRegistry.FOODSET.get(SakuraFoodSet.MINCED_MEAT).get(), 2)
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(SakuraItemTags.RAW_CHICKEN),
                        new Ingredient.TagValue(SakuraItemTags.RAW_PORK),
                        new Ingredient.TagValue(SakuraItemTags.RAW_BEEF),
                        new Ingredient.TagValue(SakuraItemTags.RAW_MUTTON))))
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(SakuraItemTags.RAW_CHICKEN),
                        new Ingredient.TagValue(SakuraItemTags.RAW_PORK),
                        new Ingredient.TagValue(SakuraItemTags.RAW_BEEF),
                        new Ingredient.TagValue(SakuraItemTags.RAW_MUTTON))))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "minced_meat"));

        StoneMortarRecipeBuilder.mortar(FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER_RAW).get(), 2)
                .addResult(FoodRegistry.FOODSET.get(SakuraFoodSet.BURGER_RAW).get(), 2)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.MINCED_MEAT).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BREADCRUMBS).get())
                .requires(SakuraItemTags.CROPS_ONION).requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "burger_raw"));

        StoneMortarRecipeBuilder.mortar(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get(), 1)
                .addResult(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get(), 1).requires(SakuraItemTags.FISHES)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "surimi_from_mortar"));

        StoneMortarRecipeBuilder.mortar(FoodRegistry.FOODSET.get(SakuraFoodSet.BREADCRUMBS).get(), 2)
                .addResult(FoodRegistry.FOODSET.get(SakuraFoodSet.BREADCRUMBS).get(), 2).requires(SakuraItemTags.BREAD)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "breadcrumbs_from_breads"));

        StoneMortarRecipeBuilder.mortar(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.RICE).get(), 1)
                .requires(SakuraItemTags.RICE_BROWN).requires(SakuraItemTags.RICE_BROWN)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_from_mortar"));
        StoneMortarRecipeBuilder.mortar(Items.SUGAR, 3)
                .addResult(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MOLASSES).get())
                .requires(Items.SUGAR_CANE).save(consumer,
                new ResourceLocation(SakuraMod.MODID, "sugar_from_mortar"));
        StoneMortarRecipeBuilder.mortar(Items.SUGAR, 1)
                .addResult(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MOLASSES).get())
                .requires(Items.BEETROOT).save(consumer,
                new ResourceLocation(SakuraMod.MODID, "beetsugar_from_mortar"));
        StoneMortarRecipeBuilder.mortar(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.FLOUR).get(), 1)
                .requires(SakuraItemTags.GRAIN_WHEAT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "flour_from_mortar"));
        StoneMortarRecipeBuilder.mortar(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.FLOUR_BUCKWHEAT).get(), 1)
                .requires(SakuraItemTags.GRAIN_BUCKWHEAT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "flour_buckwheat_from_mortar"));
        StoneMortarRecipeBuilder.mortar(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.FLOUR_RICE).get(), 1)
                .requires(SakuraItemTags.RICE_RICE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "flour_rice_from_mortar"));

    }

    private void registerFarmerDelightRecipes(Consumer<FinishedRecipe> consumer) {
        whenModLoaded(StoneMortarRecipeBuilder.mortar(ModItems.RICE.get())
                .addResult(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.STRAW).get())
                .requires(ModItems.RICE_PANICLE.get()), FarmersDelight.MODID, "farmer_rice_mortar_from_sakura")
                        .build(consumer, SakuraMod.MODID, "farmer_rice_mortar_from_sakura");
        whenModLoaded(
                ShapedRecipeBuilder.shaped(ModItems.CANVAS.get()).pattern("##").pattern("##")
                        .define('#', SakuraItemTags.STRAW).unlockedBy("has_straw", has(SakuraItemTags.STRAW)),
                FarmersDelight.MODID).build(consumer, SakuraMod.MODID, "canvas_from_sakura");
        whenModLoaded(ShapedRecipeBuilder.shaped(ModItems.TATAMI.get(), 2).pattern("S#").pattern("#S")
                .define('#', SakuraItemTags.STRAW).define('S', ModItems.CANVAS.get())
                .unlockedBy("has_straw", has(SakuraItemTags.STRAW)), FarmersDelight.MODID).build(consumer,
                        SakuraMod.MODID, "farmer_tatami_from_sakura");
        whenModLoaded(
                ShapedRecipeBuilder.shaped(ModItems.ROPE.get(), 3).pattern("s").pattern("s").pattern("s")
                        .define('s', SakuraItemTags.STRAW).unlockedBy("has_straw", has(SakuraItemTags.STRAW)),
                FarmersDelight.MODID).build(consumer, SakuraMod.MODID, "rope_from_sakura");
        whenModLoaded(ShapelessRecipeBuilder.shapeless(ModItems.ORGANIC_COMPOST.get(), 1).requires(Items.DIRT)
                .requires(Items.ROTTEN_FLESH).requires(Items.ROTTEN_FLESH).requires(SakuraItemTags.STRAW)
                .requires(SakuraItemTags.STRAW).requires(Items.BONE_MEAL).requires(Items.BONE_MEAL)
                .requires(Items.BONE_MEAL).requires(Items.BONE_MEAL)
                .unlockedBy("has_rotten_flesh", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ROTTEN_FLESH))
                .unlockedBy("has_straw", has(SakuraItemTags.STRAW)), FarmersDelight.MODID).build(consumer,
                        SakuraMod.MODID, "organic_compost_rotten_flesh_from_sakura");
        whenModLoaded(ShapelessRecipeBuilder.shapeless(ModItems.ORGANIC_COMPOST.get(), 1).requires(Items.DIRT)
                .requires(SakuraItemTags.STRAW).requires(SakuraItemTags.STRAW).requires(Items.BONE_MEAL)
                .requires(Items.BONE_MEAL).requires(ModItems.TREE_BARK.get()).requires(ModItems.TREE_BARK.get())
                .requires(ModItems.TREE_BARK.get()).requires(ModItems.TREE_BARK.get())
                .unlockedBy("has_tree_bark", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TREE_BARK.get()))
                .unlockedBy("has_straw", has(SakuraItemTags.STRAW)), FarmersDelight.MODID).build(consumer,
                        SakuraMod.MODID, "organic_compost_bark_from_sakura");
    }

    private void registerCookingRecipe(Consumer<FinishedRecipe> consumer) {
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.EMPTY, FoodRegistry.CUISINES.get(SakuraCuisineSet.BEEF_STICK).get(), 2)
                .requires(SakuraItemTags.RAW_BEEF).requires(SakuraItemTags.BAMBOO)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "beef_stick_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.EMPTY, FoodRegistry.FOODSET.get(SakuraFoodSet.NATTO).get(), 2, 1.0f, 600)
                .requires(SakuraItemTags.CROPS_SOYBEAN).requires(SakuraItemTags.STRAW)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "natto_fermenting"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.EMPTY, FoodRegistry.CUISINES.get(SakuraCuisineSet.CHICKEN_STICK).get(), 2)
                .requires(SakuraItemTags.RAW_CHICKEN).requires(SakuraItemTags.BAMBOO)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "chicken_stick_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.EMPTY, FoodRegistry.CUISINES.get(SakuraCuisineSet.PORK_STICK).get(), 2)
                .requires(SakuraItemTags.RAW_PORK).requires(SakuraItemTags.BAMBOO)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "pork_stick_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.TOFU).get(), 2)
                .requires(SakuraItemTags.CROPS_SOYBEAN).requires(SakuraItemTags.SALT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "tofu_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.TOFU_FRIED).get(), 2)
                .requires(SakuraItemTags.TOFU).requires(SakuraItemTags.FLOUR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "tofu_fried_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.KAMABOKO).get(), 2)
                .requires(SakuraItemTags.SALT).requires(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "kamaboko_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.SATSUMAAGE).get(), 2)
                .requires(SakuraItemTags.SALT).requires(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "satsumaage_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.KATSU).get(), 2)
                .requires(SakuraItemTags.RAW_PORK)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BREADCRUMBS).get())
                .requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "pork_katsu_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.FRIED_CHICKEN).get(), 2)
                .requires(SakuraItemTags.RAW_CHICKEN)
                .requires(Ingredient.fromValues(Stream.of(
                        new Ingredient.ItemValue(new ItemStack(FoodRegistry.FOODSET.get(SakuraFoodSet.BREADCRUMBS).get())),
                        new Ingredient.TagValue(SakuraItemTags.FLOUR))
                        ))
                .requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "fried_chicken_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.CROQUETTE).get(), 2)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.MASHED_POTATO).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.BREADCRUMBS).get())
                .requires(SakuraItemTags.MILK)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "croquette_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.FISHCAKE).get(), 4)
                .requires(SakuraItemTags.SALT).requires(SakuraItemTags.EGGS).requires(SakuraItemTags.CROPS_TARO)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "hanpen_taro_cooking"));
        
        CookingPotRecipeBuilder
            .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                    ItemRegistry.MATERIALS.get(SakuraNormalItemSet.KAESHI).get(), 4)
            .requires(SakuraItemTags.SUGAR)
            .requires(SakuraItemTags.SOYSAUCE)
            .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
            .save(consumer, new ResourceLocation(SakuraMod.MODID, "kaeshi_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.FISHCAKE).get(), 4)
                .requires(SakuraItemTags.SALT).requires(SakuraItemTags.EGGS)
                .requires(
                        Ingredient.fromValues(Stream.of(
                                new Ingredient.TagValue(SakuraItemTags.CROPS_TARO),
                                new Ingredient.TagValue(Tags.Items.CROPS_POTATO)
                                )
                            )
                        )
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.SURIMI).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "hanpen_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.SOUP_REDBEAN).get(), 2)
                .requires(SakuraItemTags.CROPS_REDBEAN).requires(FoodRegistry.FOODSET.get(SakuraFoodSet.MOCHI).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "soup_redbean_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.CABBAGE_ROLL).get())
                .requires(SakuraItemTags.SALAD_INGREDIENTS_CABBAGE)
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(SakuraItemTags.RAW_CHICKEN),
                        new Ingredient.TagValue(SakuraItemTags.RAW_PORK),
                        new Ingredient.TagValue(SakuraItemTags.RAW_BEEF),
                        new Ingredient.TagValue(SakuraItemTags.RAW_MUTTON),
                        new Ingredient.TagValue(SakuraItemTags.FISHES))))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "cabbage_roll_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.REDBEAN_PASTE).get(), 2)
                .requires(SakuraItemTags.CROPS_REDBEAN).requires(SakuraItemTags.CROPS_REDBEAN)
                .requires(SakuraItemTags.SUGAR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "redbean_paste_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.TOMATO_SAUCE).get(), 2)
                .requires(SakuraItemTags.CROPS_TOMATO).requires(SakuraItemTags.CROPS_TOMATO)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "tomato_sauce_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.DANGO).get(), 2)
                .requires(SakuraItemTags.DOUGH_RICE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "dango_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.DANANKO).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.DANGO).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.REDBEAN_PASTE).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "dananko_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.DANMITARASHI).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.DANGO).get()).requires(SakuraItemTags.SUGAR)
                .requires(SakuraItemTags.SUGAR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "danmitarashi_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.DANSANSYOKU).get())
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.DANGO).get())
                .requires(BlockRegistry.SAKURA_LEAVES.get()).requires(Items.GRASS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "dansansyoku_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.DAIFUKU).get(), 2)
                .requires(SakuraItemTags.DOUGH_RICE)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.REDBEAN_PASTE).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "daifuku_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.KUSA_DAIFUKU).get(), 2)
                .requires(SakuraItemTags.DOUGH_RICE).requires(Items.GRASS)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.REDBEAN_PASTE).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "kusa_daifuku_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.BROWN_RICE_COOKED).get())
                .requires(SakuraItemTags.RICE_BROWN)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "brown_rice_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get())
                .requires(SakuraItemTags.RICE_RICE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_REDBEAN).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.CROPS_REDBEAN)
                .requires(SakuraItemTags.SUGAR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_redbean_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_NATTO).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.NATTO)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_natto_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_NATTO_EGG).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.NATTO).requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_natto_egg_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_BAMBOO).get())
                .requires(SakuraItemTags.RICE_RICE)
                .requires(BlockRegistry.BAMBOOSHOOT.get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_bamboo_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_MUSHROOM).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.MUSHROOMS)
                
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_mushrooms_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_BEEF).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.RAW_BEEF)
                
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_beef_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_PORK).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.RAW_PORK)
                
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_pork_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_FISH).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.RAW_FISHES)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_fish_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_EGG).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_eggs_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_BEEF_EGG).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.RAW_BEEF).requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_beef_eggs_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_PORK_EGG).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.RAW_PORK).requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_pork_eggs_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_KATSU).get())
                .requires(SakuraItemTags.RICE_RICE)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.KATSU).get())
                .requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_katsu_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_OYAKO).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.RAW_CHICKEN).requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_oyako_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_OYAKO_FISH).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.RAW_FISHES).requires(SakuraItemTags.EGGS)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_oyako_fish_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.OMURICE).get())
                .requires(SakuraItemTags.RICE_RICE)
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(SakuraItemTags.RAW_CHICKEN),
                        new Ingredient.TagValue(SakuraItemTags.RAW_PORK),
                        new Ingredient.TagValue(SakuraItemTags.RAW_BEEF),
                        new Ingredient.TagValue(SakuraItemTags.RAW_MUTTON),
                        new Ingredient.TagValue(SakuraItemTags.FISHES))))
                .requires(SakuraItemTags.TOMATOSAUCE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "omurice_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.TEMPURA).get())
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.TEMPURA_BATTER).get())
                .requires(SakuraItemTags.SHRIMP)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "tempura_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.FRIES).get(), 2)
                .requires(Tags.Items.CROPS_POTATO)
                .requires(SakuraItemTags.SALT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "fries_cooking"));
        
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.MASHED_POTATO).get(), 2)
                .requires(Tags.Items.CROPS_POTATO)
                .requires(SakuraItemTags.SALT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "mashed_potato_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.FISH_BAKE_SALT).get())
                .requires(SakuraItemTags.SALT).requires(SakuraItemTags.RAW_FISHES)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "fish_bake_salt_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.FISH_BAKE).get())
                .requires(SakuraItemTags.RAW_FISHES).requires(SakuraItemTags.SOYSAUCE)
                
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "fish_bake_cooking"));
        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.TAMAGOYAKI).get(), 2)
                .requires(SakuraItemTags.EGGS).requires(SakuraItemTags.EGGS).requires(SakuraItemTags.SUGAR)
                .requires(SakuraItemTags.DASHI)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "tamagoyaki_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.OSUIMONO).get(), 2)
                .requires(Items.DRIED_KELP).requires(SakuraItemTags.SOYSAUCE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "osuimono_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.SOUP_MISO).get(), 2)
                .requires(SakuraItemTags.MISO).requires(SakuraItemTags.TOFU)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "soup_miso_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.NIKUJAGA).get(), 2)
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(SakuraItemTags.RAW_PORK),
                        new Ingredient.TagValue(SakuraItemTags.RAW_BEEF))))
                .requires(Tags.Items.CROPS_CARROT).requires(Tags.Items.CROPS_POTATO).requires(SakuraItemTags.SOYSAUCE)
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "nikujaga_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.NIMONO_PUMPKIN).get(), 2)
                .requires(SakuraItemTags.CROPS_PUMPKIN).requires(SakuraItemTags.SOYSAUCE)
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "nimono_pumpkin_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.NIMONO_RADISH).get(), 2)
                .requires(SakuraItemTags.CROPS_RADISH).requires(SakuraItemTags.SOYSAUCE)
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "nimono_radish_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.IMOTAKI).get(), 2)
                .requires(SakuraItemTags.CROPS_TARO).requires(SakuraItemTags.SOYSAUCE)
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "imotaki_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.CHIKUZENNI).get(), 2)
                .requires(SakuraItemTags.RAW_CHICKEN).requires(SakuraItemTags.MUSHROOMS)
                .requires(SakuraItemTags.VEGETABLES).requires(SakuraItemTags.SOYSAUCE)
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "chikuzenni_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.NOPPEI_JIRU).get(), 2)
                .requires(SakuraItemTags.RAW_CHICKEN).requires(SakuraItemTags.CROPS_TARO)
                .requires(SakuraItemTags.VEGETABLES).requires(SakuraItemTags.SOYSAUCE)
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "noppei_jiru_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 250),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.NIMONO_FISH).get(), 2)
                .requires(SakuraItemTags.RAW_FISHES).requires(SakuraItemTags.MISO).requires(SakuraItemTags.SALT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "nimono_fish_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.FUROFUKI_DAIKON).get(), 2)
                .requires(SakuraItemTags.CROPS_RADISH).requires(SakuraItemTags.MISO).requires(SakuraItemTags.SALT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "furofuki_daikon_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 500),
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.DASHI).get(), 1)
                .requires(SakuraItemTags.RAW_FISHES).requires(Items.DRIED_KELP)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "dashi_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.YAKINIKU).get())
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(SakuraItemTags.RAW_PORK),
                        new Ingredient.TagValue(SakuraItemTags.RAW_BEEF),
                        new Ingredient.TagValue(SakuraItemTags.RAW_MUTTON))))
                .requires(SakuraItemTags.SOYSAUCE)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "yakiniku_cooking"));

        CookingPotRecipeBuilder
                .cooking(FluidIngredient.fromTag(SakuraFluidTags.FOOD_OIL, 125),
                        FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_FRIED).get())
                .requires(SakuraItemTags.RICE_RICE).requires(SakuraItemTags.EGGS).requires(SakuraItemTags.VEGETABLES)
                .requires(SakuraItemTags.SALT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rice_fried_cooking"));
    }

    private void registerFermenterRecipe(Consumer<FinishedRecipe> consumer) {
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 500),
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.KOUJI).get(), 2, FluidStack.EMPTY)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(SakuraItemTags.SALT)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "kouji_fermenting"));
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 1000),
                        new FluidStack(FluidRegistry.DOBUROKU.get(), 500))
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(SakuraItemTags.KOUJI)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "doburoku_fermenting"));
        
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 200),
                        new FluidStack(FluidRegistry.BEER.get(), 100))
                .requires(SakuraItemTags.GRAIN)
                .requires(SakuraItemTags.BROWN_MUSHROOMS)
                .requires(SakuraItemTags.SUGAR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "basic_beer_fermenting"));
        
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 200),
                        new FluidStack(FluidRegistry.BEER.get(), 200),0,400)
                .requires(SakuraItemTags.GRAIN)
                .requires(SakuraItemTags.GRAIN)
                .requires(SakuraItemTags.YEAST)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "beer_fermenting"));
        
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 200),
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.YEAST).get(), 4,FluidStack.EMPTY,0,400)
                .requires(SakuraItemTags.BROWN_MUSHROOMS)
                .requires(SakuraItemTags.SUGAR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "yeast_fermenting"));
        
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 100),
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.YEAST).get(), 4,FluidStack.EMPTY,0,200)
                .requires(SakuraItemTags.YEAST)
                .requires(SakuraItemTags.SUGAR)
                .requires(SakuraItemTags.SUGAR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "yeast_multiply"));

        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.BREWERS_ALCOHOL, 500),
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MIRIN).get(), 8, FluidStack.EMPTY)
                .requires(FoodRegistry.FOODSET.get(SakuraFoodSet.RICE_COOKED).get()).requires(SakuraItemTags.KOUJI)
                .requires(SakuraItemTags.SUGAR)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "mirin_fermenting"));

        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromFluid(FluidRegistry.DOBUROKU.get(), 500),
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.SAKE_KASU).get(), 2,
                        new FluidStack(FluidRegistry.SAKE.get(), 250), 10F, 500)
                .requires(SakuraItemTags.DUST_CHARCOAL)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "sake_charcoal_fermenting"));
        
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromFluid(FluidRegistry.DOBUROKU.get(), 500),
                        new FluidStack(FluidRegistry.SAKE.get(), 100), 10F, 1000)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "sake_fermenting"));
        FermenterRecipeBuilder
                .fermenting(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 1000),
                        ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MISO).get(), 4,
                        FluidStack.EMPTY)
                .addResult(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.SOYSAUCE).get(), 4)
                .requires(SakuraItemTags.CROPS_SOYBEAN)
                .requires(SakuraItemTags.CROPS_SOYBEAN)
                .requires(SakuraItemTags.KOUJI)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "miso_fermenting"));
    }

    private void registerDistillerRecipe(Consumer<FinishedRecipe> consumer) {
        DistillerRecipeBuilder
                .distillation(FluidIngredient.fromFluid(FluidRegistry.SAKE.get(), 1000),
                        new FluidStack(FluidRegistry.SHOUCHU.get(), 500))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "shouchu_from_sake_distillation"));
        
        DistillerRecipeBuilder
                .distillation(FluidIngredient.fromFluid(FluidRegistry.BEER.get(), 1000),
                        new FluidStack(FluidRegistry.WHISKEY.get(), 500))
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "whiskey_from_beer_distillation"));
        
        DistillerRecipeBuilder
                .distillation(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 500),
                        new FluidStack(FluidRegistry.RUM.get(), 100))
                .requires(Items.SUGAR_CANE)
                .requires(Items.SUGAR_CANE)
                .requires(SakuraItemTags.YEAST)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "rum_cane_distillation"));
        
        DistillerRecipeBuilder
            .distillation(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 500),
                    new FluidStack(FluidRegistry.RUM.get(), 100))
            .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MOLASSES).get())
            .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.MOLASSES).get())
            .requires(SakuraItemTags.YEAST)
            .save(consumer, new ResourceLocation(SakuraMod.MODID, "rum_molasses_distillation"));
        
        DistillerRecipeBuilder
                .distillation(FluidIngredient.fromTag(SakuraFluidTags.WATER_WATER, 500),
                        new FluidStack(FluidRegistry.SHOUCHU.get(), 100))
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.SAKE_KASU).get())
                .requires(ItemRegistry.MATERIALS.get(SakuraNormalItemSet.SAKE_KASU).get())
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "shouchu_from_sakekasu_distillation"));
    }

    private void registerChoppingRecipes(Consumer<FinishedRecipe> consumer) {
        ChoppingBoardRecipeBuilder.chop(FoodRegistry.FOODSET.get(SakuraFoodSet.MACHINED_FISH).get())
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.ItemValue(new ItemStack(Items.COD)),
                        new Ingredient.ItemValue(new ItemStack(Items.SALMON)),
                        new Ingredient.ItemValue(new ItemStack(Items.TROPICAL_FISH)))))
                .requiresTool(SakuraItemTags.TOOLS_KNIVES_FISH)
                .addByproduce(FoodRegistry.FOODSET.get(SakuraFoodSet.MACHINED_FISH).get())
                .addByproduceWithChance(Items.BONE_MEAL, 0.5F)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "machined_fish_chopping"));

        ChoppingBoardRecipeBuilder.chop(FoodRegistry.FOODSET.get(SakuraFoodSet.SLICED_CABBAGE).get())
                .requires(SakuraItemTags.CROPS_CABBAGE).requiresTool(SakuraItemTags.TOOLS_KNIVES_FISH)
                .addByproduce(FoodRegistry.FOODSET.get(SakuraFoodSet.SLICED_CABBAGE).get())
                .addByproduceWithChance(FoodRegistry.FOODSET.get(SakuraFoodSet.SLICED_CABBAGE).get(), 0.5F)
                .save(consumer, new ResourceLocation(SakuraMod.MODID, "sliced_cabbage_chopping"));
    }

    private void foodSmeltingRecipes(String name, ItemLike ingredient, ItemLike result, float experience,
            Consumer<FinishedRecipe> consumer) {
        String namePrefix = new ResourceLocation(SakuraMod.MODID, name).toString();
        smeltingRecipe(result, ingredient, experience).save(consumer);
        campfireRecipe(result, ingredient, experience).save(consumer, namePrefix + "_from_campfire_cooking");
        smokingRecipe(result, ingredient, experience).save(consumer, namePrefix + "_from_smoking");
    }

    public ShapedRecipeBuilder makeLumberToPlank(Supplier<? extends Block> blockOut, Ingredient ingreIn) {
        return ShapedRecipeBuilder.shaped(blockOut.get()).pattern("##").pattern("##").define('#', ingreIn);
    }

    public ShapelessRecipeBuilder makeLumber(Supplier<? extends Item> ingotOut, Ingredient ingreIn) {
        return ShapelessRecipeBuilder.shapeless(ingotOut.get(), 8).requires(ingreIn);
    }

    public ShapelessRecipeBuilder makeItemToBucket(Supplier<? extends Item> ingotOut, Ingredient ingreIn) {
        return ShapelessRecipeBuilder.shapeless(ingotOut.get()).requires(ingreIn).requires(ingreIn).requires(ingreIn)
                .requires(ingreIn).requires(ingreIn).requires(ingreIn).requires(ingreIn).requires(ingreIn)
                .requires(Items.BUCKET);
    }

    public ConditionalRecipe.Builder whenModLoaded(CookingPotRecipeBuilder recipe, String modid, String path) {
        return ConditionalRecipe.builder().addCondition(new ModLoadedCondition(modid))
                .addRecipe(consumer -> recipe.save(consumer, new ResourceLocation(SakuraMod.MODID, path)));
    }

    public ConditionalRecipe.Builder whenModLoaded(StoneMortarRecipeBuilder recipe, String modid, String path) {
        return ConditionalRecipe.builder().addCondition(new ModLoadedCondition(modid))
                .addRecipe(consumer -> recipe.save(consumer, new ResourceLocation(SakuraMod.MODID, path)));
    }
}
