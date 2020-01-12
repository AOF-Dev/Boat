package cosine.boat;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import cosine.boat.logcat.Logcat;
import cosine.boat.logcat.LogcatService;
import ru.ivanarh.jndcrash.NDCrashError;
import ru.ivanarh.jndcrash.NDCrash;
import ru.ivanarh.jndcrash.NDCrashService;
import ru.ivanarh.jndcrash.NDCrashUnwinder;
import android.content.Intent;

public class LauncherActivity extends Activity implements View.OnClickListener{
	
	
    public Button playButton;
	public EditText configText;
    
    
	
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
		
		final String logPath = "/mnt/sdcard/boat/log.txt";
		Logcat.initializeOutOfProcess( this, logPath, LogcatService.class);
		
		final String reportPath = "/mnt/sdcard/boat/crash.txt";
		System.out.println("Crash report: " + reportPath);
		final NDCrashError error = NDCrash.initializeOutOfProcess( this, reportPath, NDCrashUnwinder.libcorkscrew, NDCrashService.class);
		if (error == NDCrashError.ok) {
			System.out.println("NDCrash: OK");
			// Initialization is successful. 
		} else {
			System.out.println("NDCrash: Error");
			System.out.println(error.name());
			// Initialization failed, check error value. 
		} 
		
        setContentView(R.layout.launcher_layout);
        this.playButton = (Button) findViewById(R.id.launcher_play_button);
        this.playButton.setOnClickListener(this);
		this.configText = (EditText) findViewById(R.id.launcher_config_text);
		
		this.configText.setText( getPreferences(MODE_PRIVATE).getString("config", "/sdcard/boat/config.json"));
		getPreferences(MODE_PRIVATE).edit().putString("config", this.configText.getText().toString());
    }
	
	//OnClickListener
    public void onClick(View v) {
        if (v == this.playButton) {
			
			
            Intent i = new Intent(this, BoatClientActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("config", configText.getText().toString());
			i.putExtras(bundle);
			this.startActivity(i);
			
			
        } 
		
    }

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		getPreferences(MODE_PRIVATE).edit().putString("config", this.configText.getText().toString());
	}
    
}
