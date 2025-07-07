package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UI {
    private static final int MAX_LEADERBOARD = 5;

    GamePanel gp;
    String fontName;
    Font baseFont;
    Font text_32;
    Font text_40;
    Font text_64;
    int rankTextX;
    Double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    Color headingColor = new Color(0xbcd1f5);
    Color highlightColor = new Color(0xFDD179);
    Color textColor = new Color(0xFFFFFF);
    List<Double> leaderboard;
    GameState prevGameState;
    public boolean newHighScore = false;

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            loadFont();
        } catch (Exception e) {
            System.err.println("Error loading font");
        }

        text_32 = baseFont.deriveFont(Font.PLAIN, 32);
        text_40 = baseFont.deriveFont(Font.PLAIN, 40);
        text_64 = baseFont.deriveFont(Font.PLAIN, 64);
        rankTextX = gp.screenWidth / 2 - 130;
        playTime = 0.0;
        leaderboard = new ArrayList<Double>();
        prevGameState = gp.gameState;
    }

    private void loadFont() throws IOException, FontFormatException {
        InputStream fontStream = getClass().getResourceAsStream("/font/Silkscreen.ttf");
        if (fontStream == null) {
            System.out.println("Did not load font");
            baseFont = Font.getFont("Arial");
        } else {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        }
    }

    public void draw(Graphics2D g2) {
        g2.setFont(text_40);
        g2.setColor(textColor);

        switch (gp.gameState) {
            case PLAY:
                drawPlayState(g2);
                break;
            case PAUSE:
                drawPauseState(g2);
                break;
            case END:
                if (prevGameState.equals(GameState.PLAY)) {
                    leaderboard = newLeaderboard(playTime);
                }
                drawEndState(g2);
                break;
            default:
                break;
        }
        prevGameState = gp.gameState;
    }

    public void reset() {
        playTime = 0.0;
    }

    public void drawPlayState(Graphics2D g2) {
        playTime += (double) 1/gp.FPS;

        g2.drawString("Time: "+dFormat.format(playTime), gp.tileSize*(gp.maxScreenCol-6), 65);
    }

    public void drawPauseState(Graphics2D g2) {
        g2.setFont(text_64);

        String text = "PAUSED";
        int x = getCentreX(g2, text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public void drawEndState(Graphics2D g2) {
        g2.setFont(text_64);
        g2.setColor(headingColor);
        String text = "COMPLETE";
        int x = getCentreX(g2, text);
        int y = 120;
        g2.drawString(text, x, y);

        g2.setFont(text_40);
        boolean newScoreDrawn = false;
        newHighScore = false;
        for (int i = 0; i < MAX_LEADERBOARD; i++) {
            g2.setColor(textColor);
            y = 180 + i * 56;

            // score if it exists
            if (i < leaderboard.size()) {
                text = String.format("%7.2fs", leaderboard.get(i));
                x = getScoreX(g2, text);

                if (leaderboard.get(i).equals(playTime)) {
                    newScoreDrawn = true;
                    g2.setColor(highlightColor);
                    if (i == 0) {
                        newHighScore = true;
                    }
                }

                g2.drawString(text, x, y);
            }
            // rank
            text = String.format("%d.", i+1);
            x = rankTextX;
            g2.drawString(text, x, y);
        }

        // find the rank for the new score
        if (!newScoreDrawn) {
            for (int i = MAX_LEADERBOARD; i < leaderboard.size(); i++) {
                if (leaderboard.get(i).equals(playTime)) {
                    g2.setColor(highlightColor);

                    y = 180 + 4 * 56 + 36;
                    text = "-------";
                    x = getCentreX(g2, text);
                    g2.drawString(text, x, y);

                    y += 40;
                    x = rankTextX;
                    text = String.format("%d.", i+1);
                    g2.drawString(text, x, y);

                    text = String.format("%7.2fs", leaderboard.get(i));
                    x = getScoreX(g2, text);
                    g2.drawString(text, x, y);
                }
            }
        }

        g2.setFont(text_32);
        g2.setColor(textColor);
        text = "<n>ext maze";
        x = gp.screenWidth / 2 + 110;
        y = 530;
        g2.drawString(text, x, y);
    }

    private int getCentreX(Graphics2D g2, String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    private int getScoreX(Graphics2D g2, String text) {
        int rightTarget = gp.screenWidth / 2 + 120;
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return rightTarget - length;
    }

    private List<Double> newLeaderboard(Double newTime) {
        List<Double> newLeaderboard = new ArrayList<>(this.leaderboard);
        newLeaderboard.add(newTime);
        newLeaderboard.sort(Double::compareTo);
        return newLeaderboard;
    }
}
