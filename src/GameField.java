import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    //size of one section or apple 16x16
    private final int DOT_SIZE = 16;
    //amount of sections 20*20
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    //apple position on the field
    private int appleX;
    private int appleY;
    //arrays to save snake's position at each second of the game
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    //snake's size at the moment
    private int dots;
    private Timer timer;
    //directions in which snake can move
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    //are we still playing or not
    private boolean inGame = true;

    public GameField() {
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true); //when we press keys, focus is on our gemefield, not on other programs
    }
    //initialization of the game
    public void initGame() {
        dots = 3;
        for (int i =0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }
    public void createApple() {
        appleX = new Random().nextInt(20)*DOT_SIZE;
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }
    public void loadImages() {
        ImageIcon iia = new ImageIcon("src/apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("src/dot.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String str = "Game Over";
            String score = "Your Score: " + (dots - 3);
            Font f = new Font("Courier New", Font.BOLD, 17);
            g.setColor(Color.white);
            g.setFont(f);
            g.drawString(str, 129, SIZE/2);
            g.drawString(score, 115, SIZE/2 +25);
            JButton button = new JButton("New Game");
            button.setBackground(Color.white);
            button.setSize(50,20);
            button.setLocation(115, SIZE/2 + 40);
            button.setVisible(true);
        }
    }
    //x[0] and y[0] - head of the snake
    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }
    //checks, if we have met an apple
    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            createApple();
        }
    }
    //checks, if we have bumped into border of our window or into ourself
    public void checkCollisions() {
        //checks, if snake have bumped into itself
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false; //we loose
            }
        }
        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }
    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode(); //we are becoming the code of the key, which is pressed
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                left = false;
                up = true;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                left = false;
                right = false;
                down = true;
            }
            if (key == KeyEvent.VK_S) {
                timer.stop();
            }
            if (key == KeyEvent.VK_P) {
                timer.start();
            }
        }
    }
}
