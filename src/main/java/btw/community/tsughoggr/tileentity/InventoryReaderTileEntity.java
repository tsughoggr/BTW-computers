package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

public class InventoryReaderTileEntity extends TileEntity {

	int cmd; /* DST 0xF, MDFLAG: 0x1, SIZEFLAG: 0x1, BLOCKFLAG: 0x1, SLOT: 0x1FF*/
		/*
			DST as per spec,
			MDFLAG determines whether to grab itemID or itemDamage
			SIZEFLAG deterimes whether to grab Stacksize (MUTEX WITH MDFLAG)
			BLOCKFLAG determines whether to grab the information from an inventory or from the world
				stacks with MDFLAG but SIZEFLAG will always return 1
			SLOT is what slot (modulated by the inventory size) to get
		*/
	int pos;
	int cnt; /*Item*/
	int mdt; /*MetaData*/
	int rdpt; /*Read ptr*/
	int sst; /*stacksize*/
	int tickn;

	public void
	updateEntity(){
		++tickn;
		if(!worldObj.isRemote && pos >= 4 && tickn%10 == 0){
			byte pk = (byte)(((((cmd & 0x800) == 0x800)?mdt:((cmd & 0x400) == 0x400)?sst:cnt)>>rdpt) & 0xF);
			rdpt -= 4;
			if(Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)] instanceof PacketHandler){
				if((cmd>>12) != 0){
					((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).handlePacket(worldObj, xCoord, yCoord -1, zCoord, 1, (byte) (cmd>>12), 0, null) ;
					((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).clockPacket(worldObj, xCoord, yCoord -1, zCoord, 1, (byte) (cmd>>12), 0, null);
				}
				((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).handlePacket(worldObj, xCoord, yCoord -1, zCoord, 1, pk, 0, null) ;
				((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).clockPacket(worldObj, xCoord, yCoord -1, zCoord, 1, pk, 0, null);


			}
			if(Block.blocksList[worldObj.getBlockId(xCoord, yCoord + 1, zCoord)] instanceof PacketHandler){
				if((cmd>>12) != 0){
					((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord + 1, zCoord)]).handlePacket(worldObj, xCoord, yCoord + 1, zCoord, 0, (byte) (cmd>>12), 0, null) ;
					((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord + 1, zCoord)]).clockPacket(worldObj, xCoord, yCoord + 1, zCoord, 0, (byte) (cmd>>12), 0, null);
				}
				((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord + 1, zCoord)]).handlePacket(worldObj, xCoord, yCoord + 1, zCoord, 0, pk, 0, null) ;
				((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord + 1, zCoord)]).clockPacket(worldObj, xCoord, yCoord + 1, zCoord, 0, pk, 0, null);

			}

			if(rdpt < 0){
				pos = 0;
				cmd = 0;
				mdt = 0;
				cnt = 0;
				sst = 0;
			}
		}
	}
	public void
	readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		cmd = nbt.getInteger("cmd");
		pos = nbt.getInteger("pos");

		cnt = nbt.getInteger("cnt");
		mdt = nbt.getInteger("mdt");
		tickn = nbt.getInteger("tickn");
		rdpt = nbt.getInteger("rdpt");
		sst = nbt.getInteger("sst");
	}
	public void
	writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("cmd", cmd);
		nbt.setInteger("pos", pos);

		nbt.setInteger("cnt", cnt);
		nbt.setInteger("mdt", mdt);
		nbt.setInteger("tickn", tickn);
		nbt.setInteger("rdpt", rdpt);
		nbt.setInteger("sst", sst);
	}

	void
	handlePacket(byte pk, PacketHandlerBFSIterator bp) {
		if(pos < 4){
			cmd <<=4;
			cmd |= pk;
			++pos;
			if(pos >= 3 && !worldObj.isRemote)
				processcmd();
		}
	}
	void
	processcmd(){
		int px, pz;
		px = xCoord;
		pz = zCoord;

		switch(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 3){
			case 0: /*N*/
				--pz;
				break;
			case 1: /*S*/
				++pz;
				break;
			case 2: /*W*/
				--px;
				break;
			default: /*E*/
				++px;
				break;

		}
		if((cmd&0x200) == 0x200){
			cnt = worldObj.getBlockId(px, yCoord, pz);
			mdt = worldObj.getBlockMetadata(px, yCoord, pz);
			sst = 1;
			rdpt = 12;
		} else {
	

			TileEntity inv = worldObj.getBlockTileEntity(px, yCoord, pz);
			if(inv != null && inv instanceof IInventory){
				ItemStack is = ((IInventory)inv).getStackInSlot((cmd & 0x3FF)%((IInventory)inv).getSizeInventory());
				if( is != null){
					cnt = is.itemID;
					mdt = is.getItemDamage();
					sst = is.stackSize;
					rdpt = 12;
				} else {
					cnt = 0;
					mdt = 0;
					sst = 0;
					rdpt = 12;
				}
			}
		}
	}


}
