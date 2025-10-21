import java.util.Random;

public abstract class Entity implements Battle, Element<Entity> {
    protected int currentHealth, maxHealth;
    protected int currentMana, maxMana;
    protected boolean resistFire, resistIce, resistEarth;

    public void regenLife(int lifeRegen) {
        this.currentHealth = Math.min(this.currentHealth + lifeRegen, this.maxHealth);
    }

    public void regenMana(int manaRegen) {
        this.currentMana = Math.min(this.currentMana + manaRegen, this.maxMana);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void resetMana(){
        currentMana = maxMana;
    }

    public void resetLifeAndMana() {
        this.currentHealth = maxHealth;
        this.currentMana = maxMana;
    }

    public boolean isResistant(String type) {
        return (type.equals("Fire") && resistFire) ||
                (type.equals("Ice") && resistIce) ||
                (type.equals("Earth") && resistEarth);
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    @Override
    public void useAbility(Spell ability, Entity target) {
        if (currentMana < ability.getManaCost()) {
            System.out.println("Not enough mana to use " + ability.getType() + "!");
            return;
        }

        currentMana -= ability.getManaCost();
        int damage = ability.getDamage();

        if (target.isResistant(ability.getType())) {
            damage = 0;
        }

        target.receiveDamage(damage,ability.getType());
        System.out.println(getClass().getSimpleName() + " used " + ability +
                " for " + damage + " damage!");

        ability.visit(target);
    }

    @Override
    public void receiveDamage(int damage, String damageType) {
        currentHealth = Math.max(currentHealth - damage, 0);
        System.out.println(getClass().getSimpleName() + " received " + damage + " damage!");
    }

    @Override
    public int getDamage() {
        Random random = new Random();
        int baseDamage = new Random().nextInt(10) + 10;
        if (random.nextDouble() <= 0.5) {
            baseDamage *= 2; // Double damage
            System.out.println(getClass().getSimpleName() + " landed a critical hit!");
        }
        return baseDamage;
    }

    public boolean dodge() {
        return new Random().nextDouble() <= 0.5;
    }

    public boolean criticalHit() {
        return new Random().nextDouble() <= 0.5;
    }

}