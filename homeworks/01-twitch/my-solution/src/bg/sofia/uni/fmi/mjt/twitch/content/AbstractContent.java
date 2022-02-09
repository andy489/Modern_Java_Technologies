package bg.sofia.uni.fmi.mjt.twitch.content;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.util.Objects;

public abstract class AbstractContent implements Content {
    private final Metadata metadata;

    public AbstractContent(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public abstract Duration getDuration();

    @Override
    public abstract void startWatching(User user);

    @Override
    public abstract void stopWatching(User user);

    @Override
    public abstract int getNumberOfViews();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AbstractContent)) {
            return false;
        }

        return ((AbstractContent) obj).getMetadata().equals(this.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata);
    }
}
