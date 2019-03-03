package com.revenuecatshim;

import java.util.*;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.BillingClient;
import com.revenuecat.purchases.Entitlement;
import com.revenuecat.purchases.Offering;
import com.revenuecat.purchases.PurchaserInfo;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.PurchasesError;
import com.revenuecat.purchases.interfaces.PurchaseCompletedListener;
import com.revenuecat.purchases.interfaces.ReceiveEntitlementsListener;
import com.revenuecat.purchases.interfaces.ReceivePurchaserInfoListener;

import com.revenuecatshim.Util;

import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class RevenueCatShim extends CordovaPlugin {
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("configure")) {
      String apiKey = args.getString(0);
      String appUserId = args.getString(1);
      if (appUserId.isEmpty()) {
        appUserId = null;
      }
      this.configure(apiKey, appUserId, callbackContext);
      return true;
    }

    if (action.equals("getEntitlements")) {
      this.getEntitlements(callbackContext);
      return true;
    }

    if (action.equals("makePurchase")) {
      String productId = args.getString(0);
      this.makePurchase(productId, callbackContext);
      return true;
    }

    if (action.equals("getPurchaserInfo")) {
      this.getPurchaserInfo(callbackContext);
      return true;
    }

    if (action.equals("identify")) {
      String appUserId = args.getString(0);
      this.identify(appUserId, callbackContext);
      return true;
    }

    if (action.equals("reset")) {
      this.reset(callbackContext);
      return true;
    }

    if (action.equals("getAppUserId")) {
      this.getAppUserId(callbackContext);
      return true;
    }

    return false;
  }

  private void configure(String apiKey, String appUserId, CallbackContext callbackContext) {
    Purchases.setDebugLogsEnabled(true);
    Purchases.configure(cordova.getActivity().getApplicationContext(), apiKey, appUserId);
    callbackContext.success();
  }

  private void getEntitlements(final CallbackContext callbackContext) {
    Purchases.getSharedInstance().getEntitlements(new ReceiveEntitlementsListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable Map<String, Entitlement> entitlements) {
        try {
          callbackContext.success(Util.serialize(entitlements));
        } catch (JSONException e) {
          Log.e("RCS", "error message", e);
          callbackContext.error("could not create json");
        }
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
        Log.e("RCS", error.getMessage());
				callbackContext.error("failed to get entitlements");
			}
    });
  }

  private void makePurchase(final String productId, final CallbackContext callbackContext) {
    Purchases.getSharedInstance().makePurchase(cordova.getActivity(), productId, BillingClient.SkuType.SUBS, new PurchaseCompletedListener() {
			@Override
      public void onCompleted(@android.support.annotation.Nullable String sku, @android.support.annotation.Nullable PurchaserInfo purchaserInfo) {
        if (purchaserInfo != null) {
          try {
            JSONArray arr = new JSONArray();
            arr.put(sku);
            arr.put(Util.serialize(purchaserInfo));
            callbackContext.success(arr);
          } catch (JSONException e) {
            Log.e("RCS", "error message", e);
            callbackContext.error("could not create json");
          }
        } else {
          callbackContext.error("purchaser info not exist");
        }
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
        Log.e("RCS", error.getMessage());
				callbackContext.error("failed to get make a purchase");
			}
    });
  }

  private void getPurchaserInfo(final CallbackContext callbackContext) {
    Purchases.getSharedInstance().getPurchaserInfo(new ReceivePurchaserInfoListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable PurchaserInfo purchaserInfo) {
        if (purchaserInfo != null) {
          try {
            callbackContext.success(Util.serialize(purchaserInfo));
          } catch (JSONException e) {
            Log.e("RCS", "error message", e);
            callbackContext.error("could not create json");
          }
        } else {
          callbackContext.error("purchaser info not exist");
        }
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
        Log.e("RCS", error.getMessage());
				callbackContext.error("failed to get purchaser info");
			}
    });
  }

  private void identify(String appUserId, final CallbackContext callbackContext) {
    Purchases.getSharedInstance().identify(appUserId, new ReceivePurchaserInfoListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable PurchaserInfo purchaserInfo) {
        try {
          callbackContext.success(Util.serialize(purchaserInfo));
        } catch (JSONException e) {
          Log.e("RCS", "error message", e);
          callbackContext.error("could not create json");
        }
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
        Log.e("RCS", error.getMessage());
				callbackContext.error("failed to identify");
			}
    });
  }

  private void reset(final CallbackContext callbackContext) {
    Purchases.getSharedInstance().reset(new ReceivePurchaserInfoListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable PurchaserInfo purchaserInfo) {
        try {
          callbackContext.success(Util.serialize(purchaserInfo));
        } catch (JSONException e) {
          Log.e("RCS", "error message", e);
          callbackContext.error("could not create json");
        }
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
        Log.e("RCS", error.getMessage());
				callbackContext.error("failed to reset");
			}
    });
  }

  private void getAppUserId(CallbackContext callbackContext) {
    callbackContext.success(Purchases.getSharedInstance().appUserID);
  }
}
