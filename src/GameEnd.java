import javax.swing.*;
import java.awt.*;

public class GameEnd extends JFrame {

    private Character currentCharacter;

    public GameEnd(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
        setupUI();
    }

    private void setupUI() {
        setTitle("Endgame Stats");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Endgame Stats", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // stats
        JLabel statsLabel = new JLabel(getCharacterStats(), SwingConstants.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(statsLabel, BorderLayout.CENTER);

        // butoane
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton exitButton = new JButton("Exit");
        JButton replayButton = new JButton("Replay");

        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thanks for playing!");
            System.exit(0);
        });

        // joc nou
        replayButton.addActionListener(e -> {
            dispose();
            GameLogin game = GameLogin.getInstance();
            game.run();
        });

        buttonPanel.add(replayButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private String getCharacterStats() {
        return "<html>Character: " + currentCharacter.getName() +
                "<br>Health: " + currentCharacter.getCurrentHealth() + "/" + currentCharacter.getMaxHealth() +
                "<br>Mana: " + currentCharacter.getCurrentMana() + "/" + currentCharacter.getMaxMana() +
                "<br>Experience Gained: " + currentCharacter.getExperience() +
                "<br>Level Reached: " + currentCharacter.getLevel() +
                "<br>Enemies Defeated: " + currentCharacter.getEnemiesDefeated() +
                "<br>Games Played: " + currentCharacter.getAccount().getGamesPlayed() +
                "</html>";
    }
}
