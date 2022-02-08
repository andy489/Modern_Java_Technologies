package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.concurrent.ThreadLocalRandom;

public class PitTeam extends Thread {
    private static final int SERVICE_UPPER_BOUND = 10;

    private int pitStoppedCars;
    private final int id;
    private final Pit pitStop;

    public PitTeam(int id, Pit pitStop) {
        this.pitStop = pitStop;
        this.id = id;
    }

    @Override
    public void run() {
        Car car;

        while ((car = pitStop.getCar()) != null) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(SERVICE_UPPER_BOUND));
                pitStoppedCars++; // not a shared state

                Car currCar = new Car(car.getCarId(), car.getNPitStops() - 1, car.getTrack());
                Thread currService = new Thread(currCar);

                currService.setName("car" + id);
                currService.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPitStoppedCars() {
        return pitStoppedCars;
    }
}