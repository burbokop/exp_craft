package com.example.examplemod;

import com.example.examplemod.network.PacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.example.examplemod.blocks.ModBlocks;
import com.example.examplemod.entities.MashineTileEntity;
import com.example.examplemod.entities.ModTileEntities;
import com.example.examplemod.entities.TileEntityBase;
import com.example.examplemod.fluids.ModFluids;
import com.example.examplemod.handlers.MashineGuiHandler;
import com.example.examplemod.items.ModItems;
import com.example.examplemod.proxy.CommonProxy;

//import com.example.examplemod.utils.SSS;

@Mod(modid = ExampleMod.MODID, name = ExampleMod.NAME, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MODID = "examplemod";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.0";
    
    @Instance(MODID)
    public static ExampleMod instance;
    
    @SidedProxy(serverSide = "com.example.examplemod.proxy.CommonProxy", clientSide = "com.example.examplemod.proxy.ClientProxy")
    public static CommonProxy proxy;
    
    public static SimpleNetworkWrapper network;
    
    @Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			ModItems.register(event.getRegistry());
			ModBlocks.registerItemBlocks(event.getRegistry());
		}

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			ModBlocks.register(event.getRegistry());
			ModTileEntities.register();
		}

		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			ModItems.registerModels();
			ModBlocks.registerModels();
		}

	}
    
    static {
        FluidRegistry.enableUniversalBucket();
    }
    
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("example_tab") {
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.iii, 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta());
        }
    };


    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();     
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        //network.registerMessage(new PacketUpdateMashine.Handler(), PacketUpdateMashine.class, 0, Side.CLIENT);
		//network.registerMessage(new PacketRequestUpdateMashine.Handler(), PacketRequestUpdateMashine.class, 1, Side.SERVER);
        //int nextId = 0;
        //nextId = PacketRegistry.registerSharedData(MashineTileEntity.SharedData.class, network, nextId);

        PacketRegistry.register2(network);

        ModFluids.register();        
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(ExampleMod.instance, new MashineGuiHandler());  

    	
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
