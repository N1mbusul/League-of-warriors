import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity {
    private String type;
    private ArrayList<Spell> abilities;

    public Enemy(String type, int health, int mana) {
        this.type = type;
        this.maxHealth = health;
        this.maxMana = mana;
        this.currentHealth = health;
        this.currentMana = mana;
        this.abilities = new ArrayList<>();
        generateAbilities();

        Random random = new Random();
        resistFire = random.nextBoolean();
        resistIce = random.nextBoolean();
        resistEarth = random.nextBoolean();
    }

    public ArrayList<Spell> getAbilities() {
        return abilities;
    }

    public String getName(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    private void generateAbilities() {
        Random random = new Random();
        abilities.clear();

        // cel putin un spell din fiecare tip
        String[] baseTypes = {"Fire", "Ice", "Earth"};
        for (String type : baseTypes) {
            abilities.add(Spell.generateSpell(maxMana, type));
        }

        // add 1-3 spell uri random
        int additionalSpells = random.nextInt(3) + 1;
        String[] allTypes = {"Fire", "Ice", "Earth"};

        for (int i = 0; i < additionalSpells; i++) {
            String randomType = allTypes[random.nextInt(allTypes.length)];
            abilities.add(Spell.generateSpell(maxMana, randomType));
        }
    }

    public void basicAttack(Entity target) {
        Random random = new Random();
        int damage = random.nextInt(50) + 10;

        //System.out.println(type + " used a basic attack for " + damage + " damage!");

        if (criticalHit()) {
            damage *= 2;
            //System.out.println(type + " landed a critical hit!");
        }

        if (target.dodge()) {
            //System.out.println("But " + target.getClass().getSimpleName() + " evaded the attack!");
            return;
        }

        target.receiveDamage(damage, "Physical");
    }

    @Override
    public void useAbility(Spell ability, Entity target) {

        //System.out.println("The enemy chose the ability: " + ability.getType());

        if (currentMana < ability.getManaCost()) {
            basicAttack(target);
            return;
        }

        currentMana -= ability.getManaCost();
        int damage = ability.getDamage();

        //System.out.println(type + " used " + ability.getType() + " Spell");

        if (criticalHit()) {
            damage *= 2;
            //System.out.println(type + " landed a critical hit!");
        }

        target.receiveDamage(damage, ability.getType());

    }

    @Override
    public void receiveDamage(int damage, String damageType) {

        if (isResistant(damageType)) {
            //System.out.println("But " + type + " is imune to " + damageType + "!");
            return;
        }

        if (dodge()) {
            //System.out.println("But " + type + " evaded the attack!");
            return;
        }

        currentHealth = Math.max(currentHealth - damage, 0);
       // System.out.println(type + " received " + damage + " damage!");
    }

    // Lista cu nume posibile inamici
    private static final String[] NAMES = {
            "Goblin", "Skeleton", "Zombie", "Troll", "Bandit", "Wraith", "Vampire", "Demon"
    };

    // functie statica pt ales nume random inamic
    public String generateRandomName() {
        Random random = new Random();
        return NAMES[random.nextInt(NAMES.length)];
    }

    @Override
    public String toString() {
        return generateRandomName() +
                ", Health: " + currentHealth + "/" + maxHealth +
                ", Mana: " + currentMana + "/" + maxMana ;
    }

    public boolean receiveDamages(int damage, String damageType) {
        if (isResistant(damageType)) {
            return true; // No damage taken; enemy is alive
        }

        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0; // Ensure health doesn't go negative
            return false; // Enemy is dead
        }

        return true; // Enemy is still alive
    }

    public void resetAbilities(){
        abilities.clear();
        generateAbilities();
    }

}