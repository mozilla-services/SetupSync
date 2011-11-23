package main.java.org.mozilla.android.sync.setup.main;

import org.mozilla.android.sync.setup.main.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetupSuccessActivity extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.success);
  }

  /* Click Handlers */
  public void settingsClickHandler(View target) {
    Intent intent = new Intent("android.settings.SYNC_SETTINGS");
    startActivity(intent);
    overridePendingTransition(0, 0);
    finish();
  }
}
