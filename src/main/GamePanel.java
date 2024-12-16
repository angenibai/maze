package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;

import entity.Player;
import environment.EnvironmentManager;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 px
    public final int screenHeight = tileSize * maxScreenRow; // 576 px

    // time consts
    final int secondInNano = 1000000000;
    final int FPS = 60;

    // objects
    public GameState gameState;
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    UI ui = new UI(this);
    EnvironmentManager eManager = new EnvironmentManager(this);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setup() {
        gameState = GameState.PLAY;
        eManager.setup();
    }

    @Override
    public void run() {
        setup();
        // game loop
        double drawInterval = (double) secondInNano / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // update game info
                update();
    
                // draw game
                repaint();

                delta--;
            }
        }
    }


    public void reset() {
        player.reset();
        ui.reset();
    }


    public void update() {
        player.update();
        eManager.update();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        player.draw(g2);
        eManager.draw(g2);
        ui.draw(g2);

        g2.dispose();
    }
}
