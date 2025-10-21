import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Grid {

    private ArrayList<ArrayList<Cell>> terrain;
    private int rows, columns;
    private Character character;
    private Cell currentCell;

    private Grid(ArrayList<ArrayList<Cell>> terrain, int rows, int columns, Cell currentCell) {
        this.terrain = terrain;
        this.rows = rows;
        this.columns = columns;
        this.currentCell = currentCell;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public static Grid generateMap() {
        Random random = new Random();
        int columns = random.nextInt(4, 11); // Dimensiuni între 4 și 10
        int rows = random.nextInt(4, 11);

        ArrayList<ArrayList<Cell>> terrain = new ArrayList<>();
        // initializam harta
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                row.add(new Cell(i, j, false, CellEntityType.VOID));
            }
            terrain.add(row);
        }

        // generam portal
        placeEntity(terrain, rows, columns, 1, CellEntityType.PORTAL);

        // generam sanctuare
        int sanctuaries = Math.max(random.nextInt(2, 6), 2); // Minim 2 sanctuare
        placeEntity(terrain, rows, columns, sanctuaries, CellEntityType.SANCTUARY);

        // generam inamici
        int enemies = Math.max(random.nextInt(4, 10), 4); // Minim 4 inamici
        placeEntity(terrain, rows, columns, enemies, CellEntityType.ENEMY);

        return new Grid(terrain, rows, columns, null);

    }

    public static Grid generateTestMap() {
        ArrayList<ArrayList<Cell>> teren = new ArrayList<>();

        // harta de test de 5x5
        for (int i = 0; i < 5; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                CellEntityType type = CellEntityType.VOID;

                // plasare celule cerute
                if (i == 4 && j == 4)
                    type = CellEntityType.PORTAL;
                if ((i == 0 && j == 3) || (i == 1 && j == 3) || (i == 2 && j == 0) || (i == 4 && j == 3))
                    type = CellEntityType.SANCTUARY;
                if (i == 3 && j == 4)
                    type = CellEntityType.ENEMY;

                row.add(new Cell(i, j, false, type));
            }
            teren.add(row);
        }

        return new Grid(teren, 5, 5, null);
    }

    private static void placeEntity(ArrayList<ArrayList<Cell>> terrain, int rows, int columns, int count, CellEntityType type) {
        Random random = new Random();
        int placed = 0;

        while (placed < count) {
            int x = random.nextInt(rows);
            int y = random.nextInt(columns);

            Cell cell = terrain.get(x).get(y);
            if (cell.getEntityType() == CellEntityType.VOID) {
                cell.setEntityType(type);
                placed++;
            }
        }
    }

    public void placeCharacter(Character character) {
        Cell characterCell = getCell(); // aleg o celula void pt plasare

        if (characterCell == null) {
            throw new InvalidCharacterPlacementException("No valid cell available for placing the character.");
        }

        this.character = character; // asociem cracterul cu harta
        this.currentCell = characterCell; // actualizam pozitia
        currentCell.setEntityType(CellEntityType.PLAYER); // setam celula ca PLAYER

    }

    public void placeCharacterAt(Character character, int row, int column) {
        // verificare ca e pozitia ok
        if (row < 0 || row >= this.rows || column < 0 || column >= this.columns) {
            throw new InvalidCharacterPlacementException("The chosen position is out of bounds.");
        }

        // iau celula de pe pozitia ceruta
        Cell characterCell = this.terrain.get(row).get(column);

        // verific daca e libera
        if (characterCell.getEntityType() != CellEntityType.VOID) {
            throw new InvalidCharacterPlacementException("The chosen cell is not empty.");
        }

        // plasez player ul
        this.character = character;
        this.currentCell = characterCell;
        characterCell.setEntityType(CellEntityType.PLAYER);
    }

    private Cell getCell() {
        Random random = new Random();
        ArrayList<Cell> voidCells = new ArrayList<>();

        // adun celulele de tip void
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = terrain.get(i).get(j);
                if (cell.getEntityType() == CellEntityType.VOID) {
                    voidCells.add(cell);
                }
            }
        }

        // iau una random
        return voidCells.get(random.nextInt(voidCells.size()));
    }


    public void displayMap() {
        System.out.println("\nCurrent Map:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = terrain.get(i).get(j);
                if (cell == currentCell) {
                    System.out.print("P "); // Player
                } else if (cell.getEntityType() == CellEntityType.VISITED) {
                    System.out.print("V "); // Vizitat
                } else {
                    System.out.print("N "); // Necunoscut
                }
            }
            System.out.println();
        }
    }

    public void displayTestMap() {
        System.out.println("\nCurrent Map:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = terrain.get(i).get(j);
                if (cell == currentCell) {
                    System.out.print("P "); // Player
                } else if (cell.getEntityType() == CellEntityType.VISITED) {
                    System.out.print("V "); // Vizitat
                } else if (cell.getEntityType() == CellEntityType.PORTAL) {
                    System.out.print("F "); // Portal
                } else if (cell.getEntityType() == CellEntityType.SANCTUARY) {
                    System.out.print("S "); // Sanctuary
                } else if (cell.getEntityType() == CellEntityType.ENEMY) {
                    System.out.print("E "); // Inamic
                } else {
                    System.out.print("N "); // Necunoscut
                }
            }
            System.out.println();
        }
    }

    public void moveNorth() {
//        if (currentCell == null || currentCell.getCoordX() <= 0) {
//            System.out.println("\nCannot move north!");
//            return;
//        }
        currentCell.setEntityType(CellEntityType.VISITED);
        currentCell = terrain.get(currentCell.getCoordX() - 1).get(currentCell.getCoordY());
    }

    public void moveSouth() {
//        if (currentCell == null || currentCell.getCoordX() >= rows - 1) {
//            System.out.println("\nCannot move south!");
//            return;
//        }
        currentCell.setEntityType(CellEntityType.VISITED);
        currentCell = terrain.get(currentCell.getCoordX() + 1).get(currentCell.getCoordY());
    }

    public void moveWest() {
//        if (currentCell == null || currentCell.getCoordY() <= 0) {
//            System.out.println("\nCannot move west!");
//            return;
//        }
        currentCell.setEntityType(CellEntityType.VISITED);
        currentCell = terrain.get(currentCell.getCoordX()).get(currentCell.getCoordY() - 1);
    }

    public void moveEast() {
//        if (currentCell == null || currentCell.getCoordY() >= columns - 1) {
//            System.out.println("\nCannot move east!");
//            return;
//        }
        currentCell.setEntityType(CellEntityType.VISITED);
        currentCell = terrain.get(currentCell.getCoordX()).get(currentCell.getCoordY() + 1);
    }

    public boolean canMoveNorth() {
        return currentCell != null && currentCell.getCoordX() > 0;
    }

    public boolean canMoveSouth() {
        return currentCell != null && currentCell.getCoordX() < rows - 1;
    }

    public boolean canMoveWest() {
        return currentCell != null && currentCell.getCoordY() > 0;
    }

    public boolean canMoveEast() {
        return currentCell != null && currentCell.getCoordY() < columns - 1;
    }


    public void handlePortal() {
        try {
            //System.out.println("\nYou arrived at a portal! Gaining experience...");
            int experienceGained = character.getAccount().getGamesPlayed() * 5;
            character.gainExperience(experienceGained);
            //System.out.println("+" + experienceGained + " experience gained!");
            character.getAccount().incrementGamesPlayed();
            character.levelUp();
            character.resetLifeAndMana();

            //System.out.println("Resetting the game...");

            // sterg pozitia player ului de pe harta veche
            if (currentCell != null) {
                currentCell.setEntityType(CellEntityType.VOID);
            }

            // generez si stez o noua harta
            Grid newMap = Grid.generateMap();
            this.terrain = newMap.terrain;
            this.rows = newMap.rows;
            this.columns = newMap.columns;
            this.currentCell = newMap.currentCell;

            // pun player ul pe harta noua
            placeCharacter(character);

            //System.out.println("Game reset complete. Good luck in the next round!");
            //displayEndScreen(character);
        } catch (Exception e) {
            System.out.println("Error during portal handling: " + e.getMessage());
        }
    }

    public void handleBattle(Character player) {
        Scanner scanner = new Scanner(System.in);

        // reinitializez abilitatile player ului inainte de batalie
        player.getAbilities().clear();
        player.generateAbilities();

        // inamic random
        Random random = new Random();
        int enemyHealth = random.nextInt(30) + 5; // health intre 5-35
        int enemyMana = random.nextInt(30) + 5;  // mana intre 5-35
        Enemy enemy = new Enemy("Enemy", enemyHealth, enemyMana);

        System.out.println("\nBattle Start!");
        System.out.println("Player Stats: " + player);
        System.out.println("Enemy Stats: " + enemy);

        // battle loop
        while (player.getCurrentHealth() > 0 && enemy.getCurrentHealth() > 0) {
            try {
                boolean playerTurnCompleted = false;

                // player's turn
                while (!playerTurnCompleted) {
                    System.out.println("\nYour turn! Choose an action:");
                    System.out.println("1. Basic Attack");
                    if (!player.getAbilities().isEmpty()) {
                        System.out.println("2. Use Ability");
                    }
                    System.out.print("Choice: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1 -> {
                            System.out.println("\nYou used a basic attack!");
                            player.basicAttack(enemy);
                            playerTurnCompleted = true;
                        }
                        case 2 -> {
                            if (player.getAbilities().isEmpty()) {
                                System.out.println("You have no abilities left! Returning to action selection.");
                                break;
                            }

                            boolean abilitySelected = false;
                            while (!abilitySelected) {
                                System.out.println("\nAbilities:");
                                for (int i = 0; i < player.getAbilities().size(); i++) {
                                    System.out.println((i + 1) + ". " + player.getAbilities().get(i));
                                }
                                System.out.print("Choose an ability (or 0 to cancel): ");
                                int abilityChoice = scanner.nextInt();

                                if (abilityChoice == 0) {
                                    System.out.println("Cancelled ability use. Returning to action selection.");
                                    break;
                                }

                                if (abilityChoice > 0 && abilityChoice <= player.getAbilities().size()) {
                                    Spell chosenAbility = player.getAbilities().get(abilityChoice - 1);
                                    player.useAbility(chosenAbility, enemy);
                                    player.getAbilities().remove(abilityChoice - 1);
                                    abilitySelected = true;
                                    playerTurnCompleted = true;
                                } else {
                                    System.out.println("Invalid option. Please try again!");
                                }
                            }
                        }
                        default -> System.out.println("Invalid option. Please try again!");
                    }
                }

                // verific daca a murit inamicul
                if (enemy.getCurrentHealth() <= 0) {
                    System.out.println("\nYou defeated the enemy!");
                    player.regenLife(player.getMaxHealth() / 2); // regenerez jumatate din viata
                    player.resetMana(); // resetez mana
                    int expGained = random.nextInt(10) + 10; // random xp
                    player.gainExperience(expGained);
                    System.out.println("You gained " + expGained + " experience points!");
                    player.increaseEnemiesDefeated();
                    player.levelUp();
                    return;
                }

                // enemy's turn
                System.out.println("\nEnemy's turn!");
                boolean abilityUsed = false;

                while (!enemy.getAbilities().isEmpty()) {
                    Spell randomAbility = enemy.getAbilities().get(random.nextInt(enemy.getAbilities().size()));

                    if (enemy.getCurrentMana() >= randomAbility.getManaCost()) {
                        enemy.useAbility(randomAbility, player);
                        enemy.getAbilities().remove(randomAbility);
                        abilityUsed = true;
                        break;
                    } else {
                        enemy.getAbilities().remove(randomAbility); // scot abilitatile daca nu mai are mana destula
                    }
                }

                if (!abilityUsed) {
                    // daca nu mai are abilitati sau mana pt ele
                    enemy.basicAttack(player);
                }

                // verifica daca a murit player ul
                if (player.getCurrentHealth() <= 0) {
                    System.out.println("\nYou died! Cam tristutz ngl:)");
                    displayEndScreen(character);
                    return; // inapoi la login)
                }

                // stats dupa fiecare tura
                System.out.println("\n===== Updated Stats =====");
                System.out.println("Player Stats: " + player);
                System.out.println("Enemy Stats: " + enemy);
            } catch (Exception e) {
                System.out.println("\nInvalid input detected. Please try again!");
                scanner.nextLine();
            }
        }
    }

    private void displayEndScreen(Character character) {
        System.out.println("\n===== End Screen =====");
        System.out.println("Character Name: " + character.getName());
        System.out.println("Level: " + character.getLevel());
        System.out.println("Experience: " + character.getExperience());
        System.out.println("Enemies Defeated: " + character.getEnemiesDefeated());
        System.out.println("\nGG MAH G G :)");
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return columns;
    }

    public Cell getCell(int i, int j) {
        if (i < 0 || i >= rows || j < 0 || j >= columns) {
            throw new IndexOutOfBoundsException("The given indices are out of bounds: (" + i + ", " + j + ")");
        }
        return terrain.get(i).get(j);
    }

}