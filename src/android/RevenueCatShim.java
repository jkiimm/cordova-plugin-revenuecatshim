import java.util.*;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.billingclient.api.SkuDetails;
import com.revenuecat.purchases.Entitlement;
import com.revenuecat.purchases.Offering;
import com.revenuecat.purchases.PurchaserInfo;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.interfaces.PurchaseCompletedListener;
import com.revenuecat.purchases.interfaces.ReceiveEntitlementsListener;
import com.revenuecat.purchases.interfaces.ReceivePurchaserInfoListener;
import com.revenuecat.purchases.PurchasesError;

/**
 * This class echoes a string called from JavaScript.
 */
public class RevenueCatShim extends CordovaPlugin {
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("configure")) {
      String apiKey = args.getString(0);
      String appUserId = args.getString(1);
      this.configure(apiKey, appUserId, callbackContext);
      return true;
    }

    if (action.equals("getEntitlements")) {
      this.getEntitlements(callbackContext);
      return true;
    }

    if (action.equals("makePurchase")) {
      String entitlementId = args.getString(0);
      this.makePurchase(entitlementId, callbackContext);
      return true;
    }

    if (action.equals("getPurchaserInfo")) {
      String entitlementId = args.getString(0);
      this.getPurchaserInfo(entitlementId, callbackContext);
      return true;
    }

    if (action.equals("identify")) {
      String appUserId = args.getString(0);
      this.identify(appUserId, callbackContext);
      return true;
    }

    if (action.equals("reset")) {
      String appUserId = args.getString(0);
      this.reset(appUserId, callbackContext);
      return true;
    }

    return false;
  }

  private void configure(String apiKey, String appUserId, CallbackContext callbackContext) {
    Purchases.setDebugLogsEnabled(true);
    Purchases.configure(cordova.getActivity().getApplicationContext(), apiKey, appUserId);
    callbackContext.success();
  }

  private void getEntitlements(CallbackContext callbackContext) {
    Purchases.getSharedInstance().getEntitlements(new ReceiveEntitlementsListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable Map<String, Entitlement> entitlements) {
				ObjectMapper mapper = new ObjectMapper();
				callbackContext.success(mapper.writeValueAsString(entitlements));
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
				callbackContext.error("failed to get entitlements");
			}
    });
  }

  private void makePurchase(String entitlementId, CallbackContext callbackContext) {
    /*
    Purchases.getSharedInstance().makePurchase(this, product.getSku(), BillingClient.SkuType.SUBS, new PurchaseCompletedListener() {
      public void onCompleted(@android.support.annotation.Nullable String sku, @android.support.annotation.Nullable PurchaserInfo purchaserInfo, @android.support.annotation.Nullable PurchasesError error) {
        if (error != null) {
          callbackContext.error("failed to make a purchase");
        } else if (purchaserInfo != null && purchaserInfo.getActiveEntitlements().contains(entitlementId)) {
          ArrayList dataArray = new ArrayList();
          dataArray.add(sku);
          dataArray.add(purchaserInfo);
          callbackContext.success(dataArray);
        }
      }
    });
    */
    callbackContext.success();
  }

  private void getPurchaserInfo(String entitlementId, CallbackContext callbackContext) {
    Purchases.getSharedInstance().getPurchaserInfo(new ReceivePurchaserInfoListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable PurchaserInfo purchaserInfo) {
        if (purchaserInfo != null) {
          if (purchaserInfo.getActiveEntitlements().contains(entitlementId)) {
						ObjectMapper mapper = new ObjectMapper();
            callbackContext.success(mapper.writeValueAsString(purchaserInfo));
          } else {
            callbackContext.error("entitlement not exist");
          }
        } else {
          callbackContext.error("purchaser info not exist");
        }
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
				callbackContext.error("failed to get purchaser info");
			}
    });
  }

  private void identify(String appUserId, CallbackContext callbackContext) {
    Purchases.getSharedInstance().identify(appUserId, new ReceivePurchaserInfoListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable PurchaserInfo purchaserInfo) {
				ObjectMapper mapper = new ObjectMapper();
				callbackContext.success(mapper.writeValueAsString(purchaserInfo));
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
				callbackContext.error("failed to identify");
			}
    });
  }

  private void reset(String appUserId, CallbackContext callbackContext) {
    Purchases.getSharedInstance().reset(new ReceivePurchaserInfoListener() {
			@Override
      public void onReceived(@android.support.annotation.Nullable PurchaserInfo purchaserInfo) {
				ObjectMapper mapper = new ObjectMapper();
				callbackContext.success(mapper.writeValueAsString(purchaserInfo));
      }
			@Override
			public void onError(@android.support.annotation.Nullable PurchasesError error) {
				callbackContext.error("failed to reset");
			}
    });
  }
}
