
public class Tile {

	public enum TileType { dl, tl, dw, tw, norm }
	String Letter;
	char l;
	boolean DL;
	boolean DW;
	boolean TL;
	boolean TW;
	int value;
	
	public Tile(String letter, TileType t) {
		
		Letter = letter;
		l = letter.charAt(0);
		
		switch(l) {
		case 'A':
			value = 1;
			break;
		case 'B':
			value = 4;
			break;
		case 'C':
			value = 4;
			break;
		case 'D':
			value = 2;
			break;
		case 'E':
			value = 1;
			break;
		case 'F':
			value = 4;
			break;
		case 'G':
			value = 3;
			break;
		case 'H':
			value = 3;
			break;
		case 'I':
			value = 1;
			break;
		case 'J':
			value = 10;
			break;
		case 'K':
			value = 5;
			break;
		case 'L':
			value = 2;
			break;
		case 'M':
			value = 4;
			break;
		case 'N':
			value = 2;
			break;
		case 'O':
			value = 1;
			break;
		case 'P':
			value = 4;
			break;
		case 'Q':
			Letter = "QU";
			value = 10;
			break;
		case 'R':
			value = 1;
			break;
		case 'S':
			value = 1;
			break;
		case 'T':
			value = 1;
			break;
		case 'U':
			value = 2;
			break;
		case 'V':
			value = 5;
			break;
		case 'W':
			value = 4;
			break;
		case 'X':
			value = 8;
			break;
		case 'Y':
			value = 3;
			break;
		case 'Z':
			value = 10;
			break;
			
			
		
		}
		
		switch(t) {
			case dl:
			    value = value * 2;
			    DL = true;
				break;
			case tl:
				value = value * 3;
				TL = true;
				break;
			case dw:
				DW = true;
				break;
			case tw:
				TW = true;
				break;
			case norm:
				break;
		}
		
		
	}
	
	public int getValue() {
		
		return value;
		
	}
	
	public boolean isDL() {
		
		return DL;
		
	}
	
	public boolean isTL() {
		
		return TL;
		
	}
	
	public boolean isDW() {
		
		return DW;
		
	}
	
	public boolean isTW() {
		
		return TW;
		
	}
	
	public String toString() {
		
		return Letter;
		
	}
		
}
