package com.example.wagontester;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wagontester.R;
import com.example.wagontester.db.DBContract;

public class LoginActivity extends Activity {
	
	// SharedPreferences
	private SharedPreferences mSpApp;
	
	// Views
	private Spinner mUserSpinner;
	private Spinner mDutySpinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mSpApp = getSharedPreferences("app", MODE_PRIVATE);
		 
		mUserSpinner = (Spinner)findViewById(R.id.userSpinner);
		mUserSpinner.setAdapter(new SpinnerAdapter(this,
				getContentResolver().query(DBContract.UserTable.CONTENT_URI, null, null, null, null), 
				DBContract.UserTable.POS_NAME));
		mDutySpinner = (Spinner)findViewById(R.id.dutySpinner);
		mDutySpinner.setAdapter(new SpinnerAdapter(this, 
				getContentResolver().query(DBContract.DutyTable.CONTENT_URI, null, null, null, null), 
				DBContract.DutyTable.POS_NAME));
	}
	
	private class SpinnerAdapter extends CursorAdapter {
		private int mPos; // Position for text
		public SpinnerAdapter(Context context, Cursor c, int pos) {
			super(context, c, false);
			mPos = pos;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((TextView)view).setText(cursor.getString(mPos));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			TextView view = new TextView(context);
			view.setHeight((int) context.getResources().getDimension(R.dimen.text_xlarge));
			view.setTextColor(Color.BLACK);
			view.setTextSize(20);
			return view;
		}
	}
	
	public void onLogin(View view) {
		int user = (int)mUserSpinner.getSelectedItemId();
		int duty = (int)mDutySpinner.getSelectedItemId();
		
		if(user == AdapterView.INVALID_POSITION) {
			showDialog("��ѡ���û�");
		} else if (duty == AdapterView.INVALID_POSITION) {
			showDialog("��ѡ���λ");
		} else {			
			SharedPreferences.Editor editor = mSpApp.edit();
			editor.putInt("User", user);
			editor.putInt("Duty", duty);
			editor.commit();
			
			Intent intent = new Intent(this, MainActivity.class);
	    	startActivity(intent);
			this.finish();
		}
	}
	
	private void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("ȷ��", null)
			   .show();
	}
}