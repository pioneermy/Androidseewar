package com.example.androidseewar;

import com.example.androidseewar.service.AudioApp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ActivitySettings extends Activity implements OnClickListener,
		OnSeekBarChangeListener, AnimationListener {

	private Button buttonOk;
	private Button buttonCancel;
	private Button buttonResetShip;

	private SeekBar seekBarShip1;
	private SeekBar seekBarShip2;
	private SeekBar seekBarShip3;
	private SeekBar seekBarShip4;
	private TextView textShip1;
	private TextView textShip2;
	private TextView textShip3;
	private TextView textShip4;
	private RadioGroup radioGroupStrong;

	private SharedPreferences sPref;
	private Animation animation;
	private View idButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Натройка анимации кнопок
		animation = AnimationUtils.loadAnimation(this, R.anim.buttonanim);
		animation.setAnimationListener(this);

		buttonOk = (Button) findViewById(R.id.buttonOk);
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonResetShip = (Button) findViewById(R.id.buttonResetShip);
		radioGroupStrong = (RadioGroup) findViewById(R.id.radioGroupStrong);

		seekBarShip1 = (SeekBar) findViewById(R.id.seekBar1);
		seekBarShip2 = (SeekBar) findViewById(R.id.seekBar2);
		seekBarShip3 = (SeekBar) findViewById(R.id.seekBar3);
		seekBarShip4 = (SeekBar) findViewById(R.id.seekBar4);
		// Загрузка и установка начальных параметров
		loadSettings();

		textShip1 = (TextView) findViewById(R.id.textViewShip1);
		textShip2 = (TextView) findViewById(R.id.textViewShip2);
		textShip3 = (TextView) findViewById(R.id.textViewShip3);
		textShip4 = (TextView) findViewById(R.id.textViewShip4);

		textShip1.setText(getString(R.string.ship1) + "  "
				+ seekBarShip1.getProgress());
		textShip2.setText(getString(R.string.ship2) + "  "
				+ seekBarShip2.getProgress());
		textShip3.setText(getString(R.string.ship3) + "  "
				+ seekBarShip3.getProgress());
		textShip4.setText(getString(R.string.ship4) + "  "
				+ seekBarShip4.getProgress());

		// Натройка обработчиков событий на нажатие кнопок и выбора RadioGroup
		buttonOk.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
		buttonResetShip.setOnClickListener(this);

		seekBarShip1.setOnSeekBarChangeListener(this);
		seekBarShip2.setOnSeekBarChangeListener(this);
		seekBarShip3.setOnSeekBarChangeListener(this);
		seekBarShip4.setOnSeekBarChangeListener(this);
	}

	/*
	 * Обработка нажатия кнопок
	 */
	public void onClick(View v) {
		idButton = v;
		switch (v.getId()) {
		case R.id.buttonOk:
			// Выход с подтверждением настроек
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonOk.startAnimation(animation);
			break;
		case R.id.buttonCancel:
			// Выход без подтверждения настроек
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonCancel.startAnimation(animation);
			break;
		case R.id.buttonResetShip:
			// Возврат к установкам по умолчанию
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonResetShip.startAnimation(animation);
		}
	}

	/*
	 * Обработка изменения SeekBar
	 */
	public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2) {
		switch (seekBar.getId()) {
		// однотрубный
		case R.id.seekBar1:
			textShip1.setText(getString(R.string.ship1) + "  "
					+ seekBarShip1.getProgress());
			break;
		// двухтрубный
		case R.id.seekBar2:
			textShip2.setText(getString(R.string.ship2) + "  "
					+ seekBarShip2.getProgress());
			break;
		// трехтрубный
		case R.id.seekBar3:
			textShip3.setText(getString(R.string.ship3) + "  "
					+ seekBarShip3.getProgress());
			break;
		// четырехтрубный
		case R.id.seekBar4:
			textShip4.setText(getString(R.string.ship4) + "  "
					+ seekBarShip4.getProgress());
			break;
		default:
			break;
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	/*
	 * Сохранение параметров в Preferences
	 */
	void saveSettings() {
		sPref = getSharedPreferences("mysettings", MODE_PRIVATE);
		Editor ed = sPref.edit();
		// Количество кораблей по палубам
		ed.putInt("CountShip1", seekBarShip1.getProgress());
		ed.putInt("CountShip2", seekBarShip2.getProgress());
		ed.putInt("CountShip3", seekBarShip3.getProgress());
		ed.putInt("CountShip4", seekBarShip4.getProgress());
		switch (radioGroupStrong.getCheckedRadioButtonId()) {
		// Уровень сложности
		case R.id.radioEasy:
			ed.putInt("LevelStrong", 0);
			break;
		case R.id.radioMedium:
			ed.putInt("LevelStrong", 1);
			break;
		case R.id.radioHard:
			ed.putInt("LevelStrong", 2);
			break;
		}
		ed.commit();
	}

	/*
	 * Загрузка параметров из Preferences
	 */
	void loadSettings() {
		sPref = getSharedPreferences("mysettings", MODE_PRIVATE);
		// Количество кораблей по палубам
		seekBarShip1.setProgress(sPref.getInt("CountShip1", 4));
		seekBarShip2.setProgress(sPref.getInt("CountShip2", 3));
		seekBarShip3.setProgress(sPref.getInt("CountShip3", 2));
		seekBarShip4.setProgress(sPref.getInt("CountShip4", 1));
		// Уровень сложности
		switch (sPref.getInt("LevelStrong", 0)) {
		case 0:
			radioGroupStrong.check(R.id.radioEasy);
			break;
		case 1:
			radioGroupStrong.check(R.id.radioMedium);
			break;
		case 2:
			radioGroupStrong.check(R.id.radioHard);
			break;
		}
	}

	/*
	 * Обработка действий после окончания анимации кнопок
	 */
	public void onAnimationEnd(Animation animation) {
		switch (idButton.getId()) {
		case R.id.buttonOk:
			// сохранить настройки и выйти
			saveSettings();
			finish();
			break;
		case R.id.buttonCancel:
			// Выход без сохранения настроек
			finish();
			break;
		case R.id.buttonResetShip:
			// Восстановить значения по умолчанию для количества кораблей по
			// палубам
			seekBarShip1.setProgress(4);
			seekBarShip2.setProgress(3);
			seekBarShip3.setProgress(2);
			seekBarShip4.setProgress(1);
		}

	}

	public void onAnimationRepeat(Animation animation) {


	}

	public void onAnimationStart(Animation animation) {


	}

	/*
	 * Обработка действий на паузу активити
	 */
	@Override
	protected void onPause() {
		// Пауза для сервиса фоновой музыки
		ActivityMenu.getMusicService().pause();
		super.onPause();
	}

	/*
	 * Обработка действий при повторной активации активити
	 */
	@Override
	protected void onResume() {
		// Рестарт фоновой музыки
		ActivityMenu.getMusicService().resume();
		super.onResume();
	}
}
