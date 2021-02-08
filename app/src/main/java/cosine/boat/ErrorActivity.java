package cosine.boat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import com.mio.boat.R;
public class ErrorActivity extends Activity{
		TextView errorDetailsText;//显示崩溃提示的文本
		CaocConfig config;//配置对象

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.error);
			errorDetailsText = (TextView) findViewById(R.id.error);
			initData();
			
		}

		private void initData() {
			//可以获取到的四个信息:
			String stackString = CustomActivityOnCrash.getStackTraceFromIntent(getIntent());//将堆栈跟踪作为字符串获取。
			String logString = CustomActivityOnCrash.getActivityLogFromIntent(getIntent()); //获取错误报告的Log信息
			String allString = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent());// 获取所有的信息
			config = CustomActivityOnCrash.getConfigFromIntent(getIntent());//获得配置信息,比如设置的程序崩溃显示的页面和重新启动显示的页面等等信息
			errorDetailsText.setText("程序崩溃了！\n"+stackString+"\n"+logString+"\n"+allString);
		}

		public void oc(View v) {
			if (config != null)
		       CustomActivityOnCrash.closeApplication(ErrorActivity.this, config);
		}
}
