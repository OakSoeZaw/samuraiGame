package com.ishOak.Samurai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen extends ScreenAdapter {

    private final SamuraiGame game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Viewport viewport = new ScreenViewport();
    private final GlyphLayout layout = new GlyphLayout();
    private final boolean winner;

    private float timer = 0f;
    private float flashTimer = 0f;
    private boolean showFlash = true;

    // for winner text slide-in
    private float slideTimer = 0f;
    private static final float SLIDE_DURATION = 0.5f;

    // particle-like decorations
    private float[] starX = new float[12];
    private float[] starY = new float[12];
    private float[] starSpeed = new float[12];
    private float[] starAlpha = new float[12];

    public GameOverScreen(SamuraiGame game, boolean winner) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.winner = winner;

        // randomize star positions
        for (int i = 0; i < starX.length; i++) {
            resetStar(i, true);
        }
    }

    private void resetStar(int i, boolean randomY) {
        starX[i] = MathUtils.random(0f, 800f);
        starY[i] = randomY ? MathUtils.random(0f, 600f) : -10f;
        starSpeed[i] = MathUtils.random(40f, 120f);
        starAlpha[i] = MathUtils.random(0.3f, 1f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        timer += delta;
        flashTimer += delta;
        if (slideTimer < 1f) slideTimer += delta * 2f;


        if (flashTimer >= 0.5f) {
            flashTimer = 0f;
            showFlash = !showFlash;
        }


        for (int i = 0; i < starX.length; i++) {
            starY[i] += starSpeed[i] * delta;
            if (starY[i] > viewport.getWorldHeight() + 10f)
                resetStar(i, false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new StageSelectScreen(game));
            return;
        }

        ScreenUtils.clear(new Color(0.05f, 0f, 0.1f, 1f)); // deep purple-black

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float cx = viewport.getWorldWidth() / 2;
        float worldH = viewport.getWorldHeight();


        for (int i = 0; i < starX.length; i++) {
            font.setColor(1f, 1f, 0.6f, starAlpha[i]);
            font.draw(batch, "✦", starX[i], starY[i]);
        }


        float pulse = 1f + MathUtils.sin(timer * 3f) * 0.04f; // subtle scale pulse via alpha
        float pulseAlpha = 0.75f + MathUtils.sin(timer * 3f) * 0.25f;


        font.setColor(0.5f, 0f, 0f, 1f);
        layout.setText(font, "GAME OVER");
        font.draw(batch, layout, cx - layout.width / 2 + 3, worldH * 0.78f - 3);


        font.setColor(1f, 0.15f, 0.15f, pulseAlpha);
        font.draw(batch, layout, cx - layout.width / 2, worldH * 0.78f);


        font.setColor(0.8f, 0.8f, 0.8f, 1f);
        layout.setText(font, "— ⚔ ————————————————— ⚔ —");
        font.draw(batch, layout, cx - layout.width / 2, worldH * 0.68f);


        float slide = MathUtils.clamp(slideTimer / SLIDE_DURATION, 0f, 1f);
        float slideOffsetX = (1f - slide) * 400f; // slides in from right

        String winnerText = winner ? "⚔  PLAYER 1  WINS  ⚔" : "⚔  PLAYER 2  WINS  ⚔";
        Color winnerColor = winner
            ? new Color(0f,100f,0f, slide)   // P1 blue
            : new Color(0f,100f,0, slide);   // P2 orange


        font.setColor(0f, 100f, 0f, slide * 0.8f);
        layout.setText(font, winnerText);
        font.draw(batch, layout, cx - layout.width / 2 + slideOffsetX + 3, worldH * 0.55f - 3);


        font.setColor(winnerColor);
        font.draw(batch, layout, cx - layout.width / 2 + slideOffsetX, worldH * 0.55f);


        float crownBob = MathUtils.sin(timer * 2.5f) * 5f;
        font.setColor(1f, 0.85f, 0f, slide); // gold
        layout.setText(font, "♛");
        font.draw(batch, layout, cx - layout.width / 2, worldH * 0.63f + crownBob);


        if (showFlash) {
            font.setColor(1f, 0.8f, 0f, 0.3f); // glow
            layout.setText(font, "▶  PRESS ENTER TO PLAY AGAIN  ◀");
            font.draw(batch, layout, cx - layout.width / 2 + 2, worldH * 0.25f - 2);

            font.setColor(Color.YELLOW);
            font.draw(batch, layout, cx - layout.width / 2, worldH * 0.25f);
        }


        font.setColor(0.5f, 0.5f, 0.5f, 1f);
        layout.setText(font, "returns to stage select");
        font.draw(batch, layout, cx - layout.width / 2, worldH * 0.18f);

        batch.end();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

    }
}
