import java.util.ArrayList;
import java.util.Random;

public abstract class Character extends Entity {
    private String name;
    private int experience;
    private int level;
    protected int strength;
    protected int charisma;
    protected int dexterity;
    private String profession;
    private Account account;
    private ArrayList<Spell> abilities;
    private int enemiesDefeated;
    private String imagePath;

    public Character(String name, String profession, int experience, int level) {
        this.name = name;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.abilities = new ArrayList<>();
        this.enemiesDefeated = 0;
        initializeStats();
        generateAbilities();
    }

    public String getName() {
        return name;
    }

    public int getExperience(){
        return experience;
    }

    public String getProfession(){
        return profession;
    }

    public void increaseEnemiesDefeated(){
        enemiesDefeated++;
    }

    public int getEnemiesDefeated(){
        return enemiesDefeated;
    }

    public void gainExperience(int experienceGained) {
        experience += experienceGained;
    }

    public ArrayList<Spell> getAbilities() {
        return abilities;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public int getLevel() {
        return level;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getStrenght(){
        return strength;
    }

    public int getDexterity(){
        return dexterity;
    }

    public int getCharisma(){
        return charisma;
    }

    private void initializeStats() {
        switch (profession.toLowerCase()) {
            case "warrior":
                strength = 10;
                charisma = 5;
                dexterity = 5;
                resistFire = true;
                break;
            case "mage":
                strength = 5;
                charisma = 10;
                dexterity = 5;
                resistIce = true;
                break;
            case "rogue":
                strength = 5;
                charisma = 5;
                dexterity = 10;
                resistEarth = true;
                break;
            default:
                throw new IllegalArgumentException("Unknown profession: " + profession);
        }
        maxHealth = 50;
        maxMana = 30;
        currentHealth = maxHealth;
        currentMana = maxMana;
    }

    public void generateAbilities() {
        Random random = new Random();
        abilities.clear();

        // cel putin un spell din fiecare tip
        String[] baseTypes = {"Fire", "Ice", "Earth"};
        for (String type : baseTypes) {
            abilities.add(Spell.generateSpell(maxMana, type));
        }

        // adaug intre 1-3 spell uri random
        int additionalSpells = random.nextInt(3) + 1;
        String[] allTypes = {"Fire", "Ice", "Earth"};

        for (int i = 0; i < additionalSpells; i++) {
            String randomType = allTypes[random.nextInt(allTypes.length)];
            abilities.add(Spell.generateSpell(maxMana, randomType));
        }
    }

    public boolean canUseAbility(){
        return !abilities.isEmpty();
    }

    public void basicAttack(Entity target) {
        int damage;
        switch (profession.toLowerCase()) {
            case "warrior":
                damage = strength;
                break;
            case "mage":
                damage = charisma;
                break;
            case "rogue":
                damage = dexterity;
                break;
            default:
                damage = 5; // fallback
        }

        System.out.println(name + " chose a basic attack for " + damage + " damage!");

        if (target.dodge()) {
            System.out.println("But " + target.getClass().getSimpleName() + " evaded the attack!");
            return;
        }

        target.receiveDamage(damage, "Physical");
    }

    @Override
    public void useAbility(Spell ability, Entity target) {

        System.out.println("\nYou used the ability: " + ability.getType());

        if (currentMana < ability.getManaCost()) {
            System.out.println(getName() + " doesn't have enough mana to use " + ability.getType() + "!");
            return;
        }

        currentMana -= ability.getManaCost();
        int damage = ability.getDamage();

        System.out.println(getName() + " chose " + ability.getType() + " Spell which has " + damage + " damage!");

        if (criticalHit()) {
            damage *= 2;
            System.out.println(getName() + " landed a critical hit!");
        }

        target.receiveDamage(damage, ability.getType());
    }


    @Override
    public String toString() {
        return "Character Name: " + name +
                ", Profession: " + profession +
                ", Experience: " + experience +
                ", Level: " + level +
                ", Health: " + currentHealth + "/" + maxHealth +
                ", Mana: " + currentMana + "/" + maxMana +
                ", Strength: " + strength +
                ", Charisma: " + charisma +
                ", Dexterity: " + dexterity +
                ", Enemies defeated: " + enemiesDefeated;
    }

    public int experienceToNextLevel() {
        return 50 + (level - 1) * 25;
    }

    public void levelUp() {
        while (experience >= experienceToNextLevel()) {
            int xpRequired = experienceToNextLevel();
            experience -= xpRequired;
            level++;
            //System.out.println(name + " leveled up to " + level + "!");
            improveStats();
        }
    }

    private void improveStats() {
        Random random = new Random();
        switch (profession.toLowerCase()) {
            case "warrior":
                strength += random.nextInt(9) + 2;
                charisma += random.nextInt(5) + 2;
                dexterity += random.nextInt(5) + 2;
                break;
            case "mage":
                strength += random.nextInt(5) + 2;
                charisma += random.nextInt(9) + 2;
                dexterity += random.nextInt(5) + 2;
                break;
            case "rogue":
                strength += random.nextInt(5) + 2;
                charisma += random.nextInt(5) + 2;
                dexterity += random.nextInt(9) + 2;
                break;
        }
        maxHealth += random.nextInt(10) + 5;
        maxMana += random.nextInt(10) + 5;
        regenLife(maxHealth);
        regenMana(maxMana);
    }


    public void lvlUpUndercover(Character character) {
        // obtine nivelul curent al caracterului
        int currentLevel = character.getLevel();

        // seteaza stats la nivelul 1
        character.initializeStats();

        // aplica upgrade uri pana la nivelul corect
        for (int level = 2; level <= currentLevel; level++) {
            character.improveStats();
        }
    }

    public void resetAbilities() {
        abilities.clear();
        generateAbilities(); // Assuming defaultAbilities stores the original list
    }

    public abstract void accept(Visitor<Entity> visitor);

    public abstract void receiveDamage(int damage, String damageType);

    public String getImagePath() {
        return imagePath;
    }

    public int getMana() {
        return currentMana;
    }



    public boolean receiveDamages(int damage, String damageType) {
        if (isResistant(damageType)) {
            return true; // No damage taken; character is alive
        }

        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0; // Ensure health doesn't go negative
            return false; // Character is dead
        }

        return true; // Character is still alive
    }
}

class Warrior extends Character {
    public Warrior(String name, int experience, int level) {
        super(name, "warrior", experience, level);
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    @Override
    public void receiveDamage(int damage, String damageType) {
        if ("Fire".equalsIgnoreCase(damageType)) {
            //System.out.println(getName() + " is immune to Fire damage!");
            return;
        }

        if (dodge()) {
            //System.out.println(getName() + " evaded the attack!");
            return;
        }


        currentHealth = Math.max(currentHealth - damage, 0);
        //System.out.println(getName() + " received " + damage + " damage!");
    }
}

class Rogue extends Character {
    public Rogue(String name, int experience, int level) {
        super(name, "rogue", experience, level);
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    @Override
    public void receiveDamage(int damage, String damageType) {
        if ("Earth".equalsIgnoreCase(damageType)) {
            //System.out.println(getName() + " is immune to Earth damage!");
            return;
        }

        if (dodge()) {
            //System.out.println(getName() + " evaded the attack!");
            return;
        }

        currentHealth = Math.max(currentHealth - damage, 0);
       // System.out.println(getName() + " received " + damage + " damage!");
    }
}

class Mage extends Character {
    public Mage(String name, int experience, int level) {
        super(name, "mage", experience, level);
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    @Override
    public void receiveDamage(int damage, String damageType) {
        if ("Ice".equalsIgnoreCase(damageType)) {
            //System.out.println("But " + getName() + " is immune to Ice damage!");
            return;
        }

        if (dodge()) {
            //System.out.println("But " + getName() + " evaded the attack!");
            return;
        }

        currentHealth = Math.max(currentHealth - damage, 0);
        //System.out.println(getName() + " received " + damage + " damage!");
    }
}
