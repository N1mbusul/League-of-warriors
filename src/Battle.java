public interface Battle {
    public void receiveDamage(int dmg,String damageType);
    public int getDamage();
    void useAbility(Spell ability, Entity target);
}
