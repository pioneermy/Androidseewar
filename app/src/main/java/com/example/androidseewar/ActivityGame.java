package com.example.androidseewar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.androidseewar.logica.ClSeeWar;
import com.example.androidseewar.service.AudioApp;

public class ActivityGame extends Activity implements OnItemClickListener,
		OnClickListener, AnimationListener {

	private ClSeeWar fildsOfGame; // Игровой процесс

	private ImageAdapter imageAdapterUser;
	private ImageAdapter imageAdapterComp;
	private GridView gridviewUser;
	private GridView gridviewComp;
	private Button buttonWin;
	private Button buttonOk;
	private Button buttonRepeat;
	private TextView textViewComp;
	private TextView textViewUser;

	private SharedPreferences sPref; // Начальные установки

	private Animation animation;
	private View idButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		animation = AnimationUtils.loadAnimation(this, R.anim.buttonanim);
		animation.setAnimationListener(this);

		buttonWin = (Button) findViewById(R.id.buttonWin);
		buttonOk = (Button) findViewById(R.id.buttonOk);
		buttonRepeat = (Button) findViewById(R.id.buttonRepeat);

		textViewComp = (TextView) findViewById(R.id.textViewComp);
		textViewUser = (TextView) findViewById(R.id.textViewUser);
		// Инициализация отображения поля пользователя на экране
		gridviewUser = (GridView) findViewById(R.id.gridView1);
		imageAdapterUser = new ImageAdapter(this);
		// Инициализация отображения поля компьютера на экране
		gridviewComp = (GridView) findViewById(R.id.gridView2);
		imageAdapterComp = new ImageAdapter(this);

		// Инициализация параметров игры
		// Инициализация полей
		initFilds();

		buttonOk.setVisibility(Button.VISIBLE);
		buttonRepeat.setVisibility(Button.VISIBLE);

		buttonWin.setOnClickListener(this);
		buttonOk.setOnClickListener(this);
		buttonRepeat.setOnClickListener(this);
	}

	private void initFilds() {
		// Инициализация параметров игры
		sPref = getSharedPreferences("mysettings", MODE_PRIVATE);

		// Расстанвка кораблей для игрока и компьютера
		fildsOfGame = new ClSeeWar(sPref.getInt("LevelStrong", 0),
				sPref.getInt("CountShip1", 4), sPref.getInt("CountShip2", 3),
				sPref.getInt("CountShip3", 2), sPref.getInt("CountShip4", 1));
		// Выбор цветовой схемы отображения кораблей игрока
		if ((sPref.getInt("LevelStrong", 0) == 1)
				|| (sPref.getInt("LevelStrong", 0) == 2)) {
			imageAdapterUser.initMasLow(fildsOfGame.fieldScreenShot.mas);
		} else {
			imageAdapterUser.initMas(fildsOfGame.fieldScreenShot.mas);
		}

		gridviewUser.setAdapter(imageAdapterUser);
		// Выбор цветовой схемы отображения кораблей компьютера
		if ((sPref.getInt("LevelStrong", 0) == 1)
				|| (sPref.getInt("LevelStrong", 0) == 2)) {
			imageAdapterComp.initMasLow(fildsOfGame.fieldShotUser.mas);
		} else {
			imageAdapterComp.initMas(fildsOfGame.fieldShotUser.mas);
		}

		gridviewComp.setAdapter(imageAdapterComp);
	}

	/*
	 * Обработка нажатия кнопки для перегенерации кораблей и начала игры
	 */
	public void onClick(View v) {
		idButton = v;
		switch (v.getId()) {
		case R.id.buttonWin:
			// Логотип "Победа"
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonWin.startAnimation(animation);
			break;
		case R.id.buttonOk:
			// Начало игры
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonOk.startAnimation(animation);
			break;
		case R.id.buttonRepeat:
			// Перегенерация кораблей
			AudioApp.onPlay(AudioApp.SOUND_BUTTON);
			buttonRepeat.startAnimation(animation);
			break;
		default:
			break;
		}
	}

	/*
	 * Игровой процесс
	 */
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// Toast.makeText(ActivityGame.this, "" + position,
		// Toast.LENGTH_SHORT).show();

		int flagUser; // Флаг - выстрела игрока
		// 0 - в эту ячейку игрок стрелял, повторный ход
		// 1 - "Попал", повторный ход
		// 2 - "Вода"
		boolean flagComp;// Флаг - выстрела компьютера ("Вода"/"Попал")

		// Начальные установки
		sPref = getSharedPreferences("mysettings", MODE_PRIVATE);
		// Проверка флага выигрыша компьютера или игрока
		if (fildsOfGame.isCompWin() || fildsOfGame.isUserWin())
			return;

		// Обработка выстрела игрока
		flagUser = fildsOfGame.shortUser(position);

		// Вывод поля игрока на экран
		if ((sPref.getInt("LevelStrong", 0) == 1)
				|| (sPref.getInt("LevelStrong", 0) == 2)) {
			imageAdapterComp.initMasLow(fildsOfGame.fieldShotUser.mas);
		} else {
			imageAdapterComp.initMas(fildsOfGame.fieldShotUser.mas);
		}
		gridviewComp.setAdapter(imageAdapterComp);

		// Вывод счета на экран
		textViewUser.setText(getString(R.string.step) + " - "
				+ fildsOfGame.getCountShortUser());

		// Проверка флага выигрыша игрока, чтобы не дать компьютеру доиграть
		if (fildsOfGame.isUserWin()) {
			// музыка - ТУШ
			AudioApp.onPlay(AudioApp.SOUND_WIN);
			// Отображение Поздравительного изображения
			buttonWin.setText(R.string.userWin);
			buttonWin.setBackgroundResource(R.drawable.cup);
			buttonWin.setVisibility(Button.VISIBLE);
			return;
		}

		// Флаг - выстрела игрока
		// 0 - в эту ячейку игрок стрелял, повторный ход
		// 1 - "Попал", повторный ход
		// 2 - "Вода"

		switch (flagUser) {
		case 0:
			// Звуковой эффект
			AudioApp.onPlay(AudioApp.SOUND_WATER);
			break;
		case 1:
			// Звуковой эффект
			AudioApp.onPlay(AudioApp.SOUND_FIRE);
			return;
		case 2:
			// Звуковой эффект
			AudioApp.onPlay(AudioApp.SOUND_WATER);
			return;
		}

		// Вывод поля компьютера на экран
		do {
			flagComp = fildsOfGame.shortComp();
			// В зависимости от уровня сложности:
			if ((sPref.getInt("LevelStrong", 0) == 1)
					|| (sPref.getInt("LevelStrong", 0) == 2)) {
				// Цвет корабля зависит от количества палуб
				imageAdapterUser.initMasLow(fildsOfGame.fieldScreenShot.mas);
			} else {
				// Все корабли одного цвета
				imageAdapterUser.initMas(fildsOfGame.fieldScreenShot.mas);
			}
			gridviewUser.setAdapter(imageAdapterUser);

			// Счет для компьютера
			textViewComp.setText(getString(R.string.step) + " - "
					+ fildsOfGame.getCountShortComp());
			// Проверка флага - Победа компьютера?
			if (fildsOfGame.isCompWin()) {
				// Отображение Поздравительного изображения
				buttonWin.setText(R.string.compWin);
				buttonWin.setBackgroundResource(R.drawable.happycomp);
				buttonWin.setVisibility(Button.VISIBLE);
				return;
			}
		} while (flagComp);
	}

	/*
	 * Обработка событий после окончания анимации кнопок
	 */
	public void onAnimationEnd(Animation animation) {
		switch (idButton.getId()) {
		case R.id.buttonWin:
			// Логотип "Победа"
			buttonWin.setVisibility(Button.INVISIBLE);
			finish();
			Intent intent = new Intent(this, ActivityGame.class);
			startActivity(intent);
			break;
		case R.id.buttonOk:
			// начало игры
			buttonOk.setVisibility(Button.INVISIBLE);
			buttonRepeat.setVisibility(Button.INVISIBLE);
			gridviewComp.setOnItemClickListener(this);
			break;
		case R.id.buttonRepeat:
			// Перегенерация кораблей
			initFilds();
			break;
		default:
			break;
		}
	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	/*
	 * Обработка выхода из паузы игры
	 */
	@Override
	protected void onPause() {
		ActivityMenu.musicService.pause();
		AudioApp.onPause();
		super.onStop();
	}

	/*
	 * Обработка паузы игры
	 */
	@Override
	protected void onResume() {
		ActivityMenu.musicService.resume();
		AudioApp.onResume();
		super.onResume();
	}
}
