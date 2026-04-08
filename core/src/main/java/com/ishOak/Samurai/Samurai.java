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

        hitBox = new Rectangle(x, y, width, height);
        attackHitBox = new Rectangle();
    }

    public void update(float delta){
        if(!isAlive) return;

        handleMovement(delta);
        handleAttack(delta);
        updateHitBox();

    }

    private void handleMovement(float delta){

    }

    private void handleAttack(float delta){

    }

    private void updateHitBox(){
        hitBox.setPosition(x, y);

        if(facingLeft){
            attackHitBox.set(x - 20, y + 10, 20, 20);
        }else{
            attackHitBox.set(x + width, y + 10, 20, 20);
        }
    }
}
