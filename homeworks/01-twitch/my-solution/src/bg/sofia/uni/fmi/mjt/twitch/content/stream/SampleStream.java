package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class SampleStream extends AbstractContent implements Stream {
    private final LocalDateTime timeStamp;
    private final Set<User> watchingNow;
    private int totalViews;

    public SampleStream(Metadata metadata) {
        super(metadata);
        totalViews = 0;
        timeStamp = LocalDateTime.now();
        watchingNow = new HashSet<>();
    }

    @Override
    public Duration getDuration() {
        return Duration.between(LocalDateTime.now(), timeStamp);
    }

    @Override
    public void startWatching(User user) {
        ++totalViews;
        watchingNow.add(user);
    }

    @Override
    public void stopWatching(User user) {
        --totalViews;
        //watchingNow.remove(user);
    }

    @Override
    public int getNumberOfViews() {
        return totalViews;
        //return watchingNow.size();
    }
}
