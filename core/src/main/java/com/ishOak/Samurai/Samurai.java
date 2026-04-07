package com.ishOak.Samurai;

import com.badlogic.gdx.math.Rectangle;

public class Samurai {
    public float x, y;
    private float speed;
    private float width, height;

    private boolean facingLeft = false;

    private Rectangle hitBox;
    private Rectangle attackHitBox;

    public Samurai(float x, float y){
        this.x = x;
        this.y = y;

    }
}
