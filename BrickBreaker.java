import java.awt.*;
import javax.swing.*;


/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
public class BrickBreaker extends JApplet {

    //declares panels of project
    private ControlPanel control;
    private ImagePanel image;
    private TextfieldPanel text;

    //declares variable for title label
    private JLabel titleLabel;

    //declares array of bricks, ball, & paddle
    private Brick[] bricks;
    private Ball ball;
    private Paddle paddle;

    //initiates the applet
    public void init(){

        //creates bricks and puts them in an array for the game
        bricks = new Brick[16];
        for(int i = 0; i < 16; i++){
            Brick temp;
            if(i < 8){
                temp = new Brick(i*100, 0);
                bricks[i] = temp;
            }
            else{
                temp = new Brick(i%8*100, 45);
                bricks[i] = temp;
            }
        }

        //creates an instance of the ball for the game
        ball = ball.getBall(400, 698);

        //creates paddle for game
        paddle = new Paddle(335, 720);

        //instantiates panels for program
        text = new TextfieldPanel();
        image = new ImagePanel(bricks, ball, paddle);
        control = new ControlPanel(text, image, bricks, ball, paddle);

        //sets dimensions of applet
        setSize(new Dimension(1000,800));

        //creates title label for program
        titleLabel = new JLabel("BrickBreaker");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        

        //sets up title panel
        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.BLUE);

        //sets up panel for the textfields panel and control panels
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(text, BorderLayout.NORTH);
        infoPanel.add(control, BorderLayout.SOUTH);
        infoPanel.setBackground(Color.BLUE);

        //sets up main panel of the applet
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.EAST);
        mainPanel.add(image, BorderLayout.CENTER);
        setContentPane(mainPanel);
        
    }

}