import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * This applet demonstrates Focus events and Key events.  A colored rectangle
 * is drawn on the applet.  By pressing the arrow keys, the user can move
 * the rectangle up, down, left, or right.  By pressing the keys
 * R, G, B, or K, the user can change the color of the rectangle to red,
 * green, blue, or black, respectively.  Of course, none of the keys
 * will have any effect if the applet does not have the keyboard input
 * focus.  The applet changes appearance when it has the input focus.
 * A cyan-colored border is drawn around it.  When it does not have
 * the input focus, the message "Click to activate" is displayed
 * and the border is gray.
 * <p>This class contains a main() routine; when it is run as a stand-alone
 * application, the same JPanel that is used in the applet is shown in a
 * window instead.  The JPanel is defined using a nested subclass.
 */
public class KeyboardAndFocusDemo2 extends JApplet {
    public static boolean lost = false;
    public static boolean reset = false;
    public static int score;
    public static boolean visibilitymode = false;
    public static boolean epilepsymode = false;
    public static boolean paused = false;
    public static boolean invincible = false;
    public static int rectDirection;
    public static int ballDirectionx, ballDirectiony;
    public static final int LEFT = 0, RIGHT = 1, DOWN = 2, UP = 3;
    public static final int RECTANGLE_HEIGHT = 15;  // Length of side of rectangle.
    public static int RECTANGLE_WIDTH = 70;  // Length of side of rectangle.
    public static final int BALL_DIAMETER = 10;  // Length of side of rectangle.
    public static Color rectangleColor;  // The color of the rectangle.
    public static int rectShade;
    public static Color ballColor;  // The color of the rectangle.
    public static int rectangleTop, rectangleLeft;  // Coordinates corner of rectangle.
    public static int ballTop, ballLeft;
    public static int[] squareLocx;
    public static int[] squareLocy;
    public static Color backgroundColor;
    

   /**
    * The main program just opens a window that shows an object of type
    * ContentPanel -- a nested class that is defined later in this class.
    */
   public static void main(String[] args) {
      JFrame window = new JFrame("Brick Breaker");
      window.setContentPane(new ContentPanel());
      window.setSize(1300,1000);
      window.setLocation(100,100);
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      window.setVisible(true);
      window.setResizable(true);
      
      JPanel container = new JPanel();
      container.setLayout(new BorderLayout());
      
      JMenuBar menubar = new JMenuBar();
      JMenu optionsMenu = new JMenu("Option");
      JMenuItem resetCommand = new JMenuItem("New Game");
      ResetButtonHandler resetListener = new ResetButtonHandler();
      resetCommand.addActionListener(resetListener);
      optionsMenu.add(resetCommand);
      menubar.add(optionsMenu);
      
      container.add(new ContentPanel(), BorderLayout.CENTER);
      container.add(new SmartPanel(), BorderLayout.NORTH);
      container.add(new SliderPanel(), BorderLayout.EAST);
      
      window.setJMenuBar(menubar);
      window.setContentPane(container);
      
      
      
//      JButton startGameButton = new JButton("Play Snake");
//      container.add(startGameButton, BorderLayout.WEST);
//      container.add(new JCheckBox("Check me!"), BorderLayout.WEST);
//      JPanel colorPanel = new JPanel();
//      JSlider rectColorSlider = new JSlider(0,255,100);
//      container.add(rectColorSlider, BorderLayout.EAST);
//      container.add(new JTextField(10), BorderLayout.WEST);
//      container.add(new ContentPanel(), BorderLayout.CENTER);
//      
      
   }
   public static class VisibilityHandler implements ActionListener {
	   public void actionPerformed(ActionEvent evt) {
		   if(visibilitymode == false) {
			   visibilitymode = true;
		   } 
		   else 
			   visibilitymode = false;
	   }
   }
   public static class epilepsyButtonHandler implements ActionListener {  // The event listener.
	   public void actionPerformed(ActionEvent evt) {
		   if(epilepsymode == false) {
			   epilepsymode = true;
		   } 
		   else 
			   epilepsymode = false;
	   }
   }
   public static class ResetButtonHandler implements ActionListener { // The event listener.
	   public void actionPerformed(ActionEvent evt) {
	   if (lost = true) {
		   paused = false;
		   lost = false;
		   score = 0;
	    	 rectangleTop = 680;  // Initial position of top-left corner of rectangle.
	         rectangleLeft = 600;
	         ballTop = 670;
	         ballLeft = 625;
	    	 rectangleColor = Color.getHSBColor(1, 1, 1);
	    	 ballColor = Color.getHSBColor((float).5, 1, 1);
	    	 ballDirectiony = UP;
	    	 ballDirectionx = RIGHT;
	    	 squareLocx = new int[45];
	    	 squareLocy = new int[20];
	    	 for (int i = 0; i < squareLocx.length; i++) {
	     		 squareLocx[i] = 5 + 29*i;
	          }
	     	 for (int x = 0; x < squareLocy.length; x++) {
	     		 squareLocy[x] = 5 + 29*x;
	     	 }
	     	backgroundColor = Color.getHSBColor(0, 0, 0);
	   }
   }
}
   public static class newGameButtonHandler implements ActionListener { // The event listener.
	   public void actionPerformed(ActionEvent evt) {
		   if(invincible == false) {
			   invincible = true;
		   } 
		   else 
			   invincible = false;
	   }
}
   public static class SmartPanel extends JPanel implements MouseListener, ActionListener, ChangeListener { //////////////NORTH//////////////////
	   private JPanel info;
	   private JLabel win;
	   private JButton epilepsyButton, newButton;
	   private JCheckBox visibilityCheckBox, newGameButton;
	   private JComboBox combo;
	   private JSlider colorSlider;
	   private JTextField txtField;
	   
