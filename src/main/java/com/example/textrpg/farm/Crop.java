package com.example.textrpg.farm;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Crop {
    
    private Instant timeInStage;
    private Instant timeLastWatered;
    private int growthStage;
    private boolean wateredInStage;
    private CropType cropType;
    private boolean expired;
    private static Map<CropType, Map<Integer, Integer>> growthTimeMap;
    private static Map<CropType, Integer> expirationTimeMap;

    public Crop(Instant timeInStage, Instant timeLastWatered, int growthStage, boolean wateredInStage, CropType cropType, boolean expired) {
        this.timeInStage = timeInStage;
        this.timeLastWatered = timeLastWatered;
        this.growthStage = growthStage;
        this.wateredInStage = wateredInStage;
        this.cropType = cropType;
        this.expired = expired;
        loadGrowthAndExpirationTimeMap();
    }

    public Crop(String timeInStage, String timeLastWatered, String growthStage, String wateredInStage, String cropType, String expired) {
        this.timeInStage = Instant.parse(timeInStage);
        this.timeLastWatered = "null".equals(timeLastWatered) ? null : Instant.parse(timeLastWatered);
        this.growthStage = Integer.parseInt(growthStage);
        this.wateredInStage = Boolean.valueOf(wateredInStage);
        this.cropType = CropType.valueOfString(cropType);
        this.expired = Boolean.valueOf(expired);
        loadGrowthAndExpirationTimeMap();
    }

    private void loadGrowthAndExpirationTimeMap() {
        growthTimeMap = new HashMap<>();
        Map<Integer, Integer> turnipStageTime = new HashMap<>();
        turnipStageTime.put(1, 5);
        turnipStageTime.put(2, 10);
        turnipStageTime.put(3, 10);
        growthTimeMap.put(CropType.TURNIP, turnipStageTime);

        Map<Integer, Integer> tomatoStageTime = new HashMap<>();
        tomatoStageTime.put(1, 10);
        tomatoStageTime.put(2, 15);
        tomatoStageTime.put(3, 15);
        growthTimeMap.put(CropType.TOMATO, tomatoStageTime);

        expirationTimeMap = new HashMap<>();
        expirationTimeMap.put(CropType.TURNIP, 120);
        expirationTimeMap.put(CropType.TOMATO, 100);
    }

    public void updateProperties() {
        final Instant now = Instant.now();
        final Instant expiredTime = this.getTimeInStage().plusSeconds(expirationTimeMap.get(this.getCropType()) * 60);
        if (expiredTime.isBefore(now) && !this.isWateredInStage()) {
            this.setExpired(true);
            return;
        }
        if (this.isWateredInStage()) {
            final Instant wateredTime = this.getTimeLastWatered().plusSeconds(growthTimeMap.get(this.getCropType()).get(this.getGrowthStage()) * 60);
            if (wateredTime.isBefore(now)) {
                if (this.getGrowthStage() < 4) {
                    setToNextStage();
                }
            }
        }
    }

    public void setToNextStage() {
        this.setGrowthStage(this.getGrowthStage() + 1);
        this.setWatered(false);
        this.setTimeLastWatered(null);
        this.setTimeInStage(Instant.now());
    }

    public Instant getTimeInStage() {
        return timeInStage;
    }

    public Instant getTimeLastWatered() {
        return timeLastWatered;
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public boolean isWateredInStage() {
        return wateredInStage;
    }

    public CropType getCropType() {
        return cropType;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isHarvestable() {
        return growthStage == 4;
    }

    public void setTimeInStage(Instant time) {
        this.timeInStage = time;
    }

    public void setTimeLastWatered(Instant time) {
        this.timeLastWatered = time;
    }

    public void setGrowthStage(int newStage) {
        this.growthStage = newStage;
    }

    public void setWatered(boolean watered) {
        this.wateredInStage = watered;
    }

    public void setExpired(boolean expiredState) {
        this.expired = expiredState;
    }

    public String cropExportString() {
        return String.format("%s&%s&%s&%s&%s&%s",
                this.timeInStage.toString(),
                Objects.isNull(this.timeLastWatered) ? "null" : this.timeLastWatered.toString(),
                String.valueOf(this.growthStage), 
                String.valueOf(wateredInStage),
                cropType.name(),
                String.valueOf(expired)
            );
    }

}
