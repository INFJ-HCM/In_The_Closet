package com.example.android.butcher2;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
//20210112 songhui 효과음 클래스
public class SoundManager {

    private SoundPool mSoundPool;
    private HashMap<Integer,Integer> mSoundPoolMap;
    private AudioManager mAudioManager;
    private Context mContext;

    public SoundManager(Context mContext,SoundPool mSoundPool){
        this.mContext = mContext;
        this.mSoundPool = mSoundPool;
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }


    public void addSound(int index,int soundId){ //효과음 추가
        mSoundPoolMap.put(index,mSoundPool.load(mContext,soundId,1));
    }

    public int playSound(int index){ //효과음 재생
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return mSoundPool.play(mSoundPoolMap.get(index),5f,5f,1,0,1f);
    }

    public void stopSound(int streamId){
        mSoundPool.stop(streamId);
    } //효과음 정지

    public void pauseSound(int streamId){
        mSoundPool.pause(streamId);
    } //효과음 일시정지

    public void resumeSound(int streamId){
        mSoundPool.resume(streamId);
    } //효과음 재시작
}
