package com.finalexam.coinstackgame;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


public class SoundManager {

    static private SoundManager _instance;
    private static SoundPool mSoundPool;
    private static HashMap<Integer, Integer> mSoundPoolMap;
    private static AudioManager  mAudioManager;
    private static Context mContext;

    private SoundManager()
    {
    }

    static synchronized public SoundManager getInstance()
    {
        if (_instance == null)
            _instance = new SoundManager();
        return _instance;
    }

    public static  void initSounds(Context theContext)
    {
        mContext = theContext;
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static void addSound(int Index,int SoundID)
    {
        mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
    }

    public static void loadSounds()
    {
        mSoundPoolMap.put(0, mSoundPool.load(mContext, R.raw.dropcoin, 1));
        mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.stackcoin, 1));
        mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.stageclear, 1));
        mSoundPoolMap.put(3,mSoundPool.load(mContext,R.raw.fail,1));
    }

    public static void playSound(int index,float speed)
    {

        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed);
    }

    public static void stopSound(int index)
    {
        mSoundPool.stop(mSoundPoolMap.get(index));
    }

    public static void cleanup()
    {
        mSoundPool.release();
        mSoundPool = null;
        mSoundPoolMap.clear();
        mAudioManager.unloadSoundEffects();
        _instance = null;

    }
}
