package com.example.textrpg.save;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveSystem {
    
    private static final Path SAVE_FILE_PATH = Paths.get("save_data", "savefile.txt");
    private static Map<String, String> saveMap = null;
    public static final String HERO_HEALTH = "heroHealth";
    public static final String HERO_ATTACK = "heroAttack";
    public static final String HERO_DEFENSE = "heroDefense";
    public static final String LATEST_SAVED_DATE = "latestSavedDate";
    public static final String FARM_CROPS = "farmCrops";

    public static Map<String, String> getSaveMap() {
        if (saveMap == null) {
            saveMap = new HashMap<>();
            try {
                loadSave();
            } catch (IOException e) {
                System.out.println("Failed to load save file.");
            }
        }
        return saveMap;
    }

    public static void loadSave() throws IOException {
        System.out.println("Looking for file at: " + SAVE_FILE_PATH.toAbsolutePath());
        if (!Files.exists(SAVE_FILE_PATH)) {
            System.out.println("No save file found.");
            return;
        }
    
        List<String> lines = Files.readAllLines(SAVE_FILE_PATH);
        for (String line : lines) {
            String[] splitArr = line.split("\\|");
            if (splitArr.length == 2) {
                saveMap.put(splitArr[0], splitArr[1]);
            }
        }
    }
    public static void saveFile() {
        try {
            // Ensure directory exists
            Files.createDirectories(SAVE_FILE_PATH.getParent());
    
            List<String> lines = new ArrayList<>();
            for (Map.Entry<String, String> entry : saveMap.entrySet()) {
                lines.add(entry.getKey() + "|" + entry.getValue());
            }
    
            Files.write(SAVE_FILE_PATH, lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.out.println("Failed to save file.");
            e.printStackTrace();
        }
    }

}
