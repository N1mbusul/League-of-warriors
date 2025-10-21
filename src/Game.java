
import java.util.ArrayList;
import java.util.Scanner;

interface CharacterFactory {
    Character createCharacter(String name, int experience, int level);
}

class WarriorFactory implements CharacterFactory {
    @Override
    public Character createCharacter(String name, int experience, int level) {
        return new Warrior(name,  experience, level);
    }
}

class MageFactory implements CharacterFactory {
    @Override
    public Character createCharacter(String name, int experience, int level) {
        return new Mage(name, experience, level);
    }
}

class RogueFactory implements CharacterFactory {
    @Override
    public Character createCharacter(String name, int experience, int level) {
        return new Rogue(name, experience, level);
    }
}

// vechiul cod dupa care m am luat pt implementare

//public class Game {
//
//    // Singleton Pattern
//    private static Game joc = null;
//    public static Game getInstance(){
//        // folosesc efectiv clasa pentru sincronizare
//        synchronized (Game.class) {
//            if (joc == null) {
//                joc = new Game();
//            }
//        }
//        return joc;
//    }
//
//    private ArrayList<Account> accounts;
//    private Grid map;
//
//    private Game() {
//        try {
//            accounts = JsonInput.deserializeAccounts();
//            if (accounts == null || accounts.isEmpty()) {
//                throw new RuntimeException("No accounts were loaded.");
//            }
//            System.out.println("Accounts successfully loaded.");
//            associateCharactersWithAccounts();
//        } catch (Exception e) {
//            System.out.println("Error loading accounts: " + e.getMessage());
//        }
//    }
//
//    // Factory pattern pentru a instantia personajele din lista fiecarui cont
//    private void associateCharactersWithAccounts() {
//        CharacterFactory factory;
//        for (Account account : accounts) {
//            ArrayList<Character> newCharacters = new ArrayList<>();
//            for (Character character : account.getCharacterList()) {
//                factory = getFactoryByProfession(character.getProfession());
//                Character newCharacter = factory.createCharacter(
//                        character.getName(),
//                        character.getExperience(),
//                        character.getLevel()
//                );
//                newCharacter.setAccount(account);
//                newCharacter.lvlUpUndercover(newCharacter);
//                newCharacters.add(newCharacter);
//            }
//            account.setCharacterList(newCharacters);
//        }
//    }
//
//    private CharacterFactory getFactoryByProfession(String profession) {
//        switch (profession.toLowerCase()) {
//            case "warrior":
//                return new WarriorFactory();
//            case "mage":
//                return new MageFactory();
//            case "rogue":
//                return new RogueFactory();
//            default:
//                throw new IllegalArgumentException("Unknown profession: " + profession);
//        }
//    }
//
//    public void run() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("===== Welcome to the POO Game! =====");
//
//        while (true) {
//            System.out.println("\n===== Login =====");
//            Account currentAccount = null;
//
//            while (currentAccount == null) {
//                try {
//                    System.out.print("Email: ");
//                    String email = scanner.next();
//
//                    for (Account account : accounts) {
//                        Credentials credentials = account.getInformation().getCredentials();
//                        if (credentials.getEmail().equals(email)) {
//                            currentAccount = account;
//                            break;
//                        }
//                    }
//
//                    if (currentAccount == null) {
//                        throw new IllegalArgumentException("Email does not exist. Please try again.");
//                    }
//
//                    boolean passwordCorrect = false;
//                    while (!passwordCorrect) {
//                        System.out.print("Password: ");
//                        String password = scanner.next();
//                        Credentials credentials = currentAccount.getInformation().getCredentials();
//                        if (!credentials.getPassword().equals(password)) {
//                            System.out.println("Incorrect password. Please try again.");
//                        } else {
//                            passwordCorrect = true;
//                            System.out.println("\tLogin successful!");
//                        }
//                    }
//                } catch (IllegalArgumentException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//
//            ArrayList<Character> characters = currentAccount.getCharacterList();
//            if (characters.isEmpty()) {
//                System.out.println("\tThis account has no characters. Exiting.");
//                return;
//            }
//
//            int verifDeBackLaLogin = 0;
//            Character selectedCharacter = null;
//            while (selectedCharacter == null) {
//                try {
//                    System.out.println("\n===== Select a Character: =====");
//                    for (int i = 0; i < characters.size(); i++) {
//                        System.out.println("\n" + (i + 1) + ". " + characters.get(i));
//                    }
//
//                    System.out.print("\nChoose a character (1-" + characters.size() + ") or 0 to go back to login: " );
//                    int choice = scanner.nextInt();
//                    if (choice < 0 || choice > characters.size()) {
//                        throw new IllegalArgumentException("Invalid choice. Try again.");
//                    }
//
//                    if(choice == 0){
//                        verifDeBackLaLogin = 1;
//                        break;
//                    }
//
//                    selectedCharacter = characters.get(choice - 1);
//
//                    // verific ca e in viata
//                    if (selectedCharacter.getCurrentHealth() <= 0) {
//                        System.out.println("\nThis character is dead and cannot be played. Please choose another character.");
//                        selectedCharacter = null;
//                        continue;
//                    }
//
//                    System.out.println("\nYou selected: " + selectedCharacter);
//
//                } catch (IllegalArgumentException e) {
//                    System.out.println(e.getMessage());
//                } catch (Exception e) {
//                    System.out.println("Error: Enter a valid number.");
//                    scanner.next();
//                }
//            }
//
//            if(verifDeBackLaLogin == 1)
//                continue;
//
//            map = Grid.generateMap();
//            map.placeCharacter(selectedCharacter);
//
//            System.out.println("===== Game Start! =====");
//            boolean gameRunning = true;
//
//            while (gameRunning) {
//                System.out.println("\n===== Character Stats =====");
//                System.out.println(selectedCharacter);
//
//                map.displayMap();
//
//                System.out.println("\nAvailable directions: ");
//                System.out.println("W: North");
//                System.out.println("S: South");
//                System.out.println("A: West");
//                System.out.println("D: East");
//                System.out.println("X: Exit");
//
//                System.out.print("Choose a direction: ");
//                String command = scanner.next().toUpperCase();
//
//                Cell nextCell = null;
//                switch (command) {
//                    case "W" -> {
//                        map.moveNorth();
//                        nextCell = map.getCurrentCell();
//                    }
//                    case "S" -> {
//                        map.moveSouth();
//                        nextCell = map.getCurrentCell();
//                    }
//                    case "A" -> {
//                        map.moveWest();
//                        nextCell = map.getCurrentCell();
//                    }
//                    case "D" -> {
//                        map.moveEast();
//                        nextCell = map.getCurrentCell();
//                    }
//                    case "X" -> {
//                        System.out.println("Exiting the game.\n");
//                        gameRunning = false;
//                    }
//                    default -> System.out.println("Invalid command. Try again.");
//                }
//
//                if (nextCell != null) {
//                    switch (nextCell.getEntityType()) {
//                        case SANCTUARY -> map.handleSanctuary();
//                        case ENEMY -> {
//                            System.out.println("\nYou encountered an enemy! Prepare for battle.");
//                            map.handleBattle(selectedCharacter);
//                            if (selectedCharacter.getCurrentHealth() <= 0) {
//                                System.out.println("Returning to login...");
//                                gameRunning = false;
//                            }
//                        }
//                        case PORTAL -> map.handlePortal();
//                        case VOID -> System.out.println("\nThis cell is empty, moving forward.");
//                        case VISITED -> System.out.println("\nReturning to an already visited cell.");
//                        default -> System.out.println("\nUnknown cell type!");
//                    }
//
//                    if (map.getCurrentCell() != nextCell) {
//                        map.getCurrentCell().setEntityType(CellEntityType.VISITED);
//                    }
//                    map.getCurrentCell().setEntityType(CellEntityType.PLAYER);
//                }
//            }
//        }
//    }
//
//}
