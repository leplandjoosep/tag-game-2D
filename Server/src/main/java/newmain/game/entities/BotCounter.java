package newmain.game.entities;

import java.util.Random;

public class BotCounter {
    static int count = -1;
    static int randomNum = 1;

    public static int getCount() {
        count++;
        return count;
    }
    public static void setCount() {
        count = -1;
    }

    public static int makeNewRandomNum() {
        Random rand = new Random();
        randomNum = rand.nextInt(4) + 1;
        return randomNum;
    }
    public static int getRandomNum() {
        return randomNum;
    }
}
