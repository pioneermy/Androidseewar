package com.example.androidseewar.logica;

import java.util.Random;

public class Move {
	static final byte X_FILD = 10;// Количество клеток поля по горизонтали
	static final byte Y_FILD = 10;// Количество клеток поля по вертикали
	static final byte WATER_CH = 90;// Код "ВОДА" или промах

	// Флаги для автоматического обстрела (компьютер)
	private boolean work = true;
	private boolean flagShop2 = false;
	private boolean flagShop3 = false;
	private boolean flagShop3End = false;
	private boolean flagShop4 = false;
	private boolean flagShop4End = false;

	// Флаг повторного хода
	private boolean flagRepeatStep = false;
	// Счетчики палуб при попадании
	private int count = 0;
	private int count4 = 0;

	// Растановка кораблей
	private Random ram = new Random();
	private int x;
	private int y;
	private int rotX;// Флаги вращения
//	private int rotY;

	// Позиции палуб при попандании в корабль (автоматический режим)
	private int positionX;
	private int positionY;
	private int positionX1;
	private int positionY1;
	private int newPositionX;
	private int newPositionY;

	/*
	 * Растановка кораблей field - игровое поле, paluba - количество палуб у
	 * корабля, id - номер корабля. При расстановке корабля для него в ячейку
	 * игрового поля записуется paluba * 10 + id
	 */
	public boolean filling(Field field, int paluba, int id) {
		x = ram.nextInt(X_FILD);
		y = ram.nextInt(Y_FILD);

		rotX = ram.nextInt(2);
//		if (rotX == 0)
//			rotY = 1;
//		else
//			rotY = 0;

		if (rotX == 1) {
			if (x + paluba > X_FILD) {
				return false;
			}
			for (int i = x; i < x + paluba; i++) {
				if (field.mas[i][y] != 0)
					return false;
			}
			for (int i = x; i < x + paluba; i++) {
				field.mas[i][y] = paluba * 10 + id;
			}
		}

		else {
			if (y + paluba > Y_FILD) {
				return false;
			}
			for (int i = y; i < y + paluba; i++) {
				if (field.mas[x][i] != 0)
					return false;
			}
			for (int i = y; i < y + paluba; i++) {
				field.mas[x][i] = paluba * 10 + id;
			}
		}

		int kEnd;
		int nEnd;
		int k = x - 1;
		int n = y - 1;

		if (rotX == 1) {
			kEnd = x + paluba + 1;
			nEnd = y + 2;
		} else {
			kEnd = x + 2;
			nEnd = y + paluba + 1;
		}

		if (k < 0)
			k = x;
		if (n < 0)
			n = y;
		if (kEnd > X_FILD)
			kEnd = X_FILD;
		if (nEnd > Y_FILD)
			nEnd = Y_FILD;

		for (int i = k; i < kEnd; i++) {
			for (int j = n; j < nEnd; j++) {
				if (field.mas[i][j] == 0)
					field.mas[i][j] = WATER_CH;
			}
		}
		return true;
	}

	/*
	 * "Вода" при попадании в корабль (по диагоналям) для ускорения обстрела
	 */
	private void waterShot(Field field, int positionX, int positionY) {

		if (positionX != 0) {
			if (positionY != 0)
				field.mas[positionX - 1][positionY - 1] = WATER_CH;
		}

		if (positionX != field.mas.length - 1) {
			if (positionY != field.mas[positionX].length - 1)
				field.mas[positionX + 1][positionY + 1] = WATER_CH;
		}

		if (positionX != 0) {
			if (positionY != field.mas[positionX].length - 1)
				field.mas[positionX - 1][positionY + 1] = WATER_CH;
		}

		if (positionX != field.mas.length - 1) {
			if (positionY != 0)
				field.mas[positionX + 1][positionY - 1] = WATER_CH;
		}
	}

