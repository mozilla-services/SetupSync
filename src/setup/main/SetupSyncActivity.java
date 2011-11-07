package setup.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetupSyncActivity extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.pair);
    }
  public void noDeviceClickHandler(View target) {
    Intent accountIntent = new Intent(this, AccountActivity.class);
    startActivity(accountIntent);
  }
}