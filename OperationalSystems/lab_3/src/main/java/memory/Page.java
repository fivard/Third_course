package memory;

public class Page {
    public int id;
    public int physical;
    public int inMemTime;
    public int lastTouchTime;
    public long aging;

    public Page(int id, int physical, byte R, byte M, int inMemTime, int lastTouchTime) {
        this.id = id;
        this.physical = physical;
        this.inMemTime = inMemTime;
        this.lastTouchTime = lastTouchTime;
        this.aging = 0;
    }

}
