package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;

import org.lwjgl.opengl.GL11;


public class TapeDeckModel extends ModelBase {
	public final ModelRenderer mr;
	public final ModelRenderer bones[] = new ModelRenderer[16];
	public TapeDeckModel(){
		this.textureWidth=16;
		this.textureHeight=16;

		mr = new ModelRenderer(this);
		bones[0] = new ModelRenderer(this);
		bones[0].addBox(3F, 12F, 0F, 1, 2, 1);
		bones[0].setTextureOffset(0,0);
		bones[0].isHidden = true;
		mr.addChild(bones[0]);

		bones[1] = new ModelRenderer(this);
		bones[1].addBox(6F, 12F, 0F, 1, 2, 1);
		bones[1].setTextureOffset(0,0);
		bones[1].isHidden = true;

		mr.addChild(bones[1]);

		bones[2] = new ModelRenderer(this);
		bones[2].addBox(10F, 12F, 0F, 1, 2, 1);
		bones[2].setTextureOffset(0,0);
		bones[2].isHidden = true;

		mr.addChild(bones[2]);

		bones[3] = new ModelRenderer(this);
		bones[3].addBox(13F, 12F, 0F, 1, 2, 1);
		bones[3].setTextureOffset(0,0);
		bones[3].isHidden = true;

		mr.addChild(bones[3]);

		bones[4] = new ModelRenderer(this);
		bones[4].addBox(3F, 8F, 0F, 1, 2, 1);
		bones[4].setTextureOffset(0,0);
		bones[4].isHidden = true;

		mr.addChild(bones[4]);

		bones[5] = new ModelRenderer(this);
		bones[5].addBox(6F, 8F, 0F, 1, 2, 1);
		bones[5].setTextureOffset(0,0);
		bones[5].isHidden = true;

		mr.addChild(bones[5]);

		bones[6] = new ModelRenderer(this);
		bones[6].addBox(10F, 8F, 0F, 1, 2, 1);
		bones[6].setTextureOffset(0,0);
		bones[6].isHidden = true;

		mr.addChild(bones[6]);

		bones[7] = new ModelRenderer(this);
		bones[7].addBox(13F, 8F, 0F, 1, 2, 1);
		bones[7].setTextureOffset(0,0);
		bones[7].isHidden = true;

		mr.addChild(bones[7]);

		bones[8] = new ModelRenderer(this);
		bones[8].addBox(3F, 4F, 0F, 1, 2, 1);
		bones[8].setTextureOffset(0,0);
		bones[8].isHidden = true;

		mr.addChild(bones[8]);

		bones[9] = new ModelRenderer(this);
		bones[9].addBox(6F, 4F, 0F, 1, 2, 1);
		bones[9].setTextureOffset(0,0);
		bones[9].isHidden = true;

		mr.addChild(bones[9]);

		bones[10] = new ModelRenderer(this);
		bones[10].addBox(10F, 4F, 0F, 1, 2, 1);
		bones[10].setTextureOffset(0,0);
		bones[10].isHidden = true;

		mr.addChild(bones[10]);

		bones[11] = new ModelRenderer(this);
		bones[11].addBox(13F, 4F, 0F, 1, 2, 1);
		bones[11].setTextureOffset(0,0);
		bones[11].isHidden = true;
		mr.addChild(bones[11]);


		bones[12] = new ModelRenderer(this);
		bones[12].addBox(3F, 0F, 0F, 1, 2, 1);
		bones[12].setTextureOffset(0,0);
		bones[12].isHidden = true;

		mr.addChild(bones[12]);

		bones[13] = new ModelRenderer(this);
		bones[13].addBox(6F, 0F, 0F, 1, 2, 1);
		bones[13].setTextureOffset(0,0);
		bones[13].isHidden = true;

		mr.addChild(bones[13]);

		bones[14] = new ModelRenderer(this);
		bones[14].addBox(10F, 0F, 0F, 1, 2, 1);
		bones[14].setTextureOffset(0,0);
		bones[14].isHidden = true;

		mr.addChild(bones[14]);

		bones[15] = new ModelRenderer(this);
		bones[15].addBox(13F, 0F, 0F, 1, 2, 1);
		bones[15].setTextureOffset(0,0);
		bones[15].isHidden = true;

		mr.addChild(bones[15]);


	}

}
