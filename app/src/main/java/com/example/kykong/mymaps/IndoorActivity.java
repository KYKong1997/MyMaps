package com.example.kykong.mymaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.IndoorLevel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class IndoorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean showLevelPicker=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.391786243274063, 100.13725878848028),18));
    }
    public void onToggleLevelPicker(View view){
        showLevelPicker=!showLevelPicker;
        mMap.getUiSettings().setIndoorLevelPickerEnabled(showLevelPicker);

    }
    public void onFocusedBuildingInfo(View view){
        IndoorBuilding building=mMap.getFocusedBuilding();
        if(building!=null){
            StringBuilder s=new StringBuilder();
            for(IndoorLevel level:building.getLevels()){
                s.append(level.getName()).append(" ");
            }
            if(building.isUnderground()){
                s.append("is underground");

            }
            setText(s.toString());
        }
        else{
            setText("No Visible building");
        }
    }
    public void onVisibleLevelInfo(View view){
        IndoorBuilding building=mMap.getFocusedBuilding();
        if(building!=null){
            IndoorLevel level=building.getLevels().get(building.getActiveLevelIndex());
            if(level!=null){
                setText(level.getName());
            }
            else{
                setText("No visible level");
            }
        }
        else {
            setText("No visible building");
        }
    }
    public void onHigherLevel(View view){
        IndoorBuilding building=mMap.getFocusedBuilding();
        if(building!=null){
            List<IndoorLevel> levels=building.getLevels();
            if(!levels.isEmpty()){
                int currentLevel=building.getActiveLevelIndex();
                int newLevel=currentLevel-1;
                if(newLevel==-1){
                    newLevel=levels.size()-1;
                }
                IndoorLevel level=levels.get(newLevel);
                setText("Activating level "+level.getName());
                level.activate();
            }
            else{
                setText("No levels in building");
            }
        }
        else{
            setText("No Visible building");
        }
    }

    private void setText(String s) {
        TextView text=(TextView)findViewById(R.id.top_text);
        text.setText(s);
    }

}
