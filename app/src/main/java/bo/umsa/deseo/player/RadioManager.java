package bo.umsa.deseo.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import bo.umsa.deseo.util.StationInstancer;

import org.greenrobot.eventbus.EventBus;

public class RadioManager {

    private static RadioManager instance ;

    private static RadioService service;

    private final Context context;
    private StationInstancer shoutcast;

    private RadioManager(Context context) {
        this.context = context;
    }

    public static RadioManager with(Context context) {

        if (instance == null)
            instance = new RadioManager(context);

        return instance;
    }

    public static String getStatus(){
        return service.getStatus();
    }

    public void passShoutcast(StationInstancer shoutcast){
        this.shoutcast=shoutcast;
    }

    public void playOrPause(){

        service.playOrPause(this.shoutcast);
    }

    public void bind() {

        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if(service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind() {

        context.unbindService(serviceConnection);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {

            service = ((RadioService.LocalBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

}