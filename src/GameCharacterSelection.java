import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameCharacterSelection extends JFrame implements ActionListener {

    // Singleton Pattern
    private static GameCharacterSelection joc = null;

    public static GameCharacterSelection getInstance(Account currentAccount) {
        synchronized (GameCharacterSelection.class) {
            if (joc == null) {
                joc = new GameCharacterSelection(currentAccount);
            }
        }
        return joc;
    }

    private Account currentAccount;
    private Character selectedCharacter;
    private JPanel characterPanel;

    // Constructor for Singleton
    private GameCharacterSelection(Account currentAccount) {
        this.currentAccount = currentAccount;
        this.selectedCharacter = null;

        // initializez cu ajutorul factory pattern
        initializeCharacters();

        // set up la interfata
        setupUI();
    }

    private void initializeCharacters() {
        CharacterFactory factory;
        ArrayList<Character> newCharacters = new ArrayList<>();
        for (Character character : currentAccount.getCharacterList()) {
            factory = getFactoryByProfession(character.getProfession());
            Character newCharacter = factory.createCharacter(
                    character.getName(),
                    character.getExperience(),
                    character.getLevel()
            );
            newCharacter.setAccount(currentAccount);
            newCharacter.lvlUpUndercover(newCharacter);
            newCharacters.add(newCharacter);
        }
        currentAccount.setCharacterList(newCharacters);
    }

    private CharacterFactory getFactoryByProfession(String profession) {
        switch (profession.toLowerCase()) {
            case "warrior":
                return new WarriorFactory();
            case "mage":
                return new MageFactory();
            case "rogue":
                return new RogueFactory();
            default:
                throw new IllegalArgumentException("Unknown profession: " + profession);
        }
    }

    private void setupUI() {
        setTitle("Character Selection");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // panel pt caractere
        characterPanel = new JPanel();
        characterPanel.setLayout(new GridLayout(0, 3));

        // adaug caracterele
        for (Character character : currentAccount.getCharacterList()) {
            addCharacterToPanel(character);
        }

        add(characterPanel, BorderLayout.CENTER);

        // butonul de confirm
        JButton confirmButton = new JButton("Confirm Selection");
        confirmButton.addActionListener(this);
        add(confirmButton, BorderLayout.SOUTH);
    }

    private void addCharacterToPanel(Character character) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel imageLabel = new JLabel();
        String imagePath = getRandomImagePath(character.getProfession());
        character.setImagePath(imagePath);
        ImageIcon icon = new ImageIcon(imagePath);

        // adaug imaginea
        imageLabel.setIcon(icon);
        panel.add(imageLabel, BorderLayout.CENTER);

        // detalii caractere
        JLabel detailsLabel = new JLabel("<html>" +
                "Name: " + character.getName() + "<br>" +
                "Profession: " + character.getProfession() + "<br>" +
                "Level: " + character.getLevel() + "<br>" +
                "Experience: " + character.getExperience() + "/" + character.experienceToNextLevel() + "<br>" +
                "Strength: " + character.getStrenght() + "<br>" +
                "Charisma: " + character.getCharisma() + "<br>" +
                "Dexterity: " + character.getDexterity() + "</html>");
        panel.add(detailsLabel, BorderLayout.SOUTH);

        // listener ul pt selectie
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                for (Component comp : characterPanel.getComponents()) {
                    if (comp instanceof JPanel) {
                        ((JPanel) comp).setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                    }
                }
                panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
                selectedCharacter = character;
            }
        });

        characterPanel.add(panel);
    }

    private String getRandomImagePath(String profession) {
        int randomIndex = (int) (Math.random() * 3);
        return "src/images/" + profession.toLowerCase() + randomIndex + ".png";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedCharacter == null) {
            JOptionPane.showMessageDialog(this, "No character selected. Please select one.");
        } else {
            JOptionPane.showMessageDialog(this, "Character " + selectedCharacter.getName() + " selected!");
            dispose();
            // urmeaza jocul propriu-zis
            GamePlay.getInstance(selectedCharacter);
        }
    }

    public void run() {
        setTitle("Character Selection");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
}
