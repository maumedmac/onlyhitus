package bo.umsa.deseo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import bo.umsa.deseo.player.PlaybackStatus;
import bo.umsa.deseo.player.RadioManager;
import bo.umsa.deseo.util.Show;
import bo.umsa.deseo.util.StationAdapter;
import bo.umsa.deseo.util.StationInstancer;

import bo.umsa.deseo.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime = 0;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.playTrigger)
    ImageButton trigger;



    @BindView(R.id.stationName)
    TextView stationName;

    @BindView(R.id.stationLogo)
    ImageView stationLogo;

    @BindView(R.id.sub_player)
    View subPlayer;

    @BindView(R.id.min)
    TextView min;

    @BindView(R.id.number)
    NumberPicker numberPicker;
    RadioManager radioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        radioManager = RadioManager.with(this);



        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
            }
        });

        Show.initShow();




        StationInstancer shoutcast = new StationInstancer();
        shoutcast.setName("Reproducir");
        shoutcast.setUrl("https://conectperu.com/8390/stream");
        shoutcast.setImage(R.drawable.radiod);

        stationName.setText("Radio Deseo");
        Picasso.get().load(shoutcast.getImage()).into(stationLogo);

        subPlayer.setVisibility(View.VISIBLE);

        radioManager.passShoutcast(shoutcast);
       // radioManager.playOrPause();




        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(Show.getShowArrayList().size()-1);
        numberPicker.setDisplayedValues(Show.showNames());



    }

    @NotNull
    private ArrayList<StationInstancer> loadStations() {
        ArrayList<StationInstancer> list = new ArrayList<>();

        StationInstancer onlyHitStationInstancer = new StationInstancer();
        onlyHitStationInstancer.setName("Reproducir");
        onlyHitStationInstancer.setUrl("https://conectperu.com/8396/stream");
        onlyHitStationInstancer.setImage(R.drawable.radiod);
        list.add(onlyHitStationInstancer);
        return list;

    }

    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        radioManager.unbind();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        radioManager.bind();
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 seconds
            backPressedTime = t;
            Toast.makeText(this, "Press back again to leave", Toast.LENGTH_SHORT).show();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Subscribe
    public void onEvent(String status) {

        switch (status) {
            case PlaybackStatus.LOADING:
                break;
            case PlaybackStatus.ERROR:
                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();
                break;
            case PlaybackStatus.PLAYING:
                trigger.setImageResource(R.drawable.ic_pause_black);
                break;
            case PlaybackStatus.PAUSED:
            case PlaybackStatus.IDLE:
                trigger.setImageResource(R.drawable.ic_play_arrow_black);
                break;
        }
    }

    @OnClick(R.id.playTrigger)
    public void onClicked() {
        radioManager.playOrPause();
    }


}
