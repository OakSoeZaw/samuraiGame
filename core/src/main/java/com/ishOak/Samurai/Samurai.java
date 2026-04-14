package com.ishOak.Samurai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Samurai {

    public enum State {
        IDLE, RUNNING, DYING, ATTACKING
    };

    public float x, y;
    private float speed;
    private float width, height;

    // combat
    private float health;
    public boolean facingLeft = false;
    private boolean isAttacking;
    private boolean isAlive;
    private boolean isBlocking;

    private boolean isAttackActive;

    private boolean canTakeDamage;
    private float attackCoolDown;
    private float attackTimer;

    private Rectangle hitBox;
    private Rectangle attackHitBox;

    private State currentState;
    private SamuraiAnimator animator;
    private Controls controls;

    //
    public Samurai(float x, float y, Controls controls, SamuraiAnimator animator) {
        this.x = x;
        this.y = y;
        this.controls = controls;
        this.speed = 200f;

        this.width = 32;
        this.height = 48;

        this.currentState = State.IDLE;
        this.animator = animator;

        this.isAlive = true;
        this.health = 20;
        this.canTakeDamage = true;
        this.facingLeft = false;

        hitBox = new Rectangle(x, y, width, height);
        attackHitBox = new Rectangle();
    }

    public void update(float delta) {
        if (!isAlive) {
            animator.update(delta);
            return;
        }

        handleMovement(delta);
        handleAttack(delta);
        updateHitBox();

    }

    private void handleMovement(float delta) {
        if (!isAlive) {
            return;
        }
        if (isAttacking) {
            return;
        }
        if (Gdx.input.isKeyPressed(controls.left)) {
            moveLeft(delta);
            setCurrentState(State.RUNNING);
        } else if (Gdx.input.isKeyPressed(controls.right)) {
            moveRight(delta);
            setCurrentState(State.RUNNING);

        } else {
            setCurrentState(State.IDLE);
        }
        // maybe jump and down afterwards
        // if jump, need gravity to bring it down
        x = MathUtils.clamp(x, 0, 500 - width);
    }

    private void handleAttack(float delta) {
        if (!isAlive)
            return;
        if (attackCoolDown > 0) {
            attackCoolDown -= delta;
        }

        if (isAttacking) {
            attackTimer += delta;
            setCurrentState(State.ATTACKING);
            isAttackActive = attackTimer >= 0.1f && attackTimer <= 0.3f;

            if (attackTimer >= 0.4f) {
                isAttacking = false;
                isAttackActive = false;
                attackTimer = 0;
                attackCoolDown = 0.5f;
                setCurrentState(State.IDLE);
            }
        }
        if (Gdx.input.isKeyJustPressed(controls.attack) && !isAttacking && attackCoolDown <= 0) {
            attack();
        }
    }

    private void updateHitBox() {
        hitBox.setPosition(x, y);

        if (facingLeft) {
            attackHitBox.set(x - 20, y + 10, 20, 20);
        } else {
            attackHitBox.set(x + width, y + 10, 20, 20);
        }
    }

    public void takeDamage(int damage) {
        if (!isAlive || !canTakeDamage)
            return;

        if (isBlocking)
            damage = damage / 2;

        health -= damage;

        if (health <= 0) {
            health = 0;
            isAlive = false;
            setCurrentState(State.DYING);
        }
    }

    public boolean isHitBy(Rectangle otherAttackHitBox) {
        return hitBox.overlaps(otherAttackHitBox);
    }

    private void attack() {
        if (!isAttacking) {
            isAttacking = true;
            attackTimer = 0f;
        }
    }

    public void setCurrentState(State newState) {
        if (newState != this.currentState) {
            this.currentState = newState;
            animator.resetStateTime();
        }
    }

    public float getHeatlh() {
        return health;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Rectangle getAttackHitBox() {
        return attackHitBox;
    }

    public void moveRight(float delta) {
        x += speed * delta;
        facingLeft = false;
    }

    public boolean isAttackActive() {
        return isAttackActive;
    }

    public void moveLeft(float delta) {
        x -= speed * delta;
        facingLeft = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public State getState() {
        return currentState;
    }
}
