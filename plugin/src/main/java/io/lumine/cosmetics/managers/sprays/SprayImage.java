package io.lumine.cosmetics.managers.sprays;

import java.io.File;

import io.lumine.utils.logging.Log;
import io.lumine.utils.maps.MapImage;
import lombok.Data;

@Data
public class SprayImage {

    public static final int MAP_SIZE = 128;
    
    private final SprayManager manager;
    
    private final File file;
    private final String name;
    private final int mapNumber;
    private byte[] pixels;
    
    public SprayImage(SprayManager sprayManager, File file, int mapNumber) {
        this.manager = sprayManager;
        this.file = file;
        this.name = file.getName().split("\\.")[0];
        this.mapNumber = mapNumber;
        Log.info("Loaded image {0}", name);
        MapImage.imageToMapPixels(file, 128).ifPresent(pixels -> {
            this.pixels = pixels;
        });
    }

}
