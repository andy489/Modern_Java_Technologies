package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.DefaultUser;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.Service;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitchTest {

    Map<String, User> userService;
    Twitch twitchStub;

    @Before
    public void createUserServices() {
        userService = new HashMap<>();
        userService.put("user1", new DefaultUser("user1", UserStatus.OFFLINE));
        userService.put("user2", new DefaultUser("user2", UserStatus.OFFLINE));
        userService.put("user3", new DefaultUser("user3", UserStatus.OFFLINE));
        userService.put("user4", new DefaultUser("user4", UserStatus.OFFLINE));
        userService.put("user5", new DefaultUser("user5", UserStatus.OFFLINE));

        twitchStub = new Twitch(new Service(userService));
    }

    @Test
    public void testGetMostWatchedNoWatches() {
        userService = new HashMap<>();
        twitchStub = new Twitch(new Service(userService));
        assertNull(twitchStub.getMostWatchedStreamer());
    }

    @Test
    public void testGetMostWatchedWithNoContent() {
        userService = new HashMap<>();
        twitchStub = new Twitch(new Service(userService));
        assertNull(twitchStub.getMostWatchedContent());
    }

    @Test
    public void testGetMostWatchedFromWithNoContent() throws UserNotFoundException {
        userService = new HashMap<>();
        userService.put("user1", new DefaultUser("user1", UserStatus.OFFLINE));
        twitchStub = new Twitch(new Service(userService));
        assertNull(twitchStub.getMostWatchedContentFrom("user1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartStreamWithNullUsername() throws UserNotFoundException, UserStreamingException {
        twitchStub.startStream(null, "dummyTitle", Category.GAMES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartStreamWithEmptyUsername() throws UserNotFoundException, UserStreamingException {
        twitchStub.startStream("", "dummyTitle", Category.IRL);
    }

    @Test(expected = UserNotFoundException.class)
    public void testStartStreamWithNonExistingUser() throws UserNotFoundException, UserStreamingException {
        twitchStub.startStream("someUser", "dummyTitle", Category.ESPORTS);
    }

    @Test(expected = UserStreamingException.class)
    public void testStartStreamWhileStreaming1() throws UserNotFoundException, UserStreamingException {
        twitchStub.startStream("user1", "dummyTitle", Category.MUSIC);
        twitchStub.startStream("user1", "anotherStream", Category.ESPORTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndStreamingWithNullStream() throws UserNotFoundException, UserStreamingException {
        twitchStub.endStream("user1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndStreamingWithEmptyUsername() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.IRL);
        twitchStub.endStream("", currStream);
    }

    @Test(expected = UserNotFoundException.class)
    public void testEndStreamingWithNonExistingStreamer() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.IRL);
        twitchStub.endStream("nonExistingStreamer", currStream);
    }

    @Test(expected = UserStreamingException.class)
    public void testEndStreamingWhileNotStreaming() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.IRL);
        Video newVideo = twitchStub.endStream("user1", currStream);
        twitchStub.endStream("user1", currStream);
    }

    @Test
    public void testEndStreamingAndTransformingToVideo() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.IRL);

        Content content = twitchStub.endStream("user1", currStream);

        assertNotNull(content);
        assertEquals(0, content.getNumberOfViews());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWatchWithNullUsername() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.IRL);
        Content content = twitchStub.endStream("user1", currStream);
        twitchStub.watch(null, content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWatchWithNullContent() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.IRL);
        Content content = twitchStub.endStream("user1", currStream);
        twitchStub.watch("user2", null);
    }

    @Test(expected = UserNotFoundException.class)
    public void testWatchWithNonExistingUser() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.IRL);
        Content content = twitchStub.endStream("user1", currStream);
        twitchStub.watch("nonExistingUserName", content);
    }

    @Test(expected = UserStreamingException.class)
    public void testWatchWhileStreaming() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.ESPORTS);
        twitchStub.watch("user1", currStream);
    }

    @Test
    public void testWatchingCount() throws UserNotFoundException, UserStreamingException {
        Stream currStream = twitchStub.startStream("user1", "dummyTitle", Category.ESPORTS);
        assertEquals(0, currStream.getNumberOfViews());

        twitchStub.watch("user2", currStream);
        assertEquals(1, currStream.getNumberOfViews());

        Video newVideo = twitchStub.endStream("user1", currStream);
        assertEquals(0, newVideo.getNumberOfViews());

        twitchStub.watch("user1", newVideo);
        assertEquals(1, newVideo.getNumberOfViews());

        for (int i = 0; i < 9; ++i) {
            twitchStub.watch("user2", newVideo);
        }

        assertEquals(10, newVideo.getNumberOfViews());
    }

    @Test
    public void testGetMostWatchedStreamer() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitchStub.startStream("user1", "irlStream", Category.IRL);
        twitchStub.watch("user3", firstStream);

        // user 1 has 1 watches
        assertEquals("user1", twitchStub.getMostWatchedStreamer().getName());

        Stream secondStream = twitchStub.startStream("user2", "gamesStream", Category.GAMES);
        twitchStub.watch("user3", secondStream);
        twitchStub.watch("user4", secondStream);

        // user 2 has 2 watches
        assertEquals("user2", twitchStub.getMostWatchedStreamer().getName());

        twitchStub.watch("user4", firstStream);
        twitchStub.watch("user4", firstStream);

        // user 1 has 3 watches
        assertEquals("user1", twitchStub.getMostWatchedStreamer().getName());

        Video firstVideo = twitchStub.endStream("user1", firstStream);

        // user 1 has 0 watches
        assertEquals("user2", twitchStub.getMostWatchedStreamer().getName());

        twitchStub.watch("user5", firstVideo);

        Video secondVideo = twitchStub.endStream("user2", secondStream);

        // user 1 has 1 watch and user 2 has 0 watches
        assertEquals("user1", twitchStub.getMostWatchedStreamer().getName());
    }

    @Test
    public void testGetMostWatchedContent() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitchStub.startStream("user1", "firstStreamTitle", Category.MUSIC);
        twitchStub.watch("user2", firstStream);
        twitchStub.watch("user3", firstStream);
        twitchStub.watch("user4", firstStream);

        assertEquals("firstStreamTitle", twitchStub.getMostWatchedContent().getMetadata().title());

        Stream secondStream = twitchStub.startStream("user4", "secondVideoTitle", Category.GAMES);
        Video newVideoGosho = twitchStub.endStream("user4", secondStream);
        for (int i = 0; i < 5; ++i) {
            twitchStub.watch("user4", secondStream);
        }

        assertEquals("secondVideoTitle", twitchStub.getMostWatchedContent().getMetadata().title());
    }

    @Test
    public void testGetMostWatchedContentFrom() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitchStub.startStream("user1", "firstStream", Category.ESPORTS);
        Video firstVideo = twitchStub.endStream("user1", firstStream);
        twitchStub.watch("user1", firstVideo);
        twitchStub.watch("user2", firstVideo);

        assertEquals("firstStream", twitchStub.getMostWatchedContentFrom("user1").getMetadata().title());

        Stream secondStream = twitchStub.startStream("user2", "secondStream", Category.IRL);
        twitchStub.watch("user1", secondStream);
        twitchStub.watch("user3", secondStream);
        twitchStub.watch("user4", secondStream);

        assertEquals("secondStream", twitchStub.getMostWatchedContentFrom("user2").getMetadata().title());

        twitchStub.watch("user3", firstVideo);
        twitchStub.watch("user4", firstVideo);

        assertEquals("firstStream", twitchStub.getMostWatchedContentFrom("user1").getMetadata().title());
    }


    @Test
    public void testGetMostWatchedCategoriesBy() throws UserNotFoundException, UserStreamingException {
        Stream esportsStream = twitchStub.startStream("user1", "firstStream", Category.ESPORTS);
        Video esportsVideo = twitchStub.endStream("user1", esportsStream);

        Stream musicStream = twitchStub.startStream("user1", "firstStream", Category.MUSIC);
        Video musicVideo = twitchStub.endStream("user1", musicStream);

        Stream gamesStream = twitchStub.startStream("user1", "firstStream", Category.GAMES);
        Video gamesVideo = twitchStub.endStream("user1", gamesStream);

        // 4 watches for ESPORTS
        for (int i = 0; i < 5; ++i) {
            twitchStub.watch("user2", esportsVideo);
        }

        // 3 watches for MUSIC
        for (int i = 0; i < 4; ++i) {
            twitchStub.watch("user2", musicVideo);
        }

        // 8 watches for GAMES
        for (int i = 0; i < 8; ++i) {
            twitchStub.watch("user2", gamesVideo);
        }

        List<Category> orderedListOfCategories = twitchStub.getMostWatchedCategoriesBy("user2");

        assertEquals(3, orderedListOfCategories.size());
        assertEquals(orderedListOfCategories.get(0), Category.GAMES);
        assertEquals(orderedListOfCategories.get(1), Category.ESPORTS);
        assertEquals(orderedListOfCategories.get(2), Category.MUSIC);
    }

    @Test
    public void testEmptyCollectionOfCategories() throws UserNotFoundException {
        List<Category> orderedListOfCategories = twitchStub.getMostWatchedCategoriesBy("user1");
        assertEquals(0, orderedListOfCategories.size());
    }

    @Test
    public void testWithWatchedContentFromOnlyOneCategory() throws UserNotFoundException, UserStreamingException {
        Stream firstEsportsStream = twitchStub.startStream("user1", "firstStream", Category.ESPORTS);
        Video firstVideo = twitchStub.endStream("user1", firstEsportsStream);

        Stream secondEsportsStream = twitchStub.startStream("user2", "secondStream", Category.ESPORTS);
        Video secondVideo = twitchStub.endStream("user2", secondEsportsStream);

        Stream thirdEsportsStream = twitchStub.startStream("user3", "thirdStream", Category.ESPORTS);

        twitchStub.watch("user4", firstVideo);
        twitchStub.watch("user4", secondVideo);
        twitchStub.watch("user4", thirdEsportsStream);

        List<Category> result = twitchStub.getMostWatchedCategoriesBy("user4");

        assertEquals(1, result.size());
        assertTrue(result.contains(Category.ESPORTS));
    }
}

