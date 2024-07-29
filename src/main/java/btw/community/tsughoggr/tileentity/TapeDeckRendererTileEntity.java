package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TapeDeckRendererTileEntity extends TileEntitySpecialRenderer {
	private static final float[] rotarray = new float[]{ 180F, 0F, 270F, 90F};
	protected TapeDeckModel mb;


	public
	TapeDeckRendererTileEntity(){
		mb = new TapeDeckModel();
	}

	public void
	renderTileEntityAt(TileEntity te, double px, double py, double pz, float cc){
		if(te != null && te.worldObj.checkChunksExist((int)(te.xCoord - 1),(int)( te.yCoord - 1),(int)( te.zCoord - 1),(int)( te.xCoord + 1),(int)( te.yCoord + 1),(int)( te.zCoord + 1))){
			TapeDeckTileEntity tape = (TapeDeckTileEntity)te;
			int md = tape.worldObj.getBlockMetadata(tape.xCoord, tape.yCoord, tape.zCoord);
			int rot = md &3;

			GL11.glPushMatrix();
			this.bindTextureByName("/textures/blocks/tapeDeckTest.png");
			GL11.glTranslatef((float)px, (float)py, (float)pz);
			GL11.glRotatef(rotarray[rot], 0F, 1F, 0F);
			switch(rot){
				case 0:
					GL11.glTranslatef(-1F, 0F, 0F);
					break;
				case 1:
					GL11.glTranslatef(0F, 0F, 1F);
					break;
				case 3:
					GL11.glTranslatef(-1F, 0F, 1F);
					break;
			}
			for(short i = 0;i<tape.inventory.length;++i){
				mb.bones[i].isHidden = true;
				if(tape.isSlotFilled(i)){
					mb.bones[i].isHidden=false;
				}
			}

			GL11.glPushMatrix();
			mb.mr.render(1F/16F);
			GL11.glPopMatrix();
			GL11.glPopMatrix();


			
		}
	}

}
