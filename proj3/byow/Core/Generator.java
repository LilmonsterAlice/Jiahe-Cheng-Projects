package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.*;
import java.util.Random;



public class Generator implements Serializable {
    public static TETile[][] tiles;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private long seed;
    private Random random;
    private Avatar avatar;
    private GenRoom genRoom;
    private int radius=5;
    private TETile[][] originalWorld;


    public Generator(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        tiles = new TETile[WIDTH][HEIGHT];
        this.random = new Random(seed);
        fillWithNothing();
        GenRoom newRoom = new GenRoom(random);
        new GenHallway(random, newRoom.rooms);

        }





    public void fillWithNothing() {
        if (tiles != null) {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    tiles[x][y] = Tileset.NOTHING;
                }
            }
        }
    }


    public TETile[][] getTiles() {
        return tiles;

    }
    public Avatar getAvatar() {
        return avatar;
    }
    public long getSeed() {
        return seed;
    }
    private void generateWorld() {
        this.originalWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.originalWorld[x][y] = this.getTiles()[x][y];
            }
        }
    }

}