	// Заполнение "Водой" вокруг ячейки x,y
	private void water(Field field, int x, int y) {

		int k = x - 1;
		int n = y - 1;

		int kEnd = x + 2;
		int nEnd = y + 2;

		if (k < 0)
			k = x;
		if (n < 0)
			n = y;
		if (kEnd > X_FILD)
			kEnd = X_FILD;
		if (nEnd > Y_FILD)
			nEnd = X_FILD;

		for (int i = k; i < kEnd; i++) {
			for (int j = n; j < nEnd; j++) {
				if (field.mas[i][j] == 0)
					field.mas[i][j] = WATER_CH;
			}
		}
	}

	/*
	 * Логика выстрела компьютера fieldEtalon - поле с расставлеными
	 * кораблями,Field fieldShot - поле выстрелов
	 */
	public boolean logika(Field fieldEtalon, Field fieldShot) {
		// Проверка - стреляли в это место?
		// Если да, то поле будет отличное от 0
		do {
			positionX = ram.nextInt(X_FILD);
			positionY = ram.nextInt(Y_FILD);
		} while (fieldShot.mas[positionX][positionY] != 0);

		// Проверка - если попадание, то установить Флаг повторного хода, иначе
		// сбросить

		int value = fieldEtalon.mas[positionX][positionY] / 10;
		if (value != 0) {
			flagRepeatStep = true;
		} else
			flagRepeatStep = false;

		// Проверка - сколько палуб у корабля?
		switch (value) {
		case 0: {
			// "Вода"
			fieldShot.mas[positionX][positionY] = WATER_CH;
			return true;
		}
		case 1: {
			// Однопалубный корабль
			fieldShot.mas[positionX][positionY] = fieldEtalon.mas[positionX][positionY];
			// "Вода" вокруг корабля
			water(fieldShot, positionX, positionY);
			return true;
		}
		case 2: {
			// Двухпалубный корабль
			fieldShot.mas[positionX][positionY] = fieldEtalon.mas[positionX][positionY];
			// "Вода" по диагоналям ячейки в которую стреляли
			waterShot(fieldShot, positionX, positionY);
			// Флаг попадания - двухтрубный
			flagShop2 = true;
			return true;
		}
		case 3: {
			// Трехпалубный корабль
			fieldShot.mas[positionX][positionY] = fieldEtalon.mas[positionX][positionY];
			// "Вода" по диагоналям ячейки в которую стреляли
			waterShot(fieldShot, positionX, positionY);
			// Флаг попадания - трехтрубный
			flagShop3 = true;
			return true;
		}
		case 4: {
			// Четырехпалубный корабль
			fieldShot.mas[positionX][positionY] = fieldEtalon.mas[positionX][positionY];
			// "Вода" по диагоналям ячейки в которую стреляли
			waterShot(fieldShot, positionX, positionY);
			// Первая из четырех
			count4 = 1;
			// Флаг попадания - четырехтрубный
			flagShop4 = true;
			return true;
		}
		case WATER_CH: {
			// Уже стреляли - можно не проверять
			fieldShot.mas[positionX][positionY] = WATER_CH;
			return true;
		}
		default:
			return false;
		}
	}

	/*
	 * Логика - добить двухтрубный
	 */
	public void shotShop2(Field fieldEtalon, Field fieldShot) {
		// Стреляем крестом вогруг первого попадания
		do {
			newPositionX = positionX + ram.nextInt(3) - 1;
			newPositionY = positionY + ram.nextInt(3) - 1;

			if ((newPositionX < 0) || (newPositionX > 9))
				newPositionX = positionX;
			if ((newPositionY < 0) || (newPositionY > 9))
				newPositionY = positionY;

		} while (fieldShot.mas[newPositionX][newPositionY] != 0);

		// Попали
		if (fieldEtalon.mas[newPositionX][newPositionY] / 10 == 2) {
			fieldShot.mas[newPositionX][newPositionY] = fieldEtalon.mas[newPositionX][newPositionY];
			waterShot(fieldShot, newPositionX, newPositionY);
			// "Вода" вокруг корабля
			water(fieldShot, newPositionX, newPositionY);
			water(fieldShot, positionX, positionY);
			// Флаг - двухтрубный добит
			flagShop2 = false;
			// Флаг повторного хода
			flagRepeatStep = true;
			return;
		}

		// Флаг повторного ход
		flagRepeatStep = false;
		// "Вода"
		fieldShot.mas[newPositionX][newPositionY] = WATER_CH;
	}

	/*
	 * Логика - добить трехтрубный, найти направление
	 */
	public void shotShop3(Field fieldEtalon, Field fieldShot) {
		// Стреляем крестом вогруг первого попадания
		do {
			newPositionX = positionX + ram.nextInt(3) - 1;
			if ((newPositionX < 0) || (newPositionX > 9))
				newPositionX = positionX;

			newPositionY = positionY + ram.nextInt(3) - 1;
			if ((newPositionY < 0) || (newPositionY > 9))
				newPositionY = positionY;

		} while (fieldShot.mas[newPositionX][newPositionY] != 0);
		// Попали
		if (fieldEtalon.mas[newPositionX][newPositionY] / 10 == 3) {
			fieldShot.mas[newPositionX][newPositionY] = fieldEtalon.mas[newPositionX][newPositionY];

			// "Вода" по диагоналям ячейки
			waterShot(fieldShot, newPositionX, newPositionY);
			waterShot(fieldShot, positionX, positionY);
			// Позиция второй палубы
			positionX1 = newPositionX;
			positionY1 = newPositionY;
			// Флаг - вторая палуба подбита
			flagShop3 = false;
			// Флаг - осталась третья палуба
			flagShop3End = true;
			// Флаг повторного ход
			flagRepeatStep = true;
			return;
		}

		// Флаг повторного ход
		flagRepeatStep = false;
		// "Вода"
		fieldShot.mas[newPositionX][newPositionY] = WATER_CH;
	}

	/*
	 * Логика - добить трехтрубный
	 */
	public void shotShop3End(Field fieldEtalon, Field fieldShot) {
		// Стреляем в конец или в начало трехтрубного корабля
		// Две палубы знаем
		do {
			if (positionX == positionX1)
				newPositionX = positionX;
			else if (positionX < positionX1)
				newPositionX = positionX - 1;
			else
				newPositionX = positionX + 1;

			if (positionY == positionY1)
				newPositionY = positionY;
			else if (positionY < positionY1)
				newPositionY = positionY - 1;
			else
				newPositionY = positionY + 1;
			// Проверка выстрела за игровое поле
			if ((newPositionX < 0) || (newPositionX > X_FILD)
					|| (newPositionY < 0) || (newPositionY > Y_FILD)
					|| (fieldShot.mas[newPositionX][newPositionY] != 0)) {
				newPositionX = positionX;
				newPositionY = positionY;
				positionX = positionX1;
				positionY = positionY1;
				positionX1 = newPositionX;
				positionY1 = newPositionY;
			}
		} while (fieldShot.mas[newPositionX][newPositionY] != 0);

		// Попали
		if (fieldEtalon.mas[newPositionX][newPositionY] / 10 == 3) {
			fieldShot.mas[newPositionX][newPositionY] = fieldEtalon.mas[newPositionX][newPositionY];
			// "Вода" вокруг корабля
			water(fieldShot, newPositionX, newPositionY);
			water(fieldShot, positionX, positionY);
			water(fieldShot, positionX1, positionY1);
			// Флаг - трехтрубный подбит
			flagShop3End = false;
			// Флаг повторного ход
			flagRepeatStep = true;
			return;
		}
		// "Вода"
		fieldShot.mas[newPositionX][newPositionY] = WATER_CH;
		newPositionX = positionX;
		newPositionY = positionY;
		positionX = positionX1;
		positionY = positionY1;
		positionX1 = newPositionX;
		positionY1 = newPositionY;
		// Флаг повторного ход
		flagRepeatStep = false;
	}

	/*
	 * Логика - добить четырехтрубный, найти направление
	 */
	public void shotShop4(Field fieldEtalon, Field fieldShot) {
		// Стреляем крестом вогруг первого попадания
		do {
			newPositionX = positionX + ram.nextInt(3) - 1;
			if ((newPositionX < 0) || (newPositionX > 9))
				newPositionX = positionX;

			newPositionY = positionY + ram.nextInt(3) - 1;
			if ((newPositionY < 0) || (newPositionY > 9))
				newPositionY = positionY;

		} while (fieldShot.mas[newPositionX][newPositionY] != 0);

		// Попали
		if (fieldEtalon.mas[newPositionX][newPositionY] / 10 == 4) {
			fieldShot.mas[newPositionX][newPositionY] = fieldEtalon.mas[newPositionX][newPositionY];
			waterShot(fieldShot, newPositionX, newPositionY);
			positionX1 = newPositionX;
			positionY1 = newPositionY;
			// Считаем палубы
			++count4;
			// Флаг первая палуба подбита
			flagShop4 = false;
			// Флаг добиваем четырехтрубный
			flagShop4End = true;
			// Флаг повторного ход
			flagRepeatStep = true;
			return;
		}
		// "Вода"
		fieldShot.mas[newPositionX][newPositionY] = WATER_CH;
		// Флаг повторного ход
		flagRepeatStep = false;
	}

	/*
	 * Логика - добить четырехтрубный
	 */
	public void shotShop4End(Field fieldEtalon, Field fieldShot) {

		int newPositionX;
		int newPositionY;

		// Стреляем в конец или в начало четырехтрубного корабля
		// Две палубы знаем (начало и конец)
		do {
			if (positionX == positionX1)
				newPositionX = positionX;
			else if (positionX < positionX1)
				newPositionX = positionX - 1;
			else
				newPositionX = positionX + 1;

			if (positionY == positionY1)
				newPositionY = positionY;
			else if (positionY < positionY1)
				newPositionY = positionY - 1;
			else
				newPositionY = positionY + 1;

			if ((newPositionX < 0) || (newPositionX > 9) || (newPositionY < 0)
					|| (newPositionY > 9)
					|| (fieldShot.mas[newPositionX][newPositionY] != 0)) {
				newPositionX = positionX;
				newPositionY = positionY;
				positionX = positionX1;
				positionY = positionY1;
				positionX1 = newPositionX;
				positionY1 = newPositionY;
			}

		} while (fieldShot.mas[newPositionX][newPositionY] != 0);
		// Попали				
		if (fieldEtalon.mas[newPositionX][newPositionY] / 10 == 4) {
			fieldShot.mas[newPositionX][newPositionY] = fieldEtalon.mas[newPositionX][newPositionY];
			waterShot(fieldShot, newPositionX, newPositionY);
			// Меняем координаты начала и конца корабля			
			if (positionX < newPositionX)
				if (positionX1 < positionX)
					positionX = newPositionX;
				else
					positionX1 = newPositionX;
			else if (positionX1 < positionX)
				positionX1 = newPositionX;
			else
				positionX = newPositionX;

			if (positionY < newPositionY)
				if (positionY1 < positionY)
					positionY = newPositionY;
				else
					positionY1 = newPositionY;
			else if (positionY1 < positionY)
				positionY1 = newPositionY;
			else
				positionY = newPositionY;
			
			// Считаем палубы для выхода			
			if (++count4 == 4) {
				// Ура подбили
				flagShop4End = false;
				// "Вода" вокруг четырехтрубного корабля				
				water(fieldShot, newPositionX, newPositionY);
				water(fieldShot, positionX, positionY);
				water(fieldShot, positionX1, positionY1);
			}
			// Флаг повторного ход
			flagRepeatStep = true;
			return;
		}
		// "Вода"
		fieldShot.mas[newPositionX][newPositionY] = WATER_CH;
		newPositionX = positionX;
		newPositionY = positionY;
		positionX = positionX1;
		positionY = positionY1;
		positionX1 = newPositionX;
		positionY1 = newPositionY;
		// Флаг повторного ход
		flagRepeatStep = false;
	}

	/*
	 * Подсчет подбитых палуб на всем поле
	 */
	public int getCount(Field field) {
		count = 0;
		for (int i = 0; i < field.mas.length; i++) {
			for (int j = 0; j < field.mas[i].length; j++) {
				if ((field.mas[j][i] != 0) && (field.mas[j][i] != WATER_CH))
					count++;
			}
		}
		return count;
	}

	/*
	 * Проверка - на поле есть хоть один корабль
	 */
	public boolean noNullfiel(Field field) {
		boolean flag = false;
		for (int i = 0; i < X_FILD; i++) {
			for (int j = 0; j < Y_FILD; j++) {
				if (field.mas[j][i] == 0)
					flag = true;
			}
		}
		return flag;
	}

	public void setWork(boolean work) {
		this.work = work;
	}

	public boolean getWork() {
		return work;
	};

	public boolean isFlagShop2() {
		return flagShop2;
	};

	public boolean isFlagShop3() {
		return flagShop3;
	};

	public boolean isFlagShop3End() {
		return flagShop3End;
	};

	public boolean isFlagShop4() {
		return flagShop4;
	};

	public boolean isFlagShop4End() {
		return flagShop4End;
	}

	public boolean isFlagRepeatStep() {
		return flagRepeatStep;
	}

	/*
	 * Очистка поля
	 */
	public void clear(Field field) {

		for (int i = 0; i < X_FILD; i++) {
			for (int j = 0; j < Y_FILD; j++) {
				field.mas[i][j] = 0;
			}
		}
	}

	/*
	 * Очистка поля после расстановки кораблей от "ВОДЫ"
	 */
	public void clearWater(Field field) {

		for (int i = 0; i < Move.X_FILD; i++) {
			for (int j = 0; j < Move.Y_FILD; j++) {
				if (field.mas[i][j] == WATER_CH)
					field.mas[i][j] = 0;
			}
		}
	}

	/*
	 * Смешивание полей для отображения на экране
	 */
	public void screen(Field fieldA, Field fieldB, Field fieldC) {
		for (int i = 0; i < X_FILD; i++) {
			for (int j = 0; j < Y_FILD; j++) {
				if ((fieldB.mas[i][j] == 0) && (fieldC.mas[i][j] == WATER_CH)) {
					fieldA.mas[i][j] = WATER_CH;
					continue;
				}
				if ((fieldB.mas[i][j] == 0) && (fieldC.mas[i][j] == 0)) {
					fieldA.mas[i][j] = 0;
					continue;
				}
				if (fieldB.mas[i][j] == fieldC.mas[i][j])
					fieldA.mas[i][j] = fieldB.mas[i][j] + 40;
				else
					fieldA.mas[i][j] = fieldB.mas[i][j];
			}
		}
	}

	/*
	 * "Вода" вокруг корабля
	 */
	public void warterForShip(Field field, int x, int y) {
		int paluba = field.mas[x][y] / 10;
		// int id = field.mas[x][y] % 10;
		int value = field.mas[x][y];
		int count = 0;

		for (int i = 0; i < field.mas.length; i++) {
			for (int j = 0; j < field.mas[i].length; j++) {
				if (field.mas[i][j] == value)
					count++;
			}
		}

		if (count == paluba) {
			for (int i = 0; i < field.mas.length; i++) {
				for (int j = 0; j < field.mas[i].length; j++) {
					if (field.mas[i][j] == value)
						water(field, i, j);
					;
				}
			}
		}
	}

}
