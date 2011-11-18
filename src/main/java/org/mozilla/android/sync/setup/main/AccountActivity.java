package main.java.org.mozilla.android.sync.setup.main;

import main.java.org.mozilla.android.sync.setup.Constants;

import org.mozilla.android.sync.setup.main.R;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
    String username = ((EditText) findViewById(R.id.username)).getText().toString();
    String password = ((EditText) findViewById(R.id.password)).getText().toString();
    String key = ((EditText) findViewById(R.id.key)).getText().toString();

    // TODO : Authenticate with Sync Service

    // Add account to AccountManager
    final Account account = new Account(username, Constants.ACCOUNTTYPE_SYNC);
    final Bundle userbundle = new Bundle();
    userbundle.putString(Constants.OPTION_KEY, key);
    mAccountManager.addAccountExplicitly(account, password, userbundle);

    final Intent intent = new Intent();
    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNTTYPE_SYNC);
    intent.putExtra(AccountManager.KEY_AUTHTOKEN, Constants.ACCOUNTTYPE_SYNC);
    setAccountAuthenticatorResult(intent.getExtras());
    // fake successful authentication
    setResult(RESULT_OK, intent);
    moveTaskToBack(true);
  }
}
