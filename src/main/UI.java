package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Font arial_40;
    Font arial_80;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80 = new Font("Arial", Font.PLAIN, 80);
        playTime = 0.0;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);

        switch (gp.gameState) {
            case PLAY:
                drawPlayState(g2);
                break;
            case PAUSE:
                drawPauseState(g2);
                break;
            case END:
                drawEndState(g2);
                break;
            default:
                break;
        }
    }

    public void drawPlayState(Graphics2D g2) {
        playTime += (double) 1/gp.FPS;

        g2.drawString("Time: "+dFormat.format(playTime), gp.tileSize*(gp.maxScreenCol-5), 65);
    }

    public void drawPauseState(Graphics2D g2) {
        g2.setFont(arial_80);

        String text = "PAUSED";
        int x = getCentreX(g2, text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public void drawEndState(Graphics2D g2) {
        g2.setFont(arial_80);

        String text = "COMPLETE";
        int x = getCentreX(g2, text);
        int y = gp.screenHeight / 2 - 48;
        g2.drawString(text, x, y);

        text = "in: " + dFormat.format(playTime) + "s";
        x = getCentreX(g2, text);
        y = gp.screenHeight / 2 + 48;
        g2.setFont(arial_40);
        g2.drawString(text, x, y);
    }

    private int getCentreX(Graphics2D g2, String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}
