package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class UserLibrary implements Library {

    private static final int CONTENT_CAPACITY = 21;

    private final int capacity;
    private final Playlist[] content;
    private int size;

    {
        this.capacity = CONTENT_CAPACITY;
    }

    public UserLibrary() {
        content = new Playlist[capacity];
        content[0] = new UserPlaylist("Liked Content");
        size = 1;
    }

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        /**
         * if we want to restrict uploading the same playlist -
         * uncomment here
         */

//        for (int i = 0; i < size; ++i) {
//            if (content[i].equals(playlist)) {
//                return;
//            }
//        }

        if (size == capacity) {
            throw new LibraryCapacityExceededException();
        }

        content[size++] = playlist;
    }

    @Override
    public void remove(String name) throws EmptyLibraryException, PlaylistNotFoundException {
        if (name.equals(content[0].getName())) {
            throw new IllegalArgumentException("\"Liked Content\" playlist is indelible");
        }
        int indOfPlaylistToRemove = 0;
        for (int i = 1; i < capacity; ++i) {
            if (content[i] != null && name.equals(content[i].getName())) {
                indOfPlaylistToRemove = i;
                break;
            }
        }
        if (indOfPlaylistToRemove == 0) {
            throw new PlaylistNotFoundException("Playlist " + name + " not found");
        }

        if (size - 1 - indOfPlaylistToRemove >= 0) {
            System.arraycopy(content, indOfPlaylistToRemove + 1,
                    content, indOfPlaylistToRemove, size - 1 - indOfPlaylistToRemove);
        }

        --size;
    }

    @Override
    public Playlist getLiked() {
        return content[0];
    }

}
