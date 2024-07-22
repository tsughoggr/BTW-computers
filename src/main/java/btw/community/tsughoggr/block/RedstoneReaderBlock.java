package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.Random;

public class RedstoneReaderBlock extends Block implements PacketHandler {
	private Icon blockicon;

	public RedstoneReaderBlock (int id){
		super(id, Material.rock);
	}

	public void
	updateTick(World world, int i, int j, int k, Random rn){
		updateNeighboringTIPHs(world, i, j, k);
	}

	/*PacketHandler*/
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp) {
		if(wld.isRemote)
			return;
		if(pk != 0){
			wld.setBlockMetadata(x,y,z,pk);
		}
		wld.scheduleBlockUpdate(x,y,z, blockID, 5);
	}
	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp){
		if(wld.isRemote || ((pk != 0) &&(pk == wld.getBlockMetadata(x,y,z))))
			return;
		if(pk != 0){
			wld.setBlockMetadata(x,y,z,pk);
		}
		wld.scheduleBlockUpdate(x,y,z, blockID, 5);
	}
	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return true;
	}
	public void
	updateNeighboringTIPHs(World wld, int x, int y, int z){
		byte md = (byte)wld.getBlockMetadata(x,y,z);
		byte rst;
		rst = (byte)wld.getStrongestIndirectPower(x,y,z);
		if(rst == 0){
			rst = (byte)wld.getStrongestIndirectPower(x,y,z);
		}

		if(md != 0){
			wld.setBlockMetadata(x,y,z, 0);
		}
		if(Block.blocksList[wld.getBlockId(x, y, z-1)] instanceof PacketHandler){
			if(md != 0){
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z-1)]).handlePacket(wld, x, y, z-1, 3, md, 0, null) ;
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z-1)]).clockPacket(wld, x, y, z-1, 3, md, 0, null);
			}
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z-1)]).handlePacket(wld, x, y, z-1, 3, rst, 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z-1)]).clockPacket(wld, x, y, z-1, 3, rst, 0, null);
		}
		if(Block.blocksList[wld.getBlockId(x, y, z+1)] instanceof PacketHandler){
			if(md != 0){
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z+1)]).handlePacket(wld, x, y, z+1, 2, md, 0, null) ;
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z+1)]).clockPacket(wld, x, y, z+1, 2, md, 0, null);
			}
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z+1)]).handlePacket(wld, x, y, z+1, 2, rst, 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z+1)]).clockPacket(wld, x, y, z+1, 2, rst, 0, null);
		}
		if(Block.blocksList[wld.getBlockId(x-1, y, z)] instanceof PacketHandler){

			if(md != 0){
				((PacketHandler)Block.blocksList[wld.getBlockId(x-1, y, z)]).handlePacket(wld, x-1, y, z, 5, md, 0, null) ;
				((PacketHandler)Block.blocksList[wld.getBlockId(x-1, y, z)]).clockPacket(wld, x-1, y, z, 5, md, 0, null);
			}
			((PacketHandler)Block.blocksList[wld.getBlockId(x-1, y, z)]).handlePacket(wld, x-1, y, z, 5, rst, 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x-1, y, z)]).clockPacket(wld, x-1, y, z, 5, rst, 0, null);
		
		}
		if(Block.blocksList[wld.getBlockId(x+1, y, z)] instanceof PacketHandler){
			if(md != 0){
				((PacketHandler)Block.blocksList[wld.getBlockId(x+1, y, z)]).handlePacket(wld, x+1, y, z, 4, md, 0, null) ;
				((PacketHandler)Block.blocksList[wld.getBlockId(x+1, y, z)]).clockPacket(wld, x+1, y, z, 4, md, 0, null);
			}
			((PacketHandler)Block.blocksList[wld.getBlockId(x+1, y, z)]).handlePacket(wld, x+1, y, z, 4, rst, 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x+1, y, z)]).clockPacket(wld, x+1, y, z, 4, rst, 0, null);
		}
		if(Block.blocksList[wld.getBlockId(x, y+1, z)] instanceof PacketHandler){
			if(md != 0){
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y+1, z)]).handlePacket(wld, x, y+1, z, 0, md, 0, null) ;
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y+1, z)]).clockPacket(wld, x, y+1, z, 0, md, 0, null);
			}
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y+1, z)]).handlePacket(wld, x, y+1, z, 0, rst, 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y+1, z)]).clockPacket(wld, x, y+1, z, 0, rst, 0, null);
		}
		if(Block.blocksList[wld.getBlockId(x, y-1, z)] instanceof PacketHandler){
			if(md != 0){
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y-1, z)]).handlePacket(wld, x, y-1, z, 1, md, 0, null) ;
				((PacketHandler)Block.blocksList[wld.getBlockId(x, y-1, z)]).clockPacket(wld, x, y-1, z, 1, md, 0, null);
			}
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y-1, z)]).handlePacket(wld, x, y-1, z, 1, rst, 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y-1, z)]).clockPacket(wld, x, y-1, z, 1, rst, 0, null);
		}
	}

	/*Client*/
	public void
	registerIcons(IconRegister ir){
		blockicon = ir.registerIcon("ccBlockTapeInscriber_bottom");
	}
	public Icon
	getIcon(int sid, int md){
		return blockicon;
	}	

}
