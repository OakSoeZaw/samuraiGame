package com.ishOak.Samurai;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private SamuraiGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private FitViewport viewport;

    // Game State
    public boolean gameOver;
    private float gameOverTimer;

    // Textures
    private Texture bgTexture;
    private Texture player1IdleTex, player1RunTex, player1AttackTex, player1DeathTex;
    private Texture player2IdleTex, player2RunTex, player2AttackTex, player2DeathTex;
    private Texture player1JumpTex, player1BlockTex;
    private Texture player2JumpTex, player2BlockTex;

    private final Array<Texture> backgroundTextures = loadBackgroundTextures();
    private final Animation<TextureRegion> backgroundAnimation = buildBackgroundAnimation();
    private float stateTime = 0f;

    private Animation<TextureRegion> buildBackgroundAnimation() {
        Array<TextureRegion> regions = new Array<>(24);
        for (Texture t : backgroundTextures) {
            regions.add(new TextureRegion(t));
        }
        Animation<TextureRegion> anim = new Animation<>(1 / 5f, regions);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }

    // Players
    private Samurai player1;
    private Samurai player2;

    private SamuraiAnimator animator1;
    private SamuraiAnimator animator2;

    boolean P1winner = false;// if false 2 is winner

    public GameScreen(SamuraiGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
    }

    @Override
    public void show() {
        // player 1 with WASD and F for attack
        Controls controls1 = new Controls(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.F);
        // player 2 with arrow keys and L to attack
        Controls controls2 = new Controls(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
                Input.Keys.L);

        // load background texture
        bgTexture = new Texture("bdg.png");

        // load texture for player1
        player1AttackTex = new Texture("p1_attack.png");
        player1IdleTex = new Texture("p1_idle.png");
        player1RunTex = new Texture("p1_run.png");
        player1DeathTex = new Texture("p1_death.png");
        player1JumpTex = new Texture("p1_jump.png");
        // player1BlockTex = new Texture("p1_block.png");

        player2AttackTex = new Texture("p2_attack.png");
        player2IdleTex = new Texture("p2_idle.png");
        player2RunTex = new Texture("p2_run.png");
        player2DeathTex = new Texture("p2_death.png");
        player2JumpTex = new Texture("p2_jump.png");
        // player2BlockTex = new Texture("p2_block.png");

        animator1 = new SamuraiAnimator(player1RunTex, player1AttackTex, player1DeathTex, player1IdleTex,
                player1JumpTex);
        animator2 = new SamuraiAnimator(player2RunTex, player2AttackTex, player2DeathTex, player2IdleTex,
                player2JumpTex);

        player1 = new Samurai(100, 20, controls1, animator1);
        player2 = new Samurai(300, 20, controls2, animator2);

        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 600, camera);

        shapeRenderer = new ShapeRenderer();

        gameOver = false;
        gameOverTimer = 0f;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        update(delta);
        batch.begin();
        drawBackground();
        drawPlayers();
        batch.end();

        drawHUD();

        if (gameOver) {
            game.setScreen(new GameOverScreen(game, P1winner));
        }
    }

    private Array<Texture> loadBackgroundTextures() {
        Array<Texture> textures = new Array<Texture>(24);
        for (int i = 0; i <= 23; i++) {
            textures.add(new Texture(Gdx.files.internal(String.format("frame_%02d.png", i))));
        }
        return textures;
    }

    private void drawBackground() {
        TextureRegion frame = backgroundAnimation.getKeyFrame(stateTime);
        batch.draw(frame, 0, 0, 640, 600); // fixed size, not Gdx.graphics
    }

    private void update(float delta) {
        animator1.update(delta);
        animator2.update(delta);
        stateTime += delta;

        if (gameOver)
            return;

        player1.update(delta);
        player2.update(delta);

        checkGameOver();
        checkCombat();

    }

    private void checkCombat() {
        if (player2.isAttackActive()) {
            if (player1.isHitBy(player2.getAttackHitBox())) {
                player1.takeDamage(20);
            }
        }
        if (player1.isAttackActive()) {
            if (player2.isHitBy(player1.getAttackHitBox())) {
                player2.takeDamage(20);
            }
        }
    }

    private void checkGameOver() {
        if (!player1.isAlive() || !player2.isAlive()) {

            if (player1.isAlive()) {
                P1winner = true;
            }
            boolean p1DeathDone = !player1.isAlive() ? animator1.isDeathAnimationFinished() : true;
            boolean p2DeathDone = !player2.isAlive() ? animator2.isDeathAnimationFinished() : true;

            if (p1DeathDone && p2DeathDone) {
                gameOverTimer += Gdx.graphics.getDeltaTime();
                if (gameOverTimer >= 1f) {
                    gameOver = true;
                    System.out.println("Game Over");
                }
            }

        }
    }

    private void drawPlayers() {
        TextureRegion frame1 = animator1.getFrame(player1.getState());
        TextureRegion frame2 = animator2.getFrame(player2.getState());

        if (player1.facingLeft && !frame1.isFlipX()) {
            frame1.flip(true, false);
        } else if (!player1.facingLeft && frame1.isFlipX()) {
            frame1.flip(true, false);
        }

        if (player2.facingLeft && !frame2.isFlipX()) {
            frame2.flip(true, false);
        } else if (!player2.facingLeft && frame2.isFlipX()) {
            frame2.flip(true, false);
        }
        batch.draw(frame1, player1.x - 32, player1.y);
        batch.draw(frame2, player2.x - 32, player2.y);
    }

    private void drawHUD() {
        float maxHealth = 100f;
        float barWidth = 200f;
        float barHeight = 15f;
        float barY = 575f; // near top of 600 viewport
        float p1BarX = 20f;
        float p2BarX = 420f; // right side

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
        shapeRenderer.rect(p1BarX, barY, barWidth, barHeight);
        shapeRenderer.rect(p2BarX, barY, barWidth, barHeight);

        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(p1BarX, barY, barWidth * (player1.getHealth() / maxHealth), barHeight);
        shapeRenderer.rect(p2BarX, barY, barWidth * (player2.getHealth() / maxHealth), barHeight);

        shapeRenderer.end();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        player1IdleTex.dispose();
        player1RunTex.dispose();
        player1AttackTex.dispose();
        player1DeathTex.dispose();
        player1JumpTex.dispose();

        player2JumpTex.dispose();
        player2IdleTex.dispose();
        player2RunTex.dispose();
        player2AttackTex.dispose();
        player2DeathTex.dispose();
        backgroundTextures.forEach(Texture::dispose);

        shapeRenderer.dispose();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}
