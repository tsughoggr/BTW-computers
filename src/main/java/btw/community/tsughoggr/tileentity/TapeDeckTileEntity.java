package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

public class TapeDeckTileEntity extends TileEntity {
	ItemStack[] inventory = new ItemStack[10];




	public void
	readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		inventory = new ItemStack[10];
		for(int i=0;i<inventory.length;++i){
			if(nbt.hasKey("inventory_"+i)){
				inventory[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("inventory_"+i));
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
