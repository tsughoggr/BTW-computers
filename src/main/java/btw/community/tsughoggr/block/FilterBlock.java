package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.*;
import java.util.stream.IntStream;

import java.util.Random;

/*DESIGN: passnext 0x1, ignorenext 0x1 code 0x3, if code >>2 > 0 send code forward*/

public class FilterBlock extends Block implements PacketHandler{
	private Icon[] icons = new Icon[6]; /*0-5 icons + top/bottom (CPU top/bottom)*/
	private Icon bottomIcon;

	public FilterBlock( int id){
		super(id, Material.rock);
	}

	public boolean
	onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float fx, float fy, float fz){
		if(!world.isRemote)
			world.setBlockMetadata(x,y,z, (world.getBlockMetadata(x,y,z) &8)|(((world.getBlockMetadata(x,y,z)&7) + 1) % 6));

		return true;
	}


	/*Client*/
	public void
	registerIcons(IconRegister ir){
		for(int i=0;i<6;++i){
			icons[i] = ir.registerIcon("ccBlockFilter_" + i);
		}
		bottomIcon = ir.registerIcon("ccBlockComputer_bottom_on");
	}
	public Icon
	getIcon(int sid, int md){
		switch(sid){
			case 0:
			case 1:
				return bottomIcon;
			default:
				return icons[(md & 7) % 6];
		}
	}
	/*PacketHandler*/
	public void
	sendpacket(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){

		int[] ca = IntStream.rangeClosed(0,1).toArray();
		Collections.shuffle(Arrays.asList(ca));
		Byte pk = sp;

		for(int c:ca){
			if(c==fc)
				continue;

			switch(c){
				case 0:
					if(Block.blocksList[wld.getBlockId(x,y+1,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y+1,z)]).handlePacket(wld, x,y+1,z, 0, (byte)2, dp+1, bp) ;
					}

					break;
				case 1:
					if(Block.blocksList[wld.getBlockId(x,y-1,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y-1,z)]).handlePacket(wld, x,y-1,z, 1, (byte)2, dp+1,bp) ;
					}

					break;
			}
		}

	
	}

	public void
	sendclock(World wld, int x, int y, int z, int fc, byte sp, int dp, PacketHandlerBFSIterator bp){

		int[] ca = IntStream.rangeClosed(0,1).toArray();
		Collections.shuffle(Arrays.asList(ca));
		Byte pk = sp;

		for(int c:ca){
			if(c==fc)
				continue;

			switch(c){
				case 0:
					if(Block.blocksList[wld.getBlockId(x,y+1,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y+1,z)]).clockPacket(wld, x,y+1,z, 0,(byte)2, dp+1, bp);
					}

					break;
				case 1:
					if(Block.blocksList[wld.getBlockId(x,y-1,z)] instanceof PacketHandler){
						((PacketHandler)Block.blocksList[wld.getBlockId(x,y-1,z)]).clockPacket(wld, x,y-1,z, 1,(byte)2, dp+1, bp);
					}

					break;
			}
		}

	
	}
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp) {
		int md = wld.getBlockMetadata(x,y,z);
		if((md&8 ) == 8 || fc == 0 || fc == 1 || pk != md)
			return;
		sendpacket(wld,x,y,z,fc,pk,dp, bp);
		
	}
	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp){
		int md = wld.getBlockMetadata(x,y,z);
		if((md&8)==8){
			wld.setBlockMetadataWithNotify(x,y,z,md&7);
			return;
		}
		if(fc == 0 || fc == 1 || pk != md)
			return;
		sendclock(wld,x,y,z,fc,pk,dp, bp);
		wld.setBlockMetadataWithNotify(x,y,z,md|8);
	}
	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc){
		return true;
	}
}
