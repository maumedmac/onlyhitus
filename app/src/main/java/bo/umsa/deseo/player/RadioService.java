package bo.umsa.deseo.player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import bo.umsa.deseo.util.StationInstancer;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RadioService extends Service implements  Player.Listener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.lazarowicz.deseo.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.lazarowicz.deseo.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.lazarowicz.deseo.ACTION_STOP";

    private final IBinder iBinder = new LocalBinder();
    public StationInstancer Shoutcast;

    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    private boolean onGoingCall = false;
    private TelephonyManager telephonyManager;

    private WifiManager.WifiLock wifiLock;

    private AudioManager audioManager;

    private MediaNotificationManager notificationManager;

    private String status;
    private String existingURL;
    public String currentSong;

    public String getCurrentSong() {
        return currentSong;
    }

    public class LocalBinder extends Binder {
        public RadioService getService() {
            return RadioService.this;
        }
    }

    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            pause();
        }
    };

    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == TelephonyManager.CALL_STATE_OFFHOOK
                    || state == TelephonyManager.CALL_STATE_RINGING) {

                if (!isPlaying()) return;

                onGoingCall = true;
                stop();

            } else if (state == TelephonyManager.CALL_STATE_IDLE) {

                if (!onGoingCall) return;

                onGoingCall = false;
                resume();
            }
        }
    };

    private final MediaSessionCompat.Callback mediasSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            super.onPause();

            pause();
        }

        @Override
        public void onStop() {
            super.onStop();

            stop();

            notificationManager.cancelNotify();
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        onGoingCall = false;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        notificationManager = new MediaNotificationManager(this);

        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mcScPAmpLock");

        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder().build());
        mediaSession.setCallback(mediasSessionCallback);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);

        exoPlayer = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        exoPlayer.addListener(this);
        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
        exoPlayer.addMetadataOutput(getSongTime());
        status = PlaybackStatus.IDLE;
    }

    private MetadataOutput getSongTime() {
        return metadata -> {
            final int length = metadata.length();
            if (length > 0) {
                try {
                    getSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    void getSong() throws IOException {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(existingURL.split("play")[0] + "currentsong").build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            currentSong = response.body().string();
            Log.d("getSong", currentSong);
            notificationManager.startNotify(status);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if (TextUtils.isEmpty(action))
            return START_NOT_STICKY;

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            stop();
            return START_NOT_STICKY;
        }

        if (action.equalsIgnoreCase(ACTION_PLAY)) transportControls.play();
        else if (action.equalsIgnoreCase(ACTION_PAUSE)) pause();
        else if (action.equalsIgnoreCase(ACTION_STOP)) transportControls.stop();

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        if (status.equals(PlaybackStatus.IDLE))
            stopSelf();

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        pause();

        exoPlayer.release();
        exoPlayer.removeListener(this);

        if (telephonyManager != null)
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

        notificationManager.cancelNotify();

        mediaSession.release();

        unregisterReceiver(becomingNoisyReceiver);

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
    public void onPlaybackStateChanged(int state) {

        switch (state) {
            case Player.STATE_BUFFERING:
                status = PlaybackStatus.LOADING;
                Log.d("onPlaybackStateChanged", state + " " + status);
                break;
            case Player.STATE_ENDED:
                status = PlaybackStatus.STOPPED;
                Log.d("onPlaybackStateChanged", state + " " + status);
                break;
            case Player.STATE_READY:
                if (exoPlayer.getPlayWhenReady()) {
                    status = PlaybackStatus.PLAYING;
                    notificationManager.startNotify(status);

                }
                if (!exoPlayer.getPlayWhenReady()) {
                    status = PlaybackStatus.PAUSED;
                    notificationManager.startNotify(status);
                }
                Log.d("onPlaybackStateChanged", state + " " + status);
                break;
            case Player.STATE_IDLE:
                status = PlaybackStatus.IDLE;
                Log.d("onPlaybackStateChanged", state + " " + status);
                break;
        }
        if (state != (Player.STATE_IDLE))
            EventBus.getDefault().post(status);
    }


    @Override
    public void onPlayerError(@NotNull ExoPlaybackException error) {

        EventBus.getDefault().post(PlaybackStatus.ERROR);
    }


    public void play(String streamUrl) {

        this.existingURL = streamUrl;

        if (wifiLock != null && !wifiLock.isHeld()) {

            wifiLock.acquire();

        }

        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(streamUrl));
        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    public void resume() {

        if (existingURL != null)
            play(existingURL);
    }

    public void pause() {
        exoPlayer.setPlayWhenReady(false);
        status = PlaybackStatus.PAUSED;
        EventBus.getDefault().post(status);

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void stop() {

        exoPlayer.stop();

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void playOrPause(StationInstancer shoutcast) {
        this.Shoutcast = shoutcast;
        String streamUrl = this.Shoutcast.getUrl();

        if (existingURL != null && existingURL.equals(streamUrl)) {

            if (!isPlaying()) {

                play(existingURL);

            } else {

                pause();
            }

        } else {

            if (isPlaying()) {

                pause();

            }

            play(streamUrl);
        }
    }

    public String getStatus() {

        return status;
    }

    public MediaSessionCompat getMediaSession() {

        return mediaSession;
    }

    public boolean isPlaying() {

        return this.status.equals(PlaybackStatus.PLAYING);
    }

    private void wifiLockRelease() {

        if (wifiLock != null && wifiLock.isHeld()) {

            wifiLock.release();
        }
    }
}