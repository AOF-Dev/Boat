package cosine.boat;

import android.app.Service;
import android.content.Intent;
import android.app.Notification;
import android.os.IBinder;
import android.app.PendingIntent;
import java.util.Map;
import java.util.HashMap;

public class BoatService extends Service
{
    public static final String STOP_BOAT_SERVICE = "stop_boat_service";

    private BoatTask task = null;

    public void showForegroundNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_boat);
        builder.setContentTitle("BoatService");
        builder.setContentText("Running");
        builder.setOngoing(true);
        Intent exitIntent = new Intent(this, this.getClass()).setAction(STOP_BOAT_SERVICE);
        builder.setContentIntent(PendingIntent.getService(this, 0, exitIntent, 0));
        Notification notification = builder.build();
        startForeground(8888, notification);
    }

    public void dismissForegroundNotification() {
        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissForegroundNotification();
        System.exit(0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(STOP_BOAT_SERVICE)) {
                this.stopSelf();
                return Service.START_NOT_STICKY;
            }
        }
        if (this.task == null) {
            Map<String, String> envvars = new HashMap<>();
            envvars.put(BoatScript.BOAT_ENV_WINDOW_WIDTH, "0");
            envvars.put(BoatScript.BOAT_ENV_WINDOW_HEIGHT, "0");
            envvars.put(BoatScript.BOAT_ENV_TMPDIR, this.getCacheDir().getAbsolutePath());
            this.task = new BoatTask(envvars, intent.getStringExtra(BoatTask.BOAT_TASK_SCRIPT_PATH)) {
                @Override
                public void afterExecute() {
                    BoatService.this.stopSelf();
                }
            };
            this.task.startTask();
            showForegroundNotification();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service doesn't support to be bound.
        return null;
    }
}

