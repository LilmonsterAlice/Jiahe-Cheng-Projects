package byow.Core;


import byow.TileEngine.TETile;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class test {

    @Test

    public void agTest() {

        Engine e1 = new Engine();

        Engine e2 = new Engine();

        TETile[][] result = e1.interactWithInputString("n5643591630821615871swwaawd");

        TETile[][] result2 = e2.interactWithInputString("n7313251667695476404sasd");

        System.out.println("Are two worlds equal? "+TETile.toString(result).equals(TETile.toString(result2)));

        Engine engine = new Engine();
        TETile[][] world = engine.interactWithInputString("n1392967723524655428sddsaawwsaddws");
        Engine engine2 = new Engine();
        engine2.interactWithInputString("n1392967723524655428sddsaawwss:q");
        TETile[][] world2 = engine2.interactWithInputString("laddw");
        System.out.println("Are two worlds equal? "+TETile.toString(world).equals(TETile.toString(world2)));

    }


}