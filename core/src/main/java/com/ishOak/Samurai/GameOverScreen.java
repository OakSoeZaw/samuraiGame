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

public class GameOverScreen extends ScreenAdapter {
    private final SamuraiGame game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Viewport viewport = new ScreenViewport();
    private final GlyphLayout layout = new GlyphLayout();
    boolean winner;

    public GameOverScreen(SamuraiGame game, boolean winner) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.winner=winner;
    }
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Gdx.app.exit();
            return;
        }

        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float centerX = viewport.getWorldWidth() / 2;
        float y = viewport.getWorldHeight() / 2 + 100;

        layout.setText(font, "GAME OVER!!!");
        font.draw(batch, layout, centerX - layout.width / 2, y);

        y -= 40;


        //need to make a winner method in the GameScreen to pinpoint to winner and display it to the game over screen
        if(winner)
            layout.setText(font, "PLAYER1 WINS");
        else
            layout.setText(font, "PLAYER2 WINS");
        font.draw(batch, layout, centerX - layout.width / 2, y);
        y -= 50;

        layout.setText(font, "PRESS ENTER TO EXIT");
        font.draw(batch, layout, centerX - layout.width / 2, y);

        batch.end();
    }
}

