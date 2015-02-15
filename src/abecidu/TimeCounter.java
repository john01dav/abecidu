package abecidu;

public class TimeCounter {
    private long start;

    public TimeCounter(){
        start = System.currentTimeMillis();
    }

    public long getElapsedTime(){
        return System.currentTimeMillis() - start;
    }

}
