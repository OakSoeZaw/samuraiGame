package com.ishOak.Samurai;

import com.badlogic.gdx.math.Rectangle;

public class Samurai {
    public float x, y;
    private float speed;
    private float width, height;

    //combat
    private float health;
    private boolean facingLeft = false;
    private boolean isAttacking;
    private boolean isAlive;

    private boolean canTakeDamage;
    private float attackCoolDown;
    private float attackTimer;
    

    private Rectangle hitBox;
    private Rectangle attackHitBox;

    private Controls controls;

    public Samurai(float x, float y, Controls controls){
        this.x = x;
        this.y = y;
        this.controls = controls;

        this.width = 32;
        this.height = 48;

        this.isAlive = true;
        this.health = 100;
        this.canTakeDamage = true;
        this.facingLeft = false;
    }
}
