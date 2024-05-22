package com.example.widget;

import android.content.ClipData;

public class ItemData {
    public String MusicName;

    public ItemData() {}
    public ItemData(String musicName) {
        MusicName = musicName;
    }
    public String getName() {
        return this.MusicName;
    }
}
