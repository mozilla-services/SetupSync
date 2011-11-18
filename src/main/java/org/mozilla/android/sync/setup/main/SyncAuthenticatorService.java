package main.java.org.mozilla.android.sync.setup.main;

import main.java.org.mozilla.android.sync.setup.Constants;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class SyncAuthenticatorService extends Service {
  private static final String TAG = "SyncAuthenticatorService";
  private AccountAuthenticatorImpl sAccountAuthenticator = null;
  
  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate");
    sAccountAuthenticator = getAuthenticator();
  }

  @Override
  public IBinder onBind(Intent intent) {
    IBinder ret = null;
    if (intent.getAction().equals(
        android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
      ret = getAuthenticator().getIBinder();
    return ret;
  }

  private AccountAuthenticatorImpl getAuthenticator() {
    if (sAccountAuthenticator == null) {
      sAccountAuthenticator = new AccountAuthenticatorImpl(this);
    }
    return sAccountAuthenticator;
  }

  private static class AccountAuthenticatorImpl extends
      AbstractAccountAuthenticator {
    private Context mContext;
    public AccountAuthenticatorImpl(Context context) {
      super(context);
      mContext = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
        String accountType, String authTokenType, String[] requiredFeatures,
        Bundle options) throws NetworkErrorException {
      Log.d(TAG, "addAccount");
      final Intent intent = new Intent(mContext, AccountActivity.class);
      intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
          response);
      intent.putExtra("accountType", Constants.ACCOUNTTYPE_SYNC);

      final Bundle result = new Bundle();
      result.putParcelable(AccountManager.KEY_INTENT, intent);

      return result;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response,
        Account account, Bundle options) throws NetworkErrorException {
      Log.d(TAG, "confirmCredentials");
      return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response,
        String accountType) {
      Log.d(TAG, "editProperties");
      return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response,
        Account account, String authTokenType, Bundle options)
        throws NetworkErrorException {
      Log.d(TAG, "getAuthToken");
      if (!authTokenType.equals(Constants.AUTHTOKEN_TYPE_PLAIN)) {
        final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ERROR_MESSAGE,
            "invalid authTokenType");
        return result;
      }
      // Extract the username and password from the Account Manager, and ask
      // the server for an appropriate AuthToken.
      final AccountManager am = AccountManager.get(mContext);
      final String password = am.getPassword(account);
      if (password != null) {
        final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE,
            Constants.ACCOUNTTYPE_SYNC);
        result.putString(AccountManager.KEY_PASSWORD, password);
        final String synckey = am.getUserData(account, Constants.OPTION_KEY);
        result.putString(Constants.OPTION_KEY, synckey);
        result.putString(AccountManager.KEY_AUTHTOKEN, "AUTH-TOKEN");
        return result;
      }
      return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
      Log.d(TAG, "getAuthTokenLabel");
      return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response,
        Account account, String[] features) throws NetworkErrorException {
      Log.d(TAG, "hasFeatures");
      return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response,
        Account account, String authTokenType, Bundle options)
        throws NetworkErrorException {
      Log.d(TAG, "updateCredentials");
      return null;
    }
  }
}
