package main.java.org.mozilla.android.sync.setup.main;

import main.java.org.mozilla.android.sync.setup.Constants;

import org.mozilla.android.sync.setup.main.R;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AccountActivity extends AccountAuthenticatorActivity {
  private final static String TAG = "AccountAuthenticatorActivity";
  private AccountManager mAccountManager;
  private Context mContext;
  private String username;
  private String password;
  private String key;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.account);
    mContext = getApplicationContext();
    mAccountManager = AccountManager.get(getApplicationContext());
  }
  
  @Override
  public void onStart() {
    // Start with an empty form
    super.onCreate(null);
    setContentView(R.layout.account);
  }
  
  public void cancelClickHandler(View target) {
    finish();
    overridePendingTransition(0, 0);
  }
  
  /*
   * Get credentials on "Connect" and write to AccountManager, where it can be 
   * accessed by Fennec and Sync Service.
   */
  public void connectClickHandler(View target) {
    Log.d("connecting", "ConnectClickHandler");
    username = ((EditText) findViewById(R.id.username)).getText().toString();
    password = ((EditText) findViewById(R.id.password)).getText().toString();
    key = ((EditText) findViewById(R.id.key)).getText().toString();

    // TODO : Authenticate with Sync Service, once implemented, with
    // onAuthSuccess as callback
    
    authCallback();
  }
  
  /*
   * Callback that handles auth based on success/failure
   */
  private void authCallback() {
    // Create and add account to AccountManager
    // TODO: only allow one account to be added?
    final Account account = new Account(username, Constants.ACCOUNTTYPE_SYNC);
    final Bundle userbundle = new Bundle();
    // Add sync key
    userbundle.putString(Constants.OPTION_KEY, key);
    mAccountManager.addAccountExplicitly(account, password, userbundle);
    Log.d(TAG, "account: " + account.toString());
    // Set components to sync (default: all)
    ContentResolver.setSyncAutomatically(account, Browser.BOOKMARKS_URI.getAuthority(), true);
    // TODO: add other ContentProviders as needed (e.g. passwords)
    // TODO: for each, also add to res/xml to make visible in account settings
    ContentResolver.setMasterSyncAutomatically(true);
    
    final Intent intent = new Intent();
    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNTTYPE_SYNC);
    intent.putExtra(AccountManager.KEY_AUTHTOKEN, Constants.ACCOUNTTYPE_SYNC);
    setAccountAuthenticatorResult(intent.getExtras());
    
    // Successful authentication result
    setResult(RESULT_OK, intent);
//    Intent acctIntent = new Intent("android.settings.SYNC_SETTINGS");
//    startActivity(acctIntent);
    //moveTaskToBack(true);
    
    authFailure();
  }
  
  private void authFailure() {
    Intent intent = new Intent(mContext, SetupFailureActivity.class);
    startActivity(intent);
  }
}
