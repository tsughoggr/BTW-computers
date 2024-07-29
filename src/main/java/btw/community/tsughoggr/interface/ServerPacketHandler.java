package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;


import net.minecraft.server.MinecraftServer;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class ServerPacketHandler {
	private static final ServerPacketHandler instance = new ServerPacketHandler();
	private ServerPacketHandler(){

	}
	public static ServerPacketHandler
	getInstance(){
		return instance;
	}
	public boolean
	handlePacket(NetServerHandler handler, Packet250CustomPayload packet) {
		try {
			EntityPlayerMP player = handler.playerEntity;
			World world = handler.playerEntity.worldObj;
			switch(packet.channel){
				case "CC|TI":
					DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
					int x = stream.readInt();
					int y = stream.readInt();
					int z = stream.readInt();
					int cp = stream.readInt();
					byte val = (byte)stream.readInt();
					TapeInscriberTileEntity tile = (TapeInscriberTileEntity)world.getBlockTileEntity(x,y,z);
					if(tile == null)
						return false;
					tile.updateTapeValue(cp, val);
					return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
