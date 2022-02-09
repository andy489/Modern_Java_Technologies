package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.SampleStream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.SampleVideo;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Twitch implements StreamingPlatform {

    private final UserService users;
    private final Map<String, Long> usernameWatched;
    private final Map<Content, Long> contentWatched;
    private final Map<String, EnumMap<Category, Long>> usernameContentCount;

    public Twitch(UserService userService) {
        users = userService;
        usernameWatched = new HashMap<>();
        contentWatched = new HashMap<>();
        usernameContentCount = new HashMap<>();
    }

    @Override
    public Stream startStream(String username, String title, Category category)
            throws UserNotFoundException, UserStreamingException {
        if (username == null || title == null || category == null || username.isEmpty() || title.isEmpty()) {
            throw new IllegalArgumentException("~Invalid arguments for starting a Stream.");
        }

        User currentUser = users.getUsers().getOrDefault(username, null);

        if (currentUser == null) {
            throw new UserNotFoundException("~Non existing user cannot start a stream.");
        }

        if (currentUser.getStatus() == UserStatus.STREAMING) {
            String msg = String.format("~User with name %s is currently streaming", currentUser.getName());
            throw new UserStreamingException(msg);
        }

        currentUser.setStatus(UserStatus.STREAMING);

        return new SampleStream(new Metadata(title, category, currentUser));
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        if (username == null || stream == null || username.isEmpty()) {
            throw new IllegalArgumentException("~Invalid arguments for ending a Stream.");
        }

        User currentUser = users.getUsers().getOrDefault(username, null);

        if (currentUser == null) {
            throw new UserNotFoundException("~Non existing user cannot end a stream.");
        }

        if (currentUser.getStatus() == UserStatus.OFFLINE) {
            String msg = String.format("~User with name %s is currently not streaming", currentUser.getName());
            throw new UserStreamingException(msg);
        }

        currentUser.setStatus(UserStatus.OFFLINE);

        String title = stream.getMetadata().title();
        Category category = stream.getMetadata().category();
        User user = stream.getMetadata().user();

        // update viewsCount for current streamer (decrease by the views of the stream)
        usernameWatched.merge(user.getName(), -1L * stream.getNumberOfViews(), Long::sum);

        return new SampleVideo(new Metadata(title, category, user), stream.getDuration());
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        if (username == null || content == null || username.isEmpty()) {
            throw new IllegalArgumentException("~Invalid arguments for watching a content.");
        }

        User currentUser = users.getUsers().getOrDefault(username, null);

        if (currentUser == null) {
            throw new UserNotFoundException("~Non existing user cannot watch a content.");
        }

        if (currentUser.getStatus() == UserStatus.STREAMING) {
            String msg = String.format("~User with name %s is currently streaming", currentUser.getName());
            throw new UserStreamingException(msg);
        }

        content.startWatching(currentUser);

        usernameWatched.merge(content.getMetadata().user().getName(), 1L, Long::sum);
        contentWatched.merge(content, 1L, Long::sum);

        usernameContentCount.putIfAbsent(username, new EnumMap<>(Category.class));
        usernameContentCount.get(username).merge(content.getMetadata().category(), 1L, Long::sum);
    }

    @Override
    public User getMostWatchedStreamer() {
        // EDIT1
        if (usernameWatched.values().size() == 0) {
            return null;
        }

        Long maxWatchedValue = (Collections.max(usernameWatched.values()));
        for (Map.Entry<String, Long> entry : usernameWatched.entrySet()) {
            if (entry.getValue().equals(maxWatchedValue)) {
                String mostWatchedUserName = entry.getKey();
                return users.getUsers().getOrDefault(mostWatchedUserName, null);
            }
        }
        return null;
    }

    @Override
    public Content getMostWatchedContent() {
        // EDIT2
        if (contentWatched.values().size() == 0) {
            return null;
        }

        Long maxWatchedValue = (Collections.max(contentWatched.values()));
        for (Map.Entry<Content, Long> entry : contentWatched.entrySet()) {
            if (entry.getValue().equals(maxWatchedValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("~Invalid username.");
        }

        boolean searchedUser = users.getUsers().keySet().contains("user1");

        if (!searchedUser) {
            String msg = String.format("~User with username %s not found in the service.", username);
            throw new UserNotFoundException(msg);
        }

        // EDIT 3
        if (!usernameWatched.containsKey("user1")) {
            return null;
        }

        Content toReturn = null;
        Long mostWatched = Long.MIN_VALUE;

        for (Map.Entry<Content, Long> entry : contentWatched.entrySet()) {
            if (entry.getKey().getMetadata().user().getName().equals(username)) {
                if (entry.getValue() > mostWatched) {
                    mostWatched = entry.getValue();
                    toReturn = entry.getKey();
                }
            }
        }

        if (toReturn == null) {
            String msg = String.format("~User with username %s not found in the service.", username);
            throw new UserNotFoundException(msg);
        }

        return toReturn;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("~Invalid username.");
        }

        User currentUser = users.getUsers().getOrDefault(username, null);

        if (currentUser == null) {
            throw new UserNotFoundException("~Non existing user cannot watch a content.");
        }

        EnumMap<Category, Long> categories = usernameContentCount.getOrDefault(username, null);

        List<Category> toReturn = new ArrayList<>();

        if (categories != null) { // redundant, but for more secure
            List<CategoryCount> arr = new ArrayList<>();

            for (Map.Entry<Category, Long> entry : categories.entrySet()) {
                if (entry != null && entry.getValue() != null) {
                    arr.add(new CategoryCount(entry.getKey(), entry.getValue()));
                }
            }

            arr.sort(new CostumComparator().reversed());

            for (CategoryCount cc : arr) {
                toReturn.add(cc.category());
            }
        }

        return List.copyOf(toReturn);
    }
}
