package org.mozilla.android.sync.setup.main;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.mozilla.android.sync.setup.Constants;

public class AccountActivity extends AccountAuthenticatorActivity {
  private AccountManager mAccountManager;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.account);
    mAccountManager = AccountManager.get(getApplicationContext());
  }
  
  public void cancelClickHandler(View target) {
    finish();
    overridePendingTransition(0, 0);
  }
  
  /*
   * Write credentials to AccountManager, where it can be accessed by Fennec
   * and Sync service.
   */
  public void connectClickHandler(View target) {
    Log.d("connecting", "ConnectClickHandler");
    String username = findViewById(R.id.username).toString();
    String password = findViewById(R.id.password).toString();
    String key = findViewById(R.id.key).toString();
    
    final Account account = new Account(username, Constants.TYPE_SYNC);
    //final Bundle userbundle = new Bundle();
    //userbundle.putString(Constants.OPTION_KEY, key);
    mAccountManager.addAccountExplicitly(account, password, null);
    
    final Intent intent = new Intent();
    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.TYPE_SYNC);
    intent.putExtra(AccountManager.KEY_AUTHTOKEN, Constants.TYPE_SYNC);
    this.setAccountAuthenticatorResult(intent.getExtras());
    // fake successful authentication
    this.setResult(RESULT_OK, intent);
    this.finish();
  }
}
