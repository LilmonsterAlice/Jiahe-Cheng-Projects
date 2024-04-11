package byow.Core;

/**
 * Most Style ERRORS were Rphrased by CHATGPT
 **/

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Point;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    boolean inGame = false;
    boolean colonDetected = false;
    boolean gameOver = false;

    private Generator generator;
    private Avatar avatar;

    StringBuilder inputCommands = new StringBuilder();
    private static TETile[][] tiles;
    private static final int INITIAL_HEALTH = 10;
    private int AVATARHEALTH = INITIAL_HEALTH;
    private static final int TIME_POSITION = 20;
    private static final int TEXTPOSITION = 25;
    private static int TCOUNTER = 0;
    private static final int MAXSEEDLENGTH = 19;


    public Engine() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        tiles = new TETile[WIDTH][HEIGHT];

    }


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        drawHUD("Welcome to the Game!");
        String savedGameString = SaveNLoad.loadGame();
        boolean loadable = SaveNLoad.isLoadable(savedGameString);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (inGame && key == 'n') {
                    continue;
                }
                switch (key) {
                    case 'n':
                        inputCommands.append(key);
                        gameOver = false;
                        inGame = true;
                        SaveNLoad.clearFile();
                        String seedString = promptForSeed();
                        long seednew = 0;
                        try {
                            seednew = Long.parseLong(seedString);
                        } catch (NumberFormatException e) {
                            drawHUD("Invalid seed! Please enter a valid number.");
                            continue;
                        }
                        generator = new Generator(seednew);
                        Point floorPosition = GenRoom.getFirstRoomPosition();
                        avatar = createAvatar(generator.getTiles(), floorPosition.x, floorPosition.y, AVATARHEALTH, 2);
                        break;
                    case 'l':
                        loadSavedGame(savedGameString, loadable);
                        break;
                    case 't':
                        if (!avatar.nightVision) {
                            avatar.nightVision = true;
                            avatar.toggleNightVision(generator.getTiles());
                        } else {
                            avatar.nightVision = false;
                            avatar.toggleNightVision(generator.getTiles());
                        }
                        inputCommands.append(key);
                        break;
                    case 'w':
                    case 'a':
                    case 's':
                    case 'd':
                        if (inGame && generator != null) {
                            if (key == 'w') {
                                avatar.move(0, 1, generator.getTiles(), ter);
                            } else if (key == 'a') {
                                avatar.move(-1, 0, generator.getTiles(), ter);
                            } else if (key == 's') {
                                avatar.move(0, -1, generator.getTiles(), ter);
                            } else if (key == 'd') {
                                avatar.move(1, 0, generator.getTiles(), ter);
                            }
                            inputCommands.append(key);
                        }
                        break;
                    case ':':
                        colonDetected = true;
                        inputCommands.append(key);
                        break;
                    case 'q':
                        inputCommands.append(key);
                        if (colonDetected) {
                            SaveNLoad.saveGame(inputCommands.toString());
                        }
                        System.exit(0);
                        break;
                    default:
                        colonDetected = false;
                        break;
                }
            }
            if (generator != null) {
                this.renderFrame(generator.getTiles());
            }
        }
    }

    public TETile[][] interactWithInputString(String input) throws IllegalArgumentException {
        tiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        String movements;
        if (input.charAt(0) == 'n') {
            SaveNLoad.clearFile();
            Pattern pattern = Pattern.compile("n(\\d+)");
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                long seeda = Long.parseLong(matcher.group(1));
                generator = new Generator(seeda);
                tiles = generator.getTiles();
                avatar = generator.getAvatar();
            }
            String[] parts = input.split("[:\\\\+]");
            movements = parts.length > 1 ? parts[1] : "";
        } else if (input.charAt(0) == 'l') {
            String savedGameString = SaveNLoad.loadGame();
            movements = input.substring(1);
            return interactWithInputString(savedGameString + movements);
        } else {
            throw new IllegalArgumentException("Invalid command");
        }
        processSavedMovements(movements);

        if (tiles == null) {
            throw new IllegalArgumentException("Tiles array cannot be null");
        }
        if (input.contains(":q")) {
            SaveNLoad.saveGame(input);
        }
        return tiles;
    }


    private int[] getStartingPosition(TETile[][] what) {
        Point randomPositionInFirstRoom = GenRoom.getFirstRoomPosition();
        if (randomPositionInFirstRoom != null) {
            return new int[]{randomPositionInFirstRoom.x, randomPositionInFirstRoom.y};
        }
        return null;
    }


    private void drawHUD(String message) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(midWidth, midHeight, midWidth / 2, midHeight / 8);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, message);


        // Add the following lines to display the options
        StdDraw.text(midWidth, midHeight - 2, "Press 'n' for New Game");
        StdDraw.text(midWidth, midHeight - 4, "Press 'l' to Load Saved Game");
        StdDraw.show();
    }

    private String promptForSeed() {
        StringBuilder seedString = new StringBuilder();
        int maxLength = MAXSEEDLENGTH; // You can set this to your desired maximum length
        drawHUD("Enter seed (followed by 's'):");

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (Character.isDigit(c)) {
                    seedString.append(c);
                    inputCommands.append(c);
                    if (seedString.length() > maxLength) {
                        return "ERROR: Seed entered is too long (maximum length: " + maxLength + ")";
                    }
                    drawHUD("Seed: " + seedString + " (Press 's' to start)");
                } else if (c == 's' && (seedString.length() > 0)) {
                    inputCommands.append('s');
                    break;
                } else if (c == '\b' && seedString.length() > 0) { // backspace key
                    seedString.deleteCharAt(seedString.length() - 1);
                    inputCommands.append(c);
                    drawHUD("Seed: " + seedString + " (Press 's' to start)");
                }
            }
        }
        return seedString.toString();
    }



    private int[] processSavedMovements(String movements) {
        int[] startingPosition = getStartingPosition(tiles);
        int[] finalPosition = new int[]{startingPosition[0], startingPosition[1]};
        boolean prevCharDigit = false;

        for (int i = 0; i < movements.length(); i++) {
            char c = movements.charAt(i);
            if (c == ':' || c == 'q') {
                continue; // ignore these characters
            }
            if (Character.isDigit(c)) {
                prevCharDigit = true;
                continue;
            }
            if (c == 's' && prevCharDigit) {
                prevCharDigit = false;
                continue;
            }
            switch (c) {
                case 't':
                    TCOUNTER++;
                    System.out.println("tc=" + TCOUNTER);
                    break;
                case 'w':
                    if (generator.getTiles()[finalPosition[0]][finalPosition[1] + 1] == Tileset.WALL) {
                        AVATARHEALTH--;
                        continue;
                    }
                    finalPosition[1]++;
                    break;
                case 'a':
                    if (generator.getTiles()[finalPosition[0] - 1][finalPosition[1]] == Tileset.WALL) {
                        AVATARHEALTH--;
                        continue;
                    }
                    finalPosition[0]--;
                    break;
                case 's':
                    if (generator.getTiles()[finalPosition[0]][finalPosition[1] - 1] == Tileset.WALL) {
                        AVATARHEALTH--;
                        continue;
                    }
                    finalPosition[1]--;
                    break;
                case 'd':
                    if (generator.getTiles()[finalPosition[0] + 1][finalPosition[1]] == Tileset.WALL) {
                        AVATARHEALTH--;
                        continue;
                    }
                    finalPosition[0]++;
                    break;
                default:
                    break;
            }
            prevCharDigit = false;
        }
        return finalPosition;
    }


    private Avatar createAvatar(TETile[][] world, int startX, int startY, int hp, int tc) {
        Avatar myavatar = new Avatar(startX, startY, Tileset.AVATAR, hp, 5);
        world[startX][startY] = Tileset.AVATAR; // Set the avatar tile in the world
        if (tc % 2 == 0) {
            myavatar.initializeDarkness(generator.getTiles());
            myavatar.setVisual(generator.getTiles());
            myavatar.nightVision = false;
        } else {
            myavatar.setVisual(generator.getTiles());
            myavatar.nightVision = true;
        }
        return myavatar;
    }

    static long extractSeed(String input) {
        StringBuilder digits = new StringBuilder();
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == 's') {
                break;
            }
            digits.append(input.charAt(i));
        }
        return Long.parseLong(digits.toString());
    }


    public void renderFrame(TETile[][] tt) {
        StdDraw.clear();
        int width = tt.length;
        int height = tt[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tt[x][y] != null) {
                    tt[x][y].draw(x, y);
                }
            }
        }

        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        if (mouseX >= 0 && mouseX < width && mouseY >= 0 && mouseY < height && tt[mouseX][mouseY] != null) {
            StdDraw.setPenColor(StdDraw.WHITE);
            String description = tt[mouseX][mouseY].description();
            StdDraw.text(5, height - 2, description);
        }

        updateGameHUD();
        displayDateTime();
        StdDraw.show();
    }


    /**IDEAS BROUGHT UP BY XIANGCHEN, CODIFIED BY CHATGPT/
     *
     */
    public void updateGameHUD() {
        // Update hudText with game state information
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, "Location: (" + avatar.getX() + ", " + avatar.getY() + ")");
        StdDraw.textRight(TEXTPOSITION, HEIGHT - 1, "HealthRemaining:  " + avatar.getHealth() + "");
        checkGameOver();


        // Draw the updated HUD on the screen
    }

    private void drawGameOverHUD() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, "Game Over!");
        StdDraw.text(midWidth, midHeight - 2, "Press 'n' for New Game");
        gameOver = true;

    }
    /** @ChatGpt Provided most api and syntax for the datentime methods, Xiangchen adjusted
    them to fit this specific game implementation**/
    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private void displayDateTime() {
        if (!gameOver) { // check if the game is not over
            String dateTimeString = getCurrentDateTime();
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textRight(TIME_POSITION, HEIGHT - 3, dateTimeString);

        }
    }

    private void checkGameOver() {
        if (avatar.getHealth() == 0) {
            StdDraw.clear(Color.BLACK);
            gameOver();
            inGame = false;

        }
    }

    private void gameOver() {
        SaveNLoad.clearFile();
        drawGameOverHUD();
    }

    private void loadSavedGame(String savedGameString, boolean loadable) {
        if (loadable && savedGameString != null && !savedGameString.isEmpty()) {
            inputCommands.append('l');
            gameOver = false;
            inGame = true;
            String savedGameString1 = SaveNLoad.loadGame();
            long seedl = extractSeed(savedGameString1);
            generator = new Generator(seedl);
            String movements = savedGameString1.substring(savedGameString1.indexOf('s') + 1);
            int[] avatarPosition = processSavedMovements(movements);
            avatar = createAvatar(generator.getTiles(), avatarPosition[0], avatarPosition[1], AVATARHEALTH, TCOUNTER);
            if (generator != null) {
                this.renderFrame(generator.getTiles());
            }
        }
    }


}
