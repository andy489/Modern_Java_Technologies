package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.PitTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RaceTrackTest {

    private static final int TEAMS = 4;
    private static final int CARS = 4;
    private static final int SERVICES_UPPER_BOUND = 3;

    private static final int SERVICE_TIME = 10;
    private static final int RACE_TIME = 50;

    private Track raceTrack;
    private Random rand;

    @BeforeEach
    public void setUp() {
        raceTrack = new RaceTrack(TEAMS);
        rand = new Random();
    }

    @Test
    public void testRace() throws InterruptedException {
        List<Integer> randPitStops = rand.ints(CARS, 0, SERVICES_UPPER_BOUND)
                .boxed()
                .toList();

        int totalPitStops = randPitStops.stream().mapToInt(r -> r).sum();

        List<Thread> raceCars = IntStream.range(0, CARS)
                .mapToObj(id -> new Thread(new Car(id, randPitStops.get(id), raceTrack)))
                .toList();

        raceCars.forEach(Thread::start);

        Thread.sleep((SERVICE_TIME + RACE_TIME) * SERVICES_UPPER_BOUND);

        assertEquals(CARS, raceTrack.getNumberOfFinishedCars());
        assertTrue(raceTrack.getFinishedCarsIds().containsAll(
                IntStream.range(0, CARS).boxed().toList()
        ));

        assertEquals(totalPitStops, raceTrack.getPit().getPitStopsCount());

        for (PitTeam team : raceTrack.getPit().getPitTeams()) {
            totalPitStops -= team.getPitStoppedCars();
        }

        assertEquals(0, totalPitStops);

        raceTrack.getPit().finishRace();
    }
}
