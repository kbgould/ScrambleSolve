
import java.io.*;
import java.util.*;

import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.imageio.*;


public class ScrambleSolve extends Canvas
						   implements ListSelectionListener, 
						   		      ActionListener,
						   		      ImageObserver
{
	
	private static final long serialVersionUID = 1479616588351561139L;

	public enum ProgState {
		INIT,
		ENTRY,
		SOLVING,
		SOLVED
	}
	
	public static ScrambleSolve app;
	
	public static DictTree dict = new DictTree();
	public static ArrayList<GraphNode> gameboard = new ArrayList<GraphNode>();
	public static ArrayList<Word> wordlist = new ArrayList<Word>();
	
/*	// FIRST ROW
	public static GraphNode nodelist[0];
	public static GraphNode nodelist[1];
	public static GraphNode nodelist[2];
	public static GraphNode nodelist[3];
	
	// SECOND ROW
	public static GraphNode nodelist[4];
	public static GraphNode nodelist[5];
	public static GraphNode nodelist[6];
	public static GraphNode nodelist[7];
	
	// THIRD ROW
	public static GraphNode nodelist[8];
	public static GraphNode nodelist[9];
	public static GraphNode nodelist[10];
	public static GraphNode nodelist[11];
	
	// FOURTH ROW
	public static GraphNode nodelist[12];
	public static GraphNode nodelist[13];
	public static GraphNode nodelist[14];
	public static GraphNode nodelist[15];
*/
	
	public static GraphNode[] nodelist = new GraphNode[16];
	public ArrayList<GraphNode> currPath = new ArrayList<GraphNode>();
	public Word selectedWord;
	
	// Instance attributes used in this example
	private	static JList		listbox;
	private static JPanel		gpanel;
	private static JButton		topButton;
	private static JButton		bottomButton;
	private static JScrollPane  scroll;
	private static JTextField   textbar;
	
	public static BufferedImage WhiteTile;
	public static BufferedImage RedTile;
	public static BufferedImage BlueTile;
	public static BufferedImage YellowTile;
	public static BufferedImage GreenTile;
	public static BufferedImage CyanTile;

	public static ProgState State = ProgState.INIT;
	public static int EntryState = 1;
	public int StartTile = 0;
	public Color bgColor;
	
	public static boolean RIGHT_TO_LEFT = false;
	
    public static String initListData[] = {"You're a cheater."};

    public void loadImages() {
    	
    	try {
    		
    		WhiteTile = ImageIO.read(this.getClass().getResourceAsStream("/WhiteTile.png"));
    		YellowTile = ImageIO.read(this.getClass().getResourceAsStream("/YellowTile.png"));
    		BlueTile = ImageIO.read(this.getClass().getResourceAsStream("/BlueTile.png"));
    		GreenTile = ImageIO.read(this.getClass().getResourceAsStream("/GreenTile.png"));
    		RedTile = ImageIO.read(this.getClass().getResourceAsStream("/RedTile.png"));
    		CyanTile = ImageIO.read(this.getClass().getResourceAsStream("/CyanTile.png"));
    		
    	} catch (IOException e) {
    		
    		System.err.println(e);
    		e.printStackTrace(System.err);
    		
    	}
    	
    }
    
	// Constructor of main frame
	public static void addComponentsToPane(Container pane)
	{
		
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }
        
        topButton = new JButton("Instructions");
        pane.add(topButton, BorderLayout.PAGE_START);
        
        //Make the center component big, since that's the
        //typical usage of BorderLayout.
        gpanel = new JPanel( new BorderLayout() );
        gpanel.setPreferredSize(new Dimension(250, 270));
        
        pane.add(gpanel, BorderLayout.LINE_START);
                
        textbar = new JTextField();
        
        gpanel.add(textbar, BorderLayout.SOUTH);
        
        bottomButton = new JButton("Start A New Round");
        pane.add(bottomButton, BorderLayout.PAGE_END);
        
        listbox = new JList(initListData);
        listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scroll = new JScrollPane(listbox);
        scroll.setPreferredSize(new Dimension(200,270));

        pane.add(scroll, BorderLayout.LINE_END);
        
        textbar.addActionListener(app);
        topButton.addActionListener(app);
        bottomButton.addActionListener(app);
        listbox.addListSelectionListener(app);
                
	}
	
    private static void createAndShowGUI() {
        
        //Create and set up the window.
        JFrame frame = new JFrame("Scramble Solver by Keith Gould");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        //Use the content pane's default BorderLayout. No need for
        //setLayout(new BorderLayout());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        
    }
	
	public static void main(String[] args) {
		
		app = new ScrambleSolve();
				
		InputStream filestream = app.getClass().getResourceAsStream("/ScrabbleDict");
		
		InputStreamReader ins = new InputStreamReader(filestream);
		
		BufferedReader inputStream;
		
			
		inputStream = new BufferedReader(ins);
		

		app.buildDictionaryTree(inputStream);
		
		app.prepBoard();
		
		app.loadImages();
				
		/// GRAPHICS ///
		
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(System.err);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace(System.err);
        } catch (InstantiationException ex) {
            ex.printStackTrace(System.err);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace(System.err);
        }
        /* Turn off metal's use bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

	}
	
	public void buildWordList() {
		
		for (int i = 0; i < gameboard.size(); i++) {
			
			GraphNode n = gameboard.get(i);
			
			currPath.add(n);
			WordChecker(1, n.toString(), n, n.value(), n.isDW(), n.isTW());
			currPath.remove(n);
			
			
		}
		
	}
	
	public void publishWordList() {
		
        WordComparator comparator = new WordComparator();
        Collections.sort(wordlist, comparator);
				
	}
	
	public void WordChecker(int depth, String buffer, GraphNode node, int value, boolean dw, boolean tw) {
		
		node.use();
		
		int numNeighbors = node.numNeighbors();
		
		for (int i = 0; i < numNeighbors; i++) {
			
			GraphNode neighbor = node.getNeighbor(i);
			
			if (!neighbor.isUsed()) {
			
				String tmpWord = buffer + neighbor.toString();
				
				if (dict.checkPath(tmpWord)) {
					
					currPath.add(neighbor);
										
					if (dict.isWord(tmpWord)) {
						
						int tmpValue = value + neighbor.value();
						
						if (dw || neighbor.isDW()) tmpValue = tmpValue * 2;
						
						if (tw || neighbor.isTW()) tmpValue = tmpValue * 3; 
						
						int depthBonus = getDepthBonus(depth);
						
						tmpValue += depthBonus;
						
						Word tmp = new Word(tmpWord, tmpValue);
						
						for (int k=0; k < currPath.size(); k++) tmp.addPathNode(currPath.get(k));
						
						boolean doubleExists = false;;
						
						for (int j = 0; j < wordlist.size(); j++) {
							
							Word doubleCheck = wordlist.get(j);
							
							if (doubleCheck.toString().equals(tmp.toString())) {
								
								doubleExists = true;
								
								if (tmp.getValue() > doubleCheck.getValue()) {
									
									wordlist.remove(j);
									
									wordlist.add(tmp);
									
								}
								
							}
							
						}
						
						if (!doubleExists) wordlist.add(tmp);
						
					}
					
					WordChecker(depth+1, tmpWord, neighbor, value + neighbor.value(), (dw || neighbor.isDW()), (tw || neighbor.isTW()));
					
					currPath.remove(neighbor);
					
				}
				
			}
			
		}
		
		node.clearUse();
		
	}
	
	public static int getDepthBonus(int d) {
		
		d+=1;
		
		switch(d) {
			case 0:
				return 0;
			case 1:
				return 0;
			case 2:
				return 0;
			case 3:
				return 0;
			case 4:
				return 0;
			case 5:
				return 3;
			case 6:
				return 6;
			case 7:
				return 10;
			case 8:
				return 15;
			case 9:
				return 20;
			case 10:
				return 25;
			case 11:
				return 30;
			case 12:
				return 35;
			case 13:
				return 40;
			case 14:
				return 45;
			case 15:
				return 50;
			case 16:
				return 1000;
			default:
				return 1000;
		
		}
		
	}
	
	public void buildDictionaryTree(BufferedReader inputStream) {
		try {
			
			while (inputStream.ready() == true) {
				
				String line = inputStream.readLine();
				
				String word = line.trim();
				
				if (!word.equals("")) {
					dict.addWord(line.trim());
				}
				
			}
			
		} catch (IOException e) {
			
			System.err.println(e);
			e.printStackTrace(System.err);
			
			return;
			
		}
		
	}
	
	public void prepBoard() {
		
		// FIRST ROW
		nodelist[0] = new GraphNode(0, "?", Tile.TileType.norm, 60,60);
		nodelist[1] = new GraphNode(1, "?", Tile.TileType.norm, 110,60);
		nodelist[2] = new GraphNode(2, "?", Tile.TileType.norm, 160,60);
		nodelist[3] = new GraphNode(3, "?", Tile.TileType.norm, 210,60);
		
		// SECOND ROW
		nodelist[4] = new GraphNode(4, "?", Tile.TileType.norm, 60,110);
		nodelist[5] = new GraphNode(5, "?", Tile.TileType.norm, 110,110);
		nodelist[6] = new GraphNode(6, "?", Tile.TileType.norm, 160,110);
		nodelist[7] = new GraphNode(7, "?", Tile.TileType.norm, 210,110);
		
		// THIRD ROW
		nodelist[8] = new GraphNode(8, "?", Tile.TileType.norm, 60,160);
		nodelist[9] = new GraphNode(9, "?", Tile.TileType.norm, 110,160);
		nodelist[10] = new GraphNode(10, "?", Tile.TileType.norm, 160,160);
		nodelist[11] = new GraphNode(11, "?", Tile.TileType.norm, 210,160);
		
		// FOURTH ROW
		nodelist[12] = new GraphNode(12, "?", Tile.TileType.norm, 60,210);
		nodelist[13] = new GraphNode(13, "?", Tile.TileType.norm, 110,210);
		nodelist[14] = new GraphNode(14, "?", Tile.TileType.norm, 160,210);
		nodelist[15] = new GraphNode(15, "?", Tile.TileType.norm, 210,210);
		
		// FIRST ROW NEIGHBORS
		nodelist[0].addNeighbor(nodelist[1]);
		nodelist[0].addNeighbor(nodelist[5]);
		nodelist[0].addNeighbor(nodelist[4]);
		
		nodelist[1].addNeighbor(nodelist[0]);
		nodelist[1].addNeighbor(nodelist[4]);
		nodelist[1].addNeighbor(nodelist[5]);
		nodelist[1].addNeighbor(nodelist[6]);
		nodelist[1].addNeighbor(nodelist[2]);
		
		nodelist[2].addNeighbor(nodelist[1]);
		nodelist[2].addNeighbor(nodelist[5]);
		nodelist[2].addNeighbor(nodelist[6]);
		nodelist[2].addNeighbor(nodelist[7]);
		nodelist[2].addNeighbor(nodelist[3]);
		
		nodelist[3].addNeighbor(nodelist[2]);
		nodelist[3].addNeighbor(nodelist[6]);
		nodelist[3].addNeighbor(nodelist[7]);
		
		// SECOND ROW NEIGHBORS
		nodelist[4].addNeighbor(nodelist[0]);
		nodelist[4].addNeighbor(nodelist[1]);
		nodelist[4].addNeighbor(nodelist[5]);
		nodelist[4].addNeighbor(nodelist[9]);
		nodelist[4].addNeighbor(nodelist[8]);
		
		nodelist[5].addNeighbor(nodelist[0]);
		nodelist[5].addNeighbor(nodelist[1]);
		nodelist[5].addNeighbor(nodelist[2]);
		nodelist[5].addNeighbor(nodelist[6]);
		nodelist[5].addNeighbor(nodelist[10]);
		nodelist[5].addNeighbor(nodelist[9]);
		nodelist[5].addNeighbor(nodelist[8]);
		nodelist[5].addNeighbor(nodelist[4]);
		
		nodelist[6].addNeighbor(nodelist[1]);
		nodelist[6].addNeighbor(nodelist[2]);
		nodelist[6].addNeighbor(nodelist[3]);
		nodelist[6].addNeighbor(nodelist[7]);
		nodelist[6].addNeighbor(nodelist[11]);
		nodelist[6].addNeighbor(nodelist[10]);
		nodelist[6].addNeighbor(nodelist[9]);
		nodelist[6].addNeighbor(nodelist[5]);
		
		nodelist[7].addNeighbor(nodelist[2]);
		nodelist[7].addNeighbor(nodelist[3]);
		nodelist[7].addNeighbor(nodelist[11]);
		nodelist[7].addNeighbor(nodelist[10]);
		nodelist[7].addNeighbor(nodelist[6]);
		
		// THIRD ROW NEIGHBORS
		nodelist[8].addNeighbor(nodelist[4]);
		nodelist[8].addNeighbor(nodelist[5]);
		nodelist[8].addNeighbor(nodelist[9]);
		nodelist[8].addNeighbor(nodelist[13]);
		nodelist[8].addNeighbor(nodelist[12]);
		
		nodelist[9].addNeighbor(nodelist[4]);
		nodelist[9].addNeighbor(nodelist[5]);
		nodelist[9].addNeighbor(nodelist[6]);
		nodelist[9].addNeighbor(nodelist[10]);
		nodelist[9].addNeighbor(nodelist[14]);
		nodelist[9].addNeighbor(nodelist[13]);
		nodelist[9].addNeighbor(nodelist[12]);
		nodelist[9].addNeighbor(nodelist[8]);
		
		nodelist[10].addNeighbor(nodelist[5]);
		nodelist[10].addNeighbor(nodelist[6]);
		nodelist[10].addNeighbor(nodelist[7]);
		nodelist[10].addNeighbor(nodelist[11]);
		nodelist[10].addNeighbor(nodelist[15]);
		nodelist[10].addNeighbor(nodelist[14]);
		nodelist[10].addNeighbor(nodelist[13]);
		nodelist[10].addNeighbor(nodelist[9]);
		
		nodelist[11].addNeighbor(nodelist[6]);
		nodelist[11].addNeighbor(nodelist[7]);
		nodelist[11].addNeighbor(nodelist[15]);
		nodelist[11].addNeighbor(nodelist[14]);
		nodelist[11].addNeighbor(nodelist[10]);
		
		// FOURTH ROW NEIGHBORS
		nodelist[12].addNeighbor(nodelist[8]);
		nodelist[12].addNeighbor(nodelist[9]);
		nodelist[12].addNeighbor(nodelist[13]);
		
		nodelist[13].addNeighbor(nodelist[12]);
		nodelist[13].addNeighbor(nodelist[8]);
		nodelist[13].addNeighbor(nodelist[9]);
		nodelist[13].addNeighbor(nodelist[10]);
		nodelist[13].addNeighbor(nodelist[14]);
		
		nodelist[14].addNeighbor(nodelist[13]);
		nodelist[14].addNeighbor(nodelist[9]);
		nodelist[14].addNeighbor(nodelist[10]);
		nodelist[14].addNeighbor(nodelist[11]);
		nodelist[14].addNeighbor(nodelist[15]);
		
		nodelist[15].addNeighbor(nodelist[14]);
		nodelist[15].addNeighbor(nodelist[10]);
		nodelist[15].addNeighbor(nodelist[11]);
		
		// Add nodes to the gameboard array.
		gameboard.add(nodelist[0]);
		gameboard.add(nodelist[1]);
		gameboard.add(nodelist[2]);
		gameboard.add(nodelist[3]);
		
		gameboard.add(nodelist[4]);
		gameboard.add(nodelist[5]);
		gameboard.add(nodelist[6]);
		gameboard.add(nodelist[7]);
		
		gameboard.add(nodelist[8]);
		gameboard.add(nodelist[9]);
		gameboard.add(nodelist[10]);
		gameboard.add(nodelist[11]);
		
		gameboard.add(nodelist[12]);
		gameboard.add(nodelist[13]);
		gameboard.add(nodelist[14]);
		gameboard.add(nodelist[15]);
		
		
		
		
		
	}
		
	public void drawImprint(Graphics2D g) {
		
		String text = "Scramble Solver by Keith Gould";
		String text2 = "keithbgould@gmail.com";
		g.setColor(Color.BLACK);
		Font font = new Font("Arial", Font.PLAIN, 10);
		g.setFont(font);
		g.drawString(text, 5, 230);
		g.drawString(text2, 7, 242);
		
	}
	
	public void drawText(Graphics2D g) {
		
		int baseI = 50;
		int baseJ = 38;
		
		for (int i=0; i<4; i++) {
			
			for (int j=0; j<4; j++) {
				
				int target = (i*4) + j;
				
				if (target < EntryState || State != ProgState.ENTRY) {
					g.setColor(Color.BLACK);
					Font font = new Font("Arial", Font.BOLD, 16);
					g.setFont(font);
					if (nodelist[target].toString().equals("QU")) {
						g.drawString("Qu", baseJ - 5 + (j*50), baseI + (i*50));
					} else {
						g.drawString(nodelist[target].toString(), baseJ + (j*50), baseI + (i*50));
					}
				}
					
			}
			
		}
			
			
	}
	
	public void drawPath(Graphics2D g, Word w) {
				
		if (w == null) {
			
			System.err.println("drawPath() encountered null word.");
			return;
			
		}
		
		for(int i = 0; i < w.getPathSize()-1; i++ ) {
			
			int tile1x = w.getPathNode(i).getX() - 15;
			int tile1y = w.getPathNode(i).getY() - 15;
			
			int tile2x = w.getPathNode(i+1).getX() - 15;
			int tile2y = w.getPathNode(i+1).getY() - 15;
			
			g.setColor(Color.BLACK);
			g.drawLine(tile1x, tile1y, tile2x, tile2y);
			
		}
		
	}
	
	public void drawTiles(Graphics2D g, int cyanTile) {
		
		int cyanTileI = (int) ((cyanTile-1) / 4); 
		int cyanTileJ = (cyanTile-1) % 4;
		
		for (int i=0; i < 4; i++) {
			
			for (int j=0; j < 4; j++) {
				
				int thisTile = (i*4) + j;
				
				g.drawImage(WhiteTile, (j*50)+25, (i*50)+25, 40, 40, this);
				if (nodelist[thisTile].isDL()) g.drawImage(RedTile, (j*50)+25, (i*50)+25, 40, 40, this);
				if (nodelist[thisTile].isDW()) g.drawImage(BlueTile, (j*50)+25, (i*50)+25, 40, 40, this);
				if (nodelist[thisTile].isTL()) g.drawImage(GreenTile, (j*50)+25, (i*50)+25, 40, 40, this);
				if (nodelist[thisTile].isTW()) g.drawImage(YellowTile, (j*50)+25, (i*50)+25, 40, 40, this);
				if (i == cyanTileI && j == cyanTileJ) g.drawImage(CyanTile, (j*50)+25, (i*50)+25, 40, 40, this);
				
				
			}
			
			
		}
				
		
	}
	
	public void drawTiles(Graphics2D g) {
				
		for (int i=0; i < 4; i++) {
			
			for (int j=0; j < 4; j++) {
				
				g.setColor(Color.BLACK);
				g.drawRect((i*50)+30, (j*50)+30, 30, 30);
				g.setColor(Color.YELLOW);
				g.fillRect((i*50)+30, (j*50)+30, 30, 30);
				
				
			}
			
			
		}
				
		
	}
	
	public void drawBoard() {
		
		Graphics2D g = (Graphics2D) gpanel.getGraphics();
		g.clearRect(0,0,250,250);
		
		drawImprint(g);
		
		if (State == ProgState.INIT) {
			
		} else if(State == ProgState.ENTRY) {
			drawTiles(g, EntryState);
			drawText(g);
		} else if(State == ProgState.SOLVING) {
			
			if (selectedWord == null) {
				
				Word tmp = wordlist.get(0);
				
				drawPath(g,tmp);
				
				drawTiles(g, tmp.getPathNode(0).getNum() + 1);
				
				drawText(g);
				
			} else {
				
				drawPath(g,selectedWord);
				
				drawTiles(g, selectedWord.getPathNode(0).getNum() + 1);
				
				drawText(g);				
				
			}
			
		} else if(State == ProgState.SOLVED){
			
			if (selectedWord == null) {
				
				Word tmp = wordlist.get(0);
				
				drawPath(g,tmp);
				
				drawTiles(g,tmp.getPathNode(0).getNum() + 1);
				
				drawText(g);
				
			} else {
				
				drawPath(g,selectedWord);
				
				drawTiles(g,selectedWord.getPathNode(0).getNum() + 1);
				
				drawText(g);				
				
			}
			
		}
		
		g.dispose();
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		int index = listbox.getSelectedIndex();
		
		if (index < wordlist.size() && index != -1) {
			selectedWord = wordlist.get(index);
		} else {
			selectedWord = null;
		}
		drawBoard();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		if (e.getSource().equals(topButton)) {

			String text = topButton.getText();
			
			if (text.equals("Normal")) {
				topButton.setText("Double Letter");
			} else if(text.equals("Double Letter")) {
				topButton.setText("Double Word");
			} else if(text.equals("Double Word")) {
				topButton.setText("Triple Letter");
			} else if(text.equals("Triple Letter")) {
				topButton.setText("Triple Word");
			} else if(text.equals("Triple Word")) {
				topButton.setText("Normal");
			}
			
			if (text.equals("Instructions")) {
				
				JOptionPane.showMessageDialog(this,
						"Instructions\n\nPress 'Start A New Round' to start a new round.\n" +
					    "Enter letters one at a time into the text bar and press 'Done' or hit enter.\n\n" +
					    "While entering a letter use the button on top which reads 'Normal' to toggle\n" +
					    "the tile type between Normal, Double Word, Triple Word, etc...\n\n" +
					    "Select a word in the list to see how it is formed on the board.\n" +
					    "The program only displays the highest point value formation of a given word.\n\n" +
					    "Once a puzzle has been solved you can hit 'Start A New Round' again to enter \n" +
					    "in a new game board.\n\n" +
					    "It is important to bear in mind while using the program that you are a loser.\n\n" +
					    "You always will be... people will never respect you.\n\n" +
					    "Also, you should call your grandparents every once in a while to see how they're doing...\n" +
					    "Stop being such a shitty grandchild.",
						"Instructions",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		} else if (e.getSource().equals(bottomButton) || e.getSource().equals(textbar)) {
						
			switch(State) {
			case INIT:
				State = ProgState.ENTRY;
				topButton.setText("Normal");
				bottomButton.setText("Done");
				EntryState = 1;
				drawBoard();
				
				break;
				
			case ENTRY:
				if (EntryState == 16) {
					
					String letter = textbar.getText().toUpperCase();
					
					char[] tmp = letter.toCharArray();
					
					if (letter.length() > 1) {
						textbar.setText("Please input a single letter here.");
					} else if (letter.length() == 0) {
						textbar.setText("Please input a single letter here.");
					} else if (!Character.isLetter(tmp[0])) {
						textbar.setText("What are you doing? Input a letter idiot.");
					} else {
						
						String text = topButton.getText();
						Tile.TileType type = Tile.TileType.norm;
						
						if (text.equals("Normal")) {
							type = Tile.TileType.norm;
						} else if (text.equals("Double Letter")) {
							type = Tile.TileType.dl;
						} else if (text.equals("Double Word")) {
							type = Tile.TileType.dw;
						} else if (text.equals("Triple Letter")) {
							type = Tile.TileType.tl;
						} else if (text.equals("Triple Word")) {
							type = Tile.TileType.tw;
						}
						
						nodelist[EntryState-1].newTile(letter, type);
						EntryState=1;
						State = ProgState.SOLVED;
						buildWordList();
						publishWordList();
						listbox.setListData(wordlist.toArray());
						bottomButton.setText("Start A New Round");
						topButton.setText("Instructions");
						textbar.setText("");
						scroll.repaint();
						drawBoard();
					}
					
					// CALL THE SOLVING FUNCTIONS HERE.
				} else {
					
					String letter = textbar.getText().toUpperCase();
					
					char[] tmp = letter.toCharArray();
					
					if (letter.length() > 1) {
						textbar.setText("Please input a single letter here.");
					} else if (letter.length() == 0) {
						textbar.setText("Please input a single letter here.");
					} else if (!Character.isLetter(tmp[0])) {
						textbar.setText("What are you doing? Input a letter idiot.");
					} else {
						
						String text = topButton.getText();
						Tile.TileType type = Tile.TileType.norm;
						
						if (text.equals("Normal")) {
							type = Tile.TileType.norm;
						} else if (text.equals("Double Letter")) {
							type = Tile.TileType.dl;
						} else if (text.equals("Double Word")) {
							type = Tile.TileType.dw;
						} else if (text.equals("Triple Letter")) {
							type = Tile.TileType.tl;
						} else if (text.equals("Triple Word")) {
							type = Tile.TileType.tw;
						}
						
						nodelist[EntryState-1].newTile(letter, type);
						EntryState++;
						textbar.setText("");
						topButton.setText("Normal");
						drawBoard();
					}
				}
				break;
				
			case SOLVING:
				State = ProgState.SOLVED;
				drawBoard();
				break;
				
			case SOLVED:
				if (e.getActionCommand() == "Start A New Round") {
					for (int i = 0; i < nodelist.length; i++) nodelist[i].newTile("?", Tile.TileType.norm);
					topButton.setText("Normal");
					wordlist.clear();
					bottomButton.setText("Done");
					State = ProgState.ENTRY;
					EntryState = 1;
				}
				drawBoard();
				break;
			
			}
			
		}
				
	}

}
