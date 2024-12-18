package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;

import entity.EntityManager;
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
    int count = 0;
    long averageTime = 0;

    // objects
    public GameState gameState;
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    UI ui = new UI(this);
    EnvironmentManager envManager = new EnvironmentManager(this);
    EntityManager entManager = new EntityManager(this);


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
        envManager.setup();
        entManager.setup();
        player.setup();
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
        envManager.update();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        long drawStart = System.nanoTime();

        tileM.draw(g2);
        player.draw(g2);
        envManager.draw(g2);
        ui.draw(g2);

        long drawEnd = System.nanoTime();
        long passed = drawEnd - drawStart;
        if (count > 0) {
            averageTime = (passed + averageTime * (count-1)) / count;
            System.out.println("Average draw time " + averageTime);
        } else {
            System.out.println("Initial draw time " + passed);
        }
        count++;

        g2.dispose();
    }
}
