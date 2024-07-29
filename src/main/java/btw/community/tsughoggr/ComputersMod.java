package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;

import btw.AddonHandler;
import btw.BTWAddon;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class ComputersMod extends BTWAddon{
	public static ComputersMod instance;
	public static final String computerVersion = "0.1";
	private Map<String, String> confprop;
	public int maxTapeSize;

	public
	ComputersMod(){
		super("Computer Systems", computerVersion, "CC");
		instance = this;
	}
	public static ComputersMod
	getInstance(){
		if(instance == null)
			instance = new ComputersMod();
		return instance;
	}
	private void
	registerProperty(String st, int in){
		registerProperty(st, Integer.toString(in));
	}
	private void
	normalizeProperty(String prop, int def){
		if(confprop.get(prop) == null){
			confprop.put(prop, Integer.toString(def));
		}
		try { 
			Integer.parseInt(confprop.get(prop));
		} catch (NumberFormatException e){
			confprop.put(prop, Integer.toString(def));
		}
		

	}
	public int
	getIdFromProp(String prop){
		if(confprop == null)
			throw new InvalidConfigException("Config map is null after sanitization");
		if(confprop.get(prop) == null)
			throw new InvalidConfigException("Config property: " + prop +" is called but not defined");
		return Integer.parseInt(confprop.get(prop)); /*We would actually want this to crash if it throws NFE*/
	}
	private void
	SanitizeConfigMap(){
		if(confprop == null){
			confprop = new HashMap<String, String>();
		}

		normalizeProperty("tapedrive.blockID", 1760);
		normalizeProperty("CPU.blockID", 1761);
		normalizeProperty("cable.blockID", 1762);
		normalizeProperty("keyboard.blockID", 1763);
		normalizeProperty("tapeInscriber.blockID", 1764);
		normalizeProperty("tapeDrive.blockID", 1765);
		normalizeProperty("redstoneReader.blockID", 1766);
		normalizeProperty("redstoneSignal.blockID", 1767);
		normalizeProperty("redstoneSignalActive.blockID", 1768);
		normalizeProperty("inventoryReader.blockID", 1769);
		normalizeProperty("filter.blockID", 1770);
		normalizeProperty("router.blockID", 1771);
		normalizeProperty("switch.blockID", 1772);
		normalizeProperty("tapeDeck.blockID", 1773);

		normalizeProperty("tape.itemID", 24040);
		normalizeProperty("maxTapeSize", 8192);


	}
	public void
	preInitialize(){
		registerProperty("tapedrive.blockID", 1760);
		registerProperty("CPU.blockID", 1761);
		registerProperty("cable.blockID", 1762);
		registerProperty("keyboard.blockID", 1763);
		registerProperty("tapeInscriber.blockID", 1764);
		registerProperty("tapeDrive.blockID", 1765);
		registerProperty("redstoneReader.blockID", 1766);
		registerProperty("redstoneSignal.blockID", 1767);
		registerProperty("redstoneSignalActive.blockID", 1768);
		registerProperty("inventoryReader.blockID", 1769);
		registerProperty("filter.blockID", 1770);
		registerProperty("router.blockID", 1771);
		registerProperty("switch.blockID", 1772);
		registerProperty("tapeDeck.blockID", 1773);

		registerProperty("tape.itemID", 24040);
		registerProperty("maxTapeSize", 8192);
	}
	public void
	initialize(){
		AddonHandler.logMessage("Computer Systems " + computerVersion + " initializing");
		confprop = loadConfigProperties();
		this.SanitizeConfigMap();

		maxTapeSize = getIdFromProp("maxTapeSize");

		this.InitializeBlocks();
		this.InitializeItems();
		this.InitializeTileEntities();
		this.InitializeRenderMappings();

		AddonHandler.logMessage("Computer Systems initializaiton complete");
	}

	public void
	InitializeBlocks(){
		ComputerBlocks.initialize();
	}
	public void
	InitializeItems(){
		ComputerItems.initialize();
	}
	public void
	InitializeTileEntities(){
		TileEntity.addMapping(CPUTileEntity.class, "CPUTileEntity");
		TileEntity.addMapping(TapeInscriberTileEntity.class, "TapeInscriberTileEntity");
		TileEntity.addMapping(TapeDriveTileEntity.class, "TapeDriveTileEntity");
		TileEntity.addMapping(InventoryReaderTileEntity.class, "InventoryReaderTileEntity");
		TileEntity.addMapping(TapeDeckTileEntity.class, "TapeDeckTileEntity");


	}


	public boolean
	serverCustomPacketReceived(NetServerHandler handler, Packet250CustomPayload packet){
		return ServerPacketHandler.getInstance().handlePacket(handler, packet) ;
	}
	/*Client Overrides*/
	public boolean
	clientCustomPacketReceived(Minecraft mc, Packet250CustomPayload packet){

		return GuiPacketHandler.getInstance().handleClientPacket(mc, packet);


	}
	public void
	InitializeRenderMappings(){
		TileEntityRenderer.instance.addSpecialRendererForClass(TapeDriveTileEntity.class, new TapeDriveRendererTileEntity());
		TileEntityRenderer.instance.addSpecialRendererForClass(TapeDeckTileEntity.class, new TapeDeckRendererTileEntity());

	}
}
