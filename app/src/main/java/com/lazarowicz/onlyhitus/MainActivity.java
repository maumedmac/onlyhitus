package com.lazarowicz.onlyhitus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lazarowicz.onlyhitus.player.PlaybackStatus;
import com.lazarowicz.onlyhitus.player.RadioManager;
import com.lazarowicz.onlyhitus.util.StationAdapter;
import com.lazarowicz.onlyhitus.util.StationInstancer;
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

    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.stationName)
    TextView stationName;

    @BindView(R.id.stationLogo)
    ImageView stationLogo;

    @BindView(R.id.sub_player)
    View subPlayer;

    RadioManager radioManager;

    String streamURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        radioManager = RadioManager.with(this);

        ArrayList<StationInstancer> list = loadStations();

        StationAdapter adapter = new StationAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setAdapter(new StationAdapter(this, list));
    }

    @NotNull
    private ArrayList<StationInstancer> loadStations() {
        ArrayList<StationInstancer> list = new ArrayList<>();

        StationInstancer onlyHitStationInstancer = new StationInstancer();
        onlyHitStationInstancer.setName("OnlyHit");
        onlyHitStationInstancer.setUrl("https://api.onlyhit.us");
        onlyHitStationInstancer.setImage(R.drawable.onlyhit);
        list.add(onlyHitStationInstancer);

        StationInstancer onlyHitGoldStationInstancer = new StationInstancer();
        onlyHitGoldStationInstancer.setName("OnlyHit Gold");
        onlyHitGoldStationInstancer.setUrl("https://gold.onlyhit.us");
        onlyHitGoldStationInstancer.setImage(R.drawable.onlyhit_gold);
        list.add(onlyHitGoldStationInstancer);

        StationInstancer onlyHitJapanStationInstancer = new StationInstancer();
        onlyHitJapanStationInstancer.setName("OnlyHit Japan");
        onlyHitJapanStationInstancer.setUrl("https://j.onlyhit.us");
        onlyHitJapanStationInstancer.setImage(R.drawable.onlyhit_japan);
        list.add(onlyHitJapanStationInstancer);

        StationInstancer onlyHitKPopStationInstancer = new StationInstancer();
        onlyHitKPopStationInstancer.setName("OnlyHit K-Pop");
        onlyHitKPopStationInstancer.setUrl("https://kpop.onlyhit.us");
        onlyHitKPopStationInstancer.setImage(R.drawable.onlyhitkpop);
        list.add(onlyHitKPopStationInstancer);
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

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> parent, View view, int position) {

        StationInstancer shoutcast = (StationInstancer) parent.getItemAtPosition(position);
        if (shoutcast == null || stationName.getText() == shoutcast.getName() ||
                RadioManager.getStatus().equals(PlaybackStatus.LOADING)) {
            return;
        }

        stationName.setText(shoutcast.getName());
        Picasso.get().load(shoutcast.getImage()).into(stationLogo);

        subPlayer.setVisibility(View.VISIBLE);

        radioManager.passShoutcast(shoutcast);
        radioManager.playOrPause();
    }
}
