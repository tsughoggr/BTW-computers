package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.Random;
import java.io.*;

public class TapeDriveTileEntity extends TileEntity {
	public ItemStack tape;
	


	private int amt; /*Tracking for read/write/seek amount*/
	/* DST: 0xF  CMD: 0x3 SIZE: 0x3FF*/
	/* DST - Destination of CMD for routing
		will output (DST, read, DST, read, ...) 
		DST are 1 indexed, if DST is 0 output will be (read, read, read, ...)
	*/
	/* CMD - Command (SIZE - Size in BYTES)
		- 0 : Forward Seek SIZE
		- 1 : Backward Seek SIZE
		- 2 : Read SIZE bytes
		- 3 : Write SIZE bytes
	*/
	public short cmd; 
	public int tickn;

	public byte readn;
	public byte writeregister;

	public void
	updateEntity(){
		++tickn;
		if(tickn %10 == 0 && readn > 3 && cmd != 0){
			processcmd();
		}
	}
	public void
	processcmd(){
		byte op = (byte)((cmd & 0x0c00)>>10);
		switch(op){
			case 0:
				if(amt < (cmd & 0x3FF) || (cmd & 0x3FF) == 0){
					++amt;
					if(tape != null && tape.getTagCompound() != null && tape.getTagCompound().hasKey("TapeContents") && tape.getTagCompound().hasKey("Pos")){
						int tapepos = tape.getTagCompound().getInteger("Pos");
						byte[] tapeba = tape.getTagCompound().getByteArray("TapeContents");
						if(tapepos < tapeba.length - 1){
							++tapepos;
							tape.getTagCompound().setInteger("Pos", tapepos);
						} else {
							amt = 0;
							cmd = 0;
							readn = 0;
							worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7);
						}
					}
				}
				if(amt >= (cmd & 0x3FF)){
					amt = 0;
					cmd = 0;
					readn = 0;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7);

				}
				break;
			case 1:
				if(amt < (cmd & 0x3FF) || (cmd & 0x3FF) == 0){
					++amt;
					if(tape != null && tape.getTagCompound() != null && tape.getTagCompound().hasKey("TapeContents") && tape.getTagCompound().hasKey("Pos")){
						int tapepos = tape.getTagCompound().getInteger("Pos");
						byte[] tapeba = tape.getTagCompound().getByteArray("TapeContents");
						if(tapepos > 0){
							--tapepos;
							tape.getTagCompound().setInteger("Pos", tapepos);
						} else {
							amt = 0;
							cmd = 0;
							readn = 0;
							worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7);
						}
					}
				}
				if(amt >= (cmd & 0x3FF)){
					amt = 0;
					cmd = 0;
					readn = 0;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7);

				}
				break;
			case 2:
				if(amt < (cmd & 0x3FF) || (cmd & 0x3FF) == 0){
					++amt;
					if(tape != null && tape.getTagCompound() != null && tape.getTagCompound().hasKey("TapeContents") && tape.getTagCompound().hasKey("Pos")){
						int tapepos = tape.getTagCompound().getInteger("Pos");
						byte[] tapeba = tape.getTagCompound().getByteArray("TapeContents");
						if(tapepos < tapeba.length - 1){
							if(!worldObj.isRemote && Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)] instanceof PacketHandler){
								if((cmd>>12) != 0){
									((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).handlePacket(worldObj, xCoord, yCoord -1, zCoord, 1, (byte) (cmd>>12), 0, null) ;
									((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).clockPacket(worldObj, xCoord, yCoord -1, zCoord, 1, (byte) (cmd>>12), 0, null);
								}
								((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).handlePacket(worldObj, xCoord, yCoord -1, zCoord, 1, tapeba[tapepos], 0, null) ;
								((PacketHandler)Block.blocksList[worldObj.getBlockId(xCoord, yCoord - 1, zCoord)]).clockPacket(worldObj, xCoord, yCoord -1, zCoord, 1, tapeba[tapepos], 0, null);

							}
							++tapepos;
							tape.getTagCompound().setInteger("Pos", tapepos);
						}
					}
				}
				if(amt >= (cmd & 0x3FF) && (cmd & 0x3FF) != 0){
					amt = 0;
					cmd = 0;
					readn = 0;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7);

				}
				break;
			case 3:
				if((amt < (cmd & 0x3FF) || (cmd & 0x3FF) == 0) && (writeregister >>4) == 0){
					++amt;
					writeregister |= 0xF0;

					if(tape != null && tape.getTagCompound() != null && tape.getTagCompound().hasKey("TapeContents") && tape.getTagCompound().hasKey("Pos")){
						int tapepos = tape.getTagCompound().getInteger("Pos");
						byte[] tapeba = tape.getTagCompound().getByteArray("TapeContents");
						if(tapepos < tapeba.length - 1){
 							tapeba[tapepos] = (byte)(writeregister & 0xF);
							++tapepos;
							tape.getTagCompound().setInteger("Pos", tapepos);
							tape.getTagCompound().setByteArray("TapeContents", tapeba);
						}
					}
				}
				if(amt >= (cmd & 0x3FF)){
					amt = 0;
					cmd = 0;
					readn = 0;
					writeregister =(byte) 0xF0;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7);

				}
				break;
		}
		
	}
	public void
	readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.getInteger("InventoryTape_0") == 1){
			tape = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("InventoryTape"));		}
		else {
			tape = null;
		}

		amt = nbt.getInteger("amt");
		cmd = (short)nbt.getInteger("cmd");
		tickn = nbt.getInteger("tickn");
		readn = (byte)nbt.getInteger("readn");
		writeregister = (byte)nbt.getInteger("writeregister");

	}
	public void
	writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(tape != null){
			nbt.setCompoundTag("InventoryTape", tape.writeToNBT(new NBTTagCompound()));
			nbt.setInteger("InventoryTape_0", 1);
		}
		else
			nbt.setInteger("InventoryTape_0", 0); 
		nbt.setInteger("cmd", cmd);
		nbt.setInteger("tickn", tickn);
		nbt.setInteger("readn", readn);
		nbt.setInteger("writeregister", writeregister);

	}
	public void
	redstoneTick(boolean redstate){


		int md = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if(redstate){
			cmd = (short)0x0800;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, md | 8);
			readn = 4;
		} else 	if((((cmd ^ 0x0800)&0x0800) == 0)){
			cmd = 0;
			readn = 0;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, md & 7);

		}

		
	}
	public void
	loadCmdNibble(byte in){
		cmd <<=4;
		cmd |= (in & 0xF);
		++readn;
		if(readn > 3){
			int md = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 8| md);
			amt = 0;
		}
	}
	public void
	setInventory(EntityPlayer player, ItemStack is){
		tape = is;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) | 4);
	}
	public void
	clearInventory(EntityPlayer player){
		tape = null;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 11);

	}
}