	   public SmartPanel() {
		   info = new JPanel();
		   info.setLayout(new GridLayout(1,10));
		   
		   win = new JLabel("Beat this game and Richardson will give you an A.");
		   win.setForeground(Color.BLUE);   // Display red text...
		   win.setBackground(Color.BLACK); //    on a black background...
		   win.setFont(new Font("Times New Roman",Font.PLAIN,9));  // in a big bold font.
//		   lose = new JLabel("Lose this game and Richardson will give you F!!!");
//		   lose.setForeground(Color.RED);   // Display red text...
//		   lose.setBackground(Color.BLACK); //    on a black background...
//		   lose.setFont(new Font("Helvitica",Font.BOLD,24));  // in a big bold font.
		   
//		   mousepos = new JLabel(mouseX + ", " + mouseY);
		   epilepsyButtonHandler epilepsyListener = new epilepsyButtonHandler();
		   epilepsyButton = new JButton("EPILEPSY");
		   epilepsyButton.addActionListener(epilepsyListener);
		   visibilityCheckBox = new JCheckBox("Disappear!");
		   VisibilityHandler visibilityListener = new VisibilityHandler();
		   
		   newGameButtonHandler newGameListener = new newGameButtonHandler();
		   newGameButton = new JCheckBox("I am INVINCIBLE!!");
		   newGameButton.addActionListener(newGameListener);
		   
		   newButton = new JButton("New Platform");
		   newButton.addActionListener(this);
		   
		   combo = new JComboBox();
		   combo.addItem("Gargantuan"); 
		   combo.addItem("Miniscule");
		   combo.addItem("Average");
		   combo.setSelectedIndex(2); 
		   combo.addActionListener(this); 
		   
		   colorSlider = new JSlider(1, 10, rectShade);
		   add(colorSlider, BorderLayout.SOUTH);
		   colorSlider.addChangeListener(this);
		   
		   txtField = new JTextField(20);  
	       txtField.addActionListener(this); 
		   
		   visibilityCheckBox.addActionListener(visibilityListener);
		   add(combo, BorderLayout.WEST);
		   add(epilepsyButton, BorderLayout.WEST);
		   add(newGameButton, BorderLayout.WEST);
		   add(newButton, BorderLayout.WEST);
		   add(visibilityCheckBox, BorderLayout.CENTER);
		   add(win, BorderLayout.EAST);
		   add(txtField, BorderLayout.EAST);
		   
		   //add(lose, BorderLayout.EAST);
	   }
	   public void actionPerformed(ActionEvent evt) {
		   if (evt.getSource().equals(epilepsyButton)) {
			   epilepsymode = true;
			   lost = false;
			   repaint();
		   }
		   if (evt.getSource().equals(combo)) {
			   if(combo.getSelectedIndex() == 0){ 
				   RECTANGLE_WIDTH = 200; 
					requestFocus();
				} 
				else if(combo.getSelectedIndex() == 1){ 
					RECTANGLE_WIDTH = 30;
					requestFocus();
				}
				else if(combo.getSelectedIndex() == 2){ 
					RECTANGLE_WIDTH = 70;
					requestFocus();
				} 
		   }
		   if (evt.getActionCommand().equals("New Platform")) {
			   win.setText("Displaying color chooser dialog.");
		         Color c = JColorChooser.showDialog(null,"Select a Color",rectangleColor);
		         if (c == null)
		            win.setText("You canceled without selecting a color.");
		         else {
		            rectangleColor = c;  // Remember selected color for next time.
		            int r = c.getRed();
		            int g = c.getGreen();
		            int b = c.getBlue();
		            win.setText("You selected RGB = (" + r + "," + g + "," + b + ").");
		         }
		   }
		   if (evt.getSource() == txtField) {
			   Graphics g = getGraphics(); 
			   g.drawString(txtField.getText()
					   , (int)(Math.random()*500), (int)(Math.random()*500));
		   }
	   }
	   public void stateChanged(ChangeEvent e) {
		   if (e.getSource() == colorSlider) {
			   rectShade = colorSlider.getValue();
	       }
		
	   	}

