package entity;

import item.VisionItem;
import main.GamePanel;
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
        loadMeta("/maps/map02_meta.yaml");
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
        // do i need to reset the player?
    }

    public void setupPlayer() {
        gp.player.startX = idxToCoord(entitiesData.p1Start.col);
        gp.player.startY = idxToCoord(entitiesData.p1Start.row);
        // where the actual player goes is handled by Player
    }

    private int idxToCoord(int idx) {
        return idx * gp.tileSize;
    }

    public void setupItems() {
        for (MapMetadata.Item itemData : entitiesData.items) {
            Entity newItem = null;
            if (itemData.type.equals("vision")) {
                newItem = new VisionItem(gp);
            }
            if (newItem != null) {
                newItem.startX = idxToCoord(itemData.pos.col);
                newItem.startY = idxToCoord(itemData.pos.row);
                newItem.setup();
                gp.items.add(newItem);
            }
        }
    }
}
