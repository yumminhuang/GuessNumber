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
		// ��ʼ��������
		mp.start();
		// ���ֲ�����ϵ��¼�����
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				// ѭ������
				try {
					mp.start();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		});
		// ��������ʱ����������¼�����
		mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// �ͷ���Դ
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
		// ��ʼ��������Դ
		super.onCreate();
		try {
			// ����MediaPlayer����
			mp = new MediaPlayer();
			// ��������Import�ķ�ʽ������res/raw/zhou.mp3
			mp = MediaPlayer.create(MusicServer.this, R.raw.music1);
			// ��MediaPlayerȡ�ò�����Դ��stop()֮��Ҫ׼��PlayBack��״
			// ̬ǰһ��Ҫʹ��MediaPlayer.prepeare()
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
		// ����ֹͣʱֹͣ�������ֲ��ͷ���Դ
		super.onDestroy();
		mp.stop();
		mp.release();
	}
}