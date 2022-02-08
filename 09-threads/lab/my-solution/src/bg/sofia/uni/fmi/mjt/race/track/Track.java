package bg.sofia.uni.fmi.mjt.race.track;

import java.util.List;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;

public interface Track {

    /**
     * A car enters the pit when it needs maintenance or when it finishes the race.
     * A car can finish the race, if it has no more pitStops to be done.
     *
     * @param car - the car which enters the pit
     */
    void enterPit(Car car);

    /**
     * Returns the number of cars which already finished the race.
     *
     * @return the number of cars which already finished the race
     */
    int getNumberOfFinishedCars();

    /**
     * Returns the ids of the cars which already finished the race.
     *
     * @return the ids of the cars which already finished the race
     */
    List<Integer> getFinishedCarsIds();

    /**
     * Returns the pit.
     *
     * @return the pit
     */
    Pit getPit();

}