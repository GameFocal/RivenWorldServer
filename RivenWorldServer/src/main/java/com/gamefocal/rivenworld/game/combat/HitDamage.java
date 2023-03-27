package com.gamefocal.rivenworld.game.combat;

public abstract class HitDamage<F, T> {
    protected F a;
    protected T b;
    protected float damage;

    public HitDamage(F a, T b, float damage) {
        this.a = a;
        this.b = b;
        this.damage = damage;
    }

    public F getA() {
        return a;
    }

    public T getB() {
        return b;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
