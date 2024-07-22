package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

public class TapeDeckBlock extends BlockContainer{
	private BlockModel model;
	private Icon blocktex;
	private Icon bottomtex;
	private Icon disktex;

	public TapeDeckBlock(int id){
		super(id, Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	public TileEntity
	createNewTileEntity(World world){
		return new TapeDeckTileEntity();
	}
	public void
	breakBlock(World world, int x, int y, int z, int md, int mod){
		TapeDeckTileEntity te = (TapeDeckTileEntity)world.getBlockTileEntity(x,y,z);
		if(te != null && !world.isRemote){
			for(int i=0;i<te.inventory.length;++i){
				if(te.inventory[i] != null){
					btw.item.util.ItemUtils.ejectStackWithRandomOffset(world,x,y,z,te.inventory[i]);
				}
			}
		}
	}
	public boolean
	onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float fx, float fy, float fz){
		float fg;
		if(fx == 1.0F || fx == 0.0F)
			fg = fz;
		else
			fg = fx;

		int md = world.getBlockMetadata(x,y,z) + 2;
		if(md != face || world.isRemote)
			return false;

		TapeDeckTileEntity te = (TapeDeckTileEntity)world.getBlockTileEntity(x,y,z);
		if(te == null)
			return false;
		if((int)((1.0F - fy) * 15.0F) > 9 || (int)((1.0F - fy) * 15.0F) < 1 || (int)(fg * 15.0F) > 14 || (int)(fg * 15.0F) < 1)
			return false;
		if( te.inventory[(((int)((1.0F - fy) * 15.0F)/5) + 1) * ((int)(fg * 15.0F) / 2)] != null){
			btw.item.util.ItemUtils.ejectStackWithRandomOffset(world,x,y,z,te.inventory[(((int)((1.0F - fy) * 15.0F)/5) + 1) * ((int)(fg * 15.0F) / 2)]);
			te.inventory[(((int)((1.0F - fy) * 15.0F)/5) + 1) * ((int)(fg * 15.0F) / 2)] = null;
			return true;
		}
		else if( player.getHeldItem() != null && player.getHeldItem().itemID == ComputerItems.tape.itemID){
			te.inventory[(((int)((1.0F - fy) * 15.0F)/5) + 1) * ((int)(fg * 15.0F) / 2)] = player.getHeldItem().splitStack(1);
			return true;
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






	/*Client*/
	@Override
	public boolean
	renderAsNormalBlock() {
		return true;
	}
	@Override
	public boolean
	isOpaqueCube(){
		return true;
	}
	@Override
	public void
	renderBlockAsItem(RenderBlocks rb, int md, float cd){
		BlockModel model = new BlockModel();
		model.addBox(0D,0D, 0D, 1D, 1D, 1D);
		model.renderAsItemBlock(rb, this, 0);
	}
	@Override
	public void
	registerIcons(IconRegister ir){
		blocktex = ir.registerIcon("ccBlockTapeDeck_side");
		bottomtex = ir.registerIcon("ccTapeDriveTape");
		disktex = ir.registerIcon("ccTapeDriveSpoolFront");
	}
	@Override
	public Icon
	getIcon( int fac, int md){
		if(md+2 == fac)
			return blocktex;
		if(fac == 0 || fac ==1)
			return bottomtex;
		return disktex;
	}
}