package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;
import btw.block.util.Flammability;

import java.util.*;
import java.util.stream.IntStream;

public class CableBlock extends Block implements PacketHandler {

	private Icon sideIcon;
	private Icon middleIcon;

	CableBlock(int id){
		super(id, Material.rock);
		setFireProperties(Flammability.WICKER);
	}

	@Override
	public boolean
	hasLargeCenterHardPointToFacing(IBlockAccess ba, int i, int j, int k, int face, boolean bt){
		return false;
	}

	@Override
	public AxisAlignedBB
	getBlockBoundsFromPoolBasedOnState( IBlockAccess blockAccess, int x, int y, int z ){

		/*Possible to add some conditional collision code here but probably not worth it due to how it works (collision box is always rect can't be complex shape like cross etc)*/
		return  AxisAlignedBB.getAABBPool().getAABB(7D/16D,7D/16D,7D/16D,9D/16D,9D/16D,9D/16D);
	}
	@Override
	public MovingObjectPosition
	collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay ){
		BlockModel model;
		model = new BlockModel();
		model.addBox(6D/16D, 6D/16D, 6D/16D,10D/16D,10D/16D,10D/16D );
		if(Block.blocksList[world.getBlockId(i+1, j, k)] instanceof PacketHandler){
			model.addBox(10/16D, 6/16D, 6/16D, 1D, 10/16D, 10/16D);
		}
		if(Block.blocksList[world.getBlockId(i-1, j, k)] instanceof PacketHandler){
			model.addBox(0D, 6/16D, 6/16D, 6/16D, 10/16D, 10/16D);
		}
		if(Block.blocksList[world.getBlockId(i, j, k-1)] instanceof PacketHandler){
			model.addBox(6/16D, 6/16D, 0D, 10/16D, 10/16D, 6/16D );
		}
		if(Block.blocksList[world.getBlockId(i, j, k + 1)] instanceof PacketHandler){
			model.addBox(6/16D, 6/16D, 10/16D, 10/16D, 10/16D, 1D );

		}
		if(Block.blocksList[world.getBlockId(i, j - 1, k )] instanceof PacketHandler){
			model.addBox(6/16D, 0D, 6/16D, 10/16D, 6/16D, 10/16D);
		}
		if(Block.blocksList[world.getBlockId(i, j + 1, k )] instanceof PacketHandler){
			model.addBox(6/16D, 10/16D, 6/16D, 10/16D, 1D, 10/16D);
		}
		return model.collisionRayTrace(world, i,j,k,startRay,endRay);
	}








	/*PacketHandler*/
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp) {
		byte rcflg;
		byte pk = (byte)(sp & 0xF);
		int md = wld.getBlockMetadata(x,y,z);
		if(pk == md || dp > 31)
			return;
		wld.setBlockMetadata(x,y,z, pk);
		rcflg=0;
		if(bp == null){
			rcflg = 1;
			bp = new PacketHandlerBFSIterator(x,y,z,fc,pk,dp);
		}
		int[] ca = IntStream.rangeClosed(0, 5).toArray();
		Collections.shuffle(Arrays.asList(ca));
		for(int c:ca){
			if(c==fc)
				continue;

			switch(c){
				case 0:
					if(Block.blocksList[wld.getBlockId(x,y-1,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y-1,z,1,pk, dp+1));
					}
					break;
				case 1:
					if(Block.blocksList[wld.getBlockId(x,y+1,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y+1,z,0,pk,dp+1));
					}
					break;
				case 2:
					if(Block.blocksList[wld.getBlockId(x,y,z-1)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y,z-1,3,pk,dp+1));

					}

					break;
				case 3:
					if(Block.blocksList[wld.getBlockId(x,y,z+1)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y,z+1,2,pk,dp+1));

					}
					break;
				case 4:
					if(Block.blocksList[wld.getBlockId(x-1,y,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x-1,y,z,5,pk,dp+1));

					}
					break;
				case 5:
					if(Block.blocksList[wld.getBlockId(x+1,y,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x+1,y,z,4,pk,dp+1));

					}
					break;

			}
		}
		if(rcflg == 1){
			Collections.shuffle(bp.bfsiter);
			PacketHandlerBFSIterator bpd = new PacketHandlerBFSIterator();
			for(PacketHandlerBFSIterator qp=bp.bfsiter.poll();;qp=bp.bfsiter.poll()){
				if(qp == null)
					return;

				((PacketHandler)Block.blocksList[wld.getBlockId(qp.x, qp.y, qp.z)]).handlePacket(wld, qp.x, qp.y, qp.z, qp.fc, qp.sp, qp.dp, bpd);
				if(bp.bfsiter.size() == 0){
					Collections.shuffle(bpd.bfsiter);
					bp = bpd;
					bpd = new PacketHandlerBFSIterator();
				}
			}
		}

	}

	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){
		byte pk = (byte)(sp & 0xF);
		int md = wld.getBlockMetadata(x,y,z);
		byte rcflg;
		rcflg=0;

		if(pk != md || dp > 31)
			return;
		wld.setBlockMetadata(x,y,z, ~pk);
		if(bp == null){
			rcflg = 1;
			bp = new PacketHandlerBFSIterator(x,y,z,fc,pk,dp);
		}
		int[] ca = IntStream.rangeClosed(0, 5).toArray();
		Collections.shuffle(Arrays.asList(ca));
		for(int c:ca){
			if(c==fc)
				continue;

			switch(c){
				case 0:
					if(Block.blocksList[wld.getBlockId(x,y-1,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y-1,z,1,pk, dp+1));
					}
					break;
				case 1:
					if(Block.blocksList[wld.getBlockId(x,y+1,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y+1,z,0,pk,dp+1));
					}
					break;
				case 2:
					if(Block.blocksList[wld.getBlockId(x,y,z-1)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y,z-1,3,pk,dp+1));

					}

					break;
				case 3:
					if(Block.blocksList[wld.getBlockId(x,y,z+1)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x,y,z+1,2,pk,dp+1));

					}
					break;
				case 4:
					if(Block.blocksList[wld.getBlockId(x-1,y,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x-1,y,z,5,pk,dp+1));

					}
					break;
				case 5:
					if(Block.blocksList[wld.getBlockId(x+1,y,z)] instanceof PacketHandler){
						bp.bfsiter.add(new PacketHandlerBFSIterator(x+1,y,z,4,pk,dp+1));

					}
					break;

			}
		}
		if(rcflg == 1){
			Collections.shuffle(bp.bfsiter);
			PacketHandlerBFSIterator bpd = new PacketHandlerBFSIterator();
			for(PacketHandlerBFSIterator qp=bp.bfsiter.poll();;qp=bp.bfsiter.poll()){
				if(qp == null)
					return;
				((PacketHandler)Block.blocksList[wld.getBlockId(qp.x, qp.y, qp.z)]).clockPacket(wld, qp.x, qp.y, qp.z, qp.fc, qp.sp, qp.dp, bpd);

				if(bp.bfsiter.size() == 0){
					Collections.shuffle(bpd.bfsiter);
					bp = bpd;
					bpd = new PacketHandlerBFSIterator();
				}
			}
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
		sideIcon = ir.registerIcon("ccBlockCable");
		middleIcon = ir.registerIcon("ccBlockCable_center");
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
	public Icon
	getIcon(int sid, int md){
		switch(md){
			case 0:
				return sideIcon;
			default:
				return middleIcon;

		}
	}
	@Override
	public void
	renderBlockAsItem(RenderBlocks rb, int md, float cd){
		BlockModel model[] = new BlockModel[2];
		BlockModel tmp;
		model[0] = new BlockModel();
		model[1] = new BlockModel();

		model[0].addBox(0D, 6D/16D, 6D/16D, 7D/16D, 10D/16D, 10D/16D);
		model[1].addBox(7D/16D, 7D/16D, 7D/16D,9D/16D,9D/16D,9D/16D );


		model[0].renderAsItemBlock(rb, this, 0);
		model[1].renderAsItemBlock(rb, this, 1);
		tmp = model[0].makeTemporaryCopy();
		tmp.rotateAroundYToFacing(3);
		tmp.renderAsItemBlock(rb, this, 0);



	}
	@Override
	public boolean
	renderBlock(RenderBlocks rb, int x, int y, int z){
		BlockModel model[] = new BlockModel[2];
		BlockModel tmp;
		model[0] = new BlockModel();
		model[1] = new BlockModel();
		model[0].addBox(6/16D, 6D/16D, 0D, 10D/16D, 10D/16D, 6D/16D);
		model[1].addBox(6D/16D, 6D/16D, 6D/16D,10D/16D,10D/16D,10D/16D );


		model[1].renderAsBlockWithTexture(rb, this, x, y, z, middleIcon);

		if(Block.blocksList[rb.blockAccess.getBlockId(x, y, z-1)] instanceof PacketHandler && ((PacketHandler)Block.blocksList[rb.blockAccess.getBlockId(x, y, z-1)]).visualSideConnection(rb.blockAccess, x,y,z - 1, 2)){
			rb.setUVRotateNorth(1);
			rb.setUVRotateSouth(1);

			model[0].renderAsBlockWithTexture(rb, this, x,y,z, sideIcon);
			rb.clearUVRotation();

		}
		if(Block.blocksList[rb.blockAccess.getBlockId(x, y, z + 1)] instanceof PacketHandler &&  ((PacketHandler)Block.blocksList[rb.blockAccess.getBlockId(x, y, z+1)]).visualSideConnection(rb.blockAccess, x,y,z + 1, 3)){
			rb.setUVRotateNorth(1);
			rb.setUVRotateSouth(1);

			tmp = model[0].makeTemporaryCopy();
			tmp.rotateAroundYToFacing(3);
			tmp.renderAsBlockWithTexture(rb, this, x,y,z,sideIcon);
			rb.clearUVRotation();
		}
		if(Block.blocksList[rb.blockAccess.getBlockId(x-1, y, z)] instanceof PacketHandler &&  ((PacketHandler)Block.blocksList[rb.blockAccess.getBlockId(x - 1, y, z)]).visualSideConnection(rb.blockAccess, x - 1,y,z, 5)){
			rb.setUVRotateEast(1);
			rb.setUVRotateWest(1);
			rb.setUVRotateTop(1);
			rb.setUVRotateBottom(1);
			tmp = model[0].makeTemporaryCopy();
			tmp.rotateAroundYToFacing(4);
			tmp.renderAsBlockWithTexture(rb, this, x,y,z,sideIcon);
			rb.clearUVRotation();
		}
		if(Block.blocksList[rb.blockAccess.getBlockId(x+1, y, z)] instanceof PacketHandler &&  ((PacketHandler)Block.blocksList[rb.blockAccess.getBlockId(x + 1, y, z)]).visualSideConnection(rb.blockAccess, x + 1,y,z, 4)){
			rb.setUVRotateEast(1);
			rb.setUVRotateWest(1);
			rb.setUVRotateTop(1);
			rb.setUVRotateBottom(1);
			tmp = model[0].makeTemporaryCopy();
			tmp.rotateAroundYToFacing(5);
			tmp.renderAsBlockWithTexture(rb, this, x,y,z,sideIcon);
			rb.clearUVRotation();
		}
		if(Block.blocksList[rb.blockAccess.getBlockId(x, y-1, z)] instanceof PacketHandler &&  ((PacketHandler)Block.blocksList[rb.blockAccess.getBlockId(x , y - 1, z)]).visualSideConnection(rb.blockAccess, x,y - 1,z, 1)){
			tmp = model[0].makeTemporaryCopy();
			tmp.tiltToFacingAlongY(2);
			tmp.renderAsBlockWithTexture(rb, this, x,y,z,sideIcon);

		}
		if(Block.blocksList[rb.blockAccess.getBlockId(x, y+1, z)] instanceof PacketHandler &&  ((PacketHandler)Block.blocksList[rb.blockAccess.getBlockId(x , y + 1, z)]).visualSideConnection(rb.blockAccess, x,y + 1,z, 0)){
			tmp = model[0].makeTemporaryCopy();
			tmp.tiltToFacingAlongY(3);
			tmp.renderAsBlockWithTexture(rb, this, x,y,z,sideIcon);

		}
		return true;
	}
}
