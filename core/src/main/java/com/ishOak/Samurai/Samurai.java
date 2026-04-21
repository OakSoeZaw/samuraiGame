package com.ishOak.Samurai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Samurai {

    public enum State {
        IDLE, RUNNING, DYING, ATTACKING, JUMPING, HIT
    };

    public float x, y;
    private float speed;
    private float width, height;

    // combat
    private float health;
    public boolean facingLeft = false;
    private boolean isAttacking;
    private boolean isAlive;
    private float hitTimer = 0f;
    private static final float HIT_DURATION = 0.3f;

    private boolean isAttackActive;

    private boolean canTakeDamage;
    private float attackCoolDown;
    private float attackTimer;

    private Rectangle hitBox;
    private Rectangle attackHitBox;

    private State currentState;
    private SamuraiAnimator animator;
    private Controls controls;

    // jumping
    private boolean isJumping;
    private float velocityY;
    private static final float GRAVITY = -800f;
    private static final float JUMP_FORCE = 500f;
    private static final float GROUND_Y = 20f;

    private float damageCooldownTimer = 0f;
    private static final float DAMAGE_COOLDOWN = 0.5f;

    private boolean isHit = false;

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
        this.health = 100;
        this.canTakeDamage = true;
        this.facingLeft = false;

        hitBox = new Rectangle(x, y, width, height);
        attackHitBox = new Rectangle();
    }

    public void update(float delta) {
        if (damageCooldownTimer > 0) {
            damageCooldownTimer -= delta;
            canTakeDamage = damageCooldownTimer <= 0;
        }

        if (!isAlive) {
            animator.update(delta);
            return;
        }

        if (hitTimer > 0) {
            hitTimer -= delta;
            if (hitTimer <= 0) {
                isHit = false;
                setCurrentState(State.IDLE);
            }
        }

        handleMovement(delta);
        handleAttack(delta);
        updateHitBox();

    }

    private void handleMovement(float delta) {
        if (!isAlive || isAttacking || isHit)
            return;

        // horizontal movement
        if (Gdx.input.isKeyPressed(controls.left)) {
            moveLeft(delta);
            if (!isJumping)
                setCurrentState(State.RUNNING);
        } else if (Gdx.input.isKeyPressed(controls.right)) {
            moveRight(delta);
            if (!isJumping)
                setCurrentState(State.RUNNING);
        } else {
            if (!isJumping)
                setCurrentState(State.IDLE);
        }

        // jump
        if (Gdx.input.isKeyJustPressed(controls.jump) && !isJumping) {
            velocityY = JUMP_FORCE;
            isJumping = true;
            setCurrentState(State.JUMPING);
        }

        // apply gravity
        if (isJumping) {
            velocityY += GRAVITY * delta;
            y += velocityY * delta;
            setCurrentState(State.JUMPING);

            if (y <= GROUND_Y) {
                y = GROUND_Y;
                isJumping = false;
                velocityY = 0;
                setCurrentState(State.IDLE);
            }
        }

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
                setCurrentState(isJumping ? State.JUMPING : State.IDLE);
            }
        }
        if (Gdx.input.isKeyJustPressed(controls.attack) && !isAttacking && attackCoolDown <= 0) {
            attack();
        }
    }

    private void updateHitBox() {
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
        isHit = true;   
        setCurrentState(State.HIT);

        if (health <= 0) {
            health = 0;
            isAlive = false;
            isHit = false;          
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

    public float getHealth() {
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
