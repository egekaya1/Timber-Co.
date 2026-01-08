package dev.egekaya.timber;

public class Tree {

    private Position position;

    public Tree(Position position) {
        this.position = position;
    }

    public Position getTreePos() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
