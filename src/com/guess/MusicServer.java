package com.guess;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicServer extends Service {

	private MediaPlayer mp;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// 开始播放音乐
		mp.start();
		// 音乐播放完毕的事件处理
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				// 循环播放
				try {
					mp.start();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		});
		// 播放音乐时发生错误的事件处理
		mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// 释放资源
				try {
					mp.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	@Override
	public void onCreate() {
		// 初始化音乐资源
		super.onCreate();
		try {
			// 创建MediaPlayer对象
			mp = new MediaPlayer();
			// 将音乐以Import的方式保存在res/raw/zhou.mp3
			mp = MediaPlayer.create(MusicServer.this, R.raw.music1);
			// 在MediaPlayer取得播放资源与stop()之后要准备PlayBack的状
			// 态前一定要使用MediaPlayer.prepeare()
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		// 服务停止时停止播放音乐并释放资源
		super.onDestroy();
		mp.stop();
		mp.release();
	}
}