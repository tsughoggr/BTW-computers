package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemBlock;

public class ComputerBlocks {
	public static Block tapedrive;
	public static Block CPU;
	public static Block cable;
	public static Block keyboard;
	public static Block tapeInscriber;
	public static Block tapeDrive;

	public static Block redstoneReader;
	public static Block redstoneSignal;
	public static Block redstoneSignalActive;
	public static Block inventoryReader;

	public static Block filter;
	public static Block router;
	public static Block switcher;
	public static Block tapeDeck;

	public static void
	initialize(){
		ComputersMod cm = ComputersMod.getInstance();
		CPU = new CPUBlock(cm.getIdFromProp("CPU.blockID")).setUnlocalizedName("CPUBlock").setCreativeTab(CreativeTabs.tabDecorations);
		cable = new CableBlock(cm.getIdFromProp("cable.blockID")).setUnlocalizedName("cableBlock").setCreativeTab(CreativeTabs.tabDecorations);
		keyboard = new KeyboardBlock(cm.getIdFromProp("keyboard.blockID")).setUnlocalizedName("keyboard").setCreativeTab(CreativeTabs.tabDecorations);
		tapeInscriber = new TapeInscriberBlock(cm.getIdFromProp("tapeInscriber.blockID")).setUnlocalizedName("tapeInscriber").setCreativeTab(CreativeTabs.tabDecorations);
		tapeDrive = new TapeDriveBlock(cm.getIdFromProp("tapeDrive.blockID")).setUnlocalizedName("tapeDrive").setCreativeTab(CreativeTabs.tabDecorations);
		redstoneReader = new RedstoneReaderBlock(cm.getIdFromProp("redstoneReader.blockID")).setUnlocalizedName("redstoneReader").setCreativeTab(CreativeTabs.tabDecorations);
		redstoneSignal = new RedstoneEmitterBlock(cm.getIdFromProp("redstoneSignal.blockID")).setLightValue(.2F).setUnlocalizedName("redstoneSignal").setCreativeTab(CreativeTabs.tabDecorations);
		redstoneSignalActive = new RedstoneEmitterBlock(cm.getIdFromProp("redstoneSignalActive.blockID")).setLightValue(.5F).setUnlocalizedName("redstoneSignalActive").setCreativeTab(CreativeTabs.tabDecorations);
		inventoryReader = new InventoryReaderBlock(cm.getIdFromProp("inventoryReader.blockID")).setUnlocalizedName("inventoryReader").setCreativeTab(CreativeTabs.tabDecorations);
		filter = new FilterBlock(cm.getIdFromProp("filter.blockID")).setUnlocalizedName("packetFilter").setCreativeTab(CreativeTabs.tabDecorations);
		router = new RouterBlock(cm.getIdFromProp("router.blockID")).setUnlocalizedName("packetRouter").setCreativeTab(CreativeTabs.tabDecorations);
		switcher = new SwitchBlock(cm.getIdFromProp("switch.blockID")).setUnlocalizedName("packetSwitch").setCreativeTab(CreativeTabs.tabDecorations);
		tapeDeck = new TapeDeckBlock(cm.getIdFromProp("tapeDeck.blockID")).setUnlocalizedName("tapeDeck").setCreativeTab(CreativeTabs.tabDecorations);

		new ItemBlock(cm.getIdFromProp("CPU.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("cable.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("keyboard.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("tapeInscriber.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("tapeDrive.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("redstoneReader.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("redstoneSignal.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("redstoneSignalActive.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("inventoryReader.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("filter.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("router.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("switch.blockID") - 256);
		new ItemBlock(cm.getIdFromProp("tapeDeck.blockID") - 256);
		
	}

}
