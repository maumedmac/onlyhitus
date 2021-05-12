package com.lazarowicz.onlyhitus;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lazarowicz.onlyhitus.player.PlaybackStatus;
import com.lazarowicz.onlyhitus.player.RadioManager;
import com.lazarowicz.onlyhitus.util.StationInstancer;
import com.lazarowicz.onlyhitus.util.StationAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.listview)
    ListView listView;
    
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

        StationAdapter adapter =new StationAdapter(this,list);
       listView.setAdapter(adapter);
       listView.setAdapter(new StationAdapter(this, list));
    }

    @NotNull
    private ArrayList<StationInstancer> loadStations() {
        ArrayList<StationInstancer> list=new ArrayList<>();

        StationInstancer onlyHitStationInstancer =new StationInstancer();
        onlyHitStationInstancer.setName("OnlyHit");
        onlyHitStationInstancer.setUrl("https://api.onlyhit.us/play");
        onlyHitStationInstancer.setImage(R.drawable.onlyhit);
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

        finish();
    }

    @Subscribe
    public void onEvent(String status){

        switch (status){

            case PlaybackStatus.LOADING:

                // loading

                break;

            case PlaybackStatus.ERROR:

                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();

                break;

        }



    }



    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        StationInstancer shoutcast = (StationInstancer) parent.getItemAtPosition(position);
        if(shoutcast == null){

            return;

        }



        streamURL = shoutcast.getUrl();


        radioManager.playOrPause(streamURL);
    }
}
