import java.util.Random;

public abstract class Spell implements Visitor<Entity>{
    protected int damage;
    protected int manaCost;
    protected String type;

    public Spell(int damage, int manaCost, String type) {
        this.damage = damage;
        this.manaCost = manaCost;
        this.type = type;
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getType() {
        return type;
    }

    @Override
    public void visit(Entity entity) {
        if (entity.isResistant(this.type)) {
            System.out.println(entity.getClass().getSimpleName() + " is resistant to " + this.type + " damage!");
        } else {
            entity.receiveDamage(damage, type);
        }
    }

    @Override
    public String toString() {
        return type + " Spell: Damage = " + damage + ", Mana Cost = " + manaCost;
    }

    // generare spells
    public static Spell generateSpell(int maxMana, String type) {
        Random random = new Random();
        int damage = random.nextInt(15) + maxMana / 10; // damage scaleaza cu maxMana
        int manaCost = random.nextInt(maxMana / 5) + 5; // costul scaleaza cu maxMana
        switch (type) {
            case "Fire":
                return new FireSpell(damage, manaCost);
            case "Ice":
                return new IceSpell(damage, manaCost);
            case "Earth":
                return new EarthSpell(damage, manaCost);
            default:
                throw new IllegalArgumentException("Invalid spell type: " + type);
        }
    }

}

class FireSpell extends Spell {
    public FireSpell(int damage, int manaCost) {
        super(damage, manaCost, "Fire");
    }
}

class IceSpell extends Spell {
    public IceSpell(int damage, int manaCost) {
        super(damage, manaCost, "Ice");
    }
}

class EarthSpell extends Spell {
    public EarthSpell(int damage, int manaCost) {
        super(damage, manaCost, "Earth");
    }
}