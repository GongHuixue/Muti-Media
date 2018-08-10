package com.example.android.multmedia.player;

public class MediaPlayConstants {
    public static final int RANDOM_PLAY = 1;
    public static final int SINGLE_PLAY = 2;
    public static final int SEQUENCE_PLAY = 3;

    public static final int PLAY_PRE = 4;
    public static final int PLAY = 5;
    public static final int PAUSE = 6;
    public static final int NEXT = 7;

    public static final int MSG_UPDATE_TIME = 8;
    public static final int MSG_UPDATE_PROGRESS = 9;
    public static final int MSG_SHOW_HIDE_BAR = 10;
    public static final int MSG_UPDATE_CONTROL_BAR = 11;
    public static final int MSG_UPDATE_AUDIO_INFO = 12;

    public static final int PLAY_STATE_PLAYING = 0;
    public static final int PLAY_STATE_PAUSE = 1;
    public static final int PLAY_STATE_END = 2;

//    public static final int SYSTEM_TIME_UPDATE = 1000;
//    public static final int CONTROL_BAR_UPDATE = 5000;

    public static final int ONE_SECOND_TIMER = 1000;
    public static final int THREE_SECOND_TIMER = 3000;
    public static final int FIVE_SECOND_TIMER = 5000;

    public static final String INTENT_AUDIO_LIST = "audio_list";
    public static final String INTENT_VIDEO_LIST = "video_list";
    public static final String INTENT_PHOTO_LIST = "photo_list";
    public static final String INTENT_MEDIA_POSITION = "media_position";

    public enum MediaType {
        VIDEO,
        AUDIO,
        PHOTO,
        NONE
    }

    public static final int VIDEO_TYPE = 0;
    public static final int AUDIO_TYPE = 1;
    public static final int PHOTO_TYPE = 2;
}
