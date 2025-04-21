package com.example.textrpg.farm;
public enum CropType {
    TURNIP("TURNIP"),
    TOMATO("TOMATO");

    private final String cropType;

    CropType(final String type) {
        this.cropType = type;
    }

    public static CropType valueOfString(String type) {
        for (CropType cropType : values()) {
            if (cropType.cropType.equals(type)) {
                return cropType;
            }
        }
        throw new IllegalArgumentException("No enum constant with type: " + type);
    }
    
}
