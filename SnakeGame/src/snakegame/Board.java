package snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

    private Image apple;
    private Image dot;
    private Image head;

    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 29;

    private int apple_x;
    private int apple_y;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame = true;

    private int dots;
    private Timer timer;
    private int speed;

    private int currentScore = 0;
    private int highestScore = 0;

    private JButton restartButton;

    public Board(String level) {
        setLevel(level);
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(300, 300));
        loadImages();
        initGame();
        startGame();
        addKeyListener(new TAdapter());
        
        // Initialize restart button
        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        add(restartButton);
        restartButton.setVisible(false);
    }

    public void setLevel(String level) {
        switch (level) {
            case "easy":
                speed = 200;
                break;
            case "medium":
                speed = 70;
                break;
            case "hard":
                speed = 10;
                break;
            default:
                speed = 0; // Default to easy level
                break;
        }
    }

    public void initGame() {
        dots = 3;
        currentScore = 0;
        loadHighestScore();

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();

        timer = new Timer(speed, this);
    }

    public void startGame() {
        timer.start();
        requestFocusInWindow();
    }

    public void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;

        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    public void loadImages() {
        ImageIcon i1 = new ImageIcon(getClass().getResource("/snakegame/icons/apple.png"));
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon(getClass().getResource("/snakegame/icons/dot.png"));
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon(getClass().getResource("/snakegame/icons/head.png"));
        head = i3.getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over!";
        Font font = new Font("SAN_SERIF", Font.BOLD, 24);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2);

        // Display final score
        String scoreMsg = "Final Score: " + currentScore;
        g.drawString(scoreMsg, (getWidth() - metrics.stringWidth(scoreMsg)) / 2, getHeight() / 2 + 30);
        
        // Show restart button
        restartButton.setVisible(true);
        updateHighestScore();
        saveHighestScore();
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] = x[0] - DOT_SIZE;
        }
        if (rightDirection) {
            x[0] = x[0] + DOT_SIZE;
        }
        if (upDirection) {
            y[0] = y[0] - DOT_SIZE;
        }
        if (downDirection) {
            y[0] = y[0] + DOT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            currentScore++;
            locateApple();
        }
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= getHeight()) {
            inGame = false;
        }
        if (x[0] >= getWidth()) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == KeyEvent.VK_RIGHT && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == KeyEvent.VK_UP && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            } else if (key == KeyEvent.VK_DOWN && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }

    private void loadHighestScore() {
        // Load highest score from storage
        // Example: highestScore = loadFromStorage();
    }

    private void saveHighestScore() {
        // Save highest score to storage
        // Example: saveToStorage(highestScore);
    }

    private void updateHighestScore() {
        if (currentScore > highestScore) {
            highestScore = currentScore;
        }
    }

    public void restartGame() {
        // Reset game parameters
        inGame = true;
        dots = 3;
        currentScore = 0;
        initGame();
        restartButton.setVisible(false);
        timer.restart();
        requestFocusInWindow();
    }
}
