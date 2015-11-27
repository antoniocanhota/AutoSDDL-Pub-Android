package lac.contextnet.sddl_pingservicetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.pucrio.acanhota.autosddl.commons.VehicleStatus;
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
	private Button btn_ping;
	private Button btn_startservice;
	private Button btn_stopservice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* GUI Elements */
		txt_uuid = (TextView) findViewById(R.id.txt_uuid);		
		btn_ping = (Button) findViewById(R.id.btn_ping);
		btn_startservice = (Button) findViewById(R.id.btn_startservice);
		btn_stopservice = (Button) findViewById(R.id.btn_stopservice);
		txt_uuid.setText(GetUUID(getBaseContext()));
		
		/* Ping Button Listener*/
		btn_ping.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startMainActivityTask();
			}
		});

		/* Start Service Button Listener*/
		btn_startservice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				startCommunicationService();
			}
		});

		/* Stop Service Button Listener*/
		btn_stopservice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopMainActivityTask();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopMainActivityTask();
	}
	
	@Override
	public void onCommunicationServiceAlreadyStarted() {
		super.onCommunicationServiceAlreadyStarted();
		Toast.makeText(getBaseContext(), getResources().getText(R.string.msg_d_already_connected), Toast.LENGTH_SHORT).show();
	}	
}
