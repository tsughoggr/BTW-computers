package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.Random;
import java.io.*;

import org.lwjgl.opengl.GL11;


public class TapeInscriberBlock extends BlockContainer {
	private Icon sidetex;
	private Icon bottomtex;
	private Icon keytex;
	private Icon boardtex;
	private Icon inscribertex;

	public TapeInscriberBlock(int id){
		super(id, Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
	}



	public TileEntity
	createNewTileEntity(World world){
		return new TapeInscriberTileEntity();
	}
	public void
	breakBlock(World world, int x, int y, int z, int md, int mod){
		TapeInscriberTileEntity tape = (TapeInscriberTileEntity)world.getBlockTileEntity(x,y,z);
		if(tape != null && !world.isRemote){
			for(int i=0;i<3;++i){
				ItemStack is = tape.getStackInSlot(i);
				if(is!= null){
					btw.item.util.ItemUtils.ejectStackWithRandomOffset(world, x, y, z, is);
				}
			}
		}
		super.breakBlock(world,x,y,z,md,mod);

	}

	public boolean
	onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float fx, float fy, float fz){
		if(!world.isRemote && world.getBlockTileEntity(x,y,z) != null && world.getBlockTileEntity(x,y,z) instanceof TapeInscriberTileEntity && player instanceof EntityPlayerMP){
				TapeInscriberContainer tape = new TapeInscriberContainer(player.inventory, world, (TapeInscriberTileEntity)world.getBlockTileEntity(x,y,z));
				try {
					int wid = ((EntityPlayerMP)player).incrementAndGetWindowID();
					ByteArrayOutputStream bs = new ByteArrayOutputStream();
					DataOutputStream ds = new DataOutputStream(bs);
					ds.writeInt(wid);
					ds.writeInt(x);
					ds.writeInt(y);
					ds.writeInt(z);
					Packet250CustomPayload pkt = new Packet250CustomPayload("CC|TI", bs.toByteArray());
					((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(pkt);
					player.openContainer = tape;
					player.openContainer.windowId = wid;
					player.openContainer.addCraftingToCrafters((EntityPlayerMP)player);
				} catch (IOException e){
					e.printStackTrace();
				}
		}
		return true;
	}
	public void
	onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5){
		
		boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4) || par1World.isBlockIndirectlyGettingPowered(par2, par3 + 1, par4);
		int md = par1World.getBlockMetadata(par2,par3,par4);
		if(var6 && ( md & 8) < 1 && !par1World.isRemote){
			((TapeInscriberTileEntity)(par1World.getBlockTileEntity(par2,par3,par4))).redstoneTick();
			par1World.setBlockMetadataWithNotify(par2, par3, par4, md | 8);
		} if (!var6 && (md & 8) > 0 && !par1World.isRemote){
			par1World.setBlockMetadataWithNotify(par2, par3, par4, md & 7);
		}
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
	public boolean
	shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5){
		return true;
	}
	@Override
	public void 
	registerIcons(IconRegister ir){
		sidetex = ir.registerIcon("ccBlockTapeInscriber_side");
		bottomtex = ir.registerIcon("ccBlockTapeInscriber_bottom");
		keytex = ir.registerIcon("cloth_0");
		boardtex = ir.registerIcon("ccBlockComputerBase");
		inscribertex = ir.registerIcon("fcBlockAnvil"); 
	}
	@Override
	public Icon
	getIcon(int fac, int md){
		switch(fac){
			case 0: return sidetex;
			case 1: return bottomtex;
			case 2: return keytex;
			default: return boardtex;
		}
	}

