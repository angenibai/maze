package main;

import java.awt.*;
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
        if (gp.gameState != GameState.PLAY) { return; }
        Map<String, Integer> pos = calculatePosition(entity);

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case UP:
                // check tiles above
                pos.put("topY", pos.get("topY") - entity.speed);
                tileNum1 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("topY"))];
                tileNum2 = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("topY"))];
                break;

            case DOWN:
                pos.put("bottomY", pos.get("bottomY") + entity.speed);
                tileNum1 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("bottomY"))];
                tileNum2 = gp.tileM.mapTileNum[posIdx(pos.get("rightX"))][posIdx(pos.get("bottomY"))];
                break;

            case LEFT:
                pos.put("leftX", pos.get("leftX") - entity.speed);
                tileNum1 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("topY"))];
                tileNum2 = gp.tileM.mapTileNum[posIdx(pos.get("leftX"))][posIdx(pos.get("bottomY"))];
                break;

            case RIGHT:
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
        Map<String, Integer> pos = calculatePosition(entity);

        boolean boundaryCollision = switch (entity.direction) {
            case UP -> pos.get("topY") - entity.speed < 0;
            case DOWN -> pos.get("bottomY") + entity.speed >= gp.screenHeight;
            case LEFT -> pos.get("leftX") - entity.speed < 0;
            case RIGHT -> pos.get("rightX") + entity.speed >= gp.screenWidth;
        };

        entity.collisionOn |= boundaryCollision;
    }

    /**
     * Check if player has collided with any of the items
     * @param player
     * @return the collided item
     */
    public Entity checkItems(Player player) {
        if (gp.gameState != GameState.PLAY) { return null; }

        Entity collided = null;

        for (Entity item : gp.items) {
            Rectangle playerArea = UtilityTool.getAbsoluteArea(player);
            Rectangle itemArea = UtilityTool.getAbsoluteArea(item);

            switch (player.direction) {
                case UP:
                    playerArea.y -= player.speed;
                    break;
                case DOWN:
                    playerArea.y += player.speed;
                    break;
                case LEFT:
                    playerArea.x -= player.speed;
                    break;
                case RIGHT:
                    playerArea.x += player.speed;
                    break;
                default:
                    break;
            }

            if (playerArea.intersects(itemArea)) {
                collided = item;
                break;
            }
        }

        return collided;
    }

    public void checkEnd(Player p1, Player p2) {
        // when the two players collide
        Rectangle p1Area = UtilityTool.getAbsoluteArea(p1);
        Rectangle p2Area = UtilityTool.getAbsoluteArea(p2);

        if (p1Area.intersects(p2Area) && gp.gameState == GameState.PLAY) {
            gp.gameState = GameState.END;
            gp.playFx(Sound.WIN);
        }
    }

}
