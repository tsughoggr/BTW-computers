package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.Random;

public class KeyboardBlock extends Block implements PacketHandler{
	private Icon sidetex;
	private Icon bottomtex;
	private Icon keytex;
	private Icon boardtex;

	private static int[] lmat = new int[]{ 3, 7, 11, 15,
						2, 6, 10, 14,
						1, 5, 9, 13,
						0, 4, 8, 12
					};
	private static int[] rmat = new int[]{ 12, 8, 4, 0,
						13, 9, 5, 1,
						14, 10, 6, 2,
						15, 11, 7, 3
					};

	KeyboardBlock(int id){
		super(id, Material.rock);
	}


	@Override
	public boolean
	hasLargeCenterHardPointToFacing(IBlockAccess ba, int i, int j, int k, int face, boolean bt){
		return false;
	}


	@Override
	public AxisAlignedBB
	getBlockBoundsFromPoolBasedOnState( IBlockAccess blockAccess, int x, int y, int z ){
		return  AxisAlignedBB.getAABBPool().getAABB(0D, 0D, 0D, 1D, 7/16D ,1D);
	}
	public int
	convertFposToMD(double fx, double fy, double fz){
		return  convertFposToMD((float) fx, (float) fy, (float) fz);
	}
	public int
	convertFposToMD(float fx, float fy, float fz){
		if(fy < .5 || fy > .625 || fx < (2D/16D) || fz < (2D/16D) || fx > (4D/16D + ((3.5D * 4) / 16D) )|| fy > (4D/16D + ((3.5D * 4) / 16D) )){
			return -1;
		}
		if(fz >= 0.125 && fx >= 0.125 && fz <= 0.25 && fx <= 0.25){
			return 0;
		}
		if(fz >= 0.34375 && fx >= 0.125 && fz <= 0.46875 && fx <= 0.25){
			return 1;
		}
		if(fz >= 0.5625 && fx >= 0.125 && fz <= 0.6875 && fx <= 0.25){
			return 2;
		}
		if(fz >= 0.78125 && fx >= 0.125 && fz <= 0.90625 && fx <= 0.25){
			return 3;
		}
		if(fz >= 0.125 && fx >= 0.34375 && fz <= 0.25 && fx <= 0.46875){
			return 4;
		}
		if(fz >= 0.34375 && fx >= 0.34375 && fz <= 0.46875 && fx <= 0.46875){
			return 5;
		}
		if(fz >= 0.5625 && fx >= 0.34375 && fz <= 0.6875 && fx <= 0.46875){
			return 6;
		}
		if(fz >= 0.78125 && fx >= 0.34375 && fz <= 0.90625 && fx <= 0.46875){
			return 7;
		}
		if(fz >= 0.125 && fx >= 0.5625 && fz <= 0.25 && fx <= 0.6875){
			return 8;
		}
		if(fz >= 0.34375 && fx >= 0.5625 && fz <= 0.46875 && fx <= 0.6875){
			return 9;
		}
		if(fz >= 0.5625 && fx >= 0.5625 && fz <= 0.6875 && fx <= 0.6875){
			return 10;
		}
		if(fz >= 0.78125 && fx >= 0.5625 && fz <= 0.90625 && fx <= 0.6875){
			return 11;
		}
		if(fz >= 0.125 && fx >= 0.78125 && fz <= 0.25 && fx <= 0.90625){
			return 12;
		}
		if(fz >= 0.34375 && fx >= 0.78125 && fz <= 0.46875 && fx <= 0.90625){
			return 13;
		}
		if(fz >= 0.5625 && fx >= 0.78125 && fz <= 0.6875 && fx <= 0.90625){
			return 14;
		}
		if(fz >= 0.78125 && fx >= 0.78125 && fz <= 0.90625 && fx <= 0.90625){
			return 15;
		}
		return -1;
	}
	public boolean
	onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float fx, float fy, float fz){
		if(!world.isRemote){
			System.out.println("XYZ: " + fx + ", " + fy + ", " + fz);
			int bp = convertFposToMD(fx,fy,fz);
			System.out.println("Convert: " + bp);

			if(bp > -1){
				world.setBlockMetadataWithNotify(x,y,z, bp);
				updateNeighboringTIPHs(world, x, y, z);
				world.playAuxSFX(1001, x,y,z,0);
			}
	
		}

		return true;
	}

	@Override
	public MovingObjectPosition
	collisionRayTrace( World world, int x, int y, int z, Vec3 startRay, Vec3 endRay ){
		BlockModel model = new BlockModel();
		int md = world.getBlockMetadata(x,y,z);

		model.addBox(0D,0D,0D, 1D, 0.01D, 1D);
		model.addBox(0D, 0.01D, 0D, 1D, 10D/16D, 1D/16D);
		model.addBox(15D/16D, 0.01D, 0D, 1D, 10D/16D, 1D/16D);
		model.addBox(15D/16D, 0.01D, 15/16D, 1D, 10D/16D, 1D);
		model.addBox(0D, 0.01D, 15/16D, 1/16D, 10D/16D, 1D);
		model.addBox(1D/16D, 6D/16D, 1D/16D, 15/16D, 7D/16D, 15/16D);
		for(int i = 0; i<16; ++i){
			model.addBox(
				2D/16D + ((3.5D * ((double)(i%4)))/16D),
				7D/16D - (((convertFposToMD(2D/16D + ((3.5D * ((double)(i%4)))/16D),8D/16D,2D/16D + ((3.5D * ((double)(i/4)))/16D)) - md) == 0)?2D/16D:0D),
				2D/16D + ((3.5D * ((double)(i/4)))/16D),
				4D/16D + ((3.5D * ((double)(i%4)))/16D),
				10D/16D - (((convertFposToMD(2D/16D + ((3.5D * ((double)(i%4)))/16D),8D/16D,2D/16D + ((3.5D * ((double)(i/4)))/16D)) - md) == 0)?2D/16D:0D),
				4D/16D + ((3.5D * ((double)(i/4)))/16D)
			);
		}
		return model.collisionRayTrace(world, x, y, z, startRay, endRay);
	}
	public void
	updateTick(World world, int i, int j, int k, Random rn){
		updateNeighboringTIPHs(world, i, j, k);
	}

	/*PacketHandler*/
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp) {
		byte pk = (byte)(sp & 0xF);

		if(wld.isRemote || fc != 0 )
			return;

		System.out.println("Recv pkt: " + pk + " fc " + fc + " at " + x + "," + y + "," + z);
		wld.setBlockMetadataWithNotify(x,y,z,pk);
		wld.scheduleBlockUpdate(x,y,z, blockID, 5);
		wld.playAuxSFX(1001, x,y,z,0);

	}

	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){
		byte pk = (byte)(sp & 0xF);
		if(wld.isRemote || fc != 0 || pk == wld.getBlockMetadata(x,y,z))
			return;

		System.out.println("Recv unmatched clk: " + pk + " fc " + fc + " at " + x + "," + y + "," + z);
		wld.setBlockMetadataWithNotify(x,y,z,pk);
		wld.scheduleBlockUpdate(x,y,z, blockID, 5);
		wld.playAuxSFX(1001, x,y,z,0);
	}
	public void
	updateNeighboringTIPHs(World wld, int x, int y, int z){
		if(wld.isRemote)
			return;
		byte pk = (byte)wld.getBlockMetadata(x,y,z);
		if(Block.blocksList[wld.getBlockId(x, y, z-1)] instanceof PacketHandler){
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z-1)]).handlePacket(wld, x, y, z-1, 3, (byte)rmat[pk], 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z-1)]).clockPacket(wld, x, y, z-1, 3, (byte)rmat[pk], 0, null);
		}
		if(Block.blocksList[wld.getBlockId(x, y, z+1)] instanceof PacketHandler){
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z+1)]).handlePacket(wld, x, y, z+1, 2, (byte)lmat[pk], 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x, y, z+1)]).clockPacket(wld, x, y, z+1, 2, (byte)lmat[pk], 0, null);
		}
		if(Block.blocksList[wld.getBlockId(x+1, y, z)] instanceof PacketHandler){

			((PacketHandler)Block.blocksList[wld.getBlockId(x+1, y, z)]).handlePacket(wld, x+1, y, z, 4, pk, 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x+1, y, z)]).clockPacket(wld, x+1, y, z, 4, pk, 0, null);
		}
		if(Block.blocksList[wld.getBlockId(x-1, y, z)] instanceof PacketHandler){


			((PacketHandler)Block.blocksList[wld.getBlockId(x-1, y, z)]).handlePacket(wld, x-1, y, z, 5, (byte)(~pk) , 0, null) ;
			((PacketHandler)Block.blocksList[wld.getBlockId(x-1, y, z)]).clockPacket(wld, x-1, y, z, 5, (byte)(~pk), 0, null);
		
		}
	}
	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return true;
	}
	/*Client*/
	@Override
	public void 
	registerIcons(IconRegister ir){
		sidetex = ir.registerIcon("ccBlockKeyboard_side");
		bottomtex = ir.registerIcon("ccBlockComputer_bottom_on");
		keytex = ir.registerIcon("cloth_0");
		boardtex = ir.registerIcon("ccBlockComputerBase");
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
	public boolean
	shouldSideBeRendered(IBlockAccess ba, int x, int y, int z, int side){
		return true;
	}
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


		for(int i = 0; i<16; ++i){
			model[3].addBox(
				2D/16D + ((3.5D * ((double)(i%4)))/16D),
				7D/16D,
				2D/16D + ((3.5D * ((double)(i/4)))/16D),
				4D/16D + ((3.5D * ((double)(i%4)))/16D),
				10D/16D,
				4D/16D + ((3.5D * ((double)(i/4)))/16D)
			);
		}	
		model[0].renderAsItemBlock(rb, this, 1);
		model[1].renderAsItemBlock(rb, this, 0);
		model[2].renderAsItemBlock(rb, this, 2);
		model[3].renderAsItemBlock(rb, this, 3);
	}
	@Override
	public boolean
	renderBlock(RenderBlocks rb, int x, int y, int z){
		BlockModel model[] = new BlockModel[4];
		int md = rb.blockAccess.getBlockMetadata(x,y,z);

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

		for(int i = 0; i<16; ++i){
 			model[3].addBox(
				2D/16D + ((3.5D * ((double)(i%4)))/16D),
				7D/16D -  (((convertFposToMD(2D/16D + ((3.5D * ((double)(i%4)))/16D),8D/16D,2D/16D + ((3.5D * ((double)(i/4)))/16D)) - md) == 0)?2D/16D:0D),
				2D/16D + ((3.5D * ((double)(i/4)))/16D),
				4D/16D + ((3.5D * ((double)(i%4)))/16D),
				10D/16D - (((convertFposToMD(2D/16D + ((3.5D * ((double)(i%4)))/16D),8D/16D,2D/16D + ((3.5D * ((double)(i/4)))/16D)) - md) == 0)?2D/16D:0D),
				4D/16D + ((3.5D * ((double)(i/4)))/16D)
			);
		}
		model[0].renderAsBlockWithTexture(rb, this, x,y,z, bottomtex);
		model[1].renderAsBlockWithTexture(rb, this, x,y,z, sidetex);
		model[2].renderAsBlockWithTexture(rb, this, x,y,z, boardtex);
		return model[3].renderAsBlockWithTexture(rb, this, x,y,z, keytex);
	}
}
