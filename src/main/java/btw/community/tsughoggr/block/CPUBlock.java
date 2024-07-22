package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

public class CPUBlock extends BlockContainer implements PacketHandler {
	private Icon[] sideicon = new Icon[2];
	private Icon[] topicon = new Icon[2];

	public CPUBlock(int id){
		super(id, Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
	}



	public TileEntity
	createNewTileEntity(World world){
		return new CPUTileEntity();
	}

	public boolean
	onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float fx, float fy, float fz){
		int md = world.getBlockMetadata(x,y,z);
		if(!world.isRemote){
			world.setBlockMetadataWithNotify(x,y,z, md==0?1:0);
		}
		return true;
	}

	/*PacketHandler*/
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp) {
		byte pk = (byte)(sp & 0xF);
		if(!wld.isRemote){
			System.out.println("Packet{ Reg: " + fc + " val: " + (pk & 0xf) + " depth: " + dp + "}");
		}
	}
	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){
		byte pk = (byte)(sp & 0xF);
		if(!wld.isRemote){
			System.out.println("Clock{ Reg: " + fc + " val: " + (pk & 0xf) + " depth: " + dp + "}"); /*Ignore pk val, is for cable stuff*/
		}
	}
	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return true;
	}

	/*Client*/
	public Icon
	getIcon(int fc, int md){
		switch(fc){
			case 0:
			case 1:
				return topicon[md>0?1:0];
			default:
				return sideicon[md>0?1:0];

		}
	}
	public void
	registerIcons(IconRegister ir){
		sideicon[0] = ir.registerIcon("ccBlockCPU_side_off");
		sideicon[1] = ir.registerIcon("ccBlockCPU_side_on");
		topicon[0] = ir.registerIcon("ccBlockComputer_bottom_off");
		topicon[1] = ir.registerIcon("ccBlockComputer_bottom_on");
	}
}
