package com.example.androidseewar;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.example.androidseewar.service.AudioApp;

import io.fabric.sdk.android.Fabric;


public class ActivityMain extends Activity implements AnimationListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.io
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);

        // Настройка громкости игры с помощью кнопок
        // регулирования громкости устройства
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioApp.instalation(this);
        // Инициализация анимации логотипа
        ImageView imageLogo = (ImageView) findViewById(R.id.imageViewLogo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        imageLogo.startAnimation(animation);
        animation.setAnimationListener(this);
    }

    public void onAnimationEnd(Animation animation) {
        // Запуск активити меню после окончания анимации
        Intent intent = new Intent(this, ActivityMenu.class);
        startActivity(intent);
        finish();
    }

    public void onAnimationRepeat(Animation animation) {

    }

    public void onAnimationStart(Animation animation) {

    }

}
