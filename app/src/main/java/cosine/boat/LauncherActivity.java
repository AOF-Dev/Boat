package cosine.boat;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import cosine.boat.logcat.Logcat;
import cosine.boat.logcat.LogcatService;
import android.content.Intent;
import android.widget.TextView;

public class LauncherActivity extends Activity implements View.OnClickListener {
    public Button playButton;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        final String logPath = "/mnt/sdcard/boat/log.txt";
        Logcat.initializeOutOfProcess( this, logPath, LogcatService.class);

        setContentView(R.layout.launcher_layout);
        this.playButton = (Button) findViewById(R.id.launcher_play_button);
        this.playButton.setOnClickListener(this);
    }

    // OnClickListener
    public void onClick(View v) {
        if (v == this.playButton) {
            Intent i = new Intent(this, BoatActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            this.startActivity(i);
            this.finish();
        }
    }
}

