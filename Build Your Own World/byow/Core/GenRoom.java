package byow.Core;


import byow.TileEngine.Tileset;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;



public class GenRoom {
    private static final int ROOMMAXHEIGHT = 5;
    private static final int ROOMMINHEIGHT = 2;
    private static final int HALLWAYWIDTH = 1;
    private static final int MINROOM = 15;
    private static final int MAXROOM = 20;
    private int numRooms;
    public static ArrayList<Room> rooms;
    private Random random;

    public GenRoom(Random random) {
        this.random = random;
        numRooms = MINROOM + random.nextInt(MAXROOM - MINROOM);
        rooms = new ArrayList<Room>();
        for (int i = 0; i < numRooms; i++) {
            int x = 1 + random.nextInt(Generator.WIDTH - ROOMMAXHEIGHT - 2);
            int y = 1 + random.nextInt(Generator.HEIGHT - ROOMMAXHEIGHT - 2);
            int width = ROOMMINHEIGHT + random.nextInt(ROOMMAXHEIGHT - ROOMMINHEIGHT);
            int height = ROOMMINHEIGHT + random.nextInt(ROOMMAXHEIGHT - ROOMMINHEIGHT);
            makeRoom(x, y, width, height);
        }
    }

        public class Room {
            public int roomID;
            public int x;
            public int y;
            public int width;
            public int height;
            public int left;
            public int right;
            public int top;
            public int bottom;

            public Room(int roomID, int x, int y, int width, int height) {
                this.roomID = roomID;
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.left = x;
                this.right = x + width - 1;
                this.top = y + height - 1;
                this.bottom = y;
            }
            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }


            }
    private void makeRoom(int x, int y, int width, int height) {
        if (!isOccupiedOrEmpty(x, y, width, height)) {
            Room newroom = new Room(rooms.size(), x, y, width, height);
            rooms.add(newroom);
            for (int a = x - 1; a < x + width + 1; a++) {
                for (int b = y - 1; b < y + height + 1; b++) {
                    Generator.tiles[a][b] = Tileset.WALL;
                }
            }
            for (int a = x; a < x + width; a++) {
                for (int b = y; b < y + height; b++) {
                    Generator.tiles[a][b] = Tileset.FLOOR;
                }
            }
        }
    }

    private boolean isOccupiedOrEmpty(int a, int b, int width, int height) {
        if (a == 0 || b == 0) {
            return true;
        }
        for (int x = a; x < a + width; x++) {
            for (int y = b; y < b + height; y++) {
                if (a + width > Generator.WIDTH - 1 || b + height > Generator.HEIGHT - 1) {
                    return true;
                }
                if (Generator.tiles[x][y] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }
    public static Point getFirstRoomPosition() {
        if (rooms.size() > 0) {
            Room firstRoom = rooms.get(0);
            System.out.println("First room position: " + firstRoom.toString());
            return new Point(firstRoom.getX() + 1, firstRoom.getY() + 1);
        }
        return null;
    }

}

