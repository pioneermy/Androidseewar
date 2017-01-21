package com.example.androidseewar.logica;
/*
 * Игровое поле
 */
public class Field {
	public int[][] mas;
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
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < mas.length; i++) {
			for (int j = 0; j < mas[i].length; j++) {
				if (mas[j][i] == 0) {
					buf.append(" ");
					buf.append(" ");
				} else if (mas[j][i] == 8) {
					buf.append(".");
					buf.append(" ");
				} else
//					buf.append("X");
//					buf.append(" ");
				{
					buf.append(mas[j][i]);
					buf.append(" ");
				}
			}
			buf.append("\n");

		}
		return buf.toString();
	}
}
