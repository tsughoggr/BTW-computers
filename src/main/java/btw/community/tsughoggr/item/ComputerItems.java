package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;

import net.minecraft.src.Item;
import net.minecraft.src.CreativeTabs;

public class ComputerItems {
	public static Item tape;
	public static void
	initialize(){
		ComputersMod cm = ComputersMod.getInstance();
		tape = new TapeItem(cm.getIdFromProp("tape.itemID")).setCreativeTab(CreativeTabs.tabMaterials).setUnlocalizedName("ccItemTape");
	}
}
