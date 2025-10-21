import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePlay extends JFrame implements ActionListener {

    // Singleton Pattern
    private static GamePlay instance = null;

    public static void getInstance(Character currentCharacter) {
        synchronized (GamePlay.class) {
            if (instance == null) {
                instance = new GamePlay(currentCharacter);
            }
        }
    }

    private Character currentCharacter;
    private Grid map;
    private JPanel gridPanel;
    private JLabel statsLabel;
    private JButton northButton, southButton, westButton, eastButton, exitButton;

    public GamePlay(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
        initializeGame();
        setupUI();
    }

    private void initializeGame() {
        setTitle("Jocul Calamarului");
        setSize(1000, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // initializez harta
        map = Grid.generateMap();
        map.placeCharacter(currentCharacter);
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // controale pt caracter in stanga
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 2, 15, 15));
        northButton = new JButton("North");
        southButton = new JButton("South");
        westButton = new JButton("West");
        eastButton = new JButton("East");
        exitButton = new JButton("Exit");

        // dimensiuni butoane
        Dimension buttonSize = new Dimension(125, 65);
        northButton.setPreferredSize(buttonSize);
        southButton.setPreferredSize(buttonSize);
        westButton.setPreferredSize(buttonSize);
        eastButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        // action listeners
        northButton.addActionListener(this);
        southButton.addActionListener(this);
        westButton.addActionListener(this);
        eastButton.addActionListener(this);
        exitButton.addActionListener(this);

        // butoane in stanga
        leftPanel.add(northButton);
        leftPanel.add(southButton);
        leftPanel.add(westButton);
        leftPanel.add(eastButton);
        leftPanel.add(exitButton);

        // in caz ca se spawneaza in colt sau alte cazuri langa pereti
        updateButtons();

        // imaginea din centru a caracterului
        JLabel characterImageLabel = new JLabel(new ImageIcon(currentCharacter.getImagePath()));
        characterImageLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(characterImageLabel, BorderLayout.CENTER);

        // stats in dreapta
        statsLabel = new JLabel(getCharacterStats(), JLabel.LEFT);
        statsLabel.setVerticalAlignment(JLabel.TOP);
        statsLabel.setBorder(new EmptyBorder(50, 50, 50, 50));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(statsLabel, BorderLayout.NORTH);

        // combinare pannel uri
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(10, 10));
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // harta
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(map.getRows(), map.getCols()));
        updateGrid();
        add(gridPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void updateButtons() {
        // actualizez butoanele valabile
        northButton.setVisible(map.canMoveNorth());
        southButton.setVisible(map.canMoveSouth());
        westButton.setVisible(map.canMoveWest());
        eastButton.setVisible(map.canMoveEast());
    }

    private void updateGrid() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(map.getRows(), map.getCols()));

        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getCols(); j++) {
                Cell cell = map.getCell(i, j);
                JLabel cellLabel = new JLabel(/*"UNKNOWN"*/cell.getEntityType().toString(), JLabel.CENTER);
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellLabel.setOpaque(true);

                // cum pui cell uri le si ce culoare au
                switch (cell.getEntityType()) {
                    case PLAYER -> {
                        cellLabel.setText("PLAYER");
                        cellLabel.setBackground(Color.CYAN);
                    }
                    case SANCTUARY -> cellLabel.setBackground(Color.GREEN);
                    case ENEMY -> cellLabel.setBackground(Color.RED);
                    case PORTAL -> cellLabel.setBackground(Color.YELLOW);
                    case VOID -> cellLabel.setBackground(Color.WHITE);
                    case VISITED -> {
                        cellLabel.setText("VISITED");
                        cellLabel.setBackground(Color.LIGHT_GRAY);
                    }
                }
                gridPanel.add(cellLabel);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }


    private String getCharacterStats() {
        return "<html>Character: " + currentCharacter.getName() + "\t" +
                "<br>Health: " + currentCharacter.getCurrentHealth() + "/" + currentCharacter.getMaxHealth() +
                "<br>Mana: " + currentCharacter.getCurrentMana() + "/" + currentCharacter.getMaxMana() +
                "<br>Experience: " + currentCharacter.getExperience() + "/" + currentCharacter.experienceToNextLevel() +
                "<br>Level: " + currentCharacter.getLevel() +
                "<br>Strenght: " + currentCharacter.getStrenght() +
                "<br>Charisma: " + currentCharacter.getCharisma() +
                "<br>Dexterity: " + currentCharacter.getDexterity() +
                "<br>Enemies Defeated: " + currentCharacter.getEnemiesDefeated() +
                "<br>Games Played: " + currentCharacter.getAccount().getGamesPlayed() + "</html>";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == northButton) {
            map.moveNorth();
        } else if (e.getSource() == southButton) {
            map.moveSouth();
        } else if (e.getSource() == westButton) {
            map.moveWest();
        } else if (e.getSource() == eastButton) {
            map.moveEast();
        } else if (e.getSource() == exitButton) {
            JOptionPane.showMessageDialog(this, "Exiting the game. Thanks for playing!");
            dispose();
            // resetz jocul
            new GameEnd(currentCharacter);
        }

        // cazuri pt celule
        Cell currentCell = map.getCurrentCell();
        switch (currentCell.getEntityType()) {
            case SANCTUARY -> {
                hangleUISanctuary();
            }
            case ENEMY -> {
                JOptionPane.showMessageDialog(this, "You encountered an enemy! Prepare for battle.");
                GameBattle.getInstance(currentCharacter);

                // dupa battle actualizez stats
                statsLabel.setText(getCharacterStats());
                updateGrid();
                updateButtons();
            }
            case PORTAL -> {
                handleUIPortal();
            }
            case VOID -> JOptionPane.showMessageDialog(this, "This cell is empty.");
            case VISITED -> JOptionPane.showMessageDialog(this, "You already visited this cell.");
        }

        // movement pe harta
        if (currentCell.getEntityType() != CellEntityType.PLAYER) {
            currentCell.setEntityType(CellEntityType.VISITED);
            map.getCurrentCell().setEntityType(CellEntityType.PLAYER);
        }

        // refresh UI
        statsLabel.setText(getCharacterStats());
        updateGrid();
        updateButtons();
    }

    public void hangleUISanctuary(){
        JOptionPane.showMessageDialog(this, "You found a sanctuary! Restoring health and mana.");
        int regenLife = 20;
        int regenMana = 15;

        if (currentCharacter.getCurrentHealth() < currentCharacter.getMaxHealth()) {
            currentCharacter.regenLife(regenLife);
            JOptionPane.showMessageDialog(this,"+" + regenLife + " health restored.");
        } else {
            JOptionPane.showMessageDialog(this,"Health is already full.");
        }

        if (currentCharacter.getCurrentMana() < currentCharacter.getMaxMana()) {
            currentCharacter.regenMana(regenMana);
            JOptionPane.showMessageDialog(this,"+" + regenMana + " mana restored.");
        } else {
            JOptionPane.showMessageDialog(this,"Mana is already full.");
        }
    }

    public void handleUIPortal(){
        int prevLevel = currentCharacter.getLevel();
        JOptionPane.showMessageDialog(this, "You entered a portal! Moving to a new location." +
                "\n+" + currentCharacter.getAccount().getGamesPlayed() * 5 + " experience gained!" +
                "\n Resetting the game...");
        map.handlePortal();
        // verific pt lvl up
        if (currentCharacter.getLevel() > prevLevel) {
            JOptionPane.showMessageDialog(this, "Congratulations! You leveled up to Level " + currentCharacter.getLevel() + "!");
        }
        // verific ca e updatat grid ul
        updateGrid();
    }
}
