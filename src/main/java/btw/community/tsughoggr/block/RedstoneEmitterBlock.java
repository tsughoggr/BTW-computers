package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.Random;

public class RedstoneEmitterBlock extends Block implements PacketHandler {
	public Icon blockicon;

	public RedstoneEmitterBlock(int id){
		super(id, Material.rock);
	}

	/*Redstone*/
	public int
	isProvidingWeakPower(IBlockAccess ba, int x, int y, int z, int fc){
		int md = ba.getBlockMetadata(x,y,z);
		return md;
	}
	public int
	isProvidingStrongPower(IBlockAccess ba, int x, int y, int z, int fc){
		int md = ba.getBlockMetadata(x,y,z);
		return md;
	}
	public boolean
	canProvidePower(){
		return true;
	}

	/*PacketHandler*/
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp) {
		int md = wld.getBlockMetadata(x,y,z);
		if(pk == 0){
			wld.setBlockAndMetadataWithNotify(x,y,z,ComputerBlocks.redstoneSignal.blockID, 0);
		} else {
			wld.setBlockAndMetadataWithNotify(x,y,z,ComputerBlocks.redstoneSignalActive.blockID, pk);

		}
		wld.notifyBlocksOfNeighborChange(x,y,z,this.blockID);

	}
	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp){
		int md = wld.getBlockMetadata(x,y,z);
		if(pk == md)
			return;
		if(pk == 0){
			wld.setBlockAndMetadataWithNotify(x,y,z,ComputerBlocks.redstoneSignal.blockID, 0);
		} else {
			wld.setBlockAndMetadataWithNotify(x,y,z,ComputerBlocks.redstoneSignalActive.blockID, pk);

		}
		wld.notifyBlocksOfNeighborChange(x,y,z,this.blockID);

	}
	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return true;
	}

	/*Client*/
	public void
	registerIcons(IconRegister ir){
		blockicon = ir.registerIcon("ccBlockRedstonePower");
	}
	public Icon
	getIcon(int sid, int md){
		return blockicon;
	}	

}
