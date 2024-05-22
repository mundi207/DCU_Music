package com.example.widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    File[] mp3File; // 지정 경로 상에 있는 모든 파일 저장
    String mp3Path = "/storage/emulated/0"; // 경로 지정
    ArrayList<ItemData> mp3List = new ArrayList<ItemData>(); // mp3 파일만 따로 저장

    public MediaPlayer mediaPlayer;

    private int playPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 파일 읽기 권한 요청
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.ListView);
        mp3File = new File(mp3Path).listFiles(); // 해당 경로에 있는 모든 파일 저장

        for(File file : mp3File) { // 경로에 있는 파일 수 만큼(디렉터리 포함) 반복
            String fileName = file.getName(); // 파일 이름 저장
            String extName = fileName.substring(fileName.length() - 3); // 파일 확장자명 저장
            if (extName.equals("mp3") && file.isDirectory() == false) { // 파일 확장자가 mp3이고, 디렉터리가 아니라면
                mp3List.add(new ItemData(fileName)); // mp3파일만 따로 저장 (리스트뷰 아이템 객체 생성)
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트뷰 이벤트 처리
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class); // 현재 액티비티에서 다른 액티비티로 전환
                intent.putExtra("mp3List.get(position).getName()", mp3List.get(position).getName()); // 리스트내에 저장한 mp3 파일 이름을 다른 엑티비티로 보냄
                startActivity(intent); // 다른 액티비티 시작
            }
        });
        ListAdaptor listAdapt = new ListAdaptor(getApplicationContext(), mp3List); // 리스트뷰를 위한 어댑터 생성
        listView.setAdapter(listAdapt);
    }
}
class ListAdaptor extends BaseAdapter { // 리스트뷰를 위한 어댑터 생성
    private Context conText;
    private LinearLayout linearLayout;

    private ArrayList<ItemData> mp3List = new ArrayList<ItemData>();
    private ItemData mp3ListData = new ItemData();

    private ItemData mp3Select;
    private SeekBar seekBar;

    @Override
    public int getCount() {
        return mp3List.size();
    }

    @Override
    public Object getItem(int position) {
        return mp3List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(String musicName) {
        mp3List.add(new ItemData(musicName));
    }
    public ListAdaptor(Context context, ArrayList<ItemData> item) {
        this.conText = context;
        this.mp3List = item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // 리스트뷰 띄워줌
        LayoutInflater inflater = (LayoutInflater) conText.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_layout, parent, false);

        mp3ListData = (ItemData) getItem(position);
        linearLayout = (LinearLayout)convertView.findViewById(R.id.itemlayout);

        TextView textView = (TextView) convertView.findViewById(R.id.musicname);
        textView.setText(mp3List.get(position).getName());

        return convertView;
    }
}