package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

public class TapeInscriberSlot extends Slot{
	public
	TapeInscriberSlot(IInventory inv, int i, int x, int y){
		super(inv, i, x, y);
	}
	public boolean 
	isItemValid(ItemStack stack){
		if(stack != null && stack.itemID == ComputerItems.tape.itemID)
			return true;
		return false;
	}
}
