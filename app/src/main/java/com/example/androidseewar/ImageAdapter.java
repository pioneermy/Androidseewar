package com.example.androidseewar;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds = new Integer[100];

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Resources res = mContext.getResources();
        // sizeFild зависит от разрешения экрана
        int sizeFild = res.getInteger(R.integer.sizeFild);

        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(sizeFild,
                    sizeFild));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // отступы между ячейками
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    /*
     * Отображение изображения корабля и "Воды". До 4 - изображение корабля,
     * больше 4 - изображение подбитого корабля
     */
    public boolean initArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                int value = array[j][i] / 10;
                switch (value) {
                    case 0:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview0;
                        break;
                    case 1:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview1;
                        break;
                    case 2:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview2;
                        break;
                    case 3:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview3;
                        break;
                    case 4:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview4;
                        break;
                    case 5:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview5;
                        break;
                    case 6:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview6;
                        break;
                    case 7:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview7;
                        break;
                    case 8:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview8;
                        break;
                    case 9:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview;
                        break;
                    default:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview;
                        break;
                }
            }

        }
        return true;
    }

	/*
     * Отображение изображения корабля и "Воды" когда все корабли имеют
	 * одинаковое отображение (уровень сложности)
	 */

    public boolean initArrayLow(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                int value = array[j][i] / 10;
                switch (value) {
                    case 0:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview0;
                        break;
                    case 1:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview1;
                        break;
                    case 2:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview1;
                        break;
                    case 3:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview1;
                        break;
                    case 4:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview1;
                        break;
                    case 5:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview5;
                        break;
                    case 6:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview5;
                        break;
                    case 7:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview5;
                        break;
                    case 8:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview5;
                        break;
                    case 9:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview;
                        break;
                    default:
                        mThumbIds[i * array[i].length + j] = R.drawable.gridview;
                        break;
                }
            }

        }

        return true;
    }
}