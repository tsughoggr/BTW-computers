package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import net.minecraft.src.World;
import net.minecraft.src.IBlockAccess;

public interface PacketHandler {
	public void
	handlePacket(World wld, int x, int y, int z, int fc, byte pk, int dp, PacketHandlerBFSIterator bp);
	public void
	clockPacket(World wld, int x, int y, int z, int fc, byte pk, int dp,PacketHandlerBFSIterator pb);
	public boolean
	visualSideConnection(IBlockAccess wld, int x, int y, int z, int fc);
}
