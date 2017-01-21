package com.example.androidseewar.service;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.androidseewar.R;
//звуковые эффекты	
public class AudioApp {

	private static SoundPool soundPool; // звуковые эффекты	
	private static Map<Integer, Integer> soundArr;
		
	public static boolean isSound = true;
	public static final int SOUND_BUTTON = 1;
	public static final int SOUND_FIRE = 2;
	public static final int SOUND_WATER = 3;
	public static final int SOUND_WIN = 4;
	

	public static void instalation(Context context) {
		
		// инициализация SoundPool, используемого для воспроизведения
		// звуковых эффектов

		soundArr = new HashMap<Integer, Integer>();

		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundArr.put(SOUND_BUTTON, soundPool.load(context, R.raw.soundbut, 1));
		soundArr.put(SOUND_FIRE, soundPool.load(context, R.raw.soundfire, 1));
		soundArr.put(SOUND_WATER, soundPool.load(context, R.raw.soundwater, 1));
		soundArr.put(SOUND_WIN, soundPool.load(context, R.raw.soundwin, 1));
	}

	public static void onPlay(int soundId) {
		if (isSound) {			
			soundPool.play(soundId, 1, 1, 1, 0, 1f);
		}
	}

	public static void onPause() {
		if (isSound) {
			soundPool.autoPause();
		}
	}

	public static void onResume() {
		if (isSound) {
			soundPool.autoResume();
		}
	}
		
	public static void onDestroy() {
		if (soundPool != null) {			
			soundPool.release();
		}
	}	
}
