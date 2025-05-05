package entity;

import item.VisionItem;
import main.GamePanel;
import tile.Coord;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class EntityManager {
    /**
     * Manages the information about entities from the metadata json file
     */

    GamePanel gp;
    MapMetadata entitiesData;

    public EntityManager(GamePanel gp) {
        this.gp = gp;
//        loadMeta("/maps/map02_meta.yaml");
    }

    public void loadMeta(String filePath) {
        try {
            Yaml yaml = new Yaml(new Constructor(MapMetadata.class, new LoaderOptions()));
            InputStream is = getClass().getResourceAsStream(filePath);
            entitiesData = yaml.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        setupPlayer();
        setupItems();
    }

    public void reset() {
        setupItems();
        setupPlayer();
        // do i need to reset the player?
    }

    public void setupPlayer() {
        Coord p1Start = gp.mazeManager.p1Start;
        Coord p2Start = gp.mazeManager.p2Start;

        gp.player1.startX = idxToCoord(p1Start.c);
        gp.player1.startY = idxToCoord(p1Start.r);
        gp.player2.startX = idxToCoord(p2Start.c);
        gp.player2.startY = idxToCoord(p2Start.r);
        // where the actual player goes is handled by Player
    }

    /**
     * Terrible naming
     * Converts position as in index on grid to point coordinate on screen
     */
    private int idxToCoord(int idx) {
        return idx * gp.tileSize;
    }

    /** Terrible naming
     * Converting the coords from the generated maze to the coords
     * used for the actual map
     */
    private Coord mazeCoordToMapCoord(Coord mazeCoord) {
        return new Coord(mazeCoord.r * 2 + 1, mazeCoord.c * 2 + 1);
    }

    public void setupItems() {
        gp.items.clear();
        for (MapMetadata.Item itemData : gp.mazeManager.items) {
            Entity newItem = null;
            if (itemData.type.equals("vision")) {
                newItem = new VisionItem(gp);
            }
            if (newItem != null) {
                newItem.startX = idxToCoord(itemData.pos.c);
                newItem.startY = idxToCoord(itemData.pos.r);
                newItem.setup();
                gp.items.add(newItem);
            }
        }
    }
}
