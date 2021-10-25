package org.burbokop.exp_craft.enchantments;

import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import org.burbokop.exp_craft.ExpCraftMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModEnchantments {
  public static final EnchantmentAttraction ATTRACTION = new EnchantmentAttraction();

  public static void register(IForgeRegistry<Enchantment> registry) {
    System.out.println("Registering Enchantments");
    registry.register(
            ATTRACTION
    );
  }
}
