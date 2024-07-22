package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

public class TapeDriveBlock extends BlockContainer implements PacketHandler {
	private Icon sideicon;
	private Icon topicon;

	public TapeDriveBlock(int id){
		super(id, Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	public TileEntity
	createNewTileEntity(World world){
		return new TapeDriveTileEntity();
	}
	@Override
	public boolean
	hasLargeCenterHardPointToFacing(IBlockAccess ba, int i, int j, int k, int face, boolean bt){
		return false;
	}

	public void
	breakBlock(World world, int x, int y, int z, int md, int mod){

		TapeDriveTileEntity tape = (TapeDriveTileEntity)world.getBlockTileEntity(x,y,z);
		if(tape != null && !world.isRemote){
				ItemStack is = tape.tape;
				if(is!= null){
					btw.item.util.ItemUtils.ejectStackWithRandomOffset(world, x, y, z, is);
				}
		}
		super.breakBlock(world,x,y,z,md,mod);

	}
	public boolean
	onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float fx, float fy, float fz){
		if(player.getHeldItem() != null && player.getHeldItem().itemID == ComputerItems.tape.itemID){
			if(!world.isRemote){
				TapeDriveTileEntity drive = (TapeDriveTileEntity)world.getBlockTileEntity(x,y,z);
				if(drive != null && drive.tape == null){
					drive.setInventory(player, player.getHeldItem().splitStack(1));
					return true;
				}
			}
		} else if (player.getHeldItem() == null){
			if(!world.isRemote){
				TapeDriveTileEntity drive = (TapeDriveTileEntity)world.getBlockTileEntity(x,y,z);
				if(drive != null && drive.tape != null){
					player.inventory.setInventorySlotContents(player.inventory.currentItem,drive.tape);
					drive.clearInventory(player);
					return true;
				}

			}
		}
		return false;
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
	public void
	onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5){
		
		boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4) || par1World.isBlockIndirectlyGettingPowered(par2, par3 + 1, par4);
		TapeDriveTileEntity drive = (TapeDriveTileEntity)par1World.getBlockTileEntity(par2, par3, par4);
		if(drive != null){
			drive.redstoneTick(var6);
		}
	}
	/*PacketHandler*/
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp) {
		if(!wld.isRemote && fc == 0){
			TapeDriveTileEntity drive = (TapeDriveTileEntity)wld.getBlockTileEntity(x, y, z);
			if(drive != null){
				if((drive.cmd & 0xc00) != 0xc00 || drive.readn < 4){
					drive.loadCmdNibble(sp);
				} else {
					drive.writeregister = (byte)(sp & 0xF);
				} 
			}
		}
	}
	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){
	}
	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return (fc == 0);
	}


	/*Client*/
	@Override
	public boolean
	renderAsNormalBlock() {
		return false;
	}
	@Override
	public boolean
	isOpaqueCube(){
		return false;
	}
	@Override
	public void 
	registerIcons(IconRegister ir){
		sideicon = ir.registerIcon("ccTapeInscriberBodyFront");
		topicon = ir.registerIcon("ccBlockTapeInscriber_bottom");
	}
	@Override
	public Icon
	getIcon( int fac, int md){
		return sideicon;
	}
	@Override
	public void
	renderBlockAsItem(RenderBlocks rb, int md, float cd){
		BlockModel model = new BlockModel();
		model.addBox(0D,0D, .4D, .7D, .7D, .8D);
		model.renderAsItemBlock(rb, this, 0);
	}
	@Override
	public boolean
	renderBlock(RenderBlocks rb, int x, int y, int z){
		return false;
	}
}
