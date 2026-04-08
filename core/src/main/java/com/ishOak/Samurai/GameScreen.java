package com.ishOak.Samurai;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class GameScreen implements Screen{

    private SamuraiGame game;
    private SpriteBatch batch;
    private BitmapFont font;


    //Game State
    public boolean gameOver;

    //Textures
    private Texture bgTexture;
    private Texture player1IdleTex, player1RunTex, player1AttackTex, player1DeathTex;
    private Texture player2IdleTex, player2RunTex, player2AttackTex, player2DeathTex;

    //Players
    private Samurai player1;
    private Samurai player2;

    private SamuraiAnimator animator1;
    private SamuraiAnimator animator2;

    public GameScreen(SamuraiGame game){
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
    }

    @Override
    public void show(){
        // player 1 with WASD and F for attack
        Controls controls1 = new Controls(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.F, Input.Keys.G);
        // player 2 with arrow keys and L to attack
        Controls controls2 = new Controls(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.L, Input.Keys.K);

        //load background texture
        bgTexture=new Texture("bdg.png");

        //load texture for player1
        player1AttackTex = new Texture("p1_attack.png");
        player1IdleTex = new Texture("p1_idle.png");
        player1RunTex = new Texture("p1_run.png");
        player1DeathTex = new Texture("p1_death.png");

        player2AttackTex = new Texture("p2_attack.png");
        player2IdleTex = new Texture("p2_idle.png");
        player2RunTex = new Texture("p2_run.png");
        player2DeathTex = new Texture("p2_death.png");

        animator1 = new SamuraiAnimator(player1RunTex, player1AttackTex, player1DeathTex, player1IdleTex);
        animator2 = new SamuraiAnimator(player2RunTex, player2AttackTex,player2DeathTex, player2IdleTex);

        player1 = new Samurai(100, 20, controls1, animator1);
        player2 = new Samurai(300, 20, controls2, animator2);

        gameOver = false;

    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0 , 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        batch.begin();
        drawBackground();
        drawPlayers();
        drawHUD();
        batch.end();
    }

    private void drawBackground() {
        batch.draw(bgTexture,0,0);
    }

    private void update(float delta){
        if (gameOver) return;

        player1.update(delta);
        player2.update(delta);

        animator1.update(delta);
        animator2.update(delta);

        checkCombat();
        checkGameOver();
    }

    private void checkCombat(){
        if(player2.isAttackActive()){
            if(player1.isHitBy(player2.getAttackHitBox())){
                player1.takeDamage(20);
            }
        }
        if(player1.isAttackActive()){
            if(player2.isHitBy(player1.getAttackHitBox())){
                player2.takeDamage(20);
            }
        }
    }

    private void checkGameOver(){
        if(!player1.isAlive() || !player2.isAlive()){
            gameOver = true;
            // need to make gameOver Screen;
            System.out.println("Game ended");
        }
    }

    private void drawPlayers(){
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
        batch.draw(animator1.getFrame(player1.getState()), player1.x, player1.y);
        batch.draw(animator2.getFrame(player2.getState()), player2.x, player2.y);
    }

    private void drawHUD(){
        font.draw(batch, "HP:" + (int) player1.getHeatlh(), 20, 580);
        font.draw(batch, "HP:" + (int) player2.getHeatlh(), 620, 580);
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

        player2IdleTex.dispose();
        player2RunTex.dispose();
        player2AttackTex.dispose();
        player2DeathTex.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

}
