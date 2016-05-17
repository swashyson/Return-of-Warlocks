/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlayerGround;

/**
 *
 * @author gul_h_000
 */
public class PlayerMovement {

    private float cursorPosX;
    private float cursorPoxY;
    private float sv;
    private float sh;
    private float currentPosX;
    private float currentPoxY;

    public PlayerMovement(){
    
    }
    public float getCursorPosX() {
        return cursorPosX;
    }

    public void setCursorPosX(float cursorPosX) {
        this.cursorPosX = cursorPosX;
    }

    public float getCursorPoxY() {
        return cursorPoxY;
    }

    public void setCursorPoxY(float cursorPoxY) {
        this.cursorPoxY = cursorPoxY;
    }

    public float getCurrentPosX() {
        return currentPosX;
    }

    public void setCurrentPosX(float currentPosX) {
        this.currentPosX = currentPosX;
    }

    public float getCurrentPoxY() {
        return currentPoxY;
    }

    public void setCurrentPoxY(float currentPoxY) {
        this.currentPoxY = currentPoxY;
    }
}
