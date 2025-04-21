package com.example.textrpg.entity;

import java.util.Map;

import com.example.textrpg.save.SaveSystem;

public class EntityManager {

    private static Entity hero;
    private Entity currentEnemy;

    public static Entity getHero() {
        if (hero == null) {
            final Map<String, String> saveMap = SaveSystem.getSaveMap();
            hero = new Entity(Integer.valueOf(saveMap.getOrDefault(SaveSystem.HERO_HEALTH, "18")), 
                Integer.valueOf(saveMap.getOrDefault(SaveSystem.HERO_ATTACK, "4")), 
                Integer.valueOf(saveMap.getOrDefault(SaveSystem.HERO_DEFENSE, "2")));
        } 
        return hero;
    }

    private static void saveHero() {
        Map<String, String> saveMap = SaveSystem.getSaveMap();
        saveMap.put(SaveSystem.HERO_HEALTH, String.valueOf(hero.getHealth()));
        saveMap.put(SaveSystem.HERO_DEFENSE, String.valueOf(hero.getDefense()));
        saveMap.put(SaveSystem.HERO_ATTACK, String.valueOf(hero.getAttack()));
    }

    
}
