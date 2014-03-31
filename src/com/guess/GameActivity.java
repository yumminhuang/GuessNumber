package com.guess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class GameActivity extends Activity {
	private Intent musicintent;
	private static final int REQ_SYSTEM_SETTINGS = 0;
	private int A, B, C, ori_count, ori_place, count, place;
	private boolean isOver = false;
	private boolean isMusic = false;
	// /////////////////////////
	private String strAnsNum = null;
	// ///////////////////////
	private String strInputNum;
	private EditText numText;
	private Button SetNum, Enter;
	
	private LinearLayout mLayout;
	private ScrollView mScrollView;
	private final Handler mhandler = new Handler();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ContentView
		setContentView(R.layout.game);
		// Some widgets
		mLayout = (LinearLayout) findViewById(R.id.linearLayout2);
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		numText = (EditText) findViewById(R.id.NumberEdit);
		Enter = (Button) findViewById(R.id.buttonOK);
		SetNum = (Button) findViewById(R.id.buttonSetNumber);

		// 获取设置界面PreferenceActivity中各个Preference的值
		String str_opp_list = getResources().getString(R.string.opp_list_key);
		String str_place_list = getResources().getString(
				R.string.place_list_key);
		String str_music_check = getResources().getString(
				R.string.music_check_key);
		// 取得属于整个应用程序的SharedPreferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		ori_count = Integer.parseInt(settings.getString(str_opp_list, "5"));
		ori_place = Integer.parseInt(settings.getString(str_place_list, "3"));
		isMusic = settings.getBoolean(str_music_check, true);
		//播放背景音乐
		if(isMusic){
			musicintent = new Intent(GameActivity.this,MusicServer.class);
			startService(musicintent);
		}
		// Trash dispose
		str_opp_list = str_place_list = null;
		place = ori_place;
		SetNum.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				generateNumber();
			}
		});
		Enter.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				judge();
			}
		});
	}
	
	@Override
	public void onStop(){
		super.onStop();
		Intent intent = new Intent(GameActivity.this,MusicServer.class); 
		stopService(intent); 
	}

	private void generateNumber() {
		/*
		 * 生成n位随机数
		 */
		int i, temp;
		boolean num[];
		numText.setEnabled(true);
		isOver = false;
		// count 初始化
		count = ori_count;
		num = new boolean[10];
		for (i = 0; i <= 9; i++)
			num[i] = false;
		A = B = C = 0;
		// ///////////////////////////
		StringBuffer strBuffer = new StringBuffer();
		// ////////////////////////////
		for (i = 0; i < place;) {
			temp = ((int) (Math.random() * 100)) % 10;
			if (i == 0 && temp == 0)
				continue;
			if (!num[temp]) {
				// /////////////////////////
				strBuffer.append(Integer.toString(temp));
				// ///////////////////////////
				num[temp] = true;
				i++;
			}
		}
		// /////////////////////////
		strAnsNum = strBuffer.toString();
		strBuffer = null;
		// ///////////////////////////
		Toast.makeText(GameActivity.this, "已随机生成一个" + place + "位数!",
				Toast.LENGTH_SHORT).show();
	}

	private void judge() {
		// TODO Auto-generated method stub
		int Return = input();
		if (Return == 0) {
			Toast.makeText(GameActivity.this, "输入一个" + place + "位数!",
					Toast.LENGTH_SHORT).show();
			return;
		}
		int i, j;
		boolean flag;
		A = 0;
		B = 0;
		C = 0;
		for (i = 0; i < place; i++) {
			flag = false;
			for (j = 0; j < place; j++)
				if (strInputNum.charAt(j) == strAnsNum.charAt(i) && j == i) {
					A++;
					flag = true;
					break;
				} else if (strInputNum.charAt(j) == strAnsNum.charAt(i)) {
					B++;
					flag = true;
					break;
				}
			if (!flag)
				C++;
		}
		if (A == place && !isOver) {
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			TextView resultText = new TextView(GameActivity.this);
			resultText.setTextColor(Color.BLACK);
			resultText.setText("猜对了!答案是 " + strAnsNum);
			mLayout.addView(resultText, p);
			CongratulationDialog();
		} else if (Return == 1) {
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			TextView resultText = new TextView(GameActivity.this);
			resultText.setTextColor(Color.BLACK);
			resultText.append("很遗憾，您还没猜对！正确答案:" + strAnsNum);
			mLayout.addView(resultText, p);
		} else {
			TextView resultText = new TextView(GameActivity.this);
			resultText.setTextColor(Color.BLACK);
			resultText.setText((ori_count - count) + " " + strInputNum + " "
					+ "A:" + A + " B:" + B + " C:" + C);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			mLayout.addView(resultText, p);
			mhandler.post(mScrollToBottom);
		}
		numText.setText(null);
	}

	private void CongratulationDialog() {
		// Show the player wins
		new AlertDialog.Builder(GameActivity.this)
				.setTitle("猜对了！")
				.setIcon(R.drawable.congratulations)
				.setMessage("恭喜你！")
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						}).show();
	}

	private Runnable mScrollToBottom = new Runnable() {
		@Override
		public void run() {
			int off = mLayout.getMeasuredHeight() - mScrollView.getHeight();
			if (off > 0)
				mScrollView.scrollTo(0, off);
		}
	};

	private int input() {
		strInputNum = numText.getText().toString();
		// Input numbers
		if (strInputNum.length() != place) {
			strInputNum = null;
			return 0;
		}
		// ///////////////////////////////
		if (count <= 1) {
			isOver = true;
			return 1;
		}
		count--;
		TextView resultText = new TextView(GameActivity.this);
		resultText.setText(null);
		return 2;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Create Menu
		menu.add(0, 0, 0, R.string.help);
		menu.add(0, 1, 1, R.string.setting);
		menu.add(0, 2, 2, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			// Show help information
			Intent intent_to_help = new Intent();
			intent_to_help.setClass(GameActivity.this, HelpActivity.class);
			startActivityForResult(intent_to_help, 0);
			break;
		case 1:
			// Set difficulty
			startActivityForResult(new Intent(this, SettingActivity.class),
					REQ_SYSTEM_SETTINGS);
			break;
		case 2:
			// Exit
			exitOptionDialog();
			break;
		}
		return true;
	}

	// Settings设置界面返回的结果
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_SYSTEM_SETTINGS) {
			// 获取设置界面PreferenceActivity中各个Preference的值
			String str_opp_list = getResources().getString(
					R.string.opp_list_key);
			String str_place_list = getResources().getString(
					R.string.place_list_key);
			String str_music_check = getResources().getString(
					R.string.music_check_key);
			// 取得属于整个应用程序的SharedPreferences
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(this);
			ori_count = Integer.parseInt(settings.getString(str_opp_list, "5"));
			ori_place = Integer.parseInt(settings
					.getString(str_place_list, "3"));
			isMusic = settings.getBoolean(str_music_check, true);
			if (count != ori_count) {
				numText.setEnabled(false);
			}
			count = ori_count;
			place = ori_place;
			// Trash dispose
			str_opp_list = str_place_list = null;
		} else {
			// 其他Intent返回的结果
		}
	}

	private void exitOptionDialog() {
		// Interactive dialog to exit game
		new AlertDialog.Builder(GameActivity.this)
				.setTitle(R.string.exit)
				.setIcon(R.drawable.icon2)
				.setMessage("你确定退出吗？")
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}
}