import java.util.*;

public class DictTree {

	private ArrayList<TreeNode> roots = new ArrayList<TreeNode>();

	public DictTree() {
		
		roots.add(new TreeNode("A", false));
		roots.add(new TreeNode("B", false));
		roots.add(new TreeNode("C", false));
		roots.add(new TreeNode("D", false));
		roots.add(new TreeNode("E", false));
		roots.add(new TreeNode("F", false));
		roots.add(new TreeNode("G", false));
		roots.add(new TreeNode("H", false));
		roots.add(new TreeNode("I", false));
		roots.add(new TreeNode("J", false));
		roots.add(new TreeNode("K", false));
		roots.add(new TreeNode("L", false));
		roots.add(new TreeNode("M", false));
		roots.add(new TreeNode("N", false));
		roots.add(new TreeNode("O", false));
		roots.add(new TreeNode("P", false));
		roots.add(new TreeNode("Q", false));
		roots.add(new TreeNode("R", false));
		roots.add(new TreeNode("S", false));
		roots.add(new TreeNode("T", false));
		roots.add(new TreeNode("U", false));
		roots.add(new TreeNode("V", false));
		roots.add(new TreeNode("W", false));
		roots.add(new TreeNode("X", false));
		roots.add(new TreeNode("Y", false));
		roots.add(new TreeNode("Z", false));
		
	}
	
	private int getRoot (String s) {
		
		for (int i = 0; i < roots.size(); i++) {
			
			TreeNode tmp = roots.get(i);
			
			if (tmp.getName().equals(s)) {
				
				return i;
				
			}
			
		}
		
		return -1;
		
	}
	
	public int addWord (String word) {
		
		if (word.length() < 2) {
			System.out.println("Word length too short. Not adding word.");
			return 0;
		}
		
		String buffer = "" + word.charAt(0);
		
		int rootNode = getRoot(buffer);
		
		TreeNode currNode = roots.get(rootNode);
		
		for (int i = 1; i < word.length(); i++) {
		
			buffer = buffer + word.charAt(i);
			if (currNode.hasChild(buffer)) {
				
				if ( i == (word.length()-1) ) {
					
					currNode.getChild(buffer).setValid();
					//System.out.println("I don't think this should ever get executed...");
					
				}
				
				currNode = currNode.getChild(buffer);
				
			} else {
				
				TreeNode n;
				
				if ( i == (word.length()-1) ) {
					
					n = new TreeNode(buffer, true);
					
				} else {
					
					n = new TreeNode(buffer, false);
					
				}
				
				currNode.addChild(n);
				currNode = currNode.getChild(buffer);
				
			}
			
		}
				
		return 1;
		
	}
	
	public boolean checkPath(String path) {
		
		if (path.length() < 2) {
			System.out.println("Path length too short. Not checking path.");
			return false;
		}
		
		String buffer = "" + path.charAt(0);
		
		int rootNode = getRoot(buffer);
		
		TreeNode currNode = roots.get(rootNode);
		
		for (int i = 1; i < path.length(); i++) {
			
			buffer = buffer + path.charAt(i);
			if (currNode.hasChild(buffer)) {
				
				currNode = currNode.getChild(buffer);
				
				if ( i == (path.length()-1) ) {
					
					return true;
					
				}
				
			} else {
				
				return false;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean isWord(String word) {
		
		if (word.length() < 2) {
			System.out.println("Word length too short. Not checking word.");
			return false;
		}
		
		String buffer = "" + word.charAt(0);
		
		int rootNode = getRoot(buffer);
		
		TreeNode currNode = roots.get(rootNode);
		
		for (int i = 1; i < word.length(); i++) {
			
			buffer = buffer + word.charAt(i);
			if (currNode.hasChild(buffer)) {
				
				currNode = currNode.getChild(buffer);
				
				if ( i == (word.length()-1) && currNode.checkValid() == true ) {
					
					return true;
					
				}
				
			} else {
				
				return false;
				
			}
			
		}
		
		return false;
		
	}
	
	
}
