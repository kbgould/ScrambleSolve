
import java.util.*;

public class TreeNode {

	private String name = "";
	private boolean isValid = false;
	private ArrayList<TreeNode> children;
	
	public TreeNode(String word, boolean isWord) {
		
		name = word;
		isValid = isWord;
		children = new ArrayList<TreeNode>();
		
	}
	
	public boolean checkValid() {
		return isValid;
	}
	
	public void setValid () {
		isValid = true;
	}
	
	public void unsetValid () {
		isValid = false;
	}
	
	public void setName (String s) {
		name = s;
	}
	
	public String getName() {
		return name;
	}
	
	public void addChild (TreeNode n) {
		
		children.add(n);
		
	}
	
	public void removeChild (TreeNode n) {
		
		children.remove(n);
		
	}
	
	public TreeNode getChild (int i) {
	
		return children.get(i);
		
	}
	
	public TreeNode getChild(String s) {
	
		int index = getChildIndex(s);
		
		return children.get(index);
		
	}
	
	public boolean hasChild (String s) {
		
		for (int i = 0; i < children.size(); i++) {
			
			TreeNode tmp = children.get(i);
			
			if (tmp.getName().equals(s)) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public int getChildIndex (String s) {
		
		for (int i = 0; i < children.size(); i++) {
			
			TreeNode tmp = children.get(i);
			
			if (tmp.getName().equals(s)) {
				
				return i;
				
			}
			
		}
		
		return -1;
		
	}
	
}
