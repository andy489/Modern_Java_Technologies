package bg.sofia.uni.fmi.mjt.race.track.pit;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Pit {
    private final AtomicInteger pitStopsCnt;
    private final Queue<Car> pitBoxedCars;

    private final List<PitTeam> pitTeams;
    private boolean finish = false;

    public Pit(int nPitTeams) {
        this.pitStopsCnt = new AtomicInteger();
        this.pitBoxedCars = new ArrayDeque<>();

        this.pitTeams = IntStream.range(0, nPitTeams)
                .mapToObj(i -> new PitTeam(i, this))
                .toList();

        pitTeams.forEach(PitTeam::start);
    }

    public void submitCar(Car car) {
        pitStopsCnt.getAndIncrement();

        synchronized (pitBoxedCars) {
            pitBoxedCars.add(car);
            pitBoxedCars.notifyAll();
        }
    }

    public Car getCar() {
        synchronized (pitBoxedCars) {
            while (pitBoxedCars.isEmpty() && !finish) {
                try {
                    pitBoxedCars.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return pitBoxedCars.isEmpty() ? null : pitBoxedCars.poll();
        }
    }

    public int getPitStopsCount() {
        return pitStopsCnt.get();
    }

    public List<PitTeam> getPitTeams() {
        return pitTeams;
    }

    public void finishRace() {
        finish = true;

        synchronized (pitBoxedCars) {
            pitBoxedCars.notifyAll();
        }
    }
}