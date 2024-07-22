package btw.community.tsughoggr.computers;
import btw.block.model.BlockModel;
import java.util.List;
import java.util.LinkedList;

public class PacketHandlerBFSIterator {
	public int x,y,z,fc,dp,md;
	public byte sp;
	public LinkedList<PacketHandlerBFSIterator> bfsiter;
	public PacketHandlerBFSIterator(){
		bfsiter = new LinkedList<PacketHandlerBFSIterator>();
	}
	public PacketHandlerBFSIterator(int i, int j, int k, int cf, byte ps, int pd){
		x=i;
		y=j;
		z=k;
		fc=cf;
		sp=ps;
		dp=pd;
		bfsiter = new LinkedList<PacketHandlerBFSIterator>();
	}
	public void
	rmerge(LinkedList<PacketHandlerBFSIterator> g){
		bfsiter.addAll(bfsiter.size(), g);
	}

}