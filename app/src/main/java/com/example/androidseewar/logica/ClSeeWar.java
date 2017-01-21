package com.example.androidseewar.logica;

public class ClSeeWar {

	// Флаг - победа компьютера
	private boolean isCompWin = false;
	// Флаг - победа игрока
	private boolean isUserWin = false;

	// Количество кораблей
	private int countFildsShips = 0;
	// Уровень сложности
	private int levelStrong = 0;
	// Счетчик ходов компьютера
	private int countShortComp = 0;
	// Счетчик ходов игрока
	private int countShortUser = 0;

	public Move move = new Move();
	public Field fieldComp;
	public Field fieldUser;
	public Field fieldShot;
	public Field fieldShotUser;
	public Field fieldScreenShot;

	/**
	 * Игровой процесс
	 */
	public ClSeeWar(int levelStrong, int countShip1, int countShip2,
			int countShip3, int countShip4) {

		this.levelStrong = levelStrong;
		countShortUser = 0;
		countShortComp = 0;
		// Поле компьтера
		fieldComp = new Field(Move.X_FILD, Move.Y_FILD);
		// Поле игрока
		fieldUser = new Field(Move.X_FILD, Move.Y_FILD);
		// Выстрелы на поле компьтера
		fieldShot = new Field(Move.X_FILD, Move.Y_FILD);
		// Выстрелы на поле игрока
		fieldShotUser = new Field(Move.X_FILD, Move.Y_FILD);
		// Поле смешивания выстрелов и расстановки кораблей для отображения на
		// экране
		fieldScreenShot = new Field(Move.X_FILD, Move.Y_FILD);

		// Очистка полей
		move.clear(fieldComp);
		move.clear(fieldUser);
		move.clear(fieldShot);
		move.clear(fieldShotUser);
		move.clear(fieldScreenShot);

		// Расстановка кораблей
		countFildsShips = countShip4 * 4 + countShip3 * 3 + countShip2 * 2
				+ countShip1 * 1;

		initShip(fieldComp, countShip1, countShip2, countShip3, countShip4);
		initShip(fieldUser, countShip1, countShip2, countShip3, countShip4);

		// Очистка полей от "Воды" при расстановке
		move.clearWater(fieldComp);
		move.clearWater(fieldUser);

		// Отображение на экране поля Игрока путем смешивания его кораблей и
		// выстрелов компьютера
		move.screen(fieldScreenShot, fieldUser, fieldShot);
	}

	/*
	 * Расстановка кораблей
	 */
	private void initShip(Field field, int countShip1, int countShip2,
			int countShip3, int countShip4) {
		// Четырехтрубный
		for (int i = 0; i < countShip4; i++) {
			while (!move.Filling(field, 4, i))
				;
		}
		// Трехтрубный
		for (int i = 0; i < countShip3; i++) {
			while (!move.Filling(field, 3, i))
				;
		}
		// Двухтрубный
		for (int i = 0; i < countShip2; i++) {
			while (!move.Filling(field, 2, i))
				;
		}
		// Однотрубныйтрубный
		for (int i = 0; i < countShip1; i++) {
			while (!move.Filling(field, 1, i))
				;
		}
	}

	// /**
	// * Обработка выстрела пользователя
	// * index - координата одномерного массива, в которую стрелял игрок 
	// */
	public int shortUser(int index) {
		// Выделение из index - x,y
		int x = (int) index % 10;
		int y = (int) index / 10;
		// Проверка попадания
		if (fieldShotUser.mas[x][y] != 0)
			//Если игрок уже стрелял в это поле			
			return 2; // Повторный ход
		else {
			countShortUser++;

			if (fieldComp.mas[x][y] == 0) {
				//"Вода"
				fieldShotUser.mas[x][y] = Move.WATER_CH;
				return 0;
			} else {
				//Попал
				fieldShotUser.mas[x][y] = fieldComp.mas[x][y];
				//При потоплении "Вода" вокруг корабля
				if ((levelStrong == 0) || (levelStrong == 1))
					move.warterForShip(fieldShotUser, x, y);
				
				// Проверка окончания игры
				if (move.getCount(fieldShotUser) == countFildsShips) {
					isUserWin = true;
				}				
				return 1; // Повторный ход
			}
		}
	}

	public boolean shortComp() {
		//Счетчик ходов компьютера
		countShortComp++;
		// Проверка - добить двухтрубный
		if (move.isFlagShop2()) {
			move.shotShop2(fieldUser, fieldShot);
			// Проверка - добить трехтрубный
		} else if (move.isFlagShop3End()) {
			move.shotShop3End(fieldUser, fieldShot);
			// Проверка - найти вторую палубу в трехтрубном
		} else if (move.isFlagShop3()) {
			move.shotShop3(fieldUser, fieldShot);
			// Проверка - найти вторую палубу в четырехтрубном			
		} else if (move.isFlagShop4End()) {				
			move.shotShop4End(fieldUser, fieldShot);
			// Проверка - добить четырехтрубный		
		} else if (move.isFlagShop4()) {
			move.shotShop4(fieldUser, fieldShot);
		} else
			// Случайный выстрел компьютера
			while (!move.logika(fieldUser, fieldShot))
				;
		// Смешать поля кораблей и выстрелов для вывода на экран	
		move.screen(fieldScreenShot, fieldUser, fieldShot);

		// Проверка окончания игры
		if (move.getCount(fieldShot) == countFildsShips) {
			isCompWin = true;
		}
		// Возврат флага повторного хода
		return move.isFlagRepeatStep();
	}

	public boolean isCompWin() {
		return isCompWin;
	}

	public boolean isUserWin() {
		return isUserWin;
	}

	public int getCountShortComp() {
		return countShortComp;
	}

	public int getCountShortUser() {
		return countShortUser;
	}
}
