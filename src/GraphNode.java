import java.util.*;

public class GraphNode {

	private boolean used;
	private Tile thisTile;
	private ArrayList<GraphNode> neighbors;
	private int centerX, centerY;
	private int nodeNum;
	
	public GraphNode(int num, String letter, Tile.TileType t, int x, int y) {
		
		nodeNum = num;
		thisTile = new Tile(letter, t);
		used = false;
		neighbors = new ArrayList<GraphNode>();
		centerX = x;
		centerY = y;
		
	}
	
	public int getNum() {
		
		return nodeNum;
		
	}
	
	public int getX() {
		
		return centerX;
		
	}
	
	public int getY() {
		
		return centerY;
		
	}
	
	public void newTile(String letter, Tile.TileType t) {
		
		thisTile = new Tile(letter, t);
		
	}
	
	public void addNeighbor(GraphNode n) {
		
		neighbors.add(n);
		
	}

	public int numNeighbors() {
		
		return neighbors.size();
		
	}
	
	public GraphNode getNeighbor(int i) {
		
		return neighbors.get(i);
		
	}
	
	public void use() {
		
		used = true;
		
	}
	
	public void clearUse() {
		
		used = false;
		
	}
	
	public boolean isUsed() {
		
		return used;
		
	}
	
	public String toString() {
		
		return thisTile.toString();
		
	}
	
	public int value() {
		
		return thisTile.getValue();
		
	}
	
	public boolean isDL() {
		
		return thisTile.isDL();
		
	}
	
	public boolean isDW() {
		
		return thisTile.isDW();
		
	}
	
	public boolean isTL() {
		
		return thisTile.isTL();
		
	}
	
	public boolean isTW() {
		
		return thisTile.isTW();
		
	}
	
}
