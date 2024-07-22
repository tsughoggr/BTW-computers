package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TapeDriveRendererTileEntity extends TileEntitySpecialRenderer {
	protected TapeDriveModel driveModel;
	private static final float[] rotarray = new float[]{ 180F, 0F, 270F, 90F};
	public TapeDriveRendererTileEntity(){
		driveModel = new TapeDriveModel();
	}

	public void renderTileEntityAt(TileEntity te, double px, double py, double pz, float cc){
		if(te != null && te.worldObj.checkChunksExist((int)(te.xCoord - 1),(int)( te.yCoord - 1),(int)( te.zCoord - 1),(int)( te.xCoord + 1),(int)( te.yCoord + 1),(int)( te.zCoord + 1))){
			TapeDriveTileEntity tape = (TapeDriveTileEntity)te;
			int md = tape.worldObj.getBlockMetadata(tape.xCoord, tape.yCoord, tape.zCoord);

			int rot = md & 3;
			int invfill = (md & 4)>>2;
			int running = (md & 8)>>3;

			GL11.glPushMatrix();
			GL11.glTranslatef((float)px, (float)py, (float)pz);
			GL11.glRotatef(rotarray[rot], 0F, 1F, 0F);
			switch(rot){
				case 0:
					GL11.glTranslatef(-1F, 0F, -1F);
					break;
				case 3:
					GL11.glTranslatef(-1F, 0F, 0F);
					break;
				case 2:
					GL11.glTranslatef(0F, 0F, -1F);
					break;
			}
			this.bindTextureByName("/textures/blocks/ccTapeDriveCombinedTexture.png");
			driveModel.bone.rotateAngleZ = (float)Math.PI;
			GL11.glPushMatrix();
			GL11.glTranslatef(.5F, -1.5F, .5F);
			driveModel.bone.render(1F/16F);
			GL11.glTranslatef(0F, 3F/16F, 0F);
			driveModel.group.render(1F/16F);
			GL11.glPopMatrix();
			GL11.glTranslatef(.5F, 21F/16F, .5F);

			GL11.glPushMatrix();
			GL11.glTranslatef(0F, 1F/16F + 1F/256F, 0F);
			if(invfill == 1){
				driveModel.bone3.render(1F/16F);
			}
			GL11.glPopMatrix();
			EntityItem iet = new EntityItem(tape.worldObj, 0D, 0D, 0D, new ItemStack(ComputerItems.tape));
			iet.hoverStart = 0.0F;


			GL11.glPushMatrix();
			if(running == 1){
				GL11.glTranslatef(-2.5F/16F, -10.5F/16F, -1.5F/16F);
				GL11.glRotatef(((float)tape.tickn) * 2, 0F, 0F, 1F);
				GL11.glTranslatef(2.5F/16F, 10.5F/16F, 1.5F/16F);
			}
			driveModel.bone2.render(1F/16F);
			if(invfill == 1){

				driveModel.group5.render(1F/16F);
				RenderItem.renderInFrame = true;
				GL11.glScalef(.5F, .5F, 1F);
				RenderManager.instance.renderEntityWithPosYaw(iet, -2.5D/8D, -12D/8D, 2.1D/16D,0F,0F);
				RenderItem.renderInFrame = false;
				this.bindTextureByName("/textures/blocks/ccTapeDriveCombinedTexture.png");
			}

			GL11.glPopMatrix();

			GL11.glPushMatrix();
			if(running == 1){
				GL11.glTranslatef( 2.5F/16F, -10.5F/16F, -1.5F/16F);
				GL11.glRotatef(((float)tape.tickn) * 2, 0F, 0F, 1F);
				GL11.glTranslatef(-2.5F/16F, 10.5F/16F, 1.5F/16F);
			}
			driveModel.bone4.render(1F/16F);

			if( invfill == 1){
				driveModel.group4.render(1F/16F);
				RenderItem.renderInFrame = true;
				GL11.glScalef(.5F, .5F, 1F);
				RenderManager.instance.renderEntityWithPosYaw(iet, 2.5D/8D, -12D/8D, 2.1D/16D,0F,0F);
				RenderItem.renderInFrame = false;
				this.bindTextureByName("/textures/blocks/ccTapeDriveCombinedTexture.png");
			}
			GL11.glPopMatrix();

			GL11.glPopMatrix();
		}

	}
}
