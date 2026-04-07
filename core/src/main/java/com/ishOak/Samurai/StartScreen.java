package com.ishOak.Samurai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen extends ScreenAdapter
{
    private final  SamuraiGame game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Viewport viewport = new ScreenViewport();
    private final GlyphLayout layout = new GlyphLayout();

    private final Texture bgdTexture;

    public StartScreen(SamuraiGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();

        // load textures
        bgdTexture = new Texture(Gdx.files.internal("ssbg.png"));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float deltaTime) {

        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(bgdTexture,0,0,viewport.getWorldWidth(),viewport.getWorldHeight());

        float centerX = viewport.getWorldWidth() / 2;
        float y = viewport.getWorldHeight() / 2 + 100;

        font.setColor(Color.WHITE);

        layout.setText(font, "CONTROLS");
        font.draw(batch, layout, centerX - layout.width / 2, y);
        y -= 50;

        layout.setText(font, "Player 1: A / D  - Move");
        font.draw(batch, layout, centerX - layout.width / 2, y);
        y -= 40;
        layout.setText(font, "Player 1: W - Jump");
        font.draw(batch, layout, centerX - layout.width / 2, y);
        y -= 40;


        layout.setText(font, "Player 2: <- / -> - Move");
        font.draw(batch, layout, centerX - layout.width / 2, y);
        y -= 40;

        layout.setText(font, "Player 2: UP ARROW - Jump");
        font.draw(batch, layout, centerX - layout.width / 2, y);
        y -= 70;

        layout.setText(font, "Press SPACE to start");
        font.draw(batch, layout, centerX - layout.width / 2, y);

        batch.end();
    }


}
