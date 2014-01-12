// Sujith Vishwajith
// April 22nd, 2013
// Game.java 
// This game is similar to Angry Birds but is mainly based on physics. The user must navigate and complete each
// level in order to beat the game. Certain questions related to physics will be aksed when you beat each level.
// A total of 5 twisted mind boggling levels are in the game. Try to reach the Hall of Fame for ultimate glory 
// and pride.

// All Imports:
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.awt.image.*;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.*;
import javax.imageio.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game {

    public String problem, choicea, choiceb, choicec, choiced, answer;
    public String [] questions = new String[100];	//string grid
    //Integer values to keep the score and track pauses
    public int score, pausegame;
    //boolean values deciding if it should launch, hits something, or reverses when it bounces or goes too far
    public boolean gogo, detonate, dead, reverse, woodtop, nextlevel, hit, rev;
    //Images for the game and the levels
    Image person, wall, border, borderflat, fire, sand, cannon, grass, launch, bigbomb, warn;
    Image badguy, wood;
    char c;
    //Integer values for coordinates and health of the projectile
    int y5, x5, initx, inity, xhealth, xdirect, ydirect, slope, xinit, yinit;
    //Integer values for the distance and lvl numbers
    int force, distance, xd, yd, xbounce, badx, bady, xhero, lvl1, lvl2, lvl3;
    //doubles used in calculations of parabolic motion
    double xlaunch, ylaunch, xspd, yspd, mass, g, superdist;
    //booleans for deciding whether it hit the blocks or the bad guy
    public boolean boomer, test, endgame, infiniteoxygen, woodtop1;
    public boolean woodtopright, woodtopbottom, woodtopleft, woodbottomleft;
    //Buttons used for the game panel which have certain features
    JButton next, regenerate, changer, cheat, stats, exit;
    //Images for eye candy in background of the level
    Image cloud, sun, plane, superhero;
    JFrame frame;//new frame
    MyPanel panel;//new panel
    //Container that holds layouts and levels
    private Container containMe;
    private CardLayout forum;
    
    //Creating all tehe cards for the leves and instruction
    private OPSPanel1 op1;
    private OPSPanel2 op2;
    private OPSPanel3 op3;
    private OPSPanel4 op4;
    private OPSPanel5 op5;
    public OPSPanel6 op6;
    public int diff;
    public int oxygen;
    //variables deciding start location and the player user selects
    public int player, x1, y1, xbomb, ybomb;

    //main method which calls Run()
    public static void main(String[] args) {
        Game force = new Game();
        force.Run();
    }
    //method which sets up panels and adds components
    public void Run() {
        //Scanner for reading questons
    
        Scanner keyboard = new Scanner(System.in);
        String qtemp;
        try {	// tries to read the file
            keyboard = new Scanner(new File("Questions.txt"));
        } catch (FileNotFoundException e) {
            System.err.printf("ERROR: Cannot open file");
            System.exit(1);
        }

        for (int xcor = 0; xcor < 35; xcor ++) {
            qtemp = keyboard.nextLine();
            questions[xcor] = qtemp;

        }
           
        keyboard.close();	//closes the file
        frame = new JFrame("Physics Raiders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        containMe = new Container();
        forum = new CardLayout();			//Creating the basic CardLayout interface
        containMe.setLayout(forum);
        panel = new MyPanel();
        panel.setLayout(null);
        //Settings panel
        op1 = new OPSPanel1();					//nullLayout
        op1.setLayout(null);
        op1.setBounds(0, 0, 1000, 650);                         //Making it take up the whole screen
        //Help Panel
        op2 = new OPSPanel2();					//nullLayout
        op2.setLayout(null);
        op2.setBounds(0, 0, 1000, 650);
        //Panel for level 1
        op3 = new OPSPanel3();					//nullLayout
        op3.setLayout(null);
        op3.setBounds(0, 0, 1000, 650);
        op3.setFocusable(true);
        op3.setRequestFocusEnabled(true);
        op2.setRequestFocusEnabled(true);
        op3.setBounds(0, 0, 1000, 20);
        //Panel for level 2
        op4 = new OPSPanel4();					//nullLayout
        op4.setLayout(null);
        op4.setBounds(0, 0, 1000, 650);
        op4.setFocusable(true);
        op4.setRequestFocusEnabled(true);
        op4.setBounds(0, 0, 1000, 20);
        //Panel for level 3
        op5 = new OPSPanel5();					//nullLayout
        op5.setLayout(null);
        op5.setBounds(0, 0, 1000, 650);
        op5.setFocusable(true);
        op5.setRequestFocusEnabled(true);
        op5.setBounds(0, 0, 1000, 20);
        //Panel for Credits
        op6 = new OPSPanel6();					//nullLayout
        op6.setLayout(null);
        op6.setBounds(0, 0, 1000, 650);
        op6.setFocusable(true);
        op6.setRequestFocusEnabled(true);
        op6.setBounds(0, 0, 1000, 20);
        //Adds the container to the frame
        frame.getContentPane().add(containMe, BorderLayout.CENTER);	// add panel to frame	
        //Add all the panels to the container in a CardLayout
        containMe.add(panel, "Intro");
        containMe.add(op1, "op1");
        containMe.add(op2, "op2");
        containMe.add(op3, "op3");
        containMe.add(op4, "op4");
        containMe.add(op5, "op5");
        containMe.add(op6, "op6");
        //frame.addKeyListener(panel);		
        //set the size of the frame to the size of the panel
        frame.setSize(1000, 650);	// explicitly set size in pixels
        frame.setVisible(true);		// set to false to make invisible
        frame.setResizable(false);
        frame.setLocation(150, 40);
        //Sets the location of the initial shooter
        x1 = 55;
        y1 = 60;
        //sets the location of the launcher
        xbomb = 400;
        ybomb = 400;
        //Sets the soore to 0 intially
        score = 0;

    }//end Run
    
    //This class is the main intro screen and the user can select help, play, or exit
    class MyPanel extends JPanel implements ActionListener {
        //Images used on the screen for the background etc.
        Image intro, host, robot;
        //Button to continue playing the game
        JButton next;
        //X value for chaning color in the title
        int x;

        public MyPanel() {
            //Try catch block which opens up the images
            try {
                intro = ImageIO.read(new File("intro.jpg"));
                host = ImageIO.read(new File("host.png"));
                robot = ImageIO.read(new File("robot.png"));
            } catch (IOException e) {}
            //Jbutton next for Continuing on to the settings page
            JButton next = new JButton("Next");
            next.setSize(100, 50);
            next.setLocation(600, 80);
            next.addActionListener(this);
            next.setForeground(Color.white);
            next.setBackground(Color.blue);
            this.add(next);//add the JButton to the panel
            //Jbutton help for continuing onto the help screen
            JButton help = new JButton("Help");
            help.setSize(100, 50);
            help.setLocation(710, 80);
            help.addActionListener(this);
            help.setForeground(Color.white);
            help.setBackground(Color.green);
            this.add(help);//add the JButton to the panel
            //Jbutton exit for exiting the game if the user desires to
            JButton exit = new JButton("Exit Game");
            exit.setSize(150, 50);
            exit.setLocation(820, 80);
            exit.addActionListener(this);
            exit.setForeground(Color.white);
            exit.setBackground(Color.red);
            this.add(exit);//Add the Jbutton to the panel
            RepaintAction action = new RepaintAction();//creates a new Repaint Action for the timer
            Timer mouth = new Timer(160, action);//new timer with 500ms for mouth and moving the monsters
            mouth.start();//starts the timer

        }//end MyPanel constructor

        //Timer for the color changer
        private class RepaintAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                x = x + 1;
                if (x == 6) {
                    x = 0;
                }
                repaint();// repainting to show the color changing
            }//end timermethod
        }//end RepaintAction

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            //Draw out the imaages and string layout on the panel
            g.drawImage(intro, 0, 0, null);
            g.drawImage(host, 30, 170, null);
            g.drawImage(robot, 330, 190, null);
            g.setFont(new Font("Calibri", Font.BOLD, 30));
            g.drawString("Made By:", 380, 500);
            g.drawString("Sujith Vishwajith", 380, 540);
             
            //Color Rotation
            //If the color is changing then alternate the color
            if (x == 1) {
                g.setColor(Color.white);
            } else if (x == 2) {
                g.setColor(Color.yellow);
            } else if (x == 3) {
                g.setColor(Color.gray);
            } else if (x == 4) {
                g.setColor(Color.red);
            } else if (x == 0) {
                g.setColor(Color.cyan);
            } else if (x == 5) {
                g.setColor(Color.orange);
            }
            //Draw out the title depending on the color
            g.setFont(new Font("Calibri", Font.BOLD, 80));
            g.drawString("  Physics Raiders", 05, 120);
            g.setFont(new Font("Calibri", Font.BOLD, 60));
            g.setColor(Color.white);
        }//end paintComponent

        //Action performed when you press the buttons
        public void actionPerformed(ActionEvent evt) {
            String command = evt.getActionCommand();
            if (command.equals("Exit Game")) { //Exits the game if exit is pressed
                System.exit(0);
            } else if (command.equals("Next")) {//Moves onto the game if pressed
                forum.next(containMe);//next panel in the layout
            } else if (command.equals("Help")) {//Goes to the help screen if pressed
                forum.next(containMe);
                forum.next(containMe);
            }//end else if

        }//end ActionPerformed
    }//end MyPanel
    
    //Panel for the Option screen
    class OPSPanel1 extends JPanel implements ActionListener {
        //COmponents and location for the images and Jlabel
        private JLabel options;
        public JButton save, regular;
        Image face1, face2, face3, settings;
        public JButton char1, char2, char3;
        //locations for the circle and rectangle to highlight the selected option
        private int x, y, x2, y2;
        
        //Constructor adding on the images and button
        public OPSPanel1() {
            try {
                face1 = ImageIO.read(new File("face1.png"));
                face2 = ImageIO.read(new File("face2.png"));
                face3 = ImageIO.read(new File("face3.png"));
                settings = ImageIO.read(new File("settings.png"));
            } catch (IOException e) {
            }

            //jlabel for the title
            options = new JLabel("Choose Your Settings:");
            options.setForeground(Color.green);
            options.setFont(new Font("Times New Roman", Font.BOLD, 50));
            options.setSize(600, 60);
            options.setLocation(250, 5);
            this.add(options);
            
            //Adding onto the components for the characters and difficulty
            JButton save = new JButton("Save");
            save.setSize(100, 50);
            save.setLocation(450, 550);
            save.addActionListener(this);
            save.setForeground(Color.black);
            save.setBackground(Color.orange);
            this.add(save);
            JButton char1 = new JButton("Ratchet");
            char1.setSize(80, 30);
            char1.setLocation(170, 330);
            char1.addActionListener(this);
            char1.setForeground(Color.black);
            char1.setBackground(Color.green);
            this.add(char1);
            JButton char2 = new JButton("Jimmy");
            char2.setSize(80, 30);
            char2.setLocation(465, 330);
            char2.addActionListener(this);
            char2.setForeground(Color.black);
            char2.setBackground(Color.green);
            this.add(char2);
            JButton char3 = new JButton("Rock");
            char3.setSize(80, 30);
            char3.setLocation(720, 330);
            char3.addActionListener(this);
            char3.setForeground(Color.black);
            char3.setBackground(Color.green);
            this.add(char3);
            //Initialize the rectangle and circle coordinates out of the screen
            x = 10000;
            y = 10000;
            //Default value
            player = 1;
            //Jbuttons added to the panel for Difficuly
            JButton regular = new JButton("Regular Difficulty");
            regular.setSize(200, 100);
            regular.setLocation(250, 130);
            regular.addActionListener(this);
            regular.setBackground(Color.red);
            regular.setForeground(Color.white);
            this.add(regular);
            JButton advanced = new JButton("Advanced Difficulty");
            advanced.setSize(200, 100);
            advanced.setLocation(530, 130);
            advanced.addActionListener(this);
            advanced.setBackground(Color.blue);
            advanced.setForeground(Color.white);
            this.add(advanced);
            //Defaults for difficulty and rectangle out of the screen
            diff = 1;
            y2 = 260;
            x2 = 100000;

        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //setBackground(Color.cyan);
            //draws out the images and difficulties and also circles and highlight
            //the slected option
            g.drawImage(settings, 0, 0, null);
            g.setColor(Color.red);
            g.fillRect(200, 70, 575, 5);
            g.drawImage(face1, 150, 380, null);
            g.drawImage(face2, 440, 380, null);
            g.drawImage(face3, 700, 380, null);
            g.setColor(Color.yellow);
            g.drawOval(x, y, 180, 150);
            g.fillRect(x2, y2, 160, 5);
        }//end paintComponent

        public void actionPerformed(ActionEvent evt) {
            String command = evt.getActionCommand();
            if (command.equals("Save")) {//Saves the settigns and moves on if pressed
                forum.next(containMe);
                forum.next(containMe);
                op3.requestFocus();
            } else if (command.equals("Jimmy")) {//Sets the player value and the location of the circle
                x = 412;
                y = 370;
                player = 2;
            } else if (command.equals("Rock")) {//Sets the player value and the location of the circle
                x = 672;
                y = 370;
                player = 3;
            } else if (command.equals("Ratchet")) {//Sets the player value and the location of the circle
                x = 122;
                y = 370;
                player = 1;
            } else if (command.equals("Regular Difficulty")) {//Sets the difficulty value and rectangle highlight
                diff = 1;
                x2 = 270;
            } else if (command.equals("Advanced Difficulty")) {//Sets the difficulty value and rectangle highlight
                diff = 2;
                x2 = 550;
            }
            repaint();//repaints the screen
        }
    }//end OSPPanel1

    //Help Panel for instructions
    class OPSPanel2 extends JPanel implements ActionListener {
        //Jbuttons to go back to intro or move on
        JButton cont, back;
        //JTextArea for the location and text
        JTextArea help;
        
        //Constructor for buttons and text
        public OPSPanel2() {
            JButton cont = new JButton("Continue");
            cont.setSize(100, 50);
            cont.setLocation(810, 10);
            cont.addActionListener(this);
            cont.setBackground(Color.blue);
            cont.setForeground(Color.white);
            this.add(cont);
            JButton back = new JButton("Back");
            back.setSize(100, 50);
            back.setLocation(690, 10);
            back.addActionListener(this);
            back.setBackground(Color.red);
            back.setForeground(Color.white);
            this.add(back);
            //Types in the instructions and iems
            help = new JTextArea("How To Play Physics Raiders:\n\nIn the settings page select you character and the difficulty of your      choice. You can choose between "
                    + "Ratchet, Jimmy, and The Rock.        Each character has their own special mass which affects its speed.    You aim by holding down the left mouse"
                    + "button and press 'f' or click  to shoot. The objective of the game is to hit the bad guy with your     ball and raid him of his loot. "
                    + "The amount of loot you rade is your       final score. You will earn 50 points for each wood block broken, 100 points for hitting the "
                    + "bad guy and 50 points per question. On the       advanced difficulty you will have an obstacle flying around whom if you hit will cause you to "
                    + "lose 10 points. Your options for the questionswill appear on the terminal window. Type them in carefully!");
            help.setForeground(Color.black);
            //Adds the help and makes it wrap around
            help.setFont(new Font("Times New Roman", Font.BOLD, 32));
            help.setSize(950, 530);
            help.setLocation(30, 70);
            help.setLineWrap(true);
            help.setEditable(false);
            this.add(help);
        }

        public void paintComponent(Graphics g) {
            //Draws out the rectangle formats and also the instructions
            Font myFont2 = new Font("Times New Roman", Font.BOLD, 50);
            super.paintComponent(g);
            setBackground(Color.orange);
            g.setColor(Color.white);
            g.fillRect(20, 70, 950, 530);
            g.setColor(Color.black);
            g.setFont(myFont2);
            //Prints out the title
            g.drawString("How To Play the Game", 110, 50);
        }//end paintComponent

        public void actionPerformed(ActionEvent evt) {
            String command = evt.getActionCommand();
            if (command.equals("Continue")) {//Continues the game if button is pressed
                forum.first(containMe);//back to the first panel
                forum.next(containMe);
            } else if (command.equals("Back")) {//Goes back to intro if pressed
                forum.first(containMe);//back to the first panel
            }
        }
    }//end OSPPanel2

    //Actual Game Panel
    class OPSPanel3 extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
        
        //Timer for the repainting of projectiles and superman
        RepaintAction action = new RepaintAction();//creates a new Repaint Action for the timer
        Timer boom = new Timer(500, action);
        //New Timer for the health bar and reset
        CoolAction health = new CoolAction();
        Timer tool = new Timer(1, health);
        //New timer for the flying man on top
        FlyAction superman = new FlyAction();//creates a new Repaint Action for the timer
        Timer fly = new Timer(5, superman);
        
        //Construcor which sets up the level booleans
        public OPSPanel3() {
            //Sets all the wooden blocks to exist to true
            reverse = false;
            woodtop = true;
            woodtop1 = true;
            xhero = 50;
            xbounce = 0;
            dead = false;
            //sets the locationa and protection of the bad guy
            badx = 850;
            woodbottomleft = true;
            bady = 500;
            g = 2;
            detonate = false;
            gogo = true;
            //initial launch posisiton
            xlaunch = 70;
            ylaunch = 475;
            //sets the game intially to not end
            endgame = false;
            xhealth = 150;
            initx = x1 + 55;
            inity = y1 + 60;
            pausegame = 0;
            requestFocus();
            addKeyListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
            y5 = 60;
            x5 = 10;
            score = 0;
            woodtopbottom = true;
            woodtopleft = true;
            oxygen = 100;
            
            //Adds all the Jbuttons on top for the panel (This is used for every level and is referred to
            //as 1* when commented on below
            JButton next = new JButton("Next");//JButton for going to next level upon completion
            next.setSize(80, 30);
            next.setLocation(300, 12);
            next.addActionListener(this);
            next.setBackground(Color.cyan);
            next.setForeground(Color.black);
            this.add(next);
            JButton regenerate = new JButton("Restart Level");//Restart button for restarting the level if failed
            regenerate.setSize(120, 30);
            regenerate.setLocation(390, 12);
            regenerate.addActionListener(this);
            regenerate.setBackground(Color.orange);
            regenerate.setForeground(Color.black);
            this.add(regenerate);
            JButton stats = new JButton("Game Stats");//Button that shows game score when clicked
            stats.setSize(120, 30);
            stats.setLocation(520, 12);
            stats.addActionListener(this);
            stats.setBackground(Color.green);
            stats.setForeground(Color.black);
            this.add(stats);
            JButton cheat = new JButton("Enter Cheats");//JOption Pane which allows user to enter cheats
            cheat.setSize(120, 30);
            cheat.setLocation(650, 12);
            cheat.addActionListener(this);
            cheat.setBackground(Color.LIGHT_GRAY);
            cheat.setForeground(Color.black);
            this.add(cheat);
            JButton changer = new JButton("Change Character");//JButton which changes characters for user choice
            changer.setSize(150, 30);
            changer.setLocation(780, 12);
            changer.addActionListener(this);
            woodtopleft = true;
            changer.setBackground(Color.yellow);
            changer.setForeground(Color.black);
            this.add(changer);
            JButton exit = new JButton(":)");//JButton to trick user into closing the game :)
            exit.setSize(45, 30);
            exit.setLocation(940, 12);
            exit.addActionListener(this);
            exit.setBackground(Color.red);
            exit.setForeground(Color.black);
            this.add(exit);
            nextlevel = false;
            hit = false;
        }

        //Mouse Methods
        public void mouseClicked(MouseEvent e) {
            if (endgame == false) {
                Vector();//calls the shoot method when the mouse is clicked
            }
            repaint();
        }

        public void mousePressed(MouseEvent e) {
        }
        
        //This method moves the target around within a certain range
        public void mouseDragged(MouseEvent e) {
            requestFocus();
            if (e.getX() - 15 < 600 && e.getX() - 15 > 50) {
                xbomb = e.getX();  // x-coordinate where user clicked.
            }
            if (e.getY() - 15 > 60 && e.getY() - 15 < 520) {
                ybomb = e.getY();  // y-coordinate where user clicked.
            }
            repaint();//repaints
    
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }

        //Repaint Method which resets the level and is called in Restart
        private class RepaintAction implements ActionListener {
            
            public void actionPerformed(ActionEvent evt) {
                //Reinitializes the variables and stops the timers
                xbomb = 400;
                xlaunch = 70;
                ylaunch = 475;
                ybomb = 400;
                boomer = false;
                boom.stop();
                tool.stop();
                repaint();//repaints the screen
            }//end timermethod
        }//end RepaintAction

        //This timer does the mathematical computations that determins the parabolic motion
        private class CoolAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                //sets the object to detonate on impact
                detonate = true;
                //adds the values to the loaction based on the returned values
                xlaunch += xspd;
                ylaunch += yspd;
                //adds artificial gravity neglecting drag
                yspd += g;
                //Bounce once beforing falling off the screen (increases the range)
                if (xbounce == 0) {
                    if ((int) ylaunch > 520) {
                        ylaunch = 500;
                        yspd = -yspd / 2;
                        detonate = false;
                        xbounce = 1;
                    }
                } else if (xbounce == 1) {
                    detonate = true;//detonates after 1st bounce
                }
                repaint();//repaints the screen
            }
        }//end RepaintAction

        //This timer controls the flying man and makes him go back and forth
        private class FlyAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                //if the game is still going then reverse at vertain locations and add or subtract 1 to their location
                if (gogo = true) {
                    if (xhero < 70) {
                        reverse = false;
                    }
                    if (xhero > 700) {
                        reverse = true;
                    }
                    if (reverse == false) {
                        xhero += 1;
                    }
                    if (reverse == true) {
                        xhero -= 1;
                    }
                    repaint();//repaint
                }//end timermethod
            }//end RepaintAction
        }

        public void keyPressed(KeyEvent e) {
            c = e.getKeyChar();
            if (c == 'f' && endgame == false) {
                Vector();//Calls the shooting method if you press 'f' key
            }
            repaint();//repaints with values

        }//end keyPressed
        
        //actual method which computates the parabolic motion required to travel throuhg the target
        public void Vector() {
            detonate = true;
            tool.stop();
            //resets the location of the person
            xlaunch = 70;
            ylaunch = 475;
            //sets the target point to last shot before clicking
            xinit = xbomb;
            yinit = ybomb;
            //super variables are used to calculate the power of the launch and angle
            double superx = xinit - xlaunch;
            double supery = ylaunch - yinit;
            superdist = Math.sqrt((superx * superx) + (supery * supery));
            double theta;
            //Sets the mass to equal the distance over 20
            mass = (int) superdist / 20;
            //Makes suer that the variable is a number
            do {
                theta = Math.atan(((mass * mass) + Math.sqrt((Math.pow(mass, 4)) - (g * (g * (superx * superx) + (2 * supery * mass * mass))))) / (g * superx));
                mass = mass + 1;
            } while (Double.isNaN(theta));

            gogo = true;
            //launches the projectile if within the parameters
            if (xlaunch < xinit && ylaunch > yinit) {
                tool.start();
            }
            
            //sets the speed to vector direction of the angle
            xspd = Math.cos(theta) * mass;
            yspd = -Math.sin(theta) * mass;
        }
        
        //Method which draws out the entire screen
        public void paintComponent(Graphics g) {
            fly.start();
            requestFocus();
            //Graphics Rendering for rotating the images
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(rh);
            //The following lines until the * represent the initial drawings of the setting
            if (xhealth == 0) {
                endgame = true;
            }
            //Try catch to import all images required
            try {
                //If loop to open appropriate image based on charachter selected in option screen
                if (player == 1) {
                    person = ImageIO.read(new File("play1.png"));
                    bigbomb = ImageIO.read(new File("launch1.png"));
                    mass = 20;
                }
                if (player == 2) {
                    person = ImageIO.read(new File("play2.png"));
                    bigbomb = ImageIO.read(new File("launch2.png"));
                    mass = 12;
                }
                if (player == 3) {
                    person = ImageIO.read(new File("play3.png"));
                    bigbomb = ImageIO.read(new File("launch3.png"));
                    mass = 6;
                }
                ImageIcon ii = new ImageIcon(this.getClass().getResource("wood.png"));
                wood = ii.getImage();
                sun = ImageIO.read(new File("sun.png"));
                superhero = ImageIO.read(new File("superhero.png"));
                plane = ImageIO.read(new File("plane.png"));
                cloud = ImageIO.read(new File("cloud.png"));
                badguy = ImageIO.read(new File("badguy.png"));
                cannon = ImageIO.read(new File("cannon.png"));
                fire = ImageIO.read(new File("fire.png"));
                wall = ImageIO.read(new File("gameback.png"));
                border = ImageIO.read(new File("border.png"));
                borderflat = ImageIO.read(new File("borderflat.png"));
                sand = ImageIO.read(new File("sand.jpg"));
                grass = ImageIO.read(new File("grass.png"));
                launch = ImageIO.read(new File("target.png"));
                warn = ImageIO.read(new File("warn.png"));
            } catch (IOException e) {
            }
            //Font for script
            Font myFont2 = new Font("Times New Roman", Font.BOLD, 50);
            super.paintComponent(g);
            //Border Drawing Layout
            int grassx = 30;
            g.drawImage(wall, 0, 0, null);
            //Draws out the grass
            for (int a = 0; a < 17; a++) {
                g.drawImage(grass, grassx, 535, null);
                grassx += 51;
            }
            for (int a = 0; a < 14; a++) {
                g.drawImage(border, 10, y5, null);
                g.drawImage(border, 950, y5, null);
                y5 = y5 + 39;
            }
            //draws out the border horizontally
            for (int a = 0; a < 32; a++) {
                g.drawImage(borderflat, x5, 580, null);
                x5 = x5 + 30;
            }
            y5 = 60;
            x5 = 10;
            //sets the location of the health bar
            
            //Level Title
            g.setColor(Color.white);
            g.setFont(new Font("Calibri", Font.BOLD, 30));
            g.drawString("Level 1", 30, 37);
            g.setColor(Color.green);
            //g.drawString("Options:", 150, 37);
            g.setColor(Color.red);
            g.fillRect(130, 18, 150, 20);
            g.setColor(Color.green);
            g.fillRect(130, 18, xhealth, 20);
            g.setColor(Color.black);
            g.drawRect(130, 18, 150, 20);
            g.setColor(Color.white);
            g.drawRect(130, 18, xhealth, 20);
            g.setColor(Color.gray);
            g.fillRect(10, 55, 970, 5);
            g.drawImage(cannon, 40, 470, null);
            g.drawImage(cloud, 105, 75, null);
            g.drawImage(cloud, 440, 75, null);
            if (diff == 2)
            g.drawImage(superhero, xhero, 90, null);
            //Draw the shooter and target
            g.setColor(Color.red);
            g.drawLine(80, 490, xbomb, ybomb + 3);
            g.drawImage(launch, xbomb - 15, ybomb - 15, null);
            // ******
            //Clouds and etc for eycandy
            g.drawImage(sun, 810, 70, null);
            //draws out the fire if impacts
            if (boomer == true) {
                g.drawImage(fire, xbomb - 12, ybomb - 10, null);
            }
            
            //Checks whether you hit your teamate and subracts 10 points if you did
            if ((int) xlaunch > xhero && (int) xlaunch < xhero + 50 && (int) ylaunch > 90 && (int) ylaunch < 140 && diff == 2) {
                g.setColor(Color.RED);
                score = score - 10;
                g.drawString("Ouch!", xhero + 50, 95);
            }
            
            //Draws the warning if projectile goes too high
            g.setColor(Color.cyan);
            if (ylaunch < 0) {
                g.drawImage(warn, (int) xlaunch, 65, null);
            }
            //If Game ends then draw out this and say you win
            if (endgame == true) {
                g.drawImage(fire, xbomb - 15, ybomb - 15, null);
                g.setColor(Color.red);
                g.setFont(new Font("calibri", Font.BOLD, 100));
                g.drawString("YOU WIN!", 280, 320);
                g.setColor(Color.blue);
                g.setFont(new Font("calibri", Font.BOLD, 50));
                g.drawString("Press Next to Proceed!", 250, 380);
                fly.stop();//stops the superman from moving
            }
            if (detonate = false) {
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
            }
            // Begin level 1 drawing
            if (dead == false) {
                g.drawImage(badguy, badx, bady, null);
            }
            
            //Rotates the images appropriately and lays them out
            AffineTransform at = new AffineTransform();
            at.setToTranslation(820, 495); 				// position x,y
            //woodleft
            at.rotate(Math.toRadians(90), 15, 15);
            if (woodtopleft == true) {
                g2.drawImage(wood, at, null);
            }
            
            //Woodtopright
            at.setToTranslation(890, 495); 				// position x,y
            at.rotate(Math.toRadians(90), 15, 15);
            if (woodtopright == true)
                g2.drawImage(wood, at, null);
            
            if (woodtop == true) {
                g.drawImage(wood, 828, 475, null);
                g.drawImage(wood, 863, 475, null);
            }
            at.setToTranslation(745, 515); 				// position x,y
            //Wood bottom left  
            at.rotate(Math.toRadians(100), 28, 11);
            if (woodbottomleft == true) {
                g2.drawImage(wood, at, null);
            }

            //Wood top bottom
            at.setToTranslation(770, 445); 				// position x,y          
            at.rotate(Math.toRadians(120), 28, 11);
            if (woodtopbottom == true) {
                g2.drawImage(wood, at, null);
            }

            //Wood Covering Bad Guy
            at.setToTranslation(840, 415); 				// position x,y
            at.rotate(Math.toRadians(180), 28, 11);
            if (woodtop1 == true) {
                g2.drawImage(wood, at, null);
            }
            g.drawImage(bigbomb, (int) xlaunch, (int) ylaunch, null);
            
            //Following code checks whether the projectile hit any of the objects, and if it did
            //draws out an epxlosion and adds to the score and removes the projectile
            if ((int) xlaunch > 885 && (int) xlaunch < 885+22 && (int) ylaunch > 490 && (int) ylaunch < 490+55 && dead == false && woodtopright == true) {
                woodtopright = false;
                xhealth = xhealth - 20;
                score = score + 50;
                yspd = -yspd / 2;
                xspd = xspd / 2;
                xbounce = 1;
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                
            }            
                        
            if ((int) xlaunch > 820 && (int) xlaunch < 900 && (int) ylaunch > 415 && (int) ylaunch < 440 && dead == false && woodtop1 == true) {
                woodtop1 = false;
                xhealth = xhealth - 20;
                score = score + 50;
                yspd = -yspd / 2;
                xspd = xspd / 2;
                xbounce = 1;
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                
            }
            
            //Checks if you hit the bad guy and if you did then end the level and ask a Question
            if ((int) xlaunch > badx - 10 && (int) xlaunch < badx + 50 && (int) ylaunch > bady - 5 && (int) ylaunch + 10 < bady + 60 && dead == false && woodtop == false) {
                g.drawImage(fire, badx, bady, null);
                dead = true;
                endgame = true;
                xhealth -= 150;
                nextlevel = true;
                score = score + 100;
                Question();
            }

            if ((int) xlaunch > 827 && (int) xlaunch < 920 && (int) ylaunch > 465 && (int) ylaunch < 497 && dead == false && woodtop == true) {
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                woodtop = false;
                xhealth = xhealth - 20;
                yspd = -yspd / 2;
                xspd = xspd / 2;
                xbounce = 1;
                score = score + 50;
                
            }

            if ((int) xlaunch > 800 && (int) xlaunch < 844 && (int) ylaunch > 495 && (int) ylaunch < 495 + 60 && dead == false && woodtopleft == true) {
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                woodtopleft = false;
                xhealth = xhealth - 20;
                yspd = -yspd / 3;
                xspd = -xspd / 3;
                xbounce = 1;
                score = score + 50;
                
            }

            //Wood bottom left
            if ((int) xlaunch > 735 && (int) xlaunch < 745 + 50 && (int) ylaunch > 495 && (int) ylaunch < 495 + 50 && dead == false && woodbottomleft == true) {
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                woodbottomleft = false;
                xhealth = xhealth - 20;
                yspd = -yspd / 2;
                xspd = -xspd / 2;
                xbounce = 1;
                score = score + 50;
                
            }

            //Wood top bottom 770, 445
            if ((int) xlaunch > 765 && (int) xlaunch < 765 + 50 && (int) ylaunch > 445 && (int) ylaunch < 445 + 50 && dead == false && woodtopbottom == true) {
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                woodtopbottom = false;
                xhealth = xhealth - 20;
                yspd = -yspd / 2;
                xspd = -xspd / 2;
                xbounce = 1;
                score = score + 50;
                
            }
            
            //Resets the character if it falls too far down after the bounce
            if (ylaunch > 600) {
                xlaunch = 70;
                ylaunch = 475;
                tool.stop();
                xbounce = 0;
            }

        }//end paintComponent
        
        //Question method which asks the user a question for the educational
        //Aspect of the game
        public void Question() {
            String response;
            int x = (int)(Math.random() * 4);
            x = 1;
            System.out.println("\nQuestion Options:");
            for (int a = 0; a < 5; a++) {
                System.out.println("Option " + (a + 1) + ": " + questions[7 + (6 * x) + a]);
            }
            String temporary = questions[x];
            response = JOptionPane.showInputDialog(null,
            temporary);
                     
            if (response.equals(questions[6 + (6 * (x + 1))])) {
                JOptionPane.showMessageDialog(null,
                        "Correct!");
                score = score + 50;
                
            } else {
                JOptionPane.showMessageDialog(null,
                        "Incorrect!");
                score = score - 50;
            }

        }

        public void keyTyped(KeyEvent e) {
        }//end KeyTyped			

        public void keyReleased(KeyEvent e) {
        }//end KeyReleased

        //Action performed for checking if any of the buttons where pressed
        public void actionPerformed(ActionEvent evt) {
            String command = evt.getActionCommand();
            if (command.equals(":)")) {
                System.exit(0);//Closes the game if pressed
            } else if (command.equals("Change Character")) {
                player = player + 1;//changes the character and mass if pressed
                if (player == 1) {
                    mass = 20;
                }
                if (player == 2) {
                    mass = 12;
                }
                if (player == 3) {
                    mass = 6;
                }
                if (player == 4) {//if goes too high then resets back to player one
                    player = 1;
                }
                repaint();//repaints the level
            } else if (command.equals("Enter Cheats")) {//Prompts user to enter the cheats
                String response = JOptionPane.showInputDialog(null,
                        "Enter your cheats. NO CAPS!\n");
                if (response.equals("manny")) {
                    JOptionPane.showMessageDialog(null,
                            "Initiating Self Destruct in 3..2...1...boom");
                    System.exit(0);
                } else if (response.equals("iamcool")) {
                    JOptionPane.showMessageDialog(null,
                            "Score has been set to 100000!");//sets the score to 1000000
                    score = 100000;
                } else {
                     JOptionPane.showMessageDialog(null,
                            "Not a valid cheat :(");//sets the score to 1000000
                }
                repaint();
            } else if (command.equals("Restart Level")) {//Reinitializes the level if pressed
                //same values as the initial constructor
                boomer = true;
                endgame = false;
                infiniteoxygen = false;
                xhealth = 150;
                score = 0;
                dead = false;
                woodtop1 = true;
                woodtopleft = true;
                woodtopright = true;
                woodbottomleft = true;
                xhero = 50;
                dead = false;
                woodtopbottom = true;
                woodtop = true;
                nextlevel = false;
                boom.start();//starts the timer
                repaint();
                
                
            } else if (command.equals("Next")) {
                if (nextlevel == true) {
                    //Resets the values if nextlevel is true and bad guy is defeated
                    xlaunch = 70;
                    ylaunch = 475;
                    nextlevel = false;
                    boomer = true;
                    endgame = false;
                    infiniteoxygen = false;
                    xhealth = 150;
                    lvl1 = score;
                    dead = false;
                    woodtop1 = true;
                    woodtopleft = true;
                    woodbottomleft = true;
                    xhero = 50;
                    boom.stop();
                    endgame = false;
                    woodtopbottom = true;
                    woodtop = true;
                    detonate = false;
                    detonate = false; 
                    repaint();
                    forum.next(containMe);
                    tool.stop();
                    
                                    endgame = false;
					infiniteoxygen = false;
					dead = false;
					woodtop1 = true;
					woodtopleft = true;
					woodbottomleft = true;
					xhero = 50;
					xhealth = 150;
					endgame = false;
					dead = false;
					woodtopbottom = true;
					woodtop = true;
					boom.start();//starts the timer
					boomer = true;
					endgame = false;
					infiniteoxygen = false;
					dead = false;
					woodtop1 = true;
					woodtopleft = true;
					woodtopright = true;
					woodbottomleft = true;
					xhero = 50;
					dead = false;
					woodtopbottom = true;
					woodtop = true;
					nextlevel = false;
                boom.start();//starts the timer
                } else {//else tells the user to finish
                    JOptionPane.showMessageDialog(null,
                            "Please Finish the Current Level");
                    nextlevel = false;
                }
            } else if (command.equals("Game Stats")) {
                //Prints out the score you currently have
                JOptionPane.showMessageDialog(null,
                        "Your Score is: " + score);
            }
            //Requests focus to make sure it isnt lost
            requestFocus();
        }//end actionListener
    }//end OSPPanel3
    
    //Panel for level 2 of the game
    class OPSPanel4 extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
        Image brick;
        RepaintAction action = new RepaintAction();//creates a new Repaint Action for the timer
        Timer boom = new Timer(500, action);//new timer with 500ms for mouth and moving the monsters
        CoolAction health = new CoolAction();
        Timer tool = new Timer(1, health);//new timer with 500ms for mouth and moving the monsters
        FlyAction superman = new FlyAction();//creates a new Repaint Action for the timer
        Timer fly = new Timer(5, superman);//new timer with 500ms for mouth and moving the monsters
        public int [] ycor;
        public int [] xcor;
        public int [] ycor1;
        
        public OPSPanel4() {
            reverse = false;
            woodtop = true;
            woodtop1 = true;
            xhero = 50;
            xbounce = 0;
            dead = false;
            badx = 850;
            woodbottomleft = true;
            bady = 500;
            g = 2;
            detonate = false;
            gogo = true;
            xlaunch = 70;
            ylaunch = 475;
            endgame = false;
            infiniteoxygen = false;
            xhealth = 150;
            woodtopright = true;
            initx = x1 + 55;
            inity = y1 + 60;
            pausegame = 0;
            requestFocus();
            addKeyListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
            y5 = 60;
            x5 = 10;
            woodtopbottom = true;
            JButton next = new JButton("Next");
            next.setSize(80, 30);
            next.setLocation(300, 12);
            next.addActionListener(this);
            next.setBackground(Color.cyan);
            next.setForeground(Color.black);
            this.add(next);
            JButton regenerate = new JButton("Restart Level");
            regenerate.setSize(120, 30);
            regenerate.setLocation(390, 12);
            regenerate.addActionListener(this);
            regenerate.setBackground(Color.orange);
            regenerate.setForeground(Color.black);
            this.add(regenerate);
            JButton stats = new JButton("Game Stats");
            stats.setSize(120, 30);
            stats.setLocation(520, 12);
            stats.addActionListener(this);
            stats.setBackground(Color.green);
            stats.setForeground(Color.black);
            this.add(stats);
            JButton cheat = new JButton("Enter Cheats");
            cheat.setSize(120, 30);
            cheat.setLocation(650, 12);
            cheat.addActionListener(this);
            cheat.setBackground(Color.LIGHT_GRAY);
            cheat.setForeground(Color.black);
            this.add(cheat);
            JButton changer = new JButton("Change Character");
            changer.setSize(150, 30);
            changer.setLocation(780, 12);
            changer.addActionListener(this);
            woodtopleft = true;
            changer.setBackground(Color.yellow);
            changer.setForeground(Color.black);
            this.add(changer);
            JButton exit = new JButton(":)");
            exit.setSize(45, 30);
            ycor = new int [7];
            xcor = new int [2];
            ycor1 = new int [7];
            ycor[0] = 90;
            ycor[1] = 160;
            ycor[2] = 230;
            ycor[3] = 300;
            ycor[4] = 370;
            ycor[5] = 440;
            ycor[6] = 510;
            ycor1[0] = 90;
            ycor1[1] = 160;
            ycor1[2] = 230;
            ycor1[3] = 300;
            ycor1[4] = 370;
            ycor1[5] = 440;
            ycor1[6] = 510;
            xcor[0] = 520;
            xcor[1] = 650;
            exit.setLocation(940, 12);
            exit.addActionListener(this);
            exit.setBackground(Color.red);
            exit.setForeground(Color.black);
            this.add(exit);
            nextlevel = false;
        }

        //Mouse Methods
        public void mouseClicked(MouseEvent e) {
            if (endgame == false) {
                Vector();
            }
            repaint();
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            requestFocus();

            // if (SwingUtilities.isRightMouseButton(e) && endgame == false) {
            if (e.getX() - 15 < 600 && e.getX() - 15 > 50) {
                xbomb = e.getX();  // x-coordinate where user clicked.
            }
            if (e.getY() - 15 > 60 && e.getY() - 15 < 520) {
                ybomb = e.getY();  // y-coordinate where user clicked.
            }
            repaint();
            //}
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }

        //Repaint Method
        private class RepaintAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                xbomb = 400;
                xlaunch = 70;
                ylaunch = 475;
                ybomb = 400;
                boomer = false;
                boom.stop();
                tool.stop();
                repaint();
            }//end timermethod
        }//end RepaintAction

        private class CoolAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {

                detonate = true;
                xlaunch += xspd;
                ylaunch += yspd;
                yspd += g;

                if (xbounce == 0) {
                    if ((int) ylaunch > 520) {
                        ylaunch = 500;
                        yspd = -yspd / 2;
                        detonate = false;
                        xbounce = 1;
                    }
                } else if (xbounce == 1) {
                    detonate = true;
                }
                repaint();
            }
        }//end RepaintAction

        private class FlyAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                if (gogo = true) {
                    if (xhero < 70) {
                        reverse = false;
                    }
                    if (xhero > 400) {
                        reverse = true;
                    }
                    if (reverse == false) {
                        xhero += 1;
                    }
                    if (reverse == true) {
                        xhero -= 1;
                    }
                    repaint();
                }//end timermethod
                
                if (ycor[0] < 91) 
                    rev = false;
                
                if (ycor[0] > 180)
                    rev = true;
                
                
                if (rev == false) {
                    for (int a =0; a < 6; a++) {
                        ycor[a] +=2;
                        ycor1 [a] -=2;
                    }
                }
                
                if (rev == true) {
                    for (int a =0; a < 6; a++) {
                        ycor[a] -=2;
                        ycor1[a] +=2;
                    }                   
                }
            }//end RepaintAction
        }

        public void keyPressed(KeyEvent e) {
            c = e.getKeyChar();
            if (c == 'f' && endgame == false) {
                Vector();
            }
            repaint();

        }//end keyPressed

        public void Vector() {
            detonate = true;
            tool.stop();
            //resets the location of the person
            xlaunch = 70;
            ylaunch = 475;
            //sets the target point to last shot before clicking
            xinit = xbomb;
            yinit = ybomb;
            //super variables are used to calculate the power of the launch and angle
            double superx = xinit - xlaunch;
            double supery = ylaunch - yinit;
            superdist = Math.sqrt((superx * superx) + (supery * supery));
            double theta;
            //Sets the mass to equal the distance over 20
            mass = (int) superdist / 20;
            //Makes suer that the variable is a number
            do {
                theta = Math.atan(((mass * mass) + Math.sqrt((Math.pow(mass, 4)) - (g * (g * (superx * superx) + (2 * supery * mass * mass))))) / (g * superx));
                mass = mass + 1;
            } while (Double.isNaN(theta));

            gogo = true;
            //launches the projectile if within the parameters
            if (xlaunch < xinit && ylaunch > yinit) {
                tool.start();
            }
            
            //sets the speed to vector direction of the angle
            xspd = Math.cos(theta) * mass;
            yspd = -Math.sin(theta) * mass;
        }

        public void paintComponent(Graphics g) {
            fly.start();
            requestFocus();
            //Graphics Rendering for rotating the images
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(rh);
            //The following lines until the * represent the initial drawings of the setting
            if (xhealth == 0) {
                endgame = true;
            }
            //Try catch to import all images required
            try {
                //If loop to open appropriate image based on charachter selected in option screen
                if (player == 1) {
                    person = ImageIO.read(new File("play1.png"));
                    bigbomb = ImageIO.read(new File("launch1.png"));
                    mass = 20;
                }
                if (player == 2) {
                    person = ImageIO.read(new File("play2.png"));
                    bigbomb = ImageIO.read(new File("launch2.png"));
                    mass = 12;
                }
                if (player == 3) {
                    person = ImageIO.read(new File("play3.png"));
                    bigbomb = ImageIO.read(new File("launch3.png"));
                    mass = 6;
                }
                ImageIcon ii = new ImageIcon(this.getClass().getResource("wood.png"));
                wood = ii.getImage();
                brick = ImageIO.read(new File("brick.png"));
                sun = ImageIO.read(new File("sun.png"));
                superhero = ImageIO.read(new File("superhero.png"));
                plane = ImageIO.read(new File("plane.png"));
                cloud = ImageIO.read(new File("cloud.png"));
                badguy = ImageIO.read(new File("badguy.png"));
                cannon = ImageIO.read(new File("cannon.png"));
                fire = ImageIO.read(new File("fire.png"));
                wall = ImageIO.read(new File("gameback.png"));
                border = ImageIO.read(new File("border.png"));
                borderflat = ImageIO.read(new File("borderflat.png"));
                sand = ImageIO.read(new File("sand.jpg"));
                grass = ImageIO.read(new File("grass.png"));
                launch = ImageIO.read(new File("target.png"));
                warn = ImageIO.read(new File("warn.png"));
            } catch (IOException e) {
            }
            //Font for script
            Font myFont2 = new Font("Times New Roman", Font.BOLD, 50);
            super.paintComponent(g);
            //Border Drawing Layout
            int grassx = 30;
            g.drawImage(wall, 0, 0, null);
            //Draws out the grass
            for (int a = 0; a < 17; a++) {
                g.drawImage(grass, grassx, 535, null);
                grassx += 51;
            }
            for (int a = 0; a < 14; a++) {
                g.drawImage(border, 10, y5, null);
                g.drawImage(border, 950, y5, null);
                y5 = y5 + 39;
            }
            //draws out the border horizontally
            for (int a = 0; a < 32; a++) {
                g.drawImage(borderflat, x5, 580, null);
                x5 = x5 + 30;
            }
            y5 = 60;
            x5 = 10;
            //sets the location of the health bar
            
            //Level Title
            g.setColor(Color.white);
            g.setFont(new Font("Calibri", Font.BOLD, 30));
            g.drawString("Level 2", 30, 37);
            g.setColor(Color.green);
            //g.drawString("Options:", 150, 37);
            g.setColor(Color.red);
            g.fillRect(130, 18, 150, 20);
            g.setColor(Color.green);
            g.fillRect(130, 18, xhealth, 20);
            g.setColor(Color.black);
            g.drawRect(130, 18, 150, 20);
            g.setColor(Color.white);
            g.drawRect(130, 18, xhealth, 20);
            g.setColor(Color.gray);
            g.fillRect(10, 55, 970, 5);
            g.drawImage(cannon, 40, 470, null);
            g.drawImage(cloud, 105, 75, null);
            g.drawImage(cloud, 440, 75, null);
            if (diff == 2)
            g.drawImage(superhero, xhero, 90, null);
            //Draw the shooter and target
            g.setColor(Color.red);
            g.drawLine(80, 490, xbomb, ybomb + 3);
            g.drawImage(launch, xbomb - 15, ybomb - 15, null);
            // ******
            //Clouds and etc for eycandy
            g.drawImage(sun, 810, 70, null);
            //draws out the fire if impacts
            if (boomer == true) {
                g.drawImage(fire, xbomb - 12, ybomb - 10, null);
            }
            
            //Checks whether you hit your teamate and subracts 10 points if you did
            if ((int) xlaunch > xhero && (int) xlaunch < xhero + 50 && (int) ylaunch > 90 && (int) ylaunch < 140 && diff == 2) {
                g.setColor(Color.RED);
                score = score - 10;
                g.drawString("Ouch!", xhero + 50, 95);
            }
            
            //Draws the warning if projectile goes too high
            g.setColor(Color.cyan);
            if (ylaunch < 0) {
                g.drawImage(warn, (int) xlaunch, 65, null);
            }
            //If Game ends then draw out this and say you win
            if (endgame == true) {
                g.drawImage(fire, xbomb - 15, ybomb - 15, null);
                g.setColor(Color.red);
                g.setFont(new Font("calibri", Font.BOLD, 100));
                g.drawString("YOU WIN!", 280, 320);
                g.setColor(Color.blue);
                g.setFont(new Font("calibri", Font.BOLD, 50));
                g.drawString("Press Next to Proceed!", 250, 380);
                fly.stop();//stops the superman from moving
            }
            if (detonate = false) {
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
            }

            // Begin level 2 drawing
            bady = 310;
            badx = 720;
            if (dead == false) {
                g.drawImage(badguy, badx, bady, null);
            }
            AffineTransform at = new AffineTransform();
          
            for (int a = 0; a < 6; a++) {  
                at.setToTranslation(xcor[0], ycor[a]);  
                at.rotate(Math.toRadians(90), 15, 15);
                if (a == 0) {
                    g2.drawImage(brick, at, null);
                } else {
                    g2.drawImage(wood, at, null);
                }
            }
            
            for (int a = 0; a < 6; a++) {  
                at.setToTranslation(xcor[1], ycor1[a] + 50);  
                at.rotate(Math.toRadians(90), 15, 15);
                if (a == 0) {
                    g2.drawImage(brick, at, null);
                } else {
                    g2.drawImage(wood, at, null);
                }
            }
                        
            g.drawImage(bigbomb, (int) xlaunch, (int) ylaunch, null);           

            //Check Destuction
            if ((int) xlaunch > 520 && (int) xlaunch < 545 && (int) ylaunch > ycor[0] - 15 && (int) ylaunch < ycor[0] + 55
                        && dead == false) {
                  yspd = yspd / 2;
                  xspd = -xspd;
                  g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);                  
            }
            
            if ((int) xlaunch > 650 && (int) xlaunch < 675 && (int) ylaunch > ycor1[0] - 15 && (int) ylaunch < ycor1[0] + 55
                        && dead == false) {
                  yspd = yspd / 2;
                  xspd = -xspd / 2;
                  g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                 
            }            
                
            for (int a = 1; a < 6; a++) { 
                if ((int) xlaunch > 520 && (int) xlaunch < 545 && (int) ylaunch > ycor[a] - 15 && (int) ylaunch < ycor[a] + 55
                        && dead == false) {
                    score = score + 50;
                    ycor[a] = 1000;
                    yspd = yspd / 2;
                    xspd = -xspd / 2;
                    xhealth -= 15;
                    g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                   
                }
                
                if ((int) xlaunch > 650 && (int) xlaunch < 675 && (int) ylaunch > ycor1[a] - 15 && (int) ylaunch < ycor1[a] + 55
                        && dead == false) {
                    score = score + 50;
                    ycor1[a] = 1000;
                    yspd = yspd / 2;
                    xspd = -xspd / 2;
                    xhealth -= 15;
                    g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
                    
                }
            }
            
            if (ylaunch > 600) {
                xlaunch = 70;
                ylaunch = 475;
                tool.stop();
                xbounce = 0;
                repaint();
            }
            
            if ((int) xlaunch > badx - 10  && (int) xlaunch < badx + 50 && (int) ylaunch > bady - 5 && (int) ylaunch < bady + 50
                        && dead == false) {
                  tool.stop();
                  endgame = true;
                  dead = true;
                  score = score + 100;
                  xhealth -= 150;
                  xlaunch = 40;
                  ylaunch = 475;
                  nextlevel = true;
                  Question();
                  g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
            }
            
            if (endgame == true) {
                g.drawImage(fire, xbomb - 15, ybomb - 15, null);
                g.setColor(Color.red);
                g.setFont(new Font("Calibri", Font.BOLD, 100));
                g.drawString("YOU WIN!", 280, 320);
                g.setColor(Color.blue);
                g.setFont(new Font("calibri", Font.BOLD, 50));
                g.drawString("Press Next to Proceed!", 250, 380);
                xhealth -= 150;
                fly.stop();
            }
        }//end paintComponent

        public void Question() {
            String response;
            int x = (int)(Math.random() * 4);
            x = 2;
            System.out.println("\nQuestion Options:");
            for (int a = 0; a < 5; a++) {
                System.out.println("Option " + (a + 1) + ": " + questions[7 + (6 * x) + a]);
            }
            String temporary = questions[x];
            response = JOptionPane.showInputDialog(null,
            temporary);
                     
            if (response.equals(questions[6 + (6 * (x + 1))])) {
                JOptionPane.showMessageDialog(null,
                        "Correct!");
                score = score + 50;
                
            } else {
                JOptionPane.showMessageDialog(null,
                        "Incorrect!");
                score = score - 50;
            }

        }

        public void keyTyped(KeyEvent e) {
        }//end KeyTyped			

        public void keyReleased(KeyEvent e) {
        }

        public void actionPerformed(ActionEvent evt) {
            String command = evt.getActionCommand();
            if (command.equals(":)")) {
                System.exit(0);
            } else if (command.equals("Change Character")) {
                player = player + 1;
                if (player == 1) {
                    mass = 20;
                }
                if (player == 2) {
                    mass = 12;
                }
                if (player == 3) {
                    mass = 6;
                }
                if (player == 4) {
                    player = 1;
                }
                repaint();
            } else if (command.equals("Enter Cheats")) {
                String response = JOptionPane.showInputDialog(null,
                        "Enter your cheats. NO CAPS!\n");
                if (response.equals("manny")) {
                    JOptionPane.showMessageDialog(null,
                            "Initiating Self Destruct in 3..2...1...boom");
                    System.exit(0);
                } else if (response.equals("iamcool")) {
                    JOptionPane.showMessageDialog(null,
                            "Score has been set to 100000!");
                    score = 100000;
                }
                repaint();
            } else if (command.equals("Restart Level")) {
                boomer = true;
                ycor[0] = 90;
                ycor[1] = 160;
                ycor[2] = 230;
                ycor[3] = 300;
                ycor[4] = 370;
                ycor[5] = 440;
                ycor[6] = 510;
                ycor1[0] = 90;
                ycor1[1] = 160;
                ycor1[2] = 230;
                ycor1[3] = 300;
                ycor1[4] = 370;
                ycor1[5] = 440;
                ycor1[6] = 510;
                xcor[0] = 520;
                xcor[1] = 650;
                endgame = false;
                infiniteoxygen = false;
                xhealth = 150;
                score = lvl1;
                dead = false;
                woodtop1 = true;
                woodtopleft = true;
                woodbottomleft = true;
                xhero = 50;
                xhealth = 150;
                endgame = false;
                dead = false;
                woodtopbottom = true;
                woodtop = true;
                boom.start();//starts the timer
                boomer = true;
                endgame = false;
                infiniteoxygen = false;
                xhealth = 150;
                score = 0;
                dead = false;
                woodtop1 = true;
                woodtopleft = true;
                woodtopright = true;
                woodbottomleft = true;
                xhero = 50;
                dead = false;
                woodtopbottom = true;
                woodtop = true;
                nextlevel = false;
                boom.start();//starts the timer
                repaint();
                repaint();
            } else if (command.equals("Next")) {
                if (nextlevel == true) {
                    nextlevel = false;
                    boomer = true;
                    xlaunch = 70;
                    ylaunch = 475;
                    endgame = false;
                    xhealth = 150;
                    dead = false;
                    woodtop1 = true;
                    woodtopleft = true;
                    detonate = false;
                    lvl2 = score;
                    woodbottomleft = true;
                    xhero = 50;
                    boom.stop();
                    endgame = false;
                    woodtopbottom = true;
                    woodtop = true;
                    repaint();
                    
                    
                                    endgame = false;
					infiniteoxygen = false;
					xhealth = 150;
					
					dead = false;
					woodtop1 = true;
					woodtopleft = true;
					woodbottomleft = true;
					xhero = 50;
					xhealth = 150;
					endgame = false;
					dead = false;
					woodtopbottom = true;
					woodtop = true;
					boom.start();//starts the timer
					boomer = true;
					endgame = false;
					infiniteoxygen = false;
					xhealth = 150;
					score = 0;
					dead = false;
					woodtop1 = true;
					woodtopleft = true;
					woodtopright = true;
					woodbottomleft = true;
					xhero = 50;
					dead = false;
					woodtopbottom = true;
					woodtop = true;
					nextlevel = false;
					boom.start();//starts the timer
                    forum.next(containMe);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please Finish the Current Level");
                    nextlevel = false;
                }
            } else if (command.equals("Game Stats")) {
                JOptionPane.showMessageDialog(null,
                        "Your Score is: " + score);
            }

            requestFocus();
        }
    }//end OSPPanel4
    
    class OPSPanel5 extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
        Image brick;
        OPSPanel5.RepaintAction action = new OPSPanel5.RepaintAction();//creates a new Repaint Action for the timer
        Timer boom = new Timer(500, action);//new timer with 500ms for mouth and moving the monsters
        OPSPanel5.CoolAction health = new OPSPanel5.CoolAction();
        Timer tool = new Timer(1, health);//new timer with 500ms for mouth and moving the monsters
        OPSPanel5.FlyAction superman = new OPSPanel5.FlyAction();//creates a new Repaint Action for the timer
        Timer fly = new Timer(5, superman);//new timer with 500ms for mouth and moving the monsters

        OPSPanel5.HideAction secret = new OPSPanel5.HideAction();//creates a new Repaint Action for the timer
        Timer hide = new Timer(500, secret);//new timer with 500ms for mouth and moving the monsters
        
        public int [] ycor;
        public int [] xcor;
        public int [] ycor1;
        public OPSPanel5() {
            reverse = false;
            woodtop = true;
            woodtop1 = true;
            xhero = 50;
            xbounce = 0;
            dead = false;
            badx = 850;
            woodbottomleft = true;
            bady = 500;
            g = 2;
            detonate = false;
            gogo = true;
            xlaunch = 70;
            ylaunch = 475;
            endgame = false;
            infiniteoxygen = false;
            xhealth = 150;
            woodtopright = true;
            initx = x1 + 55;
            inity = y1 + 60;
            pausegame = 0;
            
            requestFocus();
            addKeyListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
            y5 = 60;
            x5 = 10;
            woodtopbottom = true;
            JButton next = new JButton("Next");
            next.setSize(80, 30);
            next.setLocation(300, 12);
            next.addActionListener(this);
            next.setBackground(Color.cyan);
            next.setForeground(Color.black);
            this.add(next);
            JButton regenerate = new JButton("Restart Level");
            regenerate.setSize(120, 30);
            regenerate.setLocation(390, 12);
            regenerate.addActionListener(this);
            regenerate.setBackground(Color.orange);
            regenerate.setForeground(Color.black);
            this.add(regenerate);
            JButton stats = new JButton("Game Stats");
            stats.setSize(120, 30);
            stats.setLocation(520, 12);
            stats.addActionListener(this);
            stats.setBackground(Color.green);
            stats.setForeground(Color.black);
            this.add(stats);
            JButton cheat = new JButton("Enter Cheats");
            cheat.setSize(120, 30);
            cheat.setLocation(650, 12);
            cheat.addActionListener(this);
            cheat.setBackground(Color.LIGHT_GRAY);
            cheat.setForeground(Color.black);
            this.add(cheat);
            JButton changer = new JButton("Change Character");
            changer.setSize(150, 30);
            changer.setLocation(780, 12);
            changer.addActionListener(this);
            woodtopleft = true;
            changer.setBackground(Color.yellow);
            changer.setForeground(Color.black);
            this.add(changer);
            JButton exit = new JButton(":)");
            exit.setSize(45, 30);
            ycor = new int [7];
            xcor = new int [2];
            ycor1 = new int [7];
            ycor[0] = 90;
            ycor[1] = 160;
            ycor[2] = 230;
            ycor[3] = 300;
            ycor[4] = 370;
            ycor[5] = 440;
            ycor[6] = 510;
            ycor1[0] = 90;
            ycor1[1] = 160;
            ycor1[2] = 230;
            ycor1[3] = 300;
            ycor1[4] = 370;
            ycor1[5] = 440;
            ycor1[6] = 510;
            xcor[0] = 520;
            xcor[1] = 650;
            exit.setLocation(940, 12);
            score = lvl2;
            exit.addActionListener(this);
            exit.setBackground(Color.red);
            exit.setForeground(Color.black);
            this.add(exit);
            nextlevel = false;
        }

        //Mouse Methods
        public void mouseClicked(MouseEvent e) {
            if (endgame == false) {
                Vector();
            }
            repaint();
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            requestFocus();

            // if (SwingUtilities.isRightMouseButton(e) && endgame == false) {
            if (e.getX() - 15 < 600 && e.getX() - 15 > 50) {
                xbomb = e.getX();  // x-coordinate where user clicked.
            }
            if (e.getY() - 15 > 60 && e.getY() - 15 < 520) {
                ybomb = e.getY();  // y-coordinate where user clicked.
            }
            repaint();
            //}
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }

        //Repaint Method
        private class RepaintAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                xbomb = 400;
                xlaunch = 70;
                ylaunch = 475;
                ybomb = 400;
                boomer = false;
                boom.stop();
                tool.stop();
                repaint();
            }//end timermethod
        }//end RepaintAction

        private class CoolAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {

                detonate = true;
                xlaunch += xspd;
                ylaunch += yspd;
                yspd += g;

                if (xbounce == 0) {
                    if ((int) ylaunch > 520) {
                        ylaunch = 500;
                        yspd = -yspd / 2;
                        detonate = false;
                        xbounce = 1;
                    }
                } else if (xbounce == 1) {
                    detonate = true;
                }
                repaint();
            }
        }//end RepaintAction
        
         private class HideAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                do {
                    badx = (int)(Math.random() * 800);
                    bady = (int)(Math.random() * 500);
                } while(badx < 300 || badx > 800 || bady > 500 || bady < 150);
                repaint();
            }
        }//end RepaintAction

        private class FlyAction implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                if (gogo = true) {
                    if (xhero < 70) {
                        reverse = false;
                    }
                    if (xhero > 400) {
                        reverse = true;
                    }
                    if (reverse == false) {
                        xhero += 1;
                    }
                    if (reverse == true) {
                        xhero -= 1;
                    }
                    repaint();
                }//end timermethod
                

            }//end RepaintAction
        }

        public void keyPressed(KeyEvent e) {
            c = e.getKeyChar();
            if (c == 'f' && endgame == false) {
                Vector();
            }
            repaint();

        }//end keyPressed

        public void Vector() {
            detonate = true;
            tool.stop();
            //resets the location of the person
            xlaunch = 70;
            ylaunch = 475;
            //sets the target point to last shot before clicking
            xinit = xbomb;
            yinit = ybomb;
            //super variables are used to calculate the power of the launch and angle
            double superx = xinit - xlaunch;
            double supery = ylaunch - yinit;
            superdist = Math.sqrt((superx * superx) + (supery * supery));
            double theta;
            //Sets the mass to equal the distance over 20
            mass = (int) superdist / 20;
            //Makes suer that the variable is a number
            do {
                theta = Math.atan(((mass * mass) + Math.sqrt((Math.pow(mass, 4)) - (g * (g * (superx * superx) + (2 * supery * mass * mass))))) / (g * superx));
                mass = mass + 1;
            } while (Double.isNaN(theta));

            gogo = true;
            //launches the projectile if within the parameters
            if (xlaunch < xinit && ylaunch > yinit) {
                tool.start();
            }
            
            //sets the speed to vector direction of the angle
            xspd = Math.cos(theta) * mass;
            yspd = -Math.sin(theta) * mass;
        }
        public void paintComponent(Graphics g) {
            fly.start();
            hide.start();
            requestFocus();
            //Graphics Rendering for rotating the images
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(rh);
            //The following lines until the * represent the initial drawings of the setting
            if (xhealth == 0) {
                endgame = true;
            }
            //Try catch to import all images required
            try {
                //If loop to open appropriate image based on charachter selected in option screen
                if (player == 1) {
                    person = ImageIO.read(new File("play1.png"));
                    bigbomb = ImageIO.read(new File("launch1.png"));
                    mass = 20;
                }
                if (player == 2) {
                    person = ImageIO.read(new File("play2.png"));
                    bigbomb = ImageIO.read(new File("launch2.png"));
                    mass = 12;
                }
                if (player == 3) {
                    person = ImageIO.read(new File("play3.png"));
                    bigbomb = ImageIO.read(new File("launch3.png"));
                    mass = 6;
                }
                ImageIcon ii = new ImageIcon(this.getClass().getResource("wood.png"));
                wood = ii.getImage();
                sun = ImageIO.read(new File("sun.png"));
                superhero = ImageIO.read(new File("superhero.png"));
                plane = ImageIO.read(new File("plane.png"));
                cloud = ImageIO.read(new File("cloud.png"));
                badguy = ImageIO.read(new File("badguy.png"));
                cannon = ImageIO.read(new File("cannon.png"));
                fire = ImageIO.read(new File("fire.png"));
                wall = ImageIO.read(new File("gameback.png"));
                border = ImageIO.read(new File("border.png"));
                borderflat = ImageIO.read(new File("borderflat.png"));
                sand = ImageIO.read(new File("sand.jpg"));
                grass = ImageIO.read(new File("grass.png"));
                launch = ImageIO.read(new File("target.png"));
                warn = ImageIO.read(new File("warn.png"));
            } catch (IOException e) {
            }
            //Font for script
            Font myFont2 = new Font("Times New Roman", Font.BOLD, 50);
            super.paintComponent(g);
            //Border Drawing Layout
            int grassx = 30;
            g.drawImage(wall, 0, 0, null);
            //Draws out the grass
            for (int a = 0; a < 17; a++) {
                g.drawImage(grass, grassx, 535, null);
                grassx += 51;
            }
            for (int a = 0; a < 14; a++) {
                g.drawImage(border, 10, y5, null);
                g.drawImage(border, 950, y5, null);
                y5 = y5 + 39;
            }
            //draws out the border horizontally
            for (int a = 0; a < 32; a++) {
                g.drawImage(borderflat, x5, 580, null);
                x5 = x5 + 30;
            }
            y5 = 60;
            x5 = 10;
            //sets the location of the health bar
            
            //Level Title
            g.setColor(Color.white);
            g.setFont(new Font("Calibri", Font.BOLD, 30));
            g.drawString("Level 2", 30, 37);
            g.setColor(Color.green);
            //g.drawString("Options:", 150, 37);
            g.setColor(Color.red);
            g.fillRect(130, 18, 150, 20);
            g.setColor(Color.green);
            g.fillRect(130, 18, xhealth, 20);
            g.setColor(Color.black);
            g.drawRect(130, 18, 150, 20);
            g.setColor(Color.white);
            g.drawRect(130, 18, xhealth, 20);
            g.setColor(Color.gray);
            g.fillRect(10, 55, 970, 5);
            g.drawImage(cannon, 40, 470, null);
            g.drawImage(cloud, 105, 75, null);
            g.drawImage(cloud, 440, 75, null);
            if (diff == 2)
            g.drawImage(superhero, xhero, 90, null);
            //Draw the shooter and target
            g.setColor(Color.red);
            g.drawLine(80, 490, xbomb, ybomb + 3);
            g.drawImage(launch, xbomb - 15, ybomb - 15, null);
            // ******
            //Clouds and etc for eycandy
            g.drawImage(sun, 810, 70, null);
            //draws out the fire if impacts
            if (boomer == true) {
                g.drawImage(fire, xbomb - 12, ybomb - 10, null);
            }
            
            //Checks whether you hit your teamate and subracts 10 points if you did
            if ((int) xlaunch > xhero && (int) xlaunch < xhero + 50 && (int) ylaunch > 90 && (int) ylaunch < 140 && diff == 2) {
                g.setColor(Color.RED);
                score = score - 10;
                g.drawString("Ouch!", xhero + 50, 95);
            }
            
            //Draws the warning if projectile goes too high
            g.setColor(Color.cyan);
            if (ylaunch < 0) {
                g.drawImage(warn, (int) xlaunch, 65, null);
            }
            //If Game ends then draw out this and say you win
            if (endgame == true) {
                g.drawImage(fire, xbomb - 15, ybomb - 15, null);
                g.setColor(Color.red);
                g.setFont(new Font("calibri", Font.BOLD, 100));
                g.drawString("YOU WIN!", 280, 320);
                g.setColor(Color.blue);
                g.setFont(new Font("calibri", Font.BOLD, 50));
                g.drawString("Press Next to Proceed!", 250, 380);
                fly.stop();//stops the superman from moving
            }
            if (detonate = false) {
                g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
            }
            // Begin level 2 drawing
             if (dead == false) {
                g.drawImage(badguy, badx, bady, null);
            }
                       
            g.drawImage(bigbomb, (int) xlaunch, (int) ylaunch, null);           

            //Check Destuction
                        
            if ((int) xlaunch > badx - 10  && (int) xlaunch < badx + 50 && (int) ylaunch > bady - 5 && (int) ylaunch < bady + 50
                        && dead == false) {
                  tool.stop();
                  endgame = true;
                  dead = true;
                  score = score + 100;
                  xhealth -= 150;
                  xlaunch = 40;
                  ylaunch = 475;
                  nextlevel = true;
                  Question();
                  g.drawImage(fire, (int) xlaunch, (int) ylaunch, null);
            }
            
            if (endgame == true) {
                g.drawImage(fire, xbomb - 15, ybomb - 15, null);
                g.setColor(Color.red);
                g.setFont(new Font("Calibri", Font.BOLD, 100));
                g.drawString("YOU WIN!", 280, 320);
                hide.stop();
                g.setColor(Color.blue);
                g.setFont(new Font("calibri", Font.BOLD, 50));
                g.drawString("Press Next to Proceed!", 250, 380);
                fly.stop();
            }
        }//end paintComponent

        public void Question() {
            String response;
            int x = (int)(Math.random() * 6);
            x = 3;
            System.out.println("\nQuestion Options:");
            for (int a = 0; a < 5; a++) {
                System.out.println("Option " + (a + 1) + ": " + questions[7 + (6 * x) + a]);
            }
            String temporary = questions[x];
            response = JOptionPane.showInputDialog(null,
            temporary);
                     
            if (response.equals(questions[6 + (6 * (x + 1))])) {
                JOptionPane.showMessageDialog(null,
                        "Correct!");
                score = score + 50;
                
            } else {
                JOptionPane.showMessageDialog(null,
                        "Incorrect!");
                score = score - 50;
            }

        }

        public void keyTyped(KeyEvent e) {
        }//end KeyTyped			

        public void keyReleased(KeyEvent e) {
        }

        public void actionPerformed(ActionEvent evt) {
            String command = evt.getActionCommand();
            if (command.equals(":)")) {
                System.exit(0);
            } else if (command.equals("Change Character")) {
                player = player + 1;
                if (player == 1) {
                    mass = 20;
                }
                if (player == 2) {
                    mass = 12;
                }
                if (player == 3) {
                    mass = 6;
                }
                if (player == 4) {
                    player = 1;
                }
                repaint();
            } else if (command.equals("Enter Cheats")) {
                String response = JOptionPane.showInputDialog(null,
                        "Enter your cheats. NO CAPS!\n");
                if (response.equals("manny")) {
                    JOptionPane.showMessageDialog(null,
                            "Initiating Self Destruct in 3..2...1...boom");
                    System.exit(0);
                } else if (response.equals("iamcool")) {
                    JOptionPane.showMessageDialog(null,
                            "Score has been set to 100000!");
                    score = 100000;
                }
                repaint();
            } else if (command.equals("Restart Level")) {
                boomer = true;
                ycor[0] = 90;
                ycor[1] = 160;
                ycor[2] = 230;
                ycor[3] = 300;
                ycor[4] = 370;
                score = lvl2;
                ycor[5] = 440;
                ycor[6] = 510;
                ycor1[0] = 90;
                ycor1[1] = 160;
                ycor1[2] = 230;
                ycor1[3] = 300;
                ycor1[4] = 370;
                ycor1[5] = 440;
                ycor1[6] = 510;
                xcor[0] = 520;
                xcor[1] = 650;
                endgame = false;
                infiniteoxygen = false;
                xhealth = 150;
                score = lvl1;
                dead = false;
                woodtop1 = true;
                woodtopleft = true;
                woodbottomleft = true;
                xhero = 50;
                xhealth = 150;
                endgame = false;
                dead = false;
                woodtopbottom = true;
                woodtop = true;
                boom.start();//starts the timer
                boomer = true;
                endgame = false;
                infiniteoxygen = false;
                xhealth = 150;
                dead = false;
                woodtop1 = true;
                woodtopleft = true;
                woodtopright = true;
                woodbottomleft = true;
                xhero = 50;
                dead = false;
                woodtopbottom = true;
                woodtop = true;
                nextlevel = false;
                boom.start();//starts the timer
                repaint();
                repaint();
            } else if (command.equals("Next")) {
                if (nextlevel == true) {
                    nextlevel = false;
                    lvl3 = score;
                    boomer = true;
                    endgame = false;
                    xhealth = 150;
                    score = 0;
                    dead = false;
                    woodtop1 = true;
                    woodtopleft = true;
                    woodbottomleft = true;
                    xhero = 50;
                    dead = false;
                    woodtopbottom = true;
                    endgame = false;
                    boom.stop();
                    endgame = false;
                    woodtopbottom = true;
                    woodtop = true;
                    repaint();
                    
                    forum.next(containMe);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please Finish the Current Level");
                    nextlevel = false;
                }
            } else if (command.equals("Game Stats")) {
                JOptionPane.showMessageDialog(null,
                        "Your Score is: " + score); 
            }

            requestFocus();
        }
    }//end OSPPanel5
    
    //Credits screen
    class OPSPanel6 extends JPanel implements ActionListener {
        JLabel score;
        int a;
        JButton exitwin;
        //Constructor for buttons and text
        public OPSPanel6() {            
            JLabel score = new JLabel();
            a = 0;
            score = new JLabel("Congratulations You Win!");
            score.setForeground(Color.blue);
            score.setFont(new Font("Times New Roman", Font.BOLD, 50));
            score.setSize(900, 60);
            score.setLocation(200, 300);
            this.add(score); 
            JButton exitwin = new JButton("Exit Game");
            exitwin.setSize(120, 60);
            exitwin.setLocation(440, 550);
            exitwin.addActionListener(this);
            exitwin.setBackground(Color.red);
            exitwin.setForeground(Color.white);
            this.add(exitwin);
        }

        public void paintComponent(Graphics g) {
           setBackground(Color.green);
           if (a == 0) 
           System.out.println("\n\nCongratulations!\nYour score is: " + lvl3);
           a++;           
        }//end paintComponent
        
        public void actionPerformed(ActionEvent evt) {
            String response = evt.getActionCommand();
            if (response == "Exit Game") {
                System.exit(0);
            }
        }
    }//end OSPPanel6

}//end JavaGame
