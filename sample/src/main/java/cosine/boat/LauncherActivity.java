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
    public Button minecraftButton;
    public Button virglButton;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        final String logPath = "/mnt/sdcard/boat/log.txt";
        Logcat.initializeOutOfProcess(this, logPath, LogcatService.class);

        setContentView(R.layout.launcher_layout);
        this.minecraftButton = (Button) findViewById(R.id.launcher_minecraft_button);
        this.minecraftButton.setOnClickListener(this);
        this.virglButton = (Button) findViewById(R.id.launcher_virgl_button);
        this.virglButton.setOnClickListener(this);
    }

    // OnClickListener
    public void onClick(View v) {
        if (v == this.minecraftButton) {
            Intent i = new Intent(this, MinecraftActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            i.putExtra(BoatTask.BOAT_TASK_SCRIPT_PATH, "/sdcard/boat/minecraft.json");
            this.startActivity(i);
        }
        else if (v == this.virglButton) {
            Intent i = new Intent(this, VirglService.class);
            i.putExtra(BoatTask.BOAT_TASK_SCRIPT_PATH, "/sdcard/boat/virgl_test_server.json");
            this.startService(i);
        }
    }
}

