package com.ishOak.Samurai;

public class Controls {
    private int up;
    private int down;
    private int right;
    private int left;
    private int attack;
    private int block;

    public Controls(int up, int down, int left, int right, int attack, int block){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.attack = attack;
        this.block = block;
    }

    public String toString(){
        return "Controls (up: " + up + ", down: " + down + ", right: " + right +
        " left:" + left
    }
}
