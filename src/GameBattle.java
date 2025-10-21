import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class  GameBattle extends JFrame implements ActionListener {

    // Singleton pattern
    private static GameBattle battle = null;
    public static GameBattle getInstance(Character currentCharacter) {
            if (battle == null) {
                battle = new GameBattle(currentCharacter);
            }
        return battle;
    }

    // resetare battle
    public static void resetBattle() {
            battle = null;
    }

    private Character currentCharacter;
    private Enemy inamic;
    private BattleManager batalie;
    private JLabel statsCaracter, statsInamic;
    private JButton atacNormal, abilitati, iesire;
    private JTextArea logArea;

    public GameBattle(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        initializeEnemy();
        batalie = new BattleManager(currentCharacter, inamic, logArea);
        setupUI();
    }

    private void initializeEnemy() {
        currentCharacter.resetAbilities();
        Random random = new Random();

        // scalez viata si mana inamicului in functie de player
        int enemyHealth = (int) (currentCharacter.getMaxHealth() * (0.7 + random.nextDouble() * 0.6));
        int enemyMana = (int) (currentCharacter.getMaxMana() * (0.7 + random.nextDouble() * 0.6));

        inamic = new Enemy("Unknown", enemyHealth, enemyMana);
        inamic.setType(inamic.generateRandomName());

        inamic.resetAbilities();
    }

    // aici le combinam pe toate
    private void setupUI() {
        setTitle("Batalie Epica");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel leftPanel = createCharacterPanel();
        JPanel rightPanel = createEnemyPanel();
        JPanel buttonPanel = createButtonPanel();

        JScrollPane logScrollPane = new JScrollPane(logArea);

        JPanel combinaisan = new JPanel(new BorderLayout(15, 15));
        combinaisan.add(leftPanel, BorderLayout.WEST);
        combinaisan.add(rightPanel, BorderLayout.EAST);
        combinaisan.add(buttonPanel, BorderLayout.SOUTH);
        combinaisan.add(logScrollPane, BorderLayout.CENTER);

        add(combinaisan);
        setVisible(true);
    }

    // poza si stats player
    private JPanel createCharacterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel characterImage = new JLabel(new ImageIcon(currentCharacter.getImagePath()));
        statsCaracter = new JLabel(getCharacterStats());
        panel.add(characterImage, BorderLayout.NORTH);
        panel.add(statsCaracter, BorderLayout.CENTER);
        return panel;
    }

    // poza si stats inamic
    private JPanel createEnemyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel enemyImage = new JLabel(new ImageIcon(getRandomImagePathEnemy(inamic.getName())));
        statsInamic = new JLabel(getEnemyStats());
        panel.add(enemyImage, BorderLayout.NORTH);
        panel.add(statsInamic, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));
        atacNormal = new JButton("Atac Basic");
        abilitati = new JButton("Abilitati magice");
        iesire = new JButton("Exit");

        atacNormal.addActionListener(this);
        abilitati.addActionListener(this);
        iesire.addActionListener(this);

        panel.add(atacNormal);
        panel.add(abilitati);
        panel.add(iesire);

        return panel;
    }

    // daca nu mai ai abilitati dispare butonul
    private void updateButton() {
        abilitati.setVisible(currentCharacter.canUseAbility());
    }

    private String getRandomImagePathEnemy(String type) {
        return "src/images/" + type.toLowerCase() + ".png";
    }

    private String getCharacterStats() {
        return "<html>Character: " + currentCharacter.getName() +
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

    private String getEnemyStats() {
        return "<html>Character: " + inamic.getName() +
                "<br>Health: " + inamic.getCurrentHealth() + "/" + inamic.getMaxHealth() +
                "<br>Mana: " + inamic.getCurrentMana() + "/" + inamic.getMaxMana() + "</html>";
    }

    private void updateUI() {
        statsCaracter.setText(getCharacterStats());
        statsInamic.setText(getEnemyStats());
        updateButton();
        // merge in jos automat
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void checkBattleOutcome() {
        if (!batalie.isEnemyAlive()) {
            logMessage("You defeated the enemy!");

            // regenerez viata si mana
            currentCharacter.resetMana();
            currentCharacter.regenLife(currentCharacter.getMaxHealth()/2);

            // cresc nr inamici infranti
            currentCharacter.increaseEnemiesDefeated();

            // +random xp
            int prevlevel = currentCharacter.getLevel();
            Random random = new Random();
            int xpGained = random.nextInt(40) + 20;
            currentCharacter.gainExperience(xpGained);

            logMessage("You gained " + xpGained + " XP!");

            // verific level
            if (currentCharacter.getLevel() > prevlevel) {
                logMessage("Congratulations! You leveled up to Level " + currentCharacter.getLevel() + "!");
                JOptionPane.showMessageDialog(this, "Level Up! New Level: " + currentCharacter.getLevel());
            }

            JOptionPane.showMessageDialog(this, "Victory! Mana restored and health partially regenerated.");
            resetBattle();
            dispose();
        } else if (!batalie.isPlayerAlive()) {
            logMessage("You have been defeated!");
            JOptionPane.showMessageDialog(this, "Game Over!");
            resetBattle();
            //inchid tot
            closeAllWindows();
            new GameEnd(currentCharacter);
        }
    }

    private void closeAllWindows() {
        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame) {
                window.dispose();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // atac normal player dupa e randul inamicului
        if (e.getSource() == atacNormal) {
            batalie.playerBasic();
            updateUI();
            checkBattleOutcome();
            if (batalie.isEnemyAlive()) {
                batalie.enemyTurn();
                updateUI();
                checkBattleOutcome();
            }
        } else if (e.getSource() == abilitati) {
            if (currentCharacter.getAbilities().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have no abilities left!");
            } else {
                new AbilitySelect(currentCharacter, inamic, logArea, batalie, () -> {
                    updateUI();
                    checkBattleOutcome();
                    if (batalie.isEnemyAlive()) {
                        batalie.enemyTurn();
                        updateUI();
                        checkBattleOutcome();
                    }
                });
            }
        } else if (e.getSource() == iesire) {
            JOptionPane.showMessageDialog(this, "Tzeapa ai crezut ca scapi:))");
        }
    }


    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n\n"));
    }
}

