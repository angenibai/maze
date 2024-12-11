package main;

import java.util.HashMap;
import java.util.Map;

import entity.Entity;
import entity.Player;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    private Map<String, Integer> calculatePosition(Entity entity) {
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entityLeftX + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entityTopY + entity.solidArea.height;

        Map<String, Integer> posiMap = new HashMap<>();
        posiMap.put("leftX", entityLeftX);
        posiMap.put("rightX", entityRightX);
        posiMap.put("topY", entityTopY);
        posiMap.put("bottomY", entityBottomY);

        return posiMap;
    } 

    private int posIdx(int position) {
        return position / gp.tileSize;
    }

    public void checkTile(Entity entity) {
        Map<String, Integer> pos = calculatePosition(entity);

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                // check tiles above
                pos.put("topY", pos.get("topY") - entity.speed);
                tileNum1 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("topY"))];
                tileNum2 = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("topY"))];
                break;

            case "down":
                pos.put("bottomY", pos.get("bottomY") + entity.speed);
                tileNum1 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("bottomY"))];
                tileNum2 = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("bottomY"))];
                break;

            case "left":
                pos.put("leftX", pos.get("leftX") - entity.speed);
                tileNum1 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("topY"))];
                tileNum2 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("bottomY"))];
                break;

            case "right":
                pos.put("rightX", pos.get("rightX") + entity.speed);
                tileNum1 = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("topY"))];
                tileNum2 = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("bottomY"))];
                break;
            
            default:
                tileNum1 = gp.tileM.mapTileNum[entity.x / gp.tileSize][entity.y / gp.tileSize];
                tileNum2 = tileNum1;
                break;
        }
        entity.collisionOn |= gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision;
    }


    public void checkBoundary(Entity entity) {
        // TODO: refactor position calculation
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entityLeftX + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entityTopY + entity.solidArea.height;

        boolean boundaryCollision = false;

        switch (entity.direction) {
            case "up":
                boundaryCollision = entityTopY - entity.speed < 0;
                break;
            case "down":
                boundaryCollision = entityBottomY + entity.speed >= gp.screenHeight;
                break;
            case "left":
                boundaryCollision = entityLeftX - entity.speed < 0;
                break;
            case "right":
                boundaryCollision = entityRightX + entity.speed >= gp.screenWidth;
                break;
        }

        entity.collisionOn |= boundaryCollision;
    }

    public void checkEnd(Player player) {
        // when player is on the end tile
        Map<String, Integer> pos = calculatePosition(player);

        int[] tileNums = new int[4];

        tileNums[0] = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("topY"))];
        tileNums[1] = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("topY"))];
        tileNums[2] = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("bottomY"))];
        tileNums[3] = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("bottomY"))];

        // TODO: check whether any of these tiles are end tiles
    }
}
