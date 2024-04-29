package com.petrolpark.destroy.item.creativeModeTab;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.config.DestroySubstancesConfigs;
import com.petrolpark.destroy.item.BadgeItem;
import com.petrolpark.destroy.item.DestroyItems;
import com.simibubi.create.foundation.utility.Pair;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DestroyCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Destroy.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register("base",
		() -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.destroy.base"))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.icon(() -> DestroyItems.LOGO.asStack())
			.displayItems(new DestroyDisplayItemsGenerator())
			.build()
    );

    public static void register(IEventBus modEventBus) {
		TABS.register(modEventBus);
	};

	public static class DestroyDisplayItemsGenerator implements DisplayItemsGenerator {

		public static List<ItemProviderEntry<?>> excludedItems = new ArrayList<>();
		public static List<Pair<ItemProviderEntry<?>, Supplier<Boolean>>> conditionallyExcludedItems = new ArrayList<>();

		static {
			excludedItems.addAll(List.of(
				// Meta
				DestroyItems.LOGO,
				DestroyItems.MOLECULE_DISPLAY,

				// Temporary items
				DestroyItems.UNFINISHED_BLACKLIGHT,
				DestroyItems.UNFINISHED_CIRCUIT_BOARD,
				DestroyItems.UNFINISHED_VOLTAIC_PILE,
				DestroyItems.UNFINISHED_UNVARNISHED_PLYWOOD,
				DestroyItems.UNPROCESSED_MASHED_POTATO,
				DestroyItems.UNPROCESSED_NAPALM_SUNDAE,
				DestroyItems.UNFINISHED_CARD_STOCK
			));
		};

		static {
			conditionallyExcludedItems.addAll(List.of(
				// Baby Blue-related Items
				Pair.of(DestroyItems.BABY_BLUE_CRYSTAL, DestroySubstancesConfigs::babyBlueEnabled),
				Pair.of(DestroyItems.BABY_BLUE_POWDER, DestroySubstancesConfigs::babyBlueEnabled),
				Pair.of(DestroyItems.BABY_BLUE_SYRINGE, DestroySubstancesConfigs::babyBlueEnabled)
			));
		};

		
		@Override
		public void accept(ItemDisplayParameters parameters, Output output) {
			for (RegistryEntry<Item> entry : Destroy.REGISTRATE.getAll(Registries.ITEM)) {
				if (conditionallyExcludedItems.stream().filter(p -> p.getFirst().equals(entry)).map(pair -> pair.getSecond().get()).findFirst().orElse(false)) continue;
				if (!excludedItems.contains(entry) && !(entry.get() instanceof BadgeItem)) {
					output.accept(new ItemStack(entry.get().asItem()));
				};
			};
		};
		
	};
};