class BattleManager {
    private Character player;
    private Enemy inamic;
    private JTextArea logArea;

    public BattleManager(Character player, Enemy inamic, JTextArea logArea) {
        this.player = player;
        this.inamic = inamic;
        this.logArea = logArea;
    }

    public boolean isPlayerAlive() {
        return player.getCurrentHealth() > 0;
    }

    public boolean isEnemyAlive() {
        return inamic.getCurrentHealth() > 0;
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n\n"));
    }

    public void playerBasic() {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(player.getName()).append(" used a basic attack!");

        int damage = calculateBasicDamage();
        if (player.criticalHit()) {
            damage *= 2;
            logMessage.append("\nCritical hit! Damage is now ").append(damage).append(".");
        }

        if (inamic.dodge()) {
            logMessage.append("\nBut ").append(inamic.getName()).append(" evaded the attack!");
        } else {
            boolean isAlive = inamic.receiveDamages(damage, "Physical");
            if (!isAlive) {
                logMessage.append("\n").append(inamic.getName()).append(" took ").append(damage).append(" damage and was defeated!");
            } else {
                logMessage.append("\n").append(inamic.getName()).append(" took ").append(damage).append(" damage!");
            }
        }

        logMessage(logMessage.toString());
    }

    public int calculateBasicDamage() {
        int damage;
        switch (player.getProfession().toLowerCase()) {
            case "warrior":
                damage = player.getStrenght();
                break;
            case "mage":
                damage = player.getCharisma();
                break;
            case "rogue":
                damage = player.getDexterity();
                break;
            default:
                damage = 10; // fallback
        }
        return damage;
    }

    public void playerUseAbility(Spell ability) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(player.getName()).append(" used the ability: ").append(ability.getType());

        int damage = ability.getDamage();
        if (player.criticalHit()) {
            damage *= 2;
            logMessage.append("\nCritical hit! Damage doubled to ").append(damage).append("!");
        }

