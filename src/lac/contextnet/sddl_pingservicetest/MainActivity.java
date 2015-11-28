package lac.contextnet.sddl_pingservicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.pucrio.inf.acanhota.autosddl.pub.MainActivityTask;

/**
 * MainActivity: This is our application's MainActivity. It consists in 
 * 				 a UUID randomly generated and shown in txt_uuid, a text 
 * 				 field for the IP:PORT in et_ip, a "Ping!" button 
 * 				 (btn_ping) to send a Ping object message, a "Start 
 * 				 Service!" button (btn_startservice) to start the 
 * 				 communication service and a "Stop Service!" button 
 * 				 (btn_stopservice) to stop it.
 * 
 * @author andremd
 * 
 */
public class MainActivity extends MainActivityTask {
	/* Static Elements */
	private TextView txt_uuid;		
	private TextView txt_conn;
	private Button btn_startservice;
	private Button btn_stopservice;

	BroadcastReceiver receiver; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startCommunicationService();

		/* GUI Elements */
		txt_uuid = (TextView) findViewById(R.id.txt_uuid);				
		txt_conn = (TextView) findViewById(R.id.txt_conn);
		btn_startservice = (Button) findViewById(R.id.btn_startservice);
		btn_stopservice = (Button) findViewById(R.id.btn_stopservice);
		btn_stopservice.setEnabled(false);
		txt_uuid.setText(GetUUID(getBaseContext()));
		txt_conn.setText("Connecting...");

		/* Start Service Button Listener*/
		btn_startservice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				startMainActivityTask();
			}
		});

		/* Stop Service Button Listener*/
		btn_stopservice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopMainActivityTask();
			}
		});
		
		/* Receive messages from services */
		receiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            if (intent.getAction() == MessageHandler.ACTION_UPDATE_CONNECTION_STATUS) {
	            	String msg = intent.getStringExtra(MessageHandler.CONNECTION_STATUS);	            	
	            	
	            	txt_conn.setText(msg);
	            }
	        }
	    };
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    LocalBroadcastManager.getInstance(this).registerReceiver((receiver), 
	        new IntentFilter(MessageHandler.ACTION_UPDATE_CONNECTION_STATUS)
	    );
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopMainActivityTask();
		stopCommunicationService();
	}
	
	@Override
	protected void onMainActivityStart() {
		btn_startservice.setEnabled(false);
		btn_stopservice.setEnabled(true);
	}
	
	@Override
	protected void onMainActivityStop() {
		btn_startservice.setEnabled(true);
		btn_stopservice.setEnabled(false);
	}	
}
