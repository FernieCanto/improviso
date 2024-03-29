package improviso;

import java.util.Random;

/**
 *
 * @author fernando
 */
public class NumericInterval {
    public int value, valueMax, valueEnd, valueEndMax;
    public Integer resolution = null;
    Random rand = null;
    
    NumericInterval(int val, int valMax, int valEnd, int valEndMax) {
        this.value = val;
        this.valueMax = valMax;

        this.valueEnd = valEnd;
        this.valueEndMax = valEndMax;
    }
    
    NumericInterval(int val, int valMax, int valEnd, int valEndMax, int res) {
        this.value = val;
        this.valueMax = valMax;

        this.valueEnd = valEnd;
        this.valueEndMax = valEndMax;
        
        this.resolution = res;
    }
    
    public void setSeed(long seed) {
        if(rand == null)
            rand = new Random(seed);
        else
            rand.setSeed(seed);
    }
    
    protected Random getRandom() {
        if(rand == null)
            rand = new Random();
        return rand;
    }
    
    int getValue() {
        return getValue(0.0, getRandom());
    }
    
    int getValue(float position) {
        return getValue(position, getRandom());
    }
    
    int getValue(Random rand) {
        return getValue(0.0, rand);
    }
    
    int getValue(double position, Random rand) {
        int var = valueMax - value;
        int varEnd = valueEndMax - valueEnd;
        int returnVal;
        
        returnVal  = value + (int)( (valueEnd - value) * position );
        returnVal += rand.nextInt(var + (int)( (varEnd - var) * position) + 1);
        
        if(resolution != null) {
            int delta = (returnVal - value) % resolution;
            
            if(delta < (int)(resolution / 2))
                returnVal -= delta;
            else
                returnVal += resolution - delta;
        }
        
        return returnVal;
    }
}
