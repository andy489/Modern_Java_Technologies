package bg.sofia.uni.fmi.mjt.race.track;

import java.util.concurrent.ThreadLocalRandom;

public class Car implements Runnable {
    private static final int LAP_TIME_UPPER_BOUND = 50;

    private final int carId;
    private final int nPitStops;
    private final Track track;

    public Car(int id, int nPitStops, Track track) {
        this.carId = id;
        this.nPitStops = nPitStops;
        this.track = track;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(LAP_TIME_UPPER_BOUND));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        track.enterPit(this);
    }

    public int getCarId() {
        return carId;
    }

    public int getNPitStops() {
        return nPitStops;
    }

    public Track getTrack() {
        return track;
    }
}