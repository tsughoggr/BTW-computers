package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;

import net.minecraft.src.*;

import java.util.*;
import java.util.Arrays;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class TapeInscriberTileEntity extends TileEntity implements IInventory{
	private ItemStack inventory[] = new ItemStack[3];
	private List<TapeInscriberContainer> containers = new ArrayList<TapeInscriberContainer>();

	public static String
	name = "Tape Inscriber";


	public void
	readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		inventory = new ItemStack[3];
		for(int i=0;i<inventory.length;++i){
			if(nbt.hasKey("inventory_" + i)){
				inventory[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("inventory_" + i));
			}
		}
	

	}
	public void
	writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		for(int i=0;i<inventory.length;++i){
			if(inventory[i] != null){
				nbt.setCompoundTag("inventory_" + i, inventory[i].writeToNBT(new NBTTagCompound()));
			} 
		}

	}



	public void
	addContainer(TapeInscriberContainer cont){
		containers.add(cont);
	}
	public void
	unsetContainer(TapeInscriberContainer cont){
		containers.remove(cont);
	}

	/*IInventory*/
	public int
	getSizeInventory(){
		return 3;
	}
	public ItemStack
	getStackInSlot(int idx){
		if(idx > -1 && idx < 3){
			return inventory[idx];
		}
		return null;
	}
	public ItemStack
	decrStackSize(int i, int amt){
		if(i > -1 && i < 3 && inventory[i] != null){
			ItemStack rs = inventory[i];
			inventory[i] = null;
			return rs;
		}
		return null;
	}
	public ItemStack
	getStackInSlotOnClosing(int i){
		return null;
	}
	public void
	setInventorySlotContents(int i, ItemStack stack){
		if(stack != null && inventory[i] == null)
			inventory[i] = stack.splitStack(1);
	}
	public String
	getInvName(){
		return name;
	}
	public boolean
	isInvNameLocalized(){
		return false;
	}
	public int
	getInventoryStackLimit(){
		return 1;
	}
	public void
	onInventoryChanged(){
		if(containers != null){
			for(TapeInscriberContainer tape:containers){
				if( tape != null){
					tape.onCraftMatrixChanged(this);
				}
			}
		}
		if(worldObj != null && !worldObj.isRemote){
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 
				((inventory.length > 0 && inventory[0] != null)?1:0) |
				((inventory.length > 1 && inventory[1] != null)?2:0) |
				((inventory.length > 2 && inventory[2] != null)?4:0)
			);
		}
		for(int i=0;i<3;++i){
			if(inventory[i] != null){
				if(inventory[i].getTagCompound() == null){
					inventory[i].setTagCompound(new NBTTagCompound());
				}
				if(!inventory[i].getTagCompound().hasKey("Memory")){
					inventory[i].getTagCompound().setInteger("Memory", 128);
					inventory[i].getTagCompound().setInteger("Pos", 0);
					inventory[i].getTagCompound().setByteArray("TapeContents", new byte[128]);
				}

			}
		}
	}
	public boolean
	isUseableByPlayer(EntityPlayer player){
		return Math.sqrt(((xCoord - player.posX) * (xCoord - player.posX)) + ((yCoord - player.posY) * (yCoord - player.posY)) + ((zCoord - player.posZ) * (zCoord - player.posZ))) < 5;
	}

	public void
	openChest(){};
	public void
	closeChest(){};

	public boolean
	isStackValidForSlot(int i, ItemStack is){
		switch(i){
			case 0:
			case 1:
				return is.itemID == ComputerItems.tape.itemID;
			default:
				return false;
		}
	}

	/*Class specific methods*/
	public void
	redstoneTick(){
		if(inventory[2] == null && (inventory[0] != null || inventory[1] != null)){
			ItemStack isr = new ItemStack(ComputerItems.tape);
			int nmem = 0;
			byte[] bar;
			bar = null;
			isr.setTagCompound(new NBTTagCompound());
			if(inventory[0] != null){
				if(inventory[0].getTagCompound() == null){
					nmem = 128;
					bar = new byte[128];
				} else {
					nmem = inventory[0].getTagCompound().hasKey("Memory")?inventory[0].getTagCompound().getInteger("Memory"):128;
					bar = inventory[0].getTagCompound().hasKey("TapeContents")?inventory[0].getTagCompound().getByteArray("TapeContents"):new byte[nmem];
				}
				inventory[0] = null;

			}
			if(inventory[1] != null){
				if(inventory[1].getTagCompound() == null){
					if(nmem + 128 < ComputersMod.getInstance().maxTapeSize){
						nmem += 128;
						if(bar != null){
							byte[] bart = new byte[nmem];
							System.arraycopy(bar, 0, bart, 0, bar.length );
							bar = bart;
						} else {
							bar = new byte[nmem];
						}
						inventory[1] = null;
					}
				} else {
					if( nmem + (inventory[1].getTagCompound().hasKey("Memory")?inventory[1].getTagCompound().getInteger("Memory"):128) < 65535){
						nmem += inventory[1].getTagCompound().hasKey("Memory")?inventory[1].getTagCompound().getInteger("Memory"):128;
						byte[] bart = new byte[nmem];
						if(bar != null){
							System.arraycopy(bar, 0, bart, 0, bar.length );
							if(inventory[1].getTagCompound().hasKey("TapeContents")){
								System.arraycopy(inventory[1].getTagCompound().getByteArray("TapeContents"), 0, bart, bar.length, inventory[1].getTagCompound().getByteArray("TapeContents").length);
							}
						} else {
							if(inventory[1].getTagCompound().hasKey("TapeContents")){
								System.arraycopy(inventory[1].getTagCompound().getByteArray("TapeContents"), 0, bart, 0, inventory[1].getTagCompound().getByteArray("TapeContents").length);
							} else {
								bart = new byte[nmem];
							}

						}
						bar = bart;
						inventory[1] = null;
					}
				}
			}
			isr.getTagCompound().setInteger("Memory", nmem);
			isr.getTagCompound().setByteArray("TapeContents", bar);
			isr.getTagCompound().setInteger("Pos", 0);
			inventory[2] = isr;
			onInventoryChanged();
		}
	}

	public void
	updateTapeValue(int pos, byte val){
		if(inventory[0] != null) {
			if(inventory[0].getTagCompound() != null && inventory[0].getTagCompound().hasKey("Memory") && inventory[0].getTagCompound().hasKey("TapeContents")){
				byte ba[] = inventory[0].getTagCompound().getByteArray("TapeContents");
				if(ba.length > pos){
					ba[pos] = val;
					inventory[0].getTagCompound().setByteArray("TapeContents", ba);
				}
				inventory[0].getTagCompound().setInteger("Pos", pos + 1);
			} else {
				inventory[0].getTagCompound().setInteger("Memory", 128);
				byte ba[] = new byte[128];
				if(pos < 128)
					ba[pos] = val;
				inventory[0].getTagCompound().setByteArray("TapeContents", ba);
				inventory[0].getTagCompound().setInteger("Pos", pos + 1);

			}
			onInventoryChanged();
		}
	}
}