	   public void mousePressed(MouseEvent evt) { }
	   public void mouseEntered(MouseEvent evt) { }
	   public void mouseExited(MouseEvent evt) { }  
	   public void mouseReleased(MouseEvent evt) { } 
	   public void mouseClicked(MouseEvent evt) { }
	   
   }
   
   public static class SliderPanel extends JPanel { //////////////NORTH//////////////////
	   private JTextArea txtArea;
	   
	   public SliderPanel() {
		   JPanel Slider = new JPanel();
		   Slider.setLayout(new BorderLayout());
		   
		   txtArea = new JTextArea("W \n" + "O \n" + "O \n" + "O \n" + "O \n" + "W \n" + " \n" + "R \n" + "A \n" + "I \n" + "N \n" + "B \n" + "O \n" + "W \n" + "W \n" + "W \n" + "!!!"); 
		   txtArea.setEditable(false); 
	       txtArea.setLineWrap(true);  
		   txtArea.setFont(new Font("Comic Sans", Font.BOLD, 30)); 
	
		   add(txtArea, BorderLayout.SOUTH);
		   
		   
	   }
	   
	   
   }
   /**
    * The init() method of the applet just sets the content pane
    * of the applet to be a panel of type ContentPane, a nested class
    * that is defined below, which does all the work.
    */
   public void init() {
      setContentPane(new ContentPanel());
   }
   
