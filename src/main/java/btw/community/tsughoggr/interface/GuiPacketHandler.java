package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class GuiPacketHandler {
	private static final GuiPacketHandler instance = new GuiPacketHandler();
	private GuiPacketHandler(){

	}
	public static GuiPacketHandler
	getInstance(){
		return instance;
	}
	public boolean
	handleClientPacket(Minecraft mc, Packet250CustomPayload packet){
		try {
			EntityClientPlayerMP player = mc.thePlayer;
			WorldClient world = mc.theWorld;
			DataInputStream stream;
			switch(packet.channel){
				case "CC|TI":
					stream = new DataInputStream(new ByteArrayInputStream(packet.data));
					int wid = stream.readInt();
					TapeInscriberTileEntity tile = new TapeInscriberTileEntity();
					tile.xCoord = stream.readInt();
					tile.yCoord = stream.readInt();
					tile.zCoord = stream.readInt();
					GuiContainer gui = new TapeInscriberGui(player.inventory, world, tile);
					if(gui != null){
						mc.displayGuiScreen(gui);
						player.openContainer.windowId = wid;
					}
						
					return true;

			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}	
}
