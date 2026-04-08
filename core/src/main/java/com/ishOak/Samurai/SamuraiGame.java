package com.ishOak.Samurai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class SamuraiGame extends Game {
    private SpriteBatch batch;
    private BitmapFont font;



    @Override
    public void create() {
        batch = new SpriteBatch();


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("CherryCreamSoda-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        setScreen(new StartScreen(this));

    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();

    }

    public SpriteBatch getBatch() {
        return batch;
    }



    public BitmapFont getFont() {
        return font;
    }
}

