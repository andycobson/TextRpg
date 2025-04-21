package com.example.textrpg.farm;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.textrpg.save.SaveSystem;

public class FarmManager {

    private int usableFarmSpots = 3;
    private int openFarmSpots;
    private List<Crop> cropsInFarm;

    public FarmManager() {
        loadCropsFromFile();
        updateOpenFarmSpots();
    }

    public void updateCropChecks() {
        for (Crop crop : cropsInFarm) {
            crop.updateProperties();
        }
    }

    public void waterCrop(int index) {
        Crop cropToBeWatered = cropsInFarm.get(index);
        if (cropToBeWatered.isExpired()) {
            System.out.println("Crop is expired, cannot water.");
            return;
        } else if (cropToBeWatered.isWateredInStage() && !cropToBeWatered.isHarvestable()) {
            System.out.println("Crop doesn't need to be watered. It has already been watered!");
            return;
        } else if (cropToBeWatered.isHarvestable()) {
            System.out.println("Crop doesn't need to be watered. Crop is harvestable.");
            return;
        }
        cropToBeWatered.setTimeLastWatered(Instant.now());
        cropToBeWatered.setWatered(true);
        System.out.println("Crop has been watered.");
    }

    public void plantNewCrop(final CropType cropType) {
        final Crop newCrop = new Crop(Instant.now(), null, 1, false, cropType, false);
        this.cropsInFarm.add(newCrop);
    }

    private void loadCropsFromFile() {
        final Map<String, String> saveMap = SaveSystem.getSaveMap();
        final String cropsString = saveMap.get(SaveSystem.FARM_CROPS);
        if (cropsString == null || cropsString.isBlank() || cropsString.isEmpty()) {
            System.out.println("No crops found in save file.");
            cropsInFarm = new ArrayList<>();
            return;
        }
        cropsInFarm = new ArrayList<>(parseCropsFromString(cropsString));
    }

    public void saveCropsToFile() {
        final Map<String, String> saveMap = SaveSystem.getSaveMap();
        final String cropsString = createStringFromCrops();
        saveMap.put(SaveSystem.FARM_CROPS, cropsString);
    }

    private void updateOpenFarmSpots() {
        this.openFarmSpots = usableFarmSpots - cropsInFarm.size();
    }

    public int getOpenFarmSpots() {
        return openFarmSpots;
    }

    private List<Crop> parseCropsFromString(String cropString) {
        final List<Crop> cropList = new ArrayList<>();
        final String[] individualCropStrings = cropString.split(";");
        if (individualCropStrings.length == 0) return cropList;
        for (String cropStr : individualCropStrings) {
            final String[] cropAttributes = cropStr.split("&");
            if (cropAttributes.length < 6) break;
            final String instantString = cropAttributes[0];
            final String lastTimeString = cropAttributes[1];
            final String growthStageString = cropAttributes[2];
            final String wateredString = cropAttributes[3];
            final String cropType = cropAttributes[4];
            final String expired = cropAttributes[5];
            cropList.add(new Crop(instantString, lastTimeString, growthStageString, wateredString, cropType, expired));
        }
        return cropList;
    }

    private String createStringFromCrops() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cropsInFarm.size(); i++) {
            sb.append(cropsInFarm.get(i).cropExportString());
            if (i != cropsInFarm.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

}
