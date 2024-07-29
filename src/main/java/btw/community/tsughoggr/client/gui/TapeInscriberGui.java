package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.*;


import java.io.*;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.event.KeyEvent;

public class TapeInscriberGui extends GuiContainer {
	private TapeInscriberTileEntity
	inscriber;
	private InventoryPlayer
	inventory;
	private World
	world;
	private TapeInscriberContainer
	container;
	private static String
	name = "Tape Inscriber";

	/*10 x 6 is the hex screen*/
	private int
	scrollpos = 0;
	private int
	cursorpos = 0;
	private int
	selected = 0; /*Hex/text editor selected*/
	private
	byte displayBa[];
	public int
	returnAddr;


	public
	TapeInscriberGui(InventoryPlayer inv, World wld, TapeInscriberTileEntity te){
		super(new TapeInscriberContainer(inv, wld, te));
		world = wld;
		inventory = inv;
		inscriber = te;
		ySize = 168;
		container = (TapeInscriberContainer)inventorySlots;
		te.addContainer(container);
		displayBa = null;


	}
	public void
	drawScreen(int i, int j, float fp){
		super.drawScreen(i,j,fp);
		GL11.glDisable(GL11.GL_LIGHTING);

	}
	private void
	updateDisplayBa(){
		if(cursorpos - (6 * scrollpos) + 1 > 60)
			++scrollpos;
		if(cursorpos - (6 * scrollpos) < 0)
			--scrollpos;
		ItemStack is = inscriber.getStackInSlot(0);
		if(displayBa == null || is == null){
			if(is != null && is.getTagCompound() != null && is.getTagCompound().hasKey("Memory") && is.getTagCompound().hasKey("TapeContents")){
				displayBa = is.getTagCompound().getByteArray("TapeContents");
				if(is.getTagCompound().hasKey("Pos")){
					cursorpos = is.getTagCompound().getInteger("Pos");
					scrollpos = is.getTagCompound().getInteger("Pos") / 60;
				}
			} else {
				displayBa = null;
			}
		}
	}
	protected void
	drawGuiContainerForegroundLayer(int mx, int my){
		fontRenderer.drawString(name + "        " + StatCollector.translateToLocal("container.inventory"), 8, 75, 0x404040);

		if(displayBa != null){
			for(int i = 0;i<10;++i){
				for(int j=0;j<6;++j){
					if((6 * (i + scrollpos)) + j <  displayBa.length){
						fontRenderer.drawString(
							String.format("%x", displayBa[(6 * (i + scrollpos)) + j]),
							47 + j*6,
							9 + i*6,
							(((6 * (i + scrollpos)) + j)==cursorpos && selected == 0)?0xF0F0F0:0x151515

						);
						if(j%2 == 0 && j+1 < displayBa.length){
							if(!((displayBa[(6 * (i + scrollpos)) + j]<<4 | displayBa[(6 * (i + scrollpos)) + j + 1]) == 0))
							fontRenderer.drawString(
								String.format("%c", displayBa[(6 * (i + scrollpos)) + j]<<4 | displayBa[(6 * (i + scrollpos)) + j + 1]),
								89 + (j *6)/2 + ((i % 4) * 18),
								9 + (i / 4)*6, 
								((((6 * (i + scrollpos)) + j)==cursorpos || ((6 * (i + scrollpos)) + j - 1)==cursorpos )&& selected == 1)?0xf0f0f0:0x151515
							);
							if((((6 * (i + scrollpos)) + j)==cursorpos || ((6 * (i + scrollpos)) + j - 1)==cursorpos )&& selected == 1)
							fontRenderer.drawString(
								String.format("%c", '_'),
								89 + (j *6)/2 + ((i % 4) * 18),
								9 + (i / 4)*6, 
								0x000000
							);
						}
					}
				}
			}
			updateDisplayBa();
		} else {
			updateDisplayBa();
		}
		
	}
	protected void
	drawGuiContainerBackgroundLayer(float pt, int mx, int my){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/gui/ccGuiTapeInscriber.png");
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);


	}
	private void
	updateBa(int cp, byte val){
		inscriber.updateTapeValue(cp, val);
		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream ds = new DataOutputStream(bs);
			ds.writeInt(inscriber.xCoord);
			ds.writeInt(inscriber.yCoord);
			ds.writeInt(inscriber.zCoord);
			ds.writeInt(cp);
			ds.writeInt((int)val);
			Packet250CustomPayload pkt = new Packet250CustomPayload("CC|TI", bs.toByteArray());
			if(this.mc != null && this.mc.thePlayer != null)
				((EntityClientPlayerMP)this.mc.thePlayer).sendQueue.addToSendQueue(pkt);

		} catch (IOException e){
			e.printStackTrace();
		}
	}
	private void
	hexKeyTyped(char c, int id){
		int val = (int)c;
		switch (c){
			default:
				break;
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
				val -= 0x20;
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
				val -= 0x7;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				val -= 0x30;
				displayBa[cursorpos] = (byte)val;
				updateBa(cursorpos, (byte)val);
				++cursorpos;
				if(cursorpos >= displayBa.length){
					cursorpos = 0;
					scrollpos = 0;
				}

				
				break;
		}
		switch(id){
			default:
				break;
			case 203:
				--cursorpos;
				if(cursorpos < 0){
					cursorpos = displayBa.length -1;
					scrollpos = cursorpos / 6;
				}
				break;
			case 205:
				++cursorpos;
				if(cursorpos >= displayBa.length){
					cursorpos = 0;
					scrollpos = 0;
				}
				break;
			case 200:
				cursorpos -= 6;
				if(cursorpos < 0){
					cursorpos = displayBa.length + cursorpos - 1;
					scrollpos = cursorpos / 6;
				}
				break;
			case 208:
				cursorpos += 6;
				if(cursorpos >= displayBa.length){
					cursorpos = cursorpos + 1 - displayBa.length;
					scrollpos = 0;
				}
				break;

		}	
		
		if(c != 'e' && c != 'E'){
			super.keyTyped(c, id);
		}
	}
	private void
	txtKeyTyped(char c, int id){
		int val = (int)c;

		if(val >= 0x20 && val < 0x80){
			if(cursorpos % 2 == 0 && cursorpos + 1 < displayBa.length){
				displayBa[cursorpos] = (byte)(val>>4);
				updateBa(cursorpos, (byte)(val>>4));
				displayBa[cursorpos + 1] = (byte)(val&0xF);
				updateBa(cursorpos + 1, (byte)(val&0xF));
				cursorpos += 2;
				if(cursorpos >= displayBa.length){
					cursorpos = 0;
					scrollpos = 0;
				}

			} else if (cursorpos - 1 >= 0) {
				displayBa[cursorpos - 1] = (byte)(val>>4);
				updateBa(cursorpos - 1, (byte)(val>>4));
				displayBa[cursorpos] = (byte)(val&0xF);
				updateBa(cursorpos, (byte)(val& 0xF));
				cursorpos += 2;
				if(cursorpos >= displayBa.length){
					cursorpos = 0;
					scrollpos = 0;
				}

			}
		} else {
			super.keyTyped(c, id);
		}
		switch(id){
			default:
				break;
			case 203:
				cursorpos -=2;
				if(cursorpos < 0){
					cursorpos = displayBa.length -1;
					scrollpos = cursorpos / 6;
				}
				break;
			case 205:
				cursorpos +=2;
				if(cursorpos >= displayBa.length){
					cursorpos = 0;
					scrollpos = 0;
				}
				break;
			case 200:
				cursorpos -= 24;
				if(cursorpos < 0){
					cursorpos = displayBa.length + cursorpos - 1;
					scrollpos = cursorpos / 6;
				}
				break;
			case 208:
				cursorpos += 24;
				if(cursorpos >= displayBa.length){
					cursorpos = cursorpos + 1 - displayBa.length;
					scrollpos = 0;
				}
				break;

		}
	}
	protected void
	keyTyped(char c, int id){
		if(displayBa != null && selected == 0){
			hexKeyTyped(c,id);
		} else if(displayBa != null && selected == 1) {
			txtKeyTyped(c, id);
		} else {
			updateDisplayBa();
			super.keyTyped(c, id);
		}

	}
	public void
	onGuiClosed() {
		if(displayBa != null && cursorpos < displayBa.length && cursorpos > 0){
			updateBa(cursorpos - 1, displayBa[cursorpos - 1]);
		}

		super.onGuiClosed();
	}
	protected void
	mouseClicked(int x, int y, int z){
		int vx, vy;
		vx = x - this.guiLeft;
		vy = y - this.guiTop;

		switch(z){
			case 0:
			if(vx > 47 && vy > 8 && vx < 84 && vy < 70 ){
				selected = 0;
			}else if(vx > 87 && vy > 8 && vx < 165 && vy < 70 ){
				selected = 1;
			} else {
				selected = 2;
			}
			break;
		}
		super.mouseClicked(x,y,z);
	}
	public void sendProgressBarUpdate(Container cont, int a, int b){}
}
