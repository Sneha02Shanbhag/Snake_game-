package snakegame;

import javax.swing.*;

public class SnakeGame extends JFrame {

    private Board board;

    SnakeGame() {
        super("Snake Game");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String level = getLevel();

        board = new Board(level);
        add(board);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getLevel() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(null, "Select level", "Level", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 1:
                return "easy";
            case 2:
                return "medium";
            case 3:
                return "hard";
            default:
                return "easy";
        }
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
