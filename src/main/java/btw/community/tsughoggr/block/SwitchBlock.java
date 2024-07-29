package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.*;
import java.util.stream.IntStream;

public class SwitchBlock extends Block implements PacketHandler {
	private Icon topIcon;
	private Icon faceIcon;
	private Icon outIcon;
	private Icon faceIconOff;

	SwitchBlock(int id){
		super(id, Material.rock);
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
		world.setBlockMetadataWithNotify(x,y,z,face<<2);
	}

	public boolean
	onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float fx, float fy, float fz){
		if(!world.isRemote && face > 1){
			int md = world.getBlockMetadata(x,y,z);
			world.setBlockMetadata(x,y,z, ((face - 2)<<2)|(md&2) );
		}

		return true;
	}

	/*PacketHandler*/
	public void
	sendpacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){

		int[] ca = IntStream.rangeClosed(2, 5).toArray();
		Collections.shuffle(Arrays.asList(ca));
		Byte pk = sp;

		for(int c:ca){
			if(c==fc)
				continue;

			switch(c){
				case 2:
					if(Block.blocksList[wld.getBlockId(x,y,z-1)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y,z-1)]).handlePacket(wld, x,y,z-1, 3, pk, dp+1, bp) ;
					}

					break;
				case 3:
					if(Block.blocksList[wld.getBlockId(x,y,z+1)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y,z+1)]).handlePacket(wld, x,y,z+1, 2, pk, dp+1, bp) ;
					}
					break;
				case 4:
					if(Block.blocksList[wld.getBlockId(x-1,y,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x-1,y,z)]).handlePacket(wld, x - 1,y,z, 5, pk, dp+1, bp) ;
					}
					break;
				case 5:
					if(Block.blocksList[wld.getBlockId(x+1,y,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x+1,y,z)]).handlePacket(wld, x + 1,y,z, 4, pk, dp+1, bp) ;
					}
					break;

			}
		}

	
	}
	public void
	sendclock(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){

		int[] ca = IntStream.rangeClosed(2, 5).toArray();
		byte pk = sp;
		Collections.shuffle(Arrays.asList(ca));
		for(int c:ca){
			if(c==fc)
				continue;

			switch(c){
				case 0:
				case 1:
					break;
				case 2:
					if(Block.blocksList[wld.getBlockId(x,y,z-1)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y,z-1)]).clockPacket(wld, x,y,z-1, 3, pk, dp+1,   bp);
					}

					break;
				case 3:
					if(Block.blocksList[wld.getBlockId(x,y,z+1)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y,z+1)]).clockPacket(wld, x,y,z+1, 2, pk, dp+1,   bp);
					}
					break;
				case 4:
					if(Block.blocksList[wld.getBlockId(x-1,y,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x-1,y,z)]).clockPacket(wld, x - 1,y,z, 5, pk, dp+1,   bp);
					}
					break;
				case 5:
					if(Block.blocksList[wld.getBlockId(x+1,y,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x+1,y,z)]).clockPacket(wld, x + 1,y,z, 4, pk, dp+1,   bp);
					}
					break;

			}
		}

	
	}

	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp) {
		if(dp > 31) return;
		int gd = wld.getBlockMetadata(x,y,z);
		int md = 2 + (gd >> 2);
		System.out.println("FILTER: " + md + "," + gd + "," + fc);
		if((gd&2)==2)
			return;
		switch(fc){
			case 2:
				if(md != 2)
					return;
				sendpacket(wld,x,y,z,fc,pk,dp,bp);
				break;
			case 3:
				if(md != 3)
					return;
				sendpacket(wld,x,y,z,fc,pk,dp,bp);
				break;
			case 4:
				if(md != 4)
					return;
				sendpacket(wld,x,y,z,fc,pk,dp,bp);
				break;
			case 5:
				if(md != 5)
					return;
				sendpacket(wld,x,y,z,fc,pk,dp,bp);
				break;
		}
	}

	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp){
		if(dp > 31) return;

		int gd = wld.getBlockMetadata(x,y,z);
		int md = 2 + (gd >> 2);

		switch(fc){
			case 0:
			case 1:
				wld.setBlockMetadataWithNotify(x,y,z,gd|2);

				break;
			case 2:
				if(md != 2)
					return;
				if((gd & 2) == 2){
					wld.setBlockMetadataWithNotify(x,y,z,gd&0xC);
					return;
				}
				sendclock(wld,x,y,z,fc,pk,dp,bp);
				break;
			case 3:
				if(md != 3)
					return;
				if((gd & 2) == 2){
					wld.setBlockMetadataWithNotify(x,y,z,gd&0xC);
					return;
				}
				sendclock(wld,x,y,z,fc,pk,dp,bp);
				break;

			case 4:
				if(md != 4)
					return;
				if((gd & 2) == 2){
					wld.setBlockMetadataWithNotify(x,y,z,gd&0xC);
					return;
				}
				sendclock(wld,x,y,z,fc,pk,dp,bp);
				break;
			case 5:
				if(md != 5)
					return;
				if((gd & 2) == 2){
					wld.setBlockMetadataWithNotify(x,y,z,gd&0xC);
					return;
				}
				sendclock(wld,x,y,z,fc,pk,dp,bp);
				break;
		}
	}

	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return true;
	}

	/*Client*/
	public void
	registerIcons(IconRegister ir){
		topIcon = ir.registerIcon("ccBlockComputer_bottom_on");
		faceIcon = ir.registerIcon("ccBlockPostRouting_in");
		outIcon = ir.registerIcon("ccBlockPostRouting_out");
		faceIconOff = ir.registerIcon("ccBlockPostRouting_off");
	}
	public Icon
	getIcon(int sid, int md){
		if(sid == 0 || sid == 1) return topIcon;
		if(sid == ((md >> 2) + 2)) return (((md&2) == 0)?faceIcon:faceIconOff);
		return outIcon;
	}

}
