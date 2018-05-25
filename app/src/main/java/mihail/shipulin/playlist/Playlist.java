package mihail.shipulin.playlist;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

/**
 * Created by shipulin.mihail on 07.03.2018.
 */

public class Playlist {
    private boolean isPlaying;
    private boolean isShuffle;
    private int repeatMode;
    private int currentPosition;

    public void nextSong(MediaPlayer mediaPlayer) {
       // int numOfSong = songList.size();

//        if (!isShuffle) { // Shuffle mode is off
//            if (currentPosition < numOfSong - 1) {
//                currentPosition++;
//                currentSong = songList.get(currentPosition);
//                Log.d("my_log", "position = "+currentPosition);
//            } else {
//                currentPosition = 0;
//                currentSong = songList.get(currentPosition);
//                Log.d("my_log", "position = "+currentPosition);
//            }
//        } else { // Shuffle mode is on
//            Random rand = new Random();
//            currentPosition = rand.nextInt(numOfSong);
//            currentSong = songList.get(currentPosition);
//            Log.d("my_log", "position = "+currentPosition);
//        }
        playBackMusic(mediaPlayer);
    }
    public void playBackMusic(final MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.release();
            //mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    endOfTheSong(mediaPlayer);
                }
            });
            isPlaying = true;
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void endOfTheSong(MediaPlayer mediaPlayer) {
        if (repeatMode == 1) { // currently repeat one song
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else if (repeatMode == 2) { // currently repeat all songs
            nextSong(mediaPlayer);
        } else { // currently no repeat
           // if (currentPosition != songList.size() - 1) nextSong();
        }
    }
}
