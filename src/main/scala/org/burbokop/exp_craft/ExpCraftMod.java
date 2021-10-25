package org.burbokop.exp_craft;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.burbokop.exp_craft.blocks.ModBlocks;
import org.burbokop.exp_craft.enchantments.ModEnchantments;
import org.burbokop.exp_craft.entities.ModTileEntities;
import org.burbokop.exp_craft.fluids.ModFluids;
import org.burbokop.exp_craft.handlers.GuiHandler;
import org.burbokop.exp_craft.items.ModItems;
import org.burbokop.exp_craft.network.ModNetwork;
import org.burbokop.exp_craft.proxy.CommonProxy;

@Mod(modid = ExpCraftMod.MOD_ID, name = ExpCraftMod.NAME, version = ExpCraftMod.VERSION, acceptedMinecraftVersions = ExpCraftMod.MC_VERSION)
public class ExpCraftMod {
    public static final String MOD_ID = "exp_craft";
    public static final String NAME = "Exp Craft";
    public static final String VERSION = "0.0.1";
    public static final String MC_VERSION = "1.12.2";

    @Mod.Instance(MOD_ID)
    public static ExpCraftMod instance;

    @SidedProxy(serverSide = "org.burbokop.exp_craft.proxy.CommonProxy", clientSide = "org.burbokop.exp_craft.proxy.ClientProxy")
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;
    public static Logger logger;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)  {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event)  {
            ModBlocks.register(event.getRegistry());
            ModTileEntities.register();
        }

        @SubscribeEvent
        public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
            ModEnchantments.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }
    }


    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("exp_craft_tab") {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.GOLD_EXP_CRISTAL());
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)  {
        logger = event.getModLog();
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        //network.registerMessage(new PacketUpdateMashine.Handler(), PacketUpdateMashine.class, 0, Side.CLIENT);
        //network.registerMessage(new PacketRequestUpdateMashine.Handler(), PacketRequestUpdateMashine.class, 1, Side.SERVER);
        //int nextId = 0;
        //nextId = PacketRegistry.registerSharedData(MashineTileEntity.SharedData.class, network, nextId);

        ModNetwork.register(network);

        ModFluids.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)  {
        NetworkRegistry.INSTANCE.registerGuiHandler(ExpCraftMod.instance, new GuiHandler());


        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
};


