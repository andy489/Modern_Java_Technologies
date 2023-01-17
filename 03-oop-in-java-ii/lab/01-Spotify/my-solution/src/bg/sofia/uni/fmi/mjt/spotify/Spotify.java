package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class Spotify implements StreamingService {

    private final Account[] accounts;
    private final Playable[] playableContent;

    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playableContent = playableContent;
    }

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        if (account == null || title == null || title.length() == 0) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        Account targetAcc = null;

        for (Account currAcc : accounts) {
            if (account.equals(currAcc)) {
                targetAcc = currAcc;
                break;
            }
        }

        if (targetAcc == null) {
            throw new AccountNotFoundException("Invalid account (" + account.getEmail() + ").");
        }

        Playable targetPlayable = findByTitle(title);
        targetAcc.listen(targetPlayable);
    }

    @Override
    public void like(Account account, String title)
            throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {
        if (account == null || title == null || title.length() == 0) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        Account targetAcc = null;

        for (Account currAcc : accounts) {
            if (account.equals(currAcc)) {
                targetAcc = currAcc;
                break;
            }
        }

        if (targetAcc == null) {
            throw new AccountNotFoundException("Invalid account (" + account.getEmail() + ").");
        }

        Playable targetPlayable = null;
        try {
            targetPlayable = findByTitle(title);
        } catch (PlayableNotFoundException e) {
            throw new PlayableNotFoundException("Content with the specified title is not present in the platform");
        }

        try {
            targetAcc.getLibrary().getLiked().add(targetPlayable);
        } catch (PlaylistCapacityExceededException e) {
            throw new StreamingServiceException("Liked content is full.");
        }
    }


    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        if (title == null || title.length() == 0) {
            throw new IllegalArgumentException("Invalid title");
        }

        Playable playable = null;

        for (Playable plb : playableContent) {
            if (plb.getTitle().equals(title)) {
                playable = plb;
                break;
            }
        }

        if (playable == null) {
            throw new PlayableNotFoundException(title + " not present in the platform.");
        }

        return playable;
    }

    @Override
    public Playable getMostPlayed() {
        final int len = playableContent.length;
        if (len == 0) {
            return null;
        }

        int cntMostPlayed = 0;
        Playable toReturn = null;

        for (Playable playable : playableContent) {
            int currCntPlayed = playable.getTotalPlays();
            if (currCntPlayed > cntMostPlayed) {
                cntMostPlayed = currCntPlayed;
                toReturn = playable;
            }
        }
        return toReturn;
    }

    @Override
    public double getTotalListenTime() {
        double total = 0.0;
        for (Account acc : accounts) {
            if (acc != null) {
                total += acc.getTotalListenTime();
            }
        }
        return total;
    }

    @Override
    public double getTotalPlatformRevenue() {
        double total = 0.0;

        for (Account acc : accounts) {
            if (acc != null) {
                total += acc.revenue();
            }
        }

        return total;
    }

}
