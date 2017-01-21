package com.example.androidseewar.logica;
/*
 * Игровое поле
 */
public class Field {
	public int[][] mas;

	Field() {
		this(10, 10);
	}
// Инициализация пустого игрового поля
	Field(int x, int y) {
		mas = new int[x][y];
		for (int i = 0; i < mas.length; i++) {
			for (int j = 0; j < mas[i].length; j++) {
				mas[j][i] = 0;
			}
		}
	}
	
/*
 * Вывод игрового поля для тестирования 
 */
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < mas.length; i++) {
			for (int j = 0; j < mas[i].length; j++) {
				if (mas[j][i] == 0) {
					str += " " + " ";
				} else if (mas[j][i] == 8) {
					str += "." + " ";
				} else
				// str += "X" + " ";

				{
					str += mas[j][i] + " ";
				}
			}
			str += "\n";
		}
		return str;
	}
}
