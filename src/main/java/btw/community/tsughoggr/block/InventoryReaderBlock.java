package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.Random;

public class InventoryReaderBlock extends BlockContainer implements PacketHandler {
	private Icon blockicon;
	private Icon sideicon;
	private Icon bicon;

	public InventoryReaderBlock(int id){
		super(id, Material.rock);
	}
	public TileEntity
	createNewTileEntity(World world){
		return new InventoryReaderTileEntity();
	}

	public void
	onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player, ItemStack item){
		int cfac = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360F) + 0.5D) &3;
		int face;
		switch(cfac){
			case 0:
				face = 0;
				break;
			case 1:
				face = 3;
				break;
			case 2:
				face = 1;
				break;
			default:
				face = 2;
				break;
		}
		world.setBlockMetadataWithNotify(x,y,z,face);
	}
	/*PacketHandler*/
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp) {
		if(!wld.isRemote && (fc == 0 || fc == 1)){
			InventoryReaderTileEntity tile = (InventoryReaderTileEntity)wld.getBlockTileEntity(x,y,z);
			if(tile != null)
				tile.handlePacket(pk, bp) ;

		}
	}
	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp){}


	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return (fc == 0 || fc == 1);
	}


	/*Client*/
	public void
	registerIcons(IconRegister ir){
		blockicon = ir.registerIcon("ccBlockComputer_bottom_on");
		sideicon = ir.registerIcon("ccBlockInventoryContentsReaderFront");
		bicon = ir.registerIcon("ccBlockComputerFace_blank");
	}
	public Icon
	getIcon(int sid, int md){
		switch(sid){
			case 0:
			case 1:
				return blockicon;
			default:
				return (md + 2 == sid)? sideicon:bicon;
		}
	}

}
