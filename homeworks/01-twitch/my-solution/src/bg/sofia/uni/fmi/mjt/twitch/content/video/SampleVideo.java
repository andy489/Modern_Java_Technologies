package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

public class SampleVideo extends AbstractContent implements Video {

    private final Duration duration;
    private int totalViews;

    public SampleVideo(Metadata metadata, Duration duration) {
        super(metadata);
        this.duration = duration;
        totalViews = 0;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void startWatching(User user) {
        ++totalViews;
    }

    @Override
    public void stopWatching(User user) {
        // Do nothing
    }

    @Override
    public int getNumberOfViews() {
        return totalViews;
    }
}
