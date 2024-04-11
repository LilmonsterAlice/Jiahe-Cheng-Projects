package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar {
    private int x;
    private int y;
    private final TETile tile;
    int health;
    private boolean transAmActive = false;
    private int radius;
    public boolean nightVision;


    public Avatar(int x, int y, TETile tile, int health, int radius) {
        this.x = x;
        this.y = y;
        this.tile = tile;
        this.health = health;
        this.radius = radius;
    }

    public void move(int dx, int dy, TETile[][] world, TERenderer ter) {
        int newX = this.x + dx;
        int newY = this.y + dy;

        if (transAmActive) {
            newX += dx;
            newY += dy;
        }

        if (isValidMove(newX, newY, world)) {
            world[this.x][this.y] = Tileset.FLOOR;
            this.x = newX;
            this.y = newY;
            world[this.x][this.y] = this.tile;
            if (!nightVision) {
                initializeDarkness(world);
                setVisual(world);
            }
            ter.renderFrame(world);
        }

    }

    private boolean isValidMove(int newX, int newY, TETile[][] world) {
        if (newX < 0 || newX >= world.length || newY < 0 || newY >= world[0].length) {
            return false;
        } else {
            // If the target position is a wall, reduce the avatar's health
            if (world[newX][newY] != Tileset.FLOOR) {
                this.health -= 1;
                System.out.println("Avatar health: " + this.health);
            }
            return world[newX][newY] != Tileset.WALL;
        }
    }

    public void initializeDarkness(TETile[][] world) {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                if (world[x][y] == Tileset.WALL) {
                    world[x][y] = Tileset.HIDDEN_WALL;
                }
                if (world[x][y] == Tileset.FLOOR) {
                    world[x][y] = Tileset.HIDDEN_FLOOR;
                }
            }
        }
    }

    public void setVisual(TETile[][] world) {
        for (int a = x - radius; a < x + radius; a++) {
            for (int b = y - radius; b < y + radius; b++) {
                double distance = Math.sqrt(Math.pow((a - x), 2) + Math.pow((b - y), 2));
                if (distance < radius && a >= 0 && a < world.length && b >= 0 && b < world[0].length) {
                    if (world[a][b] == Tileset.HIDDEN_WALL) {
                        world[a][b] = Tileset.WALL;
                    }
                    if (world[a][b] == Tileset.HIDDEN_FLOOR) {
                        world[a][b] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    public void toggleNightVision(TETile[][] world) {
        if (nightVision) {
            for (int x = 0; x < world.length; x++) {
                for (int y = 0; y < world[0].length; y++) {
                    if (world[x][y] == Tileset.HIDDEN_WALL) {
                        world[x][y] = Tileset.WALL;
                    }
                    if (world[x][y] == Tileset.HIDDEN_FLOOR) {
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }
        } else {
            initializeDarkness(world);
            setVisual(world);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

}
