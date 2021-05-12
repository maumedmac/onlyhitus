package com.lazarowicz.onlyhitus.player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.lazarowicz.onlyhitus.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadioService extends Service implements Player.EventListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.mcakir.radio.player.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.mcakir.radio.player.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.mcakir.radio.player.ACTION_STOP";

    private final IBinder iBinder = new LocalBinder();

    private SimpleExoPlayer exoPlayer;


    private boolean onGoingCall = false;


    private String status;

    private String streamUrl;

    public class LocalBinder extends Binder {
        public RadioService getService() {
            return RadioService.this;
        }
    }





    private MediaSessionCompat.Callback mediasSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            super.onPause();

            pause();
        }

        @Override
        public void onStop() {
            super.onStop();

            stop();


        }

        @Override
        public void onPlay() {
            super.onPlay();

            resume();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        onGoingCall = false;



        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);

        exoPlayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();
        exoPlayer.addListener(this);

//        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        status = PlaybackStatus.IDLE;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if (TextUtils.isEmpty(action))
            return START_NOT_STICKY;





        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        if (status.equals(PlaybackStatus.IDLE))
            stopSelf();

        return super.onUnbind(intent);
    }

//    @Override
//    public void onRebind(final Intent intent) {
//
//    }

    @Override
    public void onDestroy() {

        pause();

        exoPlayer.release();
        exoPlayer.removeListener(this);



        super.onDestroy();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                exoPlayer.setVolume(0.8f);
                resume();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                stop();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (isPlaying()) pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (isPlaying())
                    exoPlayer.setVolume(0.1f);
                break;
        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case Player.STATE_BUFFERING:
                status = PlaybackStatus.LOADING;
                break;
            case Player.STATE_ENDED:
                status = PlaybackStatus.STOPPED;
                break;
            case Player.STATE_READY:
                status = playWhenReady ? PlaybackStatus.PLAYING : PlaybackStatus.PAUSED;
                break;
            default:
                status = PlaybackStatus.IDLE;
                break;
            case Player.STATE_IDLE:
                break;
        }

        if (!status.equals(PlaybackStatus.IDLE))


        EventBus.getDefault().post(status);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(@NotNull ExoPlaybackException error) {

        EventBus.getDefault().post(PlaybackStatus.ERROR);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(@NotNull PlaybackParameters playbackParameters) {

    }


    public void play(String streamUrl) {

        this.streamUrl = streamUrl;



        // Create a data source factory.
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory();
// Create a progressive media source pointing to a stream uri.
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(streamUrl));
        // Set the media source to be played.
        exoPlayer.setMediaSource(mediaSource);
// Prepare the player.
        exoPlayer.prepare();


        exoPlayer.setPlayWhenReady(true);
    }

    public void resume() {

        if (streamUrl != null)
            play(streamUrl);
    }

    public void pause() {

        exoPlayer.setPlayWhenReady(false);



    }

    public void stop() {

        exoPlayer.stop();



    }

    public void playOrPause(String url) {

        if (streamUrl != null && streamUrl.equals(url)) {

            if (!isPlaying()) {

                play(streamUrl);

            } else {

                pause();
            }

        } else {

            if (isPlaying()) {

                pause();

            }

            play(url);
        }
    }

    public String getStatus() {

        return status;
    }



    public boolean isPlaying() {

        return this.status.equals(PlaybackStatus.PLAYING);
    }



}
