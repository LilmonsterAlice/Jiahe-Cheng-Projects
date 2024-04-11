package byow.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import byow.Core.GenRoom.Room;
import byow.TileEngine.Tileset;

public class GenHallway {
    private final ArrayList<GenRoom.Room> rooms;
    private final ArrayList<Integer> connected; //[0,1,2,3,4,5,6,7,8,9,10]
    private final Random random;

    public GenHallway(Random random, ArrayList<Room> rooms) {
        this.random = random;
        this.rooms = rooms;
        connected = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            connected.add(i);
        }
        connectRooms();
    }

    private void connectRooms() {
        int index1 = 0;
        int ID1 = connected.remove(index1);
        while (connected.size() != 0) {
            int index2 = random.nextInt(connected.size());
            int ID2 = connected.remove(index2);
            makeHallway(ID1, ID2);
            ID1 = ID2;
        }
    }

    private void makeHallway(int iD1, int iD2) {
        Room room1 = rooms.get(iD1);
        Room room2 = rooms.get(iD2);
        int x1 = random.nextInt(room1.x, room1.x + room1.width);
        int y1 = random.nextInt(room1.y, room1.y + room1.height);
        int x2 = random.nextInt(room2.x, room2.x + room2.width);
        int y2 = random.nextInt(room2.y, room2.y + room2.height);
        fillHallway(x1, y1, x2, y2);
    }

    private void fillHallway(int x1, int y1, int x2, int y2) {
        if (x1 >= x2 && y1 >= y2) {
            horizontalhw(x2, x1, y2);
            verticalhw(y2, y1, x1);
        } else if (x1 < x2 && y1 >= y2) {
            horizontalhw(x1, x2, y2);
            verticalhw(y2, y1, x1);
        } else if (x1 < x2) {
            horizontalhw(x1, x2, y2);
            verticalhw(y1, y2, x1);
        } else {
            horizontalhw(x2, x1, y2);
            verticalhw(y1, y2, x1);
        }
    }
//
//    private int relativeLocation(int x1, int y1, int x2, int y2) {
//        //象限1234. 1:右上；2:左上；3:左下；4:右下
//        if (x1 >= x2 && y1 >= y2) {
//            return 1;
//        }
//        else if (x1 < x2 && y1 >= y2) {
//            return 2;
//        }
//        else if (x1 < x2 && y1 < y2) {
//            return 3;
//        } //(x1 >= x2 && y1 < y2)
//        return 4;
//    }

    private void horizontalhw(int x1, int x2, int y) { // x1<x2
        for (int a = x1; a <= x2; a++) {
            for (int b = y - 1; b <= y + 1; b++) {
                if (Generator.tiles[a][b] == Tileset.NOTHING) {
                    Generator.tiles[a][b] = Tileset.WALL;
                }
            }
        }

        for (int a = x1; a <= x2; a++) {
            if (Generator.tiles[a][y] == Tileset.WALL) {
                Generator.tiles[a][y] = Tileset.FLOOR;
            }
        }
        ifEmptyWall(x1 - 1, y + 1);
        ifEmptyWall(x1 - 1, y - 1);
        ifEmptyWall(x2 + 1, y + 1);
        ifEmptyWall(x2 + 1, y - 1);
    }

    private void ifEmptyWall(int x, int y) {
        if (Generator.tiles[x][y] == Tileset.NOTHING) {
            Generator.tiles[x][y] = Tileset.WALL;
        }
    }

    private void verticalhw(int y1, int y2, int x) {
        for (int b = y1; b <= y2; b++) {
            for (int a = x - 1; a <= x + 1; a++) {
                if (Generator.tiles[a][b] == Tileset.NOTHING) {
                    Generator.tiles[a][b] = Tileset.WALL;
                }
            }
        }
        for (int b = y1; b <= y2; b++) {
            if (Generator.tiles[x][b] == Tileset.WALL) {
                Generator.tiles[x][b] = Tileset.FLOOR;
            }
        }
        ifEmptyWall(x + 1, y1 - 1);
        ifEmptyWall(x - 1, y1 - 1);
        ifEmptyWall(x + 1, y2 + 1);
        ifEmptyWall(x - 1, y2 + 1);
    }
}