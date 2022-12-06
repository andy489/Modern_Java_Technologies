package sealed;

public sealed interface Exercise permits GymWorkout, Jogging, Swimming {

    int getCaloriesBurnt();

}
