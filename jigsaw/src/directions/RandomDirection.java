package directions;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomDirection {

    public static final int NORTH = 3,
                            EAST  = 1,
                            SOUTH = 2,
                            WEST  = 0;

    public static final int FULL_RANDOM=0,
                            PERM_RANDOM=1;



    //Direction can be added to a point to move in one of the 4 caridinal directions
    private final static Point[] DIRECTIONS = { new Point(0, -1),
                                                new Point(1, 0),
                                                new Point(0, 1),
                                                new Point(-1, 0)   };
    //Perm is used to index into direction.
    //It contains indecies into directions and can be shuffled before use to give a random direction.
    private ArrayList<Integer> perm;

    private int randomizerMode = FULL_RANDOM;

    private int cur;

    private Random rand = new Random(System.currentTimeMillis());

    /**
     * Constructs a new Random direction in full random mode
     */
    public RandomDirection(){

        perm = new ArrayList<>();
        for(int i=0; i<4; i++)
        {
            perm.add(i);
        }
    }

    /**
     * Constructs a new RandomDirection int the given random mode
     * @param randomizerMode
     */
    public RandomDirection(int randomizerMode)
    {
        setRandomMode(randomizerMode);

        perm = new ArrayList<>();
        for(int i=0; i<4; i++)
        {
            perm.add(i);
        }
        Collections.shuffle(perm);
    }

    /**
     * Sets the randomizer mode
     * @param randomizerMode
     */
    public void setRandomMode(int randomizerMode){
        if(randomizerMode>=0 && randomizerMode <3)
        {
            this.randomizerMode = randomizerMode;
        }
    }

    
    /**
     * Returns the the coordinate of the point 1 away from the origin in the given direction
     * @param direction The direction to move in, Use static members
     * @return
     */
    public Point getDirectionCoordinate(int direction)
    {
        if(direction>=0 && direction<4)
            return DIRECTIONS[direction];
        else
            return null;
    }

    public Point getNextRandomDirection()
    {
        if(randomizerMode == FULL_RANDOM)
        {
            return getNextFullRandomDir();
        }
        else if ( randomizerMode == PERM_RANDOM)
        {
            return getNextPermRandom();
        }
        return null;
    }

    private Point getNextFullRandomDir()
    {
        return DIRECTIONS[rand.nextInt(4)];
    }
    private Point getNextPermRandom()
    {
        Point p = DIRECTIONS[cur];
        cur++;
        cur%=4;
        if(cur == 0)
            Collections.shuffle(perm);
        return p;
    }


}