        if (inamic.isResistant(ability.getType())) {
            logMessage.append("\nBut ").append(inamic.getName()).append(" is immune to ").append(ability.getType()).append(" damage!");
        } else if (inamic.dodge()) {
            logMessage.append("\nBut ").append(inamic.getName()).append(" evaded the attack!");
        } else {
            boolean isAlive = inamic.receiveDamages(damage, ability.getType());
            if (!isAlive) {
                logMessage.append("\n").append(inamic.getName()).append(" took ").append(damage).append(" damage and was defeated!");
            } else {
                logMessage.append("\n").append(inamic.getName()).append(" took ").append(damage).append(" damage!");
            }
        }

        logMessage(logMessage.toString());
    }


    public void enemyTurn() {
        StringBuilder logMessage = new StringBuilder();

        if (inamic.getAbilities().isEmpty()) {
            logMessage.append("The enemy chose a basic attack!");
            int damage = inamic.getDamage();
            if (inamic.criticalHit()) {
                damage *= 2;
                logMessage.append("\nCritical hit! Damage doubled to ").append(damage).append("!");
            }

            if (player.dodge()) {
                logMessage.append("\nBut ").append(player.getName()).append(" evaded the attack!");
            } else {
                player.receiveDamages(damage, "Physical");
                logMessage.append("\n").append(player.getName()).append(" received ").append(damage).append(" damage!");
            }
        } else {
            Spell randomAbility = inamic.getAbilities().get(new Random().nextInt(inamic.getAbilities().size()));
            logMessage.append("The enemy chose the ability: ").append(randomAbility.getType());

            int damage = randomAbility.getDamage();
            if (inamic.criticalHit()) {
                damage *= 2;
                logMessage.append("\nCritical hit! Damage doubled to ").append(damage).append("!");
            }

            if (player.isResistant(randomAbility.getType())) {
                logMessage.append("\nBut ").append(player.getName()).append(" is immune to ").append(randomAbility.getType()).append(" damage!");
            } else if (player.dodge()) {
                logMessage.append("\nBut ").append(player.getName()).append(" evaded the attack!");
            } else {
                player.receiveDamages(damage, randomAbility.getType());
                logMessage.append("\n").append(player.getName()).append(" received ").append(damage).append(" damage!");
            }
        }

        logMessage(logMessage.toString());
    }
}


class AbilitySelect extends JFrame {
    private Character currentCharacter;
    private Enemy inamic;
    private JTextArea logArea;
    private BattleManager batalie;
    private Runnable updateUI;

    public AbilitySelect(Character currentCharacter, Enemy inamic, JTextArea logArea, BattleManager batalie, Runnable updateUI) {
        this.currentCharacter = currentCharacter;
        this.inamic = inamic;
        this.logArea = logArea;
        this.batalie = batalie;
        this.updateUI = updateUI;
        setupUI();
    }

    private void setupUI() {
        setTitle("Alege abilitate");
        setSize(550, 1200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel abilityPanel = new JPanel(new GridLayout(currentCharacter.getAbilities().size(), 1));
        for (Spell ability : currentCharacter.getAbilities()) {
            JPanel abilityRow = new JPanel(new BorderLayout());
            JLabel abilityIcon = new JLabel(new ImageIcon("src/images/" + ability.getType().toLowerCase() + ".png"));
            JLabel abilityDetails = new JLabel("<html>Damage: " + ability.getDamage() +
                    "<br>Mana Cost: " + ability.getManaCost() + "</html>");
            JButton useButton = new JButton("Use");

            useButton.addActionListener(e -> {
                if (currentCharacter.getCurrentMana() >= ability.getManaCost()) {
                    batalie.playerUseAbility(ability); // fol abilitatea
                    currentCharacter.getAbilities().remove(ability); // o scoate de acolo
                    updateUI.run(); // actualizeaza UI
                    dispose(); // inchide fereastra
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough mana!");
                }
            });

            abilityRow.add(abilityIcon, BorderLayout.WEST);
            abilityRow.add(abilityDetails, BorderLayout.CENTER);
            abilityRow.add(useButton, BorderLayout.EAST);
            abilityPanel.add(abilityRow);
        }

        JButton backButton = new JButton("Back to Battle");
        backButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(backButton, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(abilityPanel);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}


