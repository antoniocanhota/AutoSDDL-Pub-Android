package br.pucrio.inf.acanhota.autosddl.pub;

import java.util.UUID;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import br.pucrio.acanhota.autosddl.commons.VehicleStatus;
import lac.contextnet.sddl_pingservicetest.CommunicationService;
import lac.contextnet.sddl_pingservicetest.IPPort;
import lac.contextnet.sddl_pingservicetest.R;

public abstract class MainActivityTask extends Activity {
	public static final String APP_NAME = "AutoSDDL-Pub";
	public static final String IP_PORT = "10.1.1.31:5500";
	
	/* Shared Preferences */
	private static String uniqueID = null;
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

	private int DELAY_MILLIS = 1000;
	
	private Handler handler;
	private Runnable runnable;
	
	public void startMainActivityTask() {		
		startCommunicationService();		
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
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}		
		runnable = null;
		handler = null;
		
		stopCommunicationService();
	}

	public void mainActivitTask() {
		if(!isMyServiceRunning(CommunicationService.class)) {
			Toast.makeText(getBaseContext(), getResources().getText(R.string.msg_e_servicenotrunning), Toast.LENGTH_SHORT).show();
		} else {
			VehicleStatus ping = new VehicleStatus();
			
			/* Calling the SendPingMsg action to the PingBroadcastReceiver */
			Intent i = new Intent(MainActivityTask.this, CommunicationService.class);
			i.setAction("lac.contextnet.sddl_pingservicetest.broadcastmessage." + "ActionSendVehicleStatus");
			i.putExtra("lac.contextnet.sddl_pingservicetest.broadcastmessage." + "ExtraPingMsg", ping);
			LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(i);
		}
	}
	
	public void onMainActivityTaskAlreadStarted() {
		Log.i(APP_NAME, "Main Activity Task Already Started");
	}
	
	public void startCommunicationService() {
		if(!isMyServiceRunning(CommunicationService.class)) {
			IPPort ipPortObj = new IPPort(IP_PORT);
			
			/* Starting the communication service */
			Intent intent = new Intent(MainActivityTask.this, CommunicationService.class);
			intent.putExtra("ip", ipPortObj.getIP());
			intent.putExtra("port", Integer.valueOf(ipPortObj.getPort()));
			intent.putExtra("uuid", GetUUID(getBaseContext()));
			startService(intent);
		} else {
			onCommunicationServiceAlreadyStarted();
		}
	}

	public void stopCommunicationService() {
		/* Stops the service and finalizes the connection */
		stopService(new Intent(getBaseContext(), CommunicationService.class));
	}
	
	
	public void onCommunicationServiceAlreadyStarted() {
		Log.i(APP_NAME, "Communication Service Already Started");
	}
	
    //See http://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-in-android
    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    
    //See http://androidsnippets.com/generate-random-uuid-and-store-it
    public synchronized static String GetUUID(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

}
