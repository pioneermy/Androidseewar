package com.example.androidseewar.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.androidseewar.R;
/* 
 * Воспроизведение фоновой музыки
 */
public class MusicService extends Service {
	// private static final String TAG = "MyService";
	MediaPlayer player;
	IBinder binder = new MyBinder();
	public boolean isPlayMusic = true;

	public class MyBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public void onCreate() {
		// Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		// Инициализация резурсов	
		player = MediaPlayer.create(this, R.raw.kapitan);		
		player.setLooping(true);
		// Запуск проигрывателя
		player.start();
	}
	
	@Override
	public void onDestroy() {
		// Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		player.release();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void pause() {
		if (isPlayMusic) {
			player.pause();
		}
	}

	public void stop() {
		if (isPlayMusic) {
			player.stop();			
			isPlayMusic = false;
		}
	}

	public void resume() {
		if (isPlayMusic) {
			player.start();
		}
	}

	@Override
	public void onStart(Intent intent, int startid) {
		// Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
	}
}
