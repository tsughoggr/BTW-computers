package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TapeDeckRendererTileEntity extends TileEntitySpecialRenderer {
	private static final float[] rotarray = new float[]{ 180F, 0F, 270F, 90F};

	public void
	renderTileEntityAt(TileEntity te, double px, double py, double pz, float cc){
		if(te != null && te.worldObj.checkChunksExist((int)(te.xCoord - 1),(int)( te.yCoord - 1),(int)( te.zCoord - 1),(int)( te.xCoord + 1),(int)( te.yCoord + 1),(int)( te.zCoord + 1))){
			TapeDeckTileEntity tape = (TapeDeckTileEntity)te;
			int md = tape.worldObj.getBlockMetadata(tape.xCoord, tape.yCoord, tape.zCoord);
			Block bl = Block.blocksList[tape.worldObj.getBlockId(te.xCoord, te.yCoord, te.zCoord)];
			System.out.println(bl.blockID);
			int rot = md &3;
			RenderBlocks rb = new RenderBlocks(te.worldObj);
			rb.renderStandardBlock(ComputerBlocks.tapeDeck,te.xCoord, te.yCoord, te.zCoord);
			BlockModel bm = new BlockModel();
			for(int i = 0;i<tape.inventory.length;++i){
				if(tape.inventory[i] != null){
					bm.addBox( (i/5 + 1 ) * 1D/15D, (double)((( i / 5 ) * 5) + 6)/15D, -3D/15D, (i/5 + 1) * 2D/15D,  (double)((( i / 5 ) * 5) + 9) /15D, 0D);

				}
			}
			GL11.glPushMatrix();
			GL11.glRotatef(rotarray[rot], 0F, 1F, 0F);
			this.bindTextureByName("/textures/blocks/ccTapeDriveSpoolFront.png");
			bm.renderAsBlockWithTexture(rb, bl,te.xCoord, te.yCoord, te.zCoord, bl.getIcon(0,0));
			GL11.glPopMatrix();
			
		}
	}

}