package com.guess;

import android.os.Bundle;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.*;

public class SettingActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	String str_opp_list, str_place_list,str_music_check;
	ListPreference opp_list, place_list;
	CheckBoxPreference music_check;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Add Preference items from the xml file
		addPreferencesFromResource(R.xml.setting_layout);
		//Add items
		str_opp_list = getResources().getString(R.string.opp_list_key);
		str_place_list = getResources().getString(R.string.place_list_key);
		str_music_check = getResources().getString(R.string.music_check_key);
		opp_list = (ListPreference) findPreference(str_opp_list);
		place_list = (ListPreference) findPreference(str_place_list);
		music_check = (CheckBoxPreference) findPreference(str_music_check);
		// Add Listeners
		opp_list.setOnPreferenceChangeListener(this);
		opp_list.setOnPreferenceClickListener(this);
		place_list.setOnPreferenceChangeListener(this);
		place_list.setOnPreferenceClickListener(this);
		music_check.setOnPreferenceChangeListener(this);
		music_check.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals(str_opp_list)) {
			//
		} else if (preference.getKey().equals(str_place_list)) {
			//
		} else if(preference.getKey().equals(str_music_check)){
			
		}else
			return false;
		return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals(str_opp_list)) {
			//
		} else if (preference.getKey().equals(str_place_list)) {
			//
		} else if(preference.getKey().equals(str_music_check)){
			
		}else
			return false;
		return true;
	}
}