   /**
    * Defines the content pane that is used in both the applet and
    * the stand-alone application version of this program.  The
    * panel displays a colored rectangle that can be moved by the user,
    * by pressing the arrow keys.  The color of the rectangle can be
    * changed by pressing the R, G, B, and K keys.  The panel also
    * displays a border and a message.
    */
   public static class ContentPanel extends JPanel
                  implements KeyListener, FocusListener, MouseListener, ActionListener {
      
         // (Note:  MouseListener is implemented only so that
         //         the applet can request the input focus when
         //         the user clicks on it.)
      
//      private static final int RECTANGLE_HEIGHT = 15;  // Length of side of rectangle.
//      private static final int RECTANGLE_WIDTH = 70;  // Length of side of rectangle.
//      private static final int BALL_DIAMETER = 10;  // Length of side of rectangle.
//      private Color rectangleColor;  // The color of the rectangle.
//      private Color ballColor;  // The color of the rectangle.
//      private int rectangleTop, rectangleLeft;  // Coordinates corner of rectangle.
//      private int ballTop, ballLeft;
//      private int[] squareLocx;
//      private int[] squareLocy;
//      private int score;
//      private static boolean lost = false;
//  	  private static boolean reset = false;
//  	  private static Color backgroundColor;
//      
//      int rectDirection;
//      int ballDirectionx, ballDirectiony;
//      private static final int LEFT = 0, RIGHT = 1, DOWN = 2, UP = 3;
      Timer timer1 = new Timer(10, this);
      

      /**
       * The constructor sets the initial position and color of the rectangle
       * and registers itself to act as a listener for Key, Focus, and 
       * Mouse events.
       */
      public ContentPanel() {
    	 score = 0;
    	 rectangleTop = 680;  // Initial position of top-left corner of rectangle.
         rectangleLeft = 600;
         ballTop = 670;
         ballLeft = 625;
         rectShade = 1;
    	 rectangleColor = Color.getHSBColor(1, 1, 1);
    	 ballColor = Color.getHSBColor((float).5, 1, 1);
    	 backgroundColor = Color.getHSBColor(0, 0, 0);
    	 ballDirectiony = UP;
    	 ballDirectionx = RIGHT;
    	 squareLocx = new int[45];
    	 squareLocy = new int[20];
    	 for (int i = 0; i < squareLocx.length; i++) {
     		 squareLocx[i] = 5 + 29*i;
          }
     	 for (int x = 0; x < squareLocy.length; x++) {
     		 squareLocy[x] = 5 + 29*x;
     	 }
         
         setBackground(backgroundColor);

         addKeyListener(this);     // Set up event listening.
         addFocusListener(this);
         addMouseListener(this);
      } // end init();
      
      
      /**
       * Draws a border, rectangle, and message in the panel.  The message and
       * the color of  the border depend on whether or not the pane has
       * the input focus.
       */
      public void paintComponent(Graphics g) {
         
         super.paintComponent(g);  // Fills the panel with its
                                   // background color, which is white.
         
         /* Draw a 3-pixel border, colored cyan if the applet has the
            keyboard focus, or in light gray if it does not. */
         
         
         if (hasFocus()) 
            g.setColor(Color.GREEN);
         else
            g.setColor(Color.BLACK);
         
         int width = getSize().width;  // Width of the applet.
         int height = getSize().height; // Height of the applet.
         g.drawRect(0,0,width-1,height-1);
         g.drawRect(1,1,width-3,height-3);
         g.drawRect(2,2,width-5,height-5);
//         for (int i = 5; i < getSize().width; i += 29) {
//        	 for (int x = 45; x < getSize().height - 150; x += 29) {
//        		 g.fillRect(i,x,20,20);
//        		 g.setColor(Color.getHSBColor((float)i/500, 1, 1));
//        	 }
//         }
         for (int a = 0; a < squareLocx.length; a++) {
           	 for (int b = 0; b < squareLocy.length; b++) {
           		 g.fillRect(squareLocx[a], squareLocy[b],20,20);
           		 g.setColor(Color.getHSBColor((float)squareLocx[a]/500, 1, 1));
           		 }
         }

        if(lost) {
        	paused = true;
        	g.setColor(Color.WHITE);
			g.drawString("WHY YOU SO BAD?",(int)(width/2), height/2 + 250);
        }
        if(!lost) {
        	if (visibilitymode == true) {
        		g.setColor(Color.BLACK);
                g.fillOval(ballLeft, ballTop, BALL_DIAMETER, BALL_DIAMETER);
        	}
        	else {
         g.setColor(ballColor);
         g.fillOval(ballLeft, ballTop, BALL_DIAMETER, BALL_DIAMETER);
        	}
        }
        if(score == 20000) {
        	paused = true;
        	g.setColor(Color.WHITE);
			g.drawString("HURRAY! YOU WIN!!",(int)(width/2), height/2 + 250);
        }
        if (!paused) {
         switch (ballDirectionx) {
   	  case LEFT:
   		  if (hasFocus()) {
   		  ballLeft -= rectShade;
             if (ballLeft < 3)
                ballDirectionx = RIGHT;
             if (epilepsymode) 
             ballColor = Color.getHSBColor((float)Math.random(), 1, 1);
             for (int y = 0; y < squareLocx.length; y++) {
            	 for (int z = 0; z < squareLocy.length; z++) {
            	 if (squareLocx[y] > ballLeft - BALL_DIAMETER && squareLocx[y] < ballLeft + BALL_DIAMETER && squareLocy[z] > ballTop - BALL_DIAMETER && squareLocy[z] < ballTop + BALL_DIAMETER) {
            		 squareLocx[y] = -100;
            		 squareLocy[z] = -100;
            		 ballDirectionx = RIGHT;
            		 score += 1000;
            		 }
            	 }
             }
             repaint();
             break;
   		  }
   	  case RIGHT:
   		if (hasFocus()) {
   		  ballLeft += rectShade;
             if (ballLeft > getWidth() - 3 - BALL_DIAMETER)
                ballDirectionx = LEFT;
             if (epilepsymode)
             ballColor = Color.getHSBColor((float)Math.random(), 1, 1);
             for (int y = 0; y < squareLocx.length; y++) {
            	 for (int z = 0; z < squareLocy.length; z++) {
            		 if (squareLocx[y] > ballLeft - BALL_DIAMETER && squareLocx[y] < ballLeft + BALL_DIAMETER && squareLocy[z] > ballTop - BALL_DIAMETER && squareLocy[z] < ballTop + BALL_DIAMETER) {
            		 squareLocx[y] = -100;
            		 squareLocy[z] = -100;
            		 ballDirectionx = LEFT;
            		 score += 1000;
                		 }
            	 }
             }
             repaint();
             break;
          }
   		}
        }
        if (!paused) {
         switch (ballDirectiony) { 
   	  case UP:
   		if (hasFocus()) {
   		  ballTop -= rectShade;
             if (ballTop < 3)
                ballDirectiony = DOWN;
             if (epilepsymode)
             ballColor = Color.getHSBColor((float)Math.random(), 1, 1);
             for (int y = 0; y < squareLocx.length; y++) {
            	 for (int z = 0; z < squareLocy.length; z++) {
            		 if (squareLocx[y] > ballLeft - BALL_DIAMETER && squareLocx[y] < ballLeft + BALL_DIAMETER && squareLocy[z] > ballTop - BALL_DIAMETER && squareLocy[z] < ballTop + BALL_DIAMETER) {
            		 squareLocx[y] = -100;
            		 squareLocy[z] = -100;
            		 ballDirectiony = DOWN;
            		 score += 1000;
                		 }
            	 }
             }
             repaint();
             break;
   		}
   	  case DOWN:
   		if (hasFocus()) {
   		  ballTop += rectShade;
             if (ballTop > getHeight() - 3 - BALL_DIAMETER && !invincible)
                lost = true;
             if (ballTop > getHeight() - 3 - BALL_DIAMETER && invincible)
            	 ballDirectiony = UP;
             if (epilepsymode)
             ballColor = Color.getHSBColor((float)Math.random(), 1, 1);
             for (int y = 0; y < squareLocx.length; y++) {
            	 for (int z = 0; z < squareLocy.length; z++) {
            		 if (squareLocx[y] > ballLeft - BALL_DIAMETER && squareLocx[y] < ballLeft + BALL_DIAMETER && squareLocy[z] > ballTop - BALL_DIAMETER && squareLocy[z] < ballTop + BALL_DIAMETER) {
            		 squareLocx[y] = -100;
            		 squareLocy[z] = -100;
            		 ballDirectiony = UP;
            		 score += 1000;
                		 }
            		 if (rectangleLeft > ballLeft - BALL_DIAMETER && rectangleLeft < ballLeft + BALL_DIAMETER && rectangleTop > ballTop - BALL_DIAMETER && rectangleTop < ballTop + BALL_DIAMETER) {
            	     ballDirectiony = UP;
            		 }
            	 }
             }
             if (rectangleLeft > ballLeft - RECTANGLE_WIDTH && rectangleLeft < ballLeft && rectangleTop > ballTop - RECTANGLE_WIDTH && rectangleTop < ballTop + BALL_DIAMETER) {
            	 ballDirectiony = UP;
             }
             repaint();
             break;
   		}
   	  }
        }
         /* Draw the rectangle. */
         
         g.setColor(rectangleColor);
         g.fillRect(rectangleLeft, rectangleTop, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
         
         /* Print a message that depends on whether the panel has the focus. */
         
         g.setColor(Color.magenta);
         if (hasFocus()) {
            g.drawString("Arrow Keys Move rectangle",7,600);
            g.drawString("K, R, G, B Change Color",7,620);
            g.drawString("Score: " + score, 7, 640);
         }
         else {
            g.drawString("Click On The Screen And Press Any Arrow Key To Start",7,600);
         }
         
      }  // end paintComponent()

      
      // ------------------- Event handling methods ----------------------
      public void actionPerformed(ActionEvent evt) {
    	  if (!paused) {
    	  switch (rectDirection) {
    	  
    	  case LEFT:
    		  if (hasFocus()) {
    		  rectangleLeft -= 8;
              if (rectangleLeft < 3)
                 rectangleLeft = 3;
              repaint();
    		  break;
    		  }
    	  case RIGHT:
    		  if (hasFocus()) {
    		  rectangleLeft += 8;
              if (rectangleLeft > getWidth() - 3 - RECTANGLE_WIDTH)
                 rectangleLeft = getWidth() - 3 - RECTANGLE_WIDTH;
              repaint();
    		  break;
    		  }
    	  }
      }
      }

//      public void stateChanged(ChangeEvent evt) {
//    	  rectShade = ((JSlider)evt.getSource()).getValue();
//    	  rectangleColor = new Color (rectShade, 0, rectShade);
//    	  requestFocus();
//    	  repaint();
//      }
      /**
       * This will be called when the panel gains the input focus.  It just
       * calls repaint().  The panel will be redrawn with a cyan-colored border
       * and with a message about keyboard input.
       */
      public void focusGained(FocusEvent evt) {
         paused = false;  // redraw with cyan border
      }
      
      
      /**
       * This will be called when the panel loses the input focus.  It just
       * calls repaint().  The panel will be redrawn with a gray-colored border
       * and with the message "Click to activate."
       */
      public void focusLost(FocusEvent evt) {
         paused = true; // redraw without cyan border
      }
      
      
      /**
       * This method is called when the user types a character on the keyboard
       * while the panel has the input focus.  If the character is R, G, B, or K
       * (or the corresponding lower case characters), then the color of the
       * rectangle is changed to red, green, blue, or black, respectively.
       */
      public void keyTyped(KeyEvent evt) {
         
         char ch = evt.getKeyChar();  // The character typed.
         
         if (ch == 'B' || ch == 'b') {
            rectangleColor = Color.BLUE;
            repaint();   // Redraw panel with new color.
         }
         else if (ch == 'G' || ch == 'g') {
             rectangleColor = Color.GREEN;
             repaint();
          }
          else if (ch == 'R' || ch == 'r') {
             rectangleColor = Color.RED;
             repaint();
          }
          else if (ch == 'K' || ch == 'k') {
             rectangleColor = Color.BLACK;
             repaint();
          }
      }  // end keyTyped()
      
      
      /**
       * This is called each time the user presses a key while the panel has
       * the input focus.  If the key pressed was one of the arrow keys,
       * the rectangle is moved (except that it is not allowed to move off the
       * edge of the panel).
       */
      public void keyPressed(KeyEvent evt) { 
         
         int key = evt.getKeyCode();  // keyboard code for the pressed key
         
         if (key == KeyEvent.VK_LEFT) {  // left arrow key
            rectDirection = LEFT;
            timer1.start();
            repaint();
         }
         else if (key == KeyEvent.VK_RIGHT) {  // right arrow key
            rectDirection = RIGHT;
            timer1.start();
            repaint();
         }
         
      }  // end keyPressed()
      
      
      /**
       * This is called each time the user releases a key while the panel
       * has the input focus.  In this class, it does nothing, but it is
       * required to be here by the KeyListener interface.
       */
      public void keyReleased(KeyEvent evt) {
    	  timer1.stop();
    	  repaint();
      }
      
      
      /**
       * This is called when the user clicks the panel with the mouse.
       * It just requests that the input focus be given to this panel.
       */
      public void mousePressed(MouseEvent evt) {
         requestFocus();
      }   
      
      
      public void mouseEntered(MouseEvent evt) { }  // Required by the
      public void mouseExited(MouseEvent evt) { }   //    MouseListener
      public void mouseReleased(MouseEvent evt) { } //       interface.
      public void mouseClicked(MouseEvent evt) { }


	
      
   } // end nested class ContentPanel

   
} // end class KeyboardAndFocusDemo