package br.pucrio.inf.acanhota.autosddl.pub;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

public abstract class MainActivityTask extends Activity {
	public static final String APP_NAME = "AutoSDDL-Pub";

	private int DELAY_MILLIS = 1000;
	
	private Handler handler;
	private Runnable runnable;
	
	public void startMainActivityTask() {		
		if (handler == null) {
			handler = new Handler();
			runnable = new Runnable(){
				@Override
				public void run() {
					mainActivitTask();
					handler.postDelayed(this, DELAY_MILLIS);
				}	
			};
			handler.postDelayed(runnable, DELAY_MILLIS);
		} else {
			onMainActivityTaskAlreadStarted();
		}
	}

	public void stopMainActivityTask() {
		handler.removeCallbacks(runnable);
		runnable = null;
		handler = null;
	}

	public abstract void mainActivitTask();
	
	public void onMainActivityTaskAlreadStarted() {
		Log.i(APP_NAME, "Main Activity Task Already Started");
	}
}
