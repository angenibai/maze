package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import entity.Entity;
import entity.EntityManager;
import entity.Player;
import environment.EnvironmentManager;
import tile.MazeAlgorithmOption;
import tile.MazeManager;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 17;
    public final int maxScreenRow = 13;
    public final int screenWidth = tileSize * maxScreenCol; // 768 px
    public final int screenHeight = tileSize * maxScreenRow; // 576 px

    // time consts
    final int secondInNano = 1000000000;
    public final int FPS = 60;
    int count = 0;
    long averageTime = 0;

    // objects
    public GameState gameState;
    public MazeManager mazeManager = new MazeManager(maxScreenRow, maxScreenCol, MazeAlgorithmOption.GROWING_TREE);
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    SoundPlayer soundFx = new SoundPlayer();

    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);

    public Player player1 = new Player(this, 1, keyH);
    public Player player2 = new Player(this, 2, keyH);
    public List<Entity> items = new ArrayList<>();

    UI ui = new UI(this);
    public EnvironmentManager envManager = new EnvironmentManager(this);
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
        mazeManager.setup();
        tileM.setup();
        envManager.setup();
        entManager.setup();
        player1.setup();
        player2.setup();
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
        mazeManager.reset();
        tileM.reset();
        entManager.reset();
        envManager.reset();
        player1.reset();
        player2.reset();
        ui.reset();
    }

    public void update() {
        player1.update();
        player2.update();
        envManager.update();
        cChecker.checkEnd(player1, player2);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        long drawStart = System.nanoTime();

        tileM.draw(g2);

        List<Entity> itemsToDraw;
        synchronized (this) {
            itemsToDraw = new ArrayList<>(items);
        }
        for (Entity item : itemsToDraw) {
            item.draw(g2);
        }

        player1.draw(g2);
        player2.draw(g2);
        envManager.draw(g2);
        ui.draw(g2);

        // DEBUG
//       long drawEnd = System.nanoTime();
//       long passed = drawEnd - drawStart;
//       if (count > 0) {
//           averageTime = (passed + averageTime * (count-1)) / count;
//           System.out.println("Average draw time " + averageTime);
//       } else {
//           System.out.println("Initial draw time " + passed);
//       }
//        count++;

        g2.dispose();
    }

    public void playFx(Sound sound) {
        soundFx.setFile(sound);
        soundFx.play();
    }
}
