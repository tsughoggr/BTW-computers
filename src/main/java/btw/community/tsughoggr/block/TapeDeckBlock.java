package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

public class TapeDeckBlock extends BlockContainer{
	private BlockModel model;
	private Icon blocktex;
	private Icon bottomtex;

	private static final short[][] postranstbl = new short[][]{
		{-1, -1, 12, 12, -1, 13, 13, -1, -1, 14, 14, -1, 15, 15, -1, -1},	//0
		{-1, -1, 12, 12, -1, 13, 13, -1, -1, 14, 14, -1, 15, 15, -1, -1},	//1
		{-1, -1, 12, 12, -1, 13, 13, -1, -1, 14, 14, -1, 15, 15, -1, -1},	//2
		{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},	//3
		{-1, -1,  8,  8, -1,  9,  9, -1, -1, 10, 10, -1, 11, 11, -1, -1},	//4
		{-1, -1,  8,  8, -1,  9,  9, -1, -1, 10, 10, -1, 11, 11, -1, -1},	//5
		{-1, -1,  8,  8, -1,  9,  9, -1, -1, 10, 10, -1, 11, 11, -1, -1},	//6
		{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},	//7
		{-1, -1,  4,  4, -1,  5,  5, -1, -1,  6,  6, -1,  7,  7, -1, -1},	//8
		{-1, -1,  4,  4, -1,  5,  5, -1, -1,  6,  6, -1,  7,  7, -1, -1},	//9
		{-1, -1,  4,  4, -1,  5,  5, -1, -1,  6,  6, -1,  7,  7, -1, -1},	//10
		{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},	//11
		{-1, -1,  0,  0, -1,  1,  1, -1, -1,  2,  2, -1,  3,  3, -1, -1},	//12
		{-1, -1,  0,  0, -1,  1,  1, -1, -1,  2,  2, -1,  3,  3, -1, -1},	//13
		{-1, -1,  0,  0, -1,  1,  1, -1, -1,  2,  2, -1,  3,  3, -1, -1},	//14
		{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}	//15
	}; 
	public TapeDeckBlock(int id){
		super(id, Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	public TileEntity
	createNewTileEntity(World world){
		TapeDeckTileEntity te = new TapeDeckTileEntity();
		return te;
	}

	public static short
	slotFromPos(float fx, float fy){
		int x, y;
		x = (int)Math.round(fx * 15D);
		y = (int)Math.round(fy * 15D);
		if(x<0||y<0||x>15||y>15)
			return -1;
		return postranstbl[y][x];
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
		short slot;
		if(world.isRemote)
			return false;
		if(fx == 1.0F)
			fg = 1.0F - fz;
		else if(fx == 0.0F)
			fg = fz;
		else if(fz == 1.0F)
			fg = fx;
		else
			fg = 1.0F - fx;
		
		int md = world.getBlockMetadata(x,y,z) + 2;
		if(md != face || world.isRemote || (slot = slotFromPos(fg, fy)) < 0)
			return false;

		TapeDeckTileEntity te = (TapeDeckTileEntity)world.getBlockTileEntity(x,y,z);
		if(te == null)
			return false;

		if( te.inventory[slot] != null){
			if(player.getHeldItem() == null)
				btw.item.util.ItemUtils.givePlayerStackOrEjectFromTowardsFacing(player,te.inventory[slot],x,y,z,face);
			else
				btw.item.util.ItemUtils.ejectStackFromBlockTowardsFacing(world, x,y,z, te.inventory[slot],face);
			te.inventory[slot] = null;
			te.clearSlot(slot);
			return true;
		}
		else if( player.getHeldItem() != null && player.getHeldItem().itemID == ComputerItems.tape.itemID){
			te.inventory[slot] = player.getHeldItem().splitStack(1);
			te.fillSlot(slot);
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
		return false;
	}
	@Override
	public boolean
	isOpaqueCube(){
		return false;
	}
	@Override
	public void
	renderBlockAsItem(RenderBlocks rb, int md, float cd){
		BlockModel model = new BlockModel();
		model.addBox(0D,0D, 0D, 1D, 1D, 1D);
		model.renderAsItemBlock(rb, this, 3);
	}
	@Override
	public void
	registerIcons(IconRegister ir){
		blocktex = ir.registerIcon("ccBlockTapeDeck_side");
		bottomtex = ir.registerIcon("ccTapeDriveTape");
	}
	@Override
	public Icon
	getIcon( int fac, int md){
		if(md+2 == fac)
			return blocktex;
		return bottomtex;
	}
}
