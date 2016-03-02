
import java.util.*;

public class Word {

	private String word;
	private int value;
	private ArrayList<GraphNode> path;
	
	public Word(String s, int v) {
		
		word = s;
		value = v;
		path = new ArrayList<GraphNode>();
		
	}
	
	public void addPathNode(GraphNode n) {
		
		path.add(n);
		
	}
	
	public GraphNode getPathNode(int i) {
		
		return path.get(i);
		
	}
	
	public int getPathSize() {
		
		return path.size();
		
	}
	
	public int getValue() {
		
		return value;
		
	}
	
	public void setValue(int v) {
		
		value = v;
		
	}
	
	public String toString() {
		
		return value + " " + word;
		
	}
	
	public void setString(String s) {
		
		word = s;
		
	}
	
}
