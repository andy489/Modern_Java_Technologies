package bg.sofia.uni.fmi.mjt.twitch;

import java.util.List;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;

public interface StreamingPlatform {

    /**
     * Starts a new {@link Stream} and returns a reference to it.
     *
     * @param username the username of the streamer
     * @param title    the title of the stream
     * @param category the {@link Category} of the stream
     * @return the started {@link Stream}
     * @throws IllegalArgumentException if any of the parameters are null or if strings are empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     * @throws UserStreamingException   if a user with this username is currently
     *                                  streaming
     */
    Stream startStream(String username, String title, Category category)
            throws UserNotFoundException, UserStreamingException;

    /**
     * Ends an existing {@link Stream} and returns a new {@link Video} which was
     * made of it.
     *
     * @param username the username of the streamer
     * @param stream   the stream to end
     * @return the created {@link Video}
     * @throws IllegalArgumentException if any of the parameters are null or if {@code username} is empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     * @throws UserStreamingException   if a user with this username is currently not
     *                                  streaming
     */
    Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException;

    /**
     * Watches a content.
     *
     * @param username the username of the watcher
     * @param content  the content to watch
     * @throws IllegalArgumentException if any of the parameters are null or if {@code username} is empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     * @throws UserStreamingException   if the user with the specified username is
     *                                  currently streaming
     */
    void watch(String username, Content content) throws UserNotFoundException, UserStreamingException;

    /**
     * Returns the {@link User} which has the most watched {@link Content} in the
     * service.
     *
     * @return the {@link User} which has the most watched {@link Content} in the
     * service
     */
    User getMostWatchedStreamer();

    /**
     * Returns the {@link Content} which has the most views in the service.
     *
     * @return the {@link Content} which has the most views in the service.
     */
    Content getMostWatchedContent();

    /**
     * Returns the {@link Content} from user with name username which has the most
     * views in the service.
     *
     * @return the {@link Content} from user with name username which has the most
     * views in the service.
     * @throws IllegalArgumentException if {@code username} is null or empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     */
    Content getMostWatchedContentFrom(String username) throws UserNotFoundException;

    /**
     * Returns an immutable copy of a sorted list of the watched categories by user
     * with name username in descending order of the count
     *
     * @param username
     * @return an immutable copy of a sorted list of the watched categories by user
     * with name username in descending order of the count
     * @throws IllegalArgumentException if {@code username} is null or empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     */
    List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException;

}
