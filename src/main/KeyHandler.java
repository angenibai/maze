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
        } else {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_S ||
                code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
                    gp.player1.toMove = true;
                    switch (code) {
                        case KeyEvent.VK_W:
                            gp.player1.direction = Direction.UP;
                            break;
                        case KeyEvent.VK_S:
                            gp.player1.direction = Direction.DOWN;
                            break;
                        case KeyEvent.VK_A:
                            gp.player1.direction = Direction.LEFT;
                            break;
                        case KeyEvent.VK_D:
                            gp.player1.direction = Direction.RIGHT;
                            break;
                    }
                }
            
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN ||
                code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
                    gp.player2.toMove = true;
                    switch (code) {
                        case KeyEvent.VK_UP:
                            gp.player2.direction = Direction.UP;
                            break;
                        case KeyEvent.VK_DOWN:
                            gp.player2.direction = Direction.DOWN;
                            break;
                        case KeyEvent.VK_LEFT:
                            gp.player2.direction = Direction.LEFT;
                            break;
                        case KeyEvent.VK_RIGHT:
                            gp.player2.direction = Direction.RIGHT;
                            break;
                    }
                }
        }

        // System.out.println("p1 move " + gp.player1.toMove + " direction " + gp.player1.direction);
        // System.out.println("p2 move " + gp.player2.toMove + " direction " + gp.player2.direction);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_S ||
            code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
                gp.player1.toMove = false;
            }

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN ||
            code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
                gp.player2.toMove = false;
            }
    }
    
}
