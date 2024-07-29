package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import btw.block.tileentity.TileEntityDataPacketHandler;
import net.minecraft.src.*;

public class TapeDeckTileEntity extends TileEntity implements TileEntityDataPacketHandler {
	ItemStack[] inventory = new ItemStack[16];
	private short slotbmp;

	@Override
	public Packet
	getDescriptionPacket(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setShort("slotbmp", slotbmp);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void
	readNBTFromPacket(NBTTagCompound tag){
		if(tag.hasKey("slotbmp"))
			slotbmp = tag.getShort("slotbmp");
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord,zCoord);
	}
	public boolean
	isSlotFilled(short i){
		return ((1<<i) & slotbmp) == (1<<i);
	}
	public void
	clearSlot(short i){
		slotbmp &= ~(1<<i);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	public void
	fillSlot(short i){
		slotbmp |= (1<<i);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	public void
	setSlots(short i){
		slotbmp = i;
	}
	public void
	readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		inventory = new ItemStack[16];
		slotbmp = (short)0;
		for(int i=0;i<inventory.length;++i){
			if(nbt.hasKey("inventory_"+i)){
				inventory[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("inventory_"+i));
				slotbmp |= (short)(1<<i);
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
}
