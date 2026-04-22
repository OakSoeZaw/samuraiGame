package com.ishOak.Samurai;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {

    public enum State {
        IDLE, RUNNING, ATTACKING, DYING, HIT
    };

    private State currState;

    private float x, y;
    private float speed;
    private float width, height;

    private float hitTimer = 0f;
    private static final float HIT_DURATION = 0.3f;

    private SamuraiAnimator animator;
    private Rectangle hitBox;
    private Rectangle attackHitBox;

    private float attackTimer = 0f;
    private float attackCooldown = 0f;

    private float health;
    private boolean isAlive;
    private boolean isAttackActive;

    private static final float ATTACK_RANGE = 50f;
    private static final float ATTACK_COOLDOWN = 1.2f;

    private boolean facingLeft;
    public boolean canTakeDamage = true;
    private float damageCooldownTimer = 0f;
    private static final float DAMAGE_COOLDOWN = 0.5f;

    private Samurai player1;
    private Samurai player2;

    public Enemy(float x, float y, SamuraiAnimator animator, Samurai player1, Samurai player2) {
        this.x = x;
        this.y = y;
        this.animator = animator;

        this.player1 = player1;
        this.player2 = player2;

        this.width = 32;
        this.height = 48;
        this.speed = 150f;

        this.health = 20;
        this.isAlive = true;

        this.currState = State.RUNNING;

        hitBox = new Rectangle(x, y, width, height);
        attackHitBox = new Rectangle();
    }

    public void update(float delta) {
        if (!isAlive) {
            animator.update(delta);
            return;
        }

        if (attackCooldown > 0) {
            attackCooldown -= delta;
        }

        if (damageCooldownTimer > 0) {
            damageCooldownTimer -= delta;
            canTakeDamage = damageCooldownTimer <= 0;
        }


        if (hitTimer > 0) {
            hitTimer -= delta;
            if (hitTimer <= 0 && isAlive) {
                setState(State.RUNNING);
            }
        }


        if (currState == State.HIT) {
            animator.update(delta);
            return;
        }


        Samurai closerTarget = Math.abs(player1.x - this.x) <= Math.abs(player2.x - this.x)
            ? player1 : player2;


        if (!closerTarget.isAlive()) {
            closerTarget = closerTarget == player1 ? player2 : player1;
        }

        float distToTarget = Math.abs(closerTarget.x - this.x);

        if (distToTarget > ATTACK_RANGE) {
            if (closerTarget.x > this.x)
                moveRight(delta);
            else
                moveLeft(delta);
            setState(State.RUNNING);
        } else {
            handleAttack(delta);
        }

        updateHitBoxes();
        animator.update(delta);
    }

    public void handleAttack(float delta) {
        if (currState == State.ATTACKING) {
            attackTimer += delta;
            isAttackActive = attackTimer >= 0.1f && attackTimer <= 0.3f;

            if (attackTimer >= 0.4f) {
                isAttackActive = false;
                attackTimer = 0f;
                attackCooldown = ATTACK_COOLDOWN;
                setState(State.IDLE);
            }
        } else if (attackCooldown <= 0) {
            attackTimer = 0f;
            setState(State.ATTACKING);
        } else {
            setState(State.IDLE);
        }
    }

    private void updateHitBoxes() {
        hitBox.setPosition(x, y);
        if (facingLeft) {
            attackHitBox.set(x - 40, y + 10, 40, 30);
        } else {
            attackHitBox.set(x + width, y + 10, 40, 30);
        }
    }

    public void takeDamage(int damage) {
        if (!isAlive || !canTakeDamage)
            return;

        health -= damage;
        canTakeDamage = false;
        damageCooldownTimer = DAMAGE_COOLDOWN;
        hitTimer = HIT_DURATION;
        setState(State.HIT);

        if (health <= 0) {
            health = 0;
            isAlive = false;
            isAttackActive = false;
            setState(State.DYING);
        }
    }

    public boolean isHitBy(Rectangle otherAttackHitBox) {
        return hitBox.overlaps(otherAttackHitBox);
    }

    public void setState(State state) {
        if (state != this.currState) {
            this.currState = state;
            animator.resetStateTime();
        }
    }

    public void moveRight(float delta) {
        x += speed * delta;
        facingLeft = false;
    }

    public void moveLeft(float delta) {
        x -= speed * delta;
        facingLeft = true;
    }

    // Getters
    public boolean isAlive() {
        return isAlive;
    }

    public boolean isAttackActive() {
        return isAttackActive;
    }

    public boolean isDeathFinished() {
        return animator.isDeathAnimationFinished();
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Rectangle getAttackHitBox() {
        return attackHitBox;
    }

    public State getState() {
        return currState;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public TextureRegion getFrame() {
        return animator.getFrame(this.currState);
    }

    public SamuraiAnimator getAnimator() {
        return animator;
    }

}
