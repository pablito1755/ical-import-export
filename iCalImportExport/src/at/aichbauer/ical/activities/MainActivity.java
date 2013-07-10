/**
 * iCal Import/Export - import ical-Files on Android
 * 
 * Copyright (C) 2013 Gilles Baatz <baatzgilles@gmail.com>
 * Copyright (C) 2013 Dominik Schürmann <dominik@dominikschuermann.de>
 * Copyright (C) 2010-2011 Lukas Aichbauer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.aichbauer.ical.activities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import at.aichbauer.ical.Controller;
import at.aichbauer.ical.GoogleCalendar;
import at.aichbauer.ical.ICalConstants;
import at.aichbauer.ical.R;
import at.aichbauer.ical.inputAdapters.BasicInputAdapter;
import at.aichbauer.ical.inputAdapters.CredentialInputAdapter;
import at.aichbauer.ical.tools.dialogs.Credentials;
import at.aichbauer.ical.tools.dialogs.DialogTools;
import at.aichbauer.ical.tools.dialogs.SpinnerTools;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	/*
	 * Views
	 */
	private Spinner calendarSpinner;
	private Spinner fileSpinner;
	private Button calendarInformation;
	private Button searchButton;
	private Button loadButton;
	private Button insertButton;
	private Button deleteButton;
	private Button setUrlButton;
	private Button dumpCalendar;
	private TextView icalInformation;
	private Controller controller;
	private CheckBox chbDuplicates;

	/*
	 * Values
	 */
	private List<BasicInputAdapter> urls;
	private List<GoogleCalendar> calendars;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		this.controller = new Controller(this);

		// Show help menu
		preferences = getSharedPreferences("at.aichbauer.iCal",
				Context.MODE_PRIVATE);

		// Retrieve views
		calendarSpinner = (Spinner) findViewById(R.id.SpinnerChooseCalendar);
		fileSpinner = (Spinner) findViewById(R.id.SpinnerFile);
		searchButton = (Button) findViewById(R.id.SearchButton);
		loadButton = (Button) findViewById(R.id.LoadButton);
		insertButton = (Button) findViewById(R.id.InsertButton);
		deleteButton = (Button) findViewById(R.id.DeleteButton);
		calendarInformation = (Button) findViewById(R.id.ShowInformationButton);
		dumpCalendar = (Button) findViewById(R.id.SaveButton);
		icalInformation = (TextView) findViewById(R.id.IcalInfo);
		setUrlButton = (Button) findViewById(R.id.SetUrlButton);
		chbDuplicates = (CheckBox) findViewById(R.id.chbDuplicates);

		controller.init();
		loadOldPreferences();

		searchButton.setOnClickListener(controller);
		loadButton.setOnClickListener(controller);
		calendarInformation.setOnClickListener(controller);
		dumpCalendar.setOnClickListener(controller);
		deleteButton.setOnClickListener(controller);
		insertButton.setOnClickListener(controller);
		setUrlButton.setOnClickListener(controller);
		
		setListenersToSavePreferences();	

		// if file intent
		Intent intent = getIntent();
		if (intent != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
			try {
				setUrls(Arrays.asList(new BasicInputAdapter(new URL(intent
						.getDataString()))));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * when a setting is changed, it is immediately written to the preferences.
	 * This method contains all listeners where the saving happens
	 * 
	 */
	private void setListenersToSavePreferences() {
		calendarSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Editor editor = preferences.edit();
				editor.putString(ICalConstants.PREFERENCE_LAST_CALENDAR,
						calendarSpinner.getSelectedItem().toString());
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Editor editor = preferences.edit();
				editor.putString(ICalConstants.PREFERENCE_LAST_CALENDAR, "");
				editor.commit();
			}
		});
		
		fileSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				BasicInputAdapter inputAdapter = (BasicInputAdapter) fileSpinner.getSelectedItem();
				String url = inputAdapter.getURL().toString();
				String username = "";
				String password = "";
				
				if(inputAdapter instanceof CredentialInputAdapter){
					Credentials credentials = ((CredentialInputAdapter)inputAdapter).getCredentials();
					username = credentials.getUsername();
					password = credentials.getPassword();
				}
				
				Editor editor = preferences.edit();
                editor.putString(ICalConstants.PREFERENCE_LAST_URL, url);
                editor.putString(ICalConstants.PREFERENCE_LAST_USERNAME, username);
                editor.putString(ICalConstants.PREFERENCE_LAST_PASSWORD, password);
				editor.commit();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Editor editor = preferences.edit();
                editor.putString(ICalConstants.PREFERENCE_LAST_URL, "");
                editor.putString(ICalConstants.PREFERENCE_LAST_USERNAME, "");
                editor.putString(ICalConstants.PREFERENCE_LAST_PASSWORD, "");
				editor.commit();
				
			}
		});

		chbDuplicates.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Editor editor = preferences.edit();
				editor.putBoolean(ICalConstants.PREFERENCE_DUPCLICATES_CHECKED,
						chbDuplicates.isChecked());
				editor.commit();
			}
		});
		
	}

	private void loadOldPreferences() {
		// selected calendar
		String lastCalendar = getPreferenceStore().getString(
				ICalConstants.PREFERENCE_LAST_CALENDAR, "");

		for (int i = 0; i < calendarSpinner.getCount(); i++) {
			String spinnerItem = calendarSpinner.getItemAtPosition(i)
					.toString();
			if (lastCalendar.equals(spinnerItem)) {
				calendarSpinner.setSelection(i);
				break;
			}
		}

		// load last url
		try {
			String url_string = preferences.getString(ICalConstants.PREFERENCE_LAST_URL,
					"");
			URL url = new URL(url_string);
			String username = preferences.getString(
					ICalConstants.PREFERENCE_LAST_USERNAME, "");
			String password = preferences.getString(
					ICalConstants.PREFERENCE_LAST_PASSWORD, "");

			if (username != null && !username.equals("") && password != null) {
				setUrls(Arrays
						.asList((BasicInputAdapter) new CredentialInputAdapter(url,
								new Credentials(username, password))));
			} else {
				setUrls(Arrays.asList(new BasicInputAdapter(url)));
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// duplicate checkbox
		boolean checked = preferences.getBoolean(
				ICalConstants.PREFERENCE_DUPCLICATES_CHECKED, false);
		chbDuplicates.setChecked(checked);

	}

	/**
	 * Add a list of calendars to the user interface for selection.
	 * 
	 * @param calendars
	 */
	public void setCalendars(List<GoogleCalendar> calendars) {
		this.calendars = calendars;
		List<String> calendarStrings = new ArrayList<String>();
		for (GoogleCalendar cal : calendars) {
			calendarStrings
					.add(cal.getDisplayName() + " (" + cal.getId() + ")");
		}
		SpinnerTools.simpleSpinnerInUI(this, calendarSpinner, calendarStrings);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				calendarInformation
						.setEnabled(MainActivity.this.calendars == null ? false
								: true);
				dumpCalendar
						.setEnabled(MainActivity.this.calendars == null ? false
								: true);

			}
		});
	}

	/**
	 * Set a list of url's for selection.
	 * 
	 * @param urls
	 *            Url's to display in the list
	 */
	public void setUrls(List<BasicInputAdapter> urls) {
		this.urls = urls;
		SpinnerTools.simpleSpinnerInUI(this, fileSpinner, urls);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadButton.setEnabled(MainActivity.this.urls == null ? false
						: true);
			}
		});
	}

	public void setCalendar(final Calendar calendar) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				icalInformation.setVisibility(calendar == null ? View.GONE
						: View.VISIBLE);
				deleteButton.setEnabled(calendar == null ? false : true);
				insertButton.setEnabled(calendar == null ? false : true);
				chbDuplicates.setEnabled(calendar == null ? false : true);
				if (calendar != null) {
					icalInformation.setText(getString(
							R.string.textview_calendar_short_information,
							calendar.getComponents(VEvent.VEVENT).size()));
				}
			}
		});
	}

	public SharedPreferences getPreferenceStore() {
		return preferences;
	}

	public GoogleCalendar getSelectedCalendar() {
		if (calendarSpinner.getSelectedItem() != null && calendars != null) {
			String calendarName = calendarSpinner.getSelectedItem().toString();
			for (GoogleCalendar cal : calendars) {
				if ((cal.getDisplayName() + " (" + cal.getId() + ")")
						.equals(calendarName)) {
					return cal;
				}
			}
		}
		return null;
	}

	public BasicInputAdapter getSelectedURL() {
		return fileSpinner.getSelectedItem() != null ? (BasicInputAdapter) fileSpinner
				.getSelectedItem() : null;
	}

	public boolean checkForDuplicates() {
		return chbDuplicates.isChecked();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.help) {
			DialogTools
					.showInformationDialog(this, getString(R.string.menu_help),
							Html.fromHtml(ICalConstants.HELP),
							R.drawable.calendar_gray);
		} else if (item.getItemId() == R.id.changelog) {
			DialogTools.showInformationDialog(this, R.string.menu_changelog,
					R.string.changelog, R.drawable.calendar_gray);
		} else if (item.getItemId() == R.id.license) {
			DialogTools.showInformationDialog(this, R.string.menu_license,
					R.string.license, R.drawable.calendar_gray);
		}
		return super.onContextItemSelected(item);
	}
}
