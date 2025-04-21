package com.example.textrpg.entity;

public class Entity {
    private int health;
    private int attack;
    private int defense;
    private boolean alive;

    public Entity(int health, int attack, int defense) {
        this.attack = attack;
        this.health = health;
        this.defense = defense;
        this.alive = true;
    }

    public void takeDamange(int otherAttack) {
        int damage = (int) (otherAttack - (this.defense * 0.25));
        this.health = this.health - damage;
        if (this.health <= 0) {
            this.alive = false;
        }
    }

    public int getAttack() {
        return this.attack;
    }

    public int getHealth() {
        return this.health;
    }

    public int getDefense() {
        return this.defense;
    }

    public boolean isAlive() {
        return this.alive;
    }

    @Override
    public String toString() {
        return String.format("{ entityHealth: %s, entityAttack: %s, entityDefense %s, entityIsAlive: %s}", this.health, this.attack, this.defense, this.alive);
    }
 
}
