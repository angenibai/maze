package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class KeyHandler implements KeyListener {
    private static final List<Integer> kxg = List.of(KeyEvent.VK_K, KeyEvent.VK_X, KeyEvent.VK_G);

    GamePanel gp;
    public Set<Integer> pressedMoveKeys = new HashSet<>();
    Set<Integer> validMoveKeys = new HashSet<>();
    List<Integer> last3KeysList = new ArrayList<>();

    public KeyHandler(GamePanel gp) {
        this.gp = gp;

        validMoveKeys.add(KeyEvent.VK_W);
        validMoveKeys.add(KeyEvent.VK_S);
        validMoveKeys.add(KeyEvent.VK_A);
        validMoveKeys.add(KeyEvent.VK_D);
        validMoveKeys.add(KeyEvent.VK_UP);
        validMoveKeys.add(KeyEvent.VK_DOWN);
        validMoveKeys.add(KeyEvent.VK_LEFT);
        validMoveKeys.add(KeyEvent.VK_RIGHT);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) {
            switch (gp.gameState) {
                case START -> gp.setupPlay();
                case PLAY -> gp.gameState = GameState.PAUSE;
                case PAUSE -> gp.gameState = GameState.PLAY;
            }
        } else if (code == KeyEvent.VK_N) {
            if (Objects.requireNonNull(gp.gameState) == GameState.END) {
                gp.reset();
                gp.gameState = GameState.PLAY;
            }
        } else if (validMoveKeys.contains(code)) {
            pressedMoveKeys.add(code);
        }

        updateLast3Keys(code);
        if (last3KeysList.equals(kxg)) {
            gp.player2.togglePlayerImage(2, 3);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        pressedMoveKeys.remove(code);
    }

    private void updateLast3Keys(int newKey) {
        last3KeysList.add(newKey);
        if (last3KeysList.size() > 3) {
            last3KeysList.removeFirst();
        }
    }
}
