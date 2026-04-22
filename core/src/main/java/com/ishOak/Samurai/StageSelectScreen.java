package com.ishOak.Samurai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StageSelectScreen extends ScreenAdapter {

    private final SamuraiGame game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Viewport viewport = new ScreenViewport();
    private final GlyphLayout layout = new GlyphLayout();

    private int selectedStage = 1; // 1 or 2

    public StageSelectScreen(SamuraiGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        // left/right to switch stage
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A))
            selectedStage = 1;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D))
            selectedStage = 2;

        // confirm selection
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game, selectedStage));
            dispose();
            return;
        }

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float cx = viewport.getWorldWidth() / 2;
        float y = viewport.getWorldHeight() / 2 + 80;

        font.setColor(Color.WHITE);
        layout.setText(font, "SELECT STAGE");
        font.draw(batch, layout, cx - layout.width / 2, y);
        y -= 60;

        // stage 1 option
        font.setColor(selectedStage == 1 ? Color.YELLOW : Color.WHITE);
        layout.setText(font, "[ STAGE 1 - NIGHT ]");
        font.draw(batch, layout, cx - layout.width / 2, y);
        y -= 40;

        // stage 2 option
        font.setColor(selectedStage == 2 ? Color.YELLOW : Color.WHITE);
        layout.setText(font, "[ STAGE 2 - DAY ]");
        font.draw(batch, layout, cx - layout.width / 2, y);
        y -= 60;

        font.setColor(Color.WHITE);
        layout.setText(font, "LEFT/RIGHT to select, SPACE to confirm");
        font.draw(batch, layout, cx - layout.width / 2, y);

        batch.end();
    }
}
