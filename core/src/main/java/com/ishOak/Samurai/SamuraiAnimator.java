package com.ishOak.Samurai;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;

public class SamuraiAnimator{
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> attackAnimation;
    // private Animation<TextureRegion> blockAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> idleAnimation;

    private float stateTime = 0f;

    public SamuraiAnimator(Texture run, Texture attack, Texture block,
        Texture death, Texture idle){
        runAnimation = buildAnimation(run, 200, 200, 0.08f );
        attackAnimation = buildAnimation(attack, 200, 200, 0.08f );
        deathAnimation = buildAnimation(death, 200, 200, 0.08f );
        idleAnimation = buildAnimation(idle, 200, 200, 0.08f );

        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        deathAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void update(float delta){
        stateTime += delta;
    }

    public void resetStateTime(){
        stateTime = 0f;
    }

    public TextureRegion getFrame(Samurai.State state){
        switch(state){
            case RUNNING :
                return runAnimation.getKeyFrame(stateTime,true);
            case ATTACKING :
                return attackAnimation.getKeyFrame(stateTime, false);
            case DYING : 
                return deathAnimation.getKeyFrame(stateTime,false);
            default :
             return idleAnimation.getKeyFrame(stateTime, true);
        }
    }


    public Animation<TextureRegion> buildAnimation(Texture texture, int frameWidth, int frameHeight,
        float frameDuration){
        TextureRegion[][] grid = TextureRegion.split(texture, frameWidth, frameHeight);
        return new Animation<>(frameDuration, grid[0]);
    }
    
}