package com.ishOak.Samurai;

public class Controls {
    public int jump;
    public int down;
    public int right;
    public int left;
    public int attack;
    public int block;

    public Controls(int jump, int down, int left, int right, int attack) {
        this.jump = jump;
        this.down = down;
        this.left = left;
        this.right = right;
        this.attack = attack;

    }

    public String toString() {
        return ("Controls (jump: " + jump + ", down: " + down + ", right: " + right +
            " left:" + left + ", attack: " + attack +  ")");
    }
}
