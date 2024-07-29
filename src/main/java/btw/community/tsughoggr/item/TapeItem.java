package btw.community.tsughoggr.computers;
import btw.crafting.util.FurnaceBurnTime;

import net.minecraft.src.Item;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.ItemStack;
import net.minecraft.src.EntityPlayer;

import java.util.List;

public class TapeItem extends Item {
	public
	TapeItem(int id){
		super(id);
		setMaxStackSize(1);
		setIncineratedInCrucible();
		setfurnaceburntime(FurnaceBurnTime.KINDLING);
		setFilterableProperties(Item.FILTERABLE_SMALL);
	}
	public void
	addInformation(ItemStack is, EntityPlayer player, List taglist, boolean b4){
		NBTTagCompound tag = is.getTagCompound();
		if(tag != null){
			if(tag.hasKey("Name")){
				taglist.add("Label: " + tag.getString("Name"));
			}
			if(tag.hasKey("Memory")){
				taglist.add("Size: " + tag.getInteger("Memory"));
			}
			if(tag.hasKey("Pos")){
				taglist.add("Position: " + tag.getInteger("Pos"));
			}
		}
	}
}
