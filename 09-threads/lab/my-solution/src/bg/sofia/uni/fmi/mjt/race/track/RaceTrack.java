package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RaceTrack implements Track {

    private final Pit racePit;
    private final List<Integer> finishedCarsIds;
    private final AtomicInteger finishedCarsCnt;

    public RaceTrack(int pitTeamsCount) {
        this.racePit = new Pit(pitTeamsCount);
        this.finishedCarsCnt = new AtomicInteger();
        this.finishedCarsIds = new ArrayList<>();
    }

    @Override
    public void enterPit(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Invalid car arg");
        }

        if (car.getNPitStops() == 0) {
            synchronized (finishedCarsIds) {
                finishedCarsIds.add(car.getCarId());
            }

            finishedCarsCnt.getAndIncrement();
        } else {
            racePit.submitCar(car);
        }
    }

    @Override
    public int getNumberOfFinishedCars() {
        return finishedCarsCnt.get();
    }

    @Override
    public List<Integer> getFinishedCarsIds() {
        return finishedCarsIds;
    }

    @Override
    public Pit getPit() {
        return racePit;
    }
}
