package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public Set<Integer> pressedMoveKeys = new HashSet<>();
    Set<Integer> validMoveKeys = new HashSet<>();

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

        if (code == KeyEvent.VK_P) {
            switch (gp.gameState) {
                case PLAY:
                    gp.gameState = GameState.PAUSE;
                    break;
                case PAUSE:
                    gp.gameState = GameState.PLAY;
                    break;
                default:
                    break;
            }
        } else if (code == KeyEvent.VK_R) {
            switch (gp.gameState) {
                case END:
                    gp.reset();
                    gp.gameState = GameState.PLAY;
                    break;
                default:
                    break;
            }
        } else if (validMoveKeys.contains(code)) {
            pressedMoveKeys.add(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        pressedMoveKeys.remove(code);
    }
    
}
