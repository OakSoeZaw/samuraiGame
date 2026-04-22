package com.ishOak.Samurai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen extends ScreenAdapter {

    private final SamuraiGame game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Viewport viewport = new ScreenViewport();
    private final GlyphLayout layout = new GlyphLayout();
    private final Texture bgdTexture;

    private float timer = 0f;


    private float titleY;
    private static final float TITLE_BOB_SPEED = 2f;
    private static final float TITLE_BOB_AMOUNT = 6f;


    private float flashTimer = 0f;
    private boolean showFlash = true;


    private float slideTimer = 0f;
    private static final float SLIDE_DURATION = 0.4f;

    public StartScreen(SamuraiGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        bgdTexture = new Texture(Gdx.files.internal("ssbg.png"));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        timer += delta;
        flashTimer += delta;
        if (slideTimer < 1f) slideTimer += delta * 1.5f;

        // flash toggle every 0.5s
        if (flashTimer >= 0.5f) {
            flashTimer = 0f;
            showFlash = !showFlash;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new StageSelectScreen(game));
            dispose();
            return;
        }

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // background
        batch.draw(bgdTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        batch.setColor(0, 0, 0, 0.45f);
        batch.draw(bgdTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(Color.WHITE);

        float cx = viewport.getWorldWidth() / 2;
        float worldH = viewport.getWorldHeight();

        float bobOffset = MathUtils.sin(timer * TITLE_BOB_SPEED) * TITLE_BOB_AMOUNT;
        titleY = worldH * 0.82f + bobOffset;


        font.setColor(0.7f, 0f, 0f, 1f);
        layout.setText(font, "⚔  SAMURAI DUEL  ⚔");
        font.draw(batch, layout, cx - layout.width / 2 + 3, titleY - 3);

        font.setColor(Color.WHITE);
        font.draw(batch, layout, cx - layout.width / 2, titleY);


        font.setColor(0.8f, 0.2f, 0.2f, 1f);
        layout.setText(font, "────────────────────────────");
        font.draw(batch, layout, cx - layout.width / 2, worldH * 0.74f);


        float slide = MathUtils.clamp(slideTimer / SLIDE_DURATION, 0f, 1f);
        float slideOffsetX = (1f - slide) * -300f;

        font.setColor(1f, 0.85f, 0.2f, 1f); // gold
        layout.setText(font, "CONTROLS");
        font.draw(batch, layout, cx - layout.width / 2 + slideOffsetX, worldH * 0.68f);

        //  P1 controls
        font.setColor(0.4f, 0.8f, 1f, slide); // blue, fades in with slide
        layout.setText(font, "P1  |  A / D — Move   W — Jump   F — Attack");
        font.draw(batch, layout, cx - layout.width / 2 + slideOffsetX, worldH * 0.60f);

        // P2 controls
        font.setColor(1f, 0.5f, 0.3f, slide); // orange, fades in with slide
        layout.setText(font, "P2  |  ← / → — Move   ↑ — Jump   L — Attack");
        font.draw(batch, layout, cx - layout.width / 2 + slideOffsetX, worldH * 0.52f);

        if (showFlash) {
            font.setColor(1f, 0.8f, 0f, 0.3f);
            layout.setText(font, "▶  PRESS SPACE TO START  ◀");
            font.draw(batch, layout, cx - layout.width / 2 + 2, worldH * 0.30f - 2);

            font.setColor(Color.YELLOW);
            font.draw(batch, layout, cx - layout.width / 2, worldH * 0.30f);
        }



        batch.end();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (bgdTexture != null) bgdTexture.dispose();
    }
}