	@Override
	public void
	renderBlockAsItem(RenderBlocks rb, int md, float cd){
		BlockModel model[] = new BlockModel[4];

		model[0] = new BlockModel();
		model[1] = new BlockModel();
		model[2] = new BlockModel();
		model[3] = new BlockModel();
		model[0].addBox(0D,0D,0D, 1D, 0.01D, 1D);

		model[1].addBox(0D, 0.01D, 0D, 1D, 7D/16D, 1D/16D);
		model[1].addBox(0D, 0.01D, 15/16D, 1D, 7D/16D, 1D);
		model[1].addBox(15D/16D, 0.01D, 0D, 1D, 7D/16D, 1D);
		model[1].addBox(0D, 0.01D, 0D, 1/16D, 7D/16D, 1D );

		model[1].addBox(1D/16D, 7D/16D, 0D, 15D/16D, 8D/16D, 1D/16D);
		model[1].addBox(0D, 7D/16D, 1D/16D, 1/16D, 8D/16D, 15D/16D );
		model[1].addBox(1D/16D, 7D/16D, 15/16D, 15/16D, 8D/16D, 1D);
		model[1].addBox(15D/16D, 7D/16D, 1D/16D, 1D, 8D/16D, 15D/16D);

		model[1].addBox(4D/16D, 8D/16D, 0D, 12D/16D, 9D/16D, 1D/16D);
		model[1].addBox(0D, 8D/16D, 4D/16D, 1/16D, 9D/16D, 12D/16D );
		model[1].addBox(4D/16D, 8D/16D, 15/16D, 12/16D, 9D/16D, 1D);
		model[1].addBox(15D/16D, 8D/16D, 4D/16D, 1D, 9D/16D, 12D/16D);

		model[1].addBox(5D/16D, 9D/16D, 0D, 11D/16D, 10D/16D, 1D/16D);
		model[1].addBox(0D, 9D/16D, 5D/16D, 1/16D, 10D/16D, 11D/16D );
		model[1].addBox(5D/16D, 9D/16D, 15/16D, 11/16D, 10D/16D, 1D);
		model[1].addBox(15D/16D, 9D/16D, 5D/16D, 1D, 10D/16D, 11D/16D);

		model[2].addBox(1D/16D, 6D/16D, 1D/16D, 15/16D, 7D/16D, 15/16D);

		model[2].addBox(0D, 7D/16D, 0D, 1D/16D, 8D/16D, 1D/16D );
		model[2].addBox(0D, 8D/16D, 1D/16D, 1D/16D, 9D/16D, 4D/16D);
		model[2].addBox(0D, 9D/16D, 4D/16D, 1D/16D, 10D/16D, 5D/16D);
		model[2].addBox(0D, 10D/16D, 5D/16D, 1D/16D, 11D/16D, 11D/16D);
		model[2].addBox(0D, 9D/16D, 11D/16D, 1D/16D, 10D/16D, 12D/16D);
		model[2].addBox(0D, 8D/16D, 12D/16D, 1D/16D, 9D/16D, 15D/16D);

		model[2].addBox(15D/16D, 7D/16D, 0D, 1D, 8D/16D, 1D/16D );
		model[2].addBox(15D/16D, 8D/16D, 1D/16D, 1D, 9D/16D, 4D/16D);
		model[2].addBox(15D/16D, 9D/16D, 4D/16D, 1D, 10D/16D, 5D/16D);
		model[2].addBox(15D/16D, 10D/16D, 5D/16D, 1D, 11D/16D, 11D/16D);
		model[2].addBox(15D/16D, 9D/16D, 11D/16D, 1D, 10D/16D, 12D/16D);
		model[2].addBox(15D/16D, 8D/16D, 12D/16D, 1D, 9D/16D, 15D/16D);

		model[2].addBox(0D, 7D/16D, 0D, 1D/16D, 8D/16D, 1D/16D );
		model[2].addBox(1D/16D, 8D/16D, 0D, 4D/16D, 9D/16D, 1D/16D);
		model[2].addBox(4D/16D, 9D/16D, 0D, 5D/16D, 10D/16D, 1D/16D);
		model[2].addBox(5D/16D, 10D/16D, 0D, 11D/16D, 11D/16D, 1D/16D);
		model[2].addBox(11D/16D, 9D/16D, 0D, 12D/16D, 10D/16D, 1D/16D);
		model[2].addBox(12D/16D, 8D/16D, 0D, 15D/16D, 9D/16D, 1D/16D);

		model[2].addBox(0D, 7D/16D, 15D/16D, 1D/16D, 8D/16D, 1D );
		model[2].addBox(1D/16D, 8D/16D, 15D/16D, 4D/16D, 9D/16D, 1D);
		model[2].addBox(4D/16D, 9D/16D, 15D/16D, 5D/16D, 10D/16D, 1D);
		model[2].addBox(5D/16D, 10D/16D, 15D/16D, 11D/16D, 11D/16D, 1D);
		model[2].addBox(11D/16D, 9D/16D, 15D/16D, 12D/16D, 10D/16D, 1D);
		model[2].addBox(12D/16D, 8D/16D, 15D/16D, 15D/16D, 9D/16D, 1D);
		model[2].addBox(15D/16D, 7D/16D, 15D/16D, 1D, 8D/16D, 1D);


		model[3].addBox(7/16D, 7/16D, 7/16D, 9/16D, 1D, 9/16D);
	
		model[0].renderAsItemBlock(rb, this, 1);
		model[1].renderAsItemBlock(rb, this, 0);
		model[2].renderAsItemBlock(rb, this, 2);
		model[3].renderAsItemBlock(rb, this, 3);
	}
	@Override
	public boolean
	renderBlock(RenderBlocks rb, int x, int y, int z){
		BlockModel model[] = new BlockModel[5];
		int md = rb.blockAccess.getBlockMetadata(x,y,z);

		model[0] = new BlockModel();
		model[1] = new BlockModel();
		model[2] = new BlockModel();
		model[3] = new BlockModel();
		model[4] = new BlockModel();

		model[0].addBox(0D,0D,0D, 1D, 0.01D, 1D);

		model[1].addBox(0D, 0.01D, 0D, 1D, 7D/16D, 1D/16D);
		model[1].addBox(0D, 0.01D, 15/16D, 1D, 7D/16D, 1D);
		model[1].addBox(15D/16D, 0.01D, 0D, 1D, 7D/16D, 1D);
		model[1].addBox(0D, 0.01D, 0D, 1/16D, 7D/16D, 1D );

		model[1].addBox(1D/16D, 7D/16D, 0D, 15D/16D, 8D/16D, 1D/16D);
		model[1].addBox(0D, 7D/16D, 1D/16D, 1/16D, 8D/16D, 15D/16D );
		model[1].addBox(1D/16D, 7D/16D, 15/16D, 15/16D, 8D/16D, 1D);
		model[1].addBox(15D/16D, 7D/16D, 1D/16D, 1D, 8D/16D, 15D/16D);

		model[1].addBox(4D/16D, 8D/16D, 0D, 12D/16D, 9D/16D, 1D/16D);
		model[1].addBox(0D, 8D/16D, 4D/16D, 1/16D, 9D/16D, 12D/16D );
		model[1].addBox(4D/16D, 8D/16D, 15/16D, 12/16D, 9D/16D, 1D);
		model[1].addBox(15D/16D, 8D/16D, 4D/16D, 1D, 9D/16D, 12D/16D);

		model[1].addBox(5D/16D, 9D/16D, 0D, 11D/16D, 10D/16D, 1D/16D);
		model[1].addBox(0D, 9D/16D, 5D/16D, 1/16D, 10D/16D, 11D/16D );
		model[1].addBox(5D/16D, 9D/16D, 15/16D, 11/16D, 10D/16D, 1D);
		model[1].addBox(15D/16D, 9D/16D, 5D/16D, 1D, 10D/16D, 11D/16D);

		model[2].addBox(1D/16D, 6D/16D, 1D/16D, 15/16D, 7D/16D, 15/16D);

		model[2].addBox(0D, 7D/16D, 0D, 1D/16D, 8D/16D, 1D/16D );
		model[2].addBox(0D, 8D/16D, 1D/16D, 1D/16D, 9D/16D, 4D/16D);
		model[2].addBox(0D, 9D/16D, 4D/16D, 1D/16D, 10D/16D, 5D/16D);
		model[2].addBox(0D, 10D/16D, 5D/16D, 1D/16D, 11D/16D, 11D/16D);
		model[2].addBox(0D, 9D/16D, 11D/16D, 1D/16D, 10D/16D, 12D/16D);
		model[2].addBox(0D, 8D/16D, 12D/16D, 1D/16D, 9D/16D, 15D/16D);

		model[2].addBox(15D/16D, 7D/16D, 0D, 1D, 8D/16D, 1D/16D );
		model[2].addBox(15D/16D, 8D/16D, 1D/16D, 1D, 9D/16D, 4D/16D);
		model[2].addBox(15D/16D, 9D/16D, 4D/16D, 1D, 10D/16D, 5D/16D);
		model[2].addBox(15D/16D, 10D/16D, 5D/16D, 1D, 11D/16D, 11D/16D);
		model[2].addBox(15D/16D, 9D/16D, 11D/16D, 1D, 10D/16D, 12D/16D);
		model[2].addBox(15D/16D, 8D/16D, 12D/16D, 1D, 9D/16D, 15D/16D);

		model[2].addBox(0D, 7D/16D, 0D, 1D/16D, 8D/16D, 1D/16D );
		model[2].addBox(1D/16D, 8D/16D, 0D, 4D/16D, 9D/16D, 1D/16D);
		model[2].addBox(4D/16D, 9D/16D, 0D, 5D/16D, 10D/16D, 1D/16D);
		model[2].addBox(5D/16D, 10D/16D, 0D, 11D/16D, 11D/16D, 1D/16D);
		model[2].addBox(11D/16D, 9D/16D, 0D, 12D/16D, 10D/16D, 1D/16D);
		model[2].addBox(12D/16D, 8D/16D, 0D, 15D/16D, 9D/16D, 1D/16D);

		model[2].addBox(0D, 7D/16D, 15D/16D, 1D/16D, 8D/16D, 1D );
		model[2].addBox(1D/16D, 8D/16D, 15D/16D, 4D/16D, 9D/16D, 1D);
		model[2].addBox(4D/16D, 9D/16D, 15D/16D, 5D/16D, 10D/16D, 1D);
		model[2].addBox(5D/16D, 10D/16D, 15D/16D, 11D/16D, 11D/16D, 1D);
		model[2].addBox(11D/16D, 9D/16D, 15D/16D, 12D/16D, 10D/16D, 1D);
		model[2].addBox(12D/16D, 8D/16D, 15D/16D, 15D/16D, 9D/16D, 1D);
		model[2].addBox(15D/16D, 7D/16D, 15D/16D, 1D, 8D/16D, 1D);
		if((md&7)>0){
			model[3].addBox(1D/16D,12D/16D, 7D/16D, 15D/16D, 13D/16D ,9D/16D);
			model[3].addBox(1D/16D,3D/16D, 7D/16D, 5D/16D, 12D/16D ,9D/16D);
			model[3].addBox(11D/16D,3D/16D, 7D/16D, 15D/16D, 12D/16D ,9D/16D);
		}
		model[4].addBox(6D/16D,3D/16D,6D/16D, 10D/16D, 12D/16D, 10D/16D );
		model[4].addBox(6D/16D, 12D/17D, 9D/16D,10D/16D, 13D/16D,10D/16D  );
		model[4].addBox(6D/16D, 13D/16D, 6D/16D, 10D/16D, 16D/16D, 10D/16D);

		model[0].renderAsBlockWithTexture(rb, this, x,y,z, bottomtex);
		model[1].renderAsBlockWithTexture(rb, this, x,y,z, sidetex);
		model[2].renderAsBlockWithTexture(rb, this, x,y,z, boardtex);
		model[4].renderAsBlockWithTexture(rb, this, x,y,z, inscribertex);
		return model[3].renderAsBlockWithTexture(rb, this, x,y,z, keytex);
	}

}
