package com.example.androidseewar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.example.androidseewar.service.AudioApp;
import com.example.androidseewar.service.MusicService;

public class ActivityMenu extends Activity implements OnClickListener,
		OnCheckedChangeListener, AnimationListener {

	private ToggleButton toggleButtonMusic;
	private ToggleButton toggleButtonAudio;
	private Button buttonPlay;
	private Button buttonSettings;
	private Button buttonExit;
	private SharedPreferences sPref;
	private Intent intent;
	private Animation animation;
	private View idButton;

	private ServiceConnection sConn;
	private static MusicService musicService;
	private boolean flagOnStop = false;
	private boolean flagOnActivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// Настройка громкости игры с помощью кнопок
		// регулирования громкости устройства
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Настройка анимации для кнопок
		animation = AnimationUtils.loadAnimation(this, R.anim.buttonanim);
		animation.setAnimationListener(this);

		toggleButtonMusic = (ToggleButton) findViewById(R.id.toggleButtonMusic);
		toggleButtonAudio = (ToggleButton) findViewById(R.id.toggleButtonAudio);
		// Настройка Preferences для хранения настроек игры
		sPref = getSharedPreferences("mysettings", MODE_PRIVATE);

		// Запуск сервиса Музыка
		// Переключение toggleButton в состояние ON/OFF
		sConn = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder binder) {
				musicService = ((MusicService.MyBinder) binder).getService();
				toggleButtonMusic.setChecked(sPref.getBoolean("isCheckedMusic",
						false));

				if (toggleButtonMusic.isChecked()) {
					musicService.pause();
					musicService.isPlayMusic = false;
					toggleButtonMusic.setBackgroundResource(R.drawable.nomusic);
				} else {
					toggleButtonMusic.setBackgroundResource(R.drawable.music);
				}
			}

			public void onServiceDisconnected(ComponentName name) {

			}
		};

		bindService(new Intent(this, MusicService.class), sConn,
				BIND_AUTO_CREATE);
		// Инициализация Аудио для воспроизведения звуковых эффектов
		// Переключение toggleButton в состояние ON/OFF
		toggleButtonAudio.setChecked(sPref.getBoolean("isCheckedAudio", false));

		if (toggleButtonAudio.isChecked()) {
			toggleButtonAudio.setBackgroundResource(R.drawable.noaudio);
			AudioApp.setIsSound(false);
		} else {
			toggleButtonAudio.setBackgroundResource(R.drawable.audio);
			AudioApp.setIsSound(true);
		}

		buttonPlay = (Button) findViewById(R.id.buttonPlay);
		buttonSettings = (Button) findViewById(R.id.buttonSettings);
		buttonExit = (Button) findViewById(R.id.buttonExit);
		// Назначение обработчика событий
		toggleButtonMusic.setOnCheckedChangeListener(this);
		toggleButtonAudio.setOnCheckedChangeListener(this);
		buttonPlay.setOnClickListener(this);
		buttonSettings.setOnClickListener(this);
		buttonExit.setOnClickListener(this);
	}

	public void onClick(View v) {
		idButton = v;// Для обработчика анимации
		switch (v.getId()) {
		case R.id.buttonPlay:
			flagOnActivity = false;
			// Запуск игры после окончания анимации кнопки
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonPlay.startAnimation(animation);
			break;
		case R.id.buttonSettings:
			flagOnActivity = false;
			// Запуск активити НАСТРОЙКИ после окончания анимации кнопки
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonSettings.startAnimation(animation);
			break;
		case R.id.buttonExit:
			flagOnActivity = true;
			// Выход из приложения
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonExit.startAnimation(animation);
			break;
		}
	}

	/*
	 * Обработка события на переключение toggleButton *
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// Сохранение настроек (ON/OFF) для Фоновой музыки и звуков
		sPref = getSharedPreferences("mysettings", MODE_PRIVATE);
		Editor ed = sPref.edit();

		idButton = buttonView;// Для обработчика анимации

		switch (buttonView.getId()) {
		case R.id.toggleButtonMusic:
			// Music
			if (isChecked) {
				toggleButtonMusic.setBackgroundResource(R.drawable.nomusic);
				musicService.pause();
				musicService.isPlayMusic = false;
			} else {
				toggleButtonMusic.setBackgroundResource(R.drawable.music);
				musicService.isPlayMusic = true;
				musicService.resume();
			}
			toggleButtonMusic.startAnimation(animation);
			ed.putBoolean("isCheckedMusic", isChecked);
			ed.commit();
			break;
		case R.id.toggleButtonAudio:
			// Sound
			if (isChecked) {
				toggleButtonAudio.setBackgroundResource(R.drawable.noaudio);
				AudioApp.setIsSound(false);
			} else {
				toggleButtonAudio.setBackgroundResource(R.drawable.audio);
				AudioApp.setIsSound(true);
			}
			toggleButtonAudio.startAnimation(animation);
			ed.putBoolean("isCheckedAudio", isChecked);
			ed.commit();
			break;
		}
	}

	/*
	 * Обработка событий при выходе из паузы активити *
	 */
	@Override
	protected void onResume() {
		if (flagOnStop || flagOnActivity) {
			// AudioApp.onResume();
			musicService.resume();
			flagOnStop = false;
		}
		super.onResume();
	}

	/*
	 * Обработка событий на паузу активити
	 */
	@Override
	protected void onStop() {
		if (flagOnActivity) {
			AudioApp.onPause();
			musicService.pause();
			flagOnStop = true;
		}
		flagOnActivity = true;
		super.onStop();
	}

	/*
	 * Обработка событий на завершение активити
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AudioApp.onDestroy();
		unbindService(sConn);
	}

	/*
	 * Запуск соответствующей активити после окончания анимации кнопки
	 */
	public void onAnimationEnd(Animation animation) {
		switch (idButton.getId()) {
		case R.id.buttonPlay:
			intent = new Intent(this, ActivityGame.class);
			startActivity(intent);
			break;
		case R.id.buttonSettings:
			intent = new Intent(this, ActivitySettings.class);
			startActivity(intent);
			break;
		case R.id.buttonExit:
			AudioApp.onPause();
			musicService.stop();
			finish();
			break;
		default:
			break;
		}
	}

	public void onAnimationRepeat(Animation animation) {

	}

	public void onAnimationStart(Animation animation) {

	}

	public static MusicService getMusicService() {
		return musicService;
	}
}
