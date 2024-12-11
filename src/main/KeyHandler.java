package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_P:
                // pause
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
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W:
                upPressed = false;
                // break;
            case KeyEvent.VK_S:
                downPressed = false;
                // break;
            case KeyEvent.VK_A:
                leftPressed = false;
                // break;
            case KeyEvent.VK_D:
                rightPressed = false;
                // break;
        }
    }
    
}
