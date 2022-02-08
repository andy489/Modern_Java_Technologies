package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist {

    public static final int PLAYLIST_CAPACITY = 20;

    private final int capacity;
    private int size;
    private final String name;
    private final Playable[] content;

    public UserPlaylist(String name) {
        this.name = name;
        this.content = new Playable[capacity];
    }

    {
        this.size = 0;
        this.capacity = PLAYLIST_CAPACITY;
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        /**
         * if we want to restrict uploading the same playable -
         * uncomment here
         */

//        for (int i = 0; i < size; ++i) {
//            if (content[i].equals(playable)) {
//                return;
//            }
//        }

        if (size >= capacity) { // only == will also work
            throw new PlaylistCapacityExceededException("Playlist " + name + " limit exceeded.");
        }

        content[size++] = playable;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
