package bg.sofia.uni.fmi.mjt.escaperoom.main;

import bg.sofia.uni.fmi.mjt.escaperoom.EscapeRoomPlatform;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Difficulty;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Theme;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;
import bg.sofia.uni.fmi.mjt.escaperoom.team.TeamMember;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String... args) throws RoomAlreadyExistsException, RoomNotFoundException, TeamNotFoundException {
        TeamMember member1 = new TeamMember("Pesho", LocalDateTime.of(1988, 12, 22, 0, 0));
        TeamMember member2 = new TeamMember("Vesela", LocalDateTime.of(1988, 12, 15, 0, 0));
        TeamMember member3 = new TeamMember("Maria", LocalDateTime.of(1990, 7, 17, 0, 0));
        TeamMember member4 = new TeamMember("Tanya", LocalDateTime.of(1991, 8, 12, 0, 0));

        Team team1 = Team.of("Prisoners", new TeamMember[]{member1, member2});
        Team team2 = Team.of("Escapers", new TeamMember[]{member3, member4});

        System.out.println(team1.getRating()); // 0.0
        team1.updateRating(3);
        System.out.println(team1.getRating()); // 3.0

        EscapeRoom escapeRoom1 = new EscapeRoom("Bornazium", Theme.HORROR, Difficulty.HARD, 3, 80, 8);
        EscapeRoom escapeRoom2 = new EscapeRoom("MindQuest", Theme.SCIFI, Difficulty.MEDIUM, 2, 90, 7);
        EscapeRoom escapeRoom3 = new EscapeRoom("Enigma", Theme.FANTASY, Difficulty.EXTREME, 1, 70, 9);

        EscapeRoom escapeRoom4 = new EscapeRoom("Enigma", Theme.FANTASY, Difficulty.EXTREME, 1, 70, 9);

        EscapeRoomPlatform platform = new EscapeRoomPlatform(new Team[]{team1, team2}, 10);

        platform.addEscapeRoom(escapeRoom1);
        platform.addEscapeRoom(escapeRoom2);
        System.out.println(Arrays.toString(platform.getAllEscapeRooms()));

        try {
            platform.addEscapeRoom(escapeRoom1);
        } catch (RoomAlreadyExistsException ree) {
            System.out.println("RoomAlreadyExistsException exception caught");
        }

        platform.reviewEscapeRoom("Bornazium", new Review(10, "It was a lot of fun. I like it very much"));
        platform.reviewEscapeRoom("Bornazium", new Review(6, "Not impressed. So-so room. It could be improved."));
        System.out.println(Arrays.toString(platform.getReviews("Bornazium")));

        System.out.println(platform.getTopTeamByRating());


        System.out.println(team1.getRating());
        platform.registerAchievement("Bornazium", "Prisoners", 2);
        System.out.println(team1.getRating()); // 3(previous) + 3(Hard) + 1(bonus) = 8
    }
}
