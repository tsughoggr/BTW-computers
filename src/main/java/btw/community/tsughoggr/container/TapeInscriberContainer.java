package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import java.util.Iterator;

public class TapeInscriberContainer extends Container {
	private TapeInscriberTileEntity
	inscriber;
	private InventoryPlayer
	inventory;
	private World
	world;
	 
	TapeInscriberContainer(InventoryPlayer inv, World wld, TapeInscriberTileEntity te){
		world = wld;
		inventory = inv;
		inscriber = te;
		addSlotToContainer(new TapeInscriberSlot(inscriber, 0, 8, 17));
		addSlotToContainer(new TapeInscriberSlot(inscriber, 1, 28, 17));
		addSlotToContainer(new TapeInscriberSlot(inscriber, 2, 8, 53));

		for(int i = 0;i<3;++i){
			for(int j = 0;j<9;++j){
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}

		}
		for (int i = 0; i < 9; ++i){
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}
	public boolean
	canInteractWith(EntityPlayer player){
		return inscriber.isUseableByPlayer(player);
	}
	public ItemStack
	transferStackInSlot(EntityPlayer player, int i){

		Slot slot = (Slot)inventorySlots.get(i);
		if((slot != null && slot.getHasStack()) || i < 3){

			if(i > 2){
				ItemStack is = slot.getStack();
				if(is == null)
					return null;

				if(inscriber.getStackInSlot(0) == null && this.getSlot(0).isItemValid(is)){
					inscriber.setInventorySlotContents(0, is.splitStack(1));
					((Slot)inventorySlots.get(i)).decrStackSize(1);
	
					
				} else if(inscriber.getStackInSlot(1) == null && this.getSlot(1).isItemValid(is)){
					inscriber.setInventorySlotContents(1, is.splitStack(1));
					((Slot)inventorySlots.get(i)).decrStackSize(1);
	
				}
			} else {
				ItemStack is = inscriber.getStackInSlot(i);

				if(is==null)
					return null;
				int slt = inventory.getFirstEmptyStack();

				if(slt >= 0){
					inventory.setInventorySlotContents(slt, is.splitStack(1));
					((Slot)inventorySlots.get(i)).decrStackSize(1);								}
			}
		}
		inscriber.onInventoryChanged();
		return null;
	}
}
