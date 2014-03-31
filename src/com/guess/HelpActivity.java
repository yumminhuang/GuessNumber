package com.guess;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity{
	private Button BackButton;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		BackButton = (Button)findViewById(R.id.help_ok_button);
		BackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HelpActivity.this.finish();
			}
		});
	}
}
