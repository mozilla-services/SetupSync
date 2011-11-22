package main.java.org.mozilla.android.sync.setup.main;

import org.mozilla.android.sync.setup.main.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetupFailureActivity extends Activity {
  private Context mContext;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.failure);
    mContext = this.getApplicationContext();
  }
  
  public void manualClickHandler(View target) {
    Intent intent = new Intent(mContext, AccountActivity.class);
    startActivity(intent);
    overridePendingTransition(0, 0);
    finish();
  }
  
  public void tryAgainClickHandler(View target) {
    finish();
  }
  
  public void cancelClickHandler(View target) {
    moveTaskToBack(true);
  }

}
