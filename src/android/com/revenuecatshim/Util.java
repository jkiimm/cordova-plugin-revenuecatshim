package com.revenuecatshim;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.android.billingclient.api.SkuDetails;
import com.revenuecat.purchases.*;

public class Util {
  static JSONObject serialize(Map<String, Entitlement> ents) throws JSONException {
    JSONObject obj = new JSONObject(); 

    for (Map.Entry<String, Entitlement> ent : ents.entrySet()) {
      JSONObject entObj = new JSONObject();
      JSONObject offsObj = new JSONObject();
      Map<String, Offering> offerings = ent.getValue().getOfferings();

      for (Map.Entry<String, Offering> off : offerings.entrySet()) {
        offsObj.put(off.getKey(), Util.offToJSON(off.getValue()));
      }

      entObj.put("offerings", offsObj);
      obj.put(ent.getKey(), entObj);
    }
    return obj;
  }

  static JSONObject serialize(PurchaserInfo pur) throws JSONException {
    JSONObject obj = new JSONObject(); 
    obj.put("activeEntitlements", Util.setToJSON(pur.getActiveEntitlements()));
    obj.put("activeSubscriptions", Util.setToJSON(pur.getActiveSubscriptions()));
    obj.put("allPurchasedSkus", Util.setToJSON(pur.getAllPurchasedSkus()));

    Date latest = pur.getLatestExpirationDate();
    if (latest != null) {
      obj.put("latestExpirationDate", Util.dateToISO(latest));
    } else {
      obj.put("latestExpirationDate", JSONObject.NULL);
    }

    return obj;
  }

  private static JSONArray setToJSON(Set<String> set) throws JSONException {
    JSONArray arr = new JSONArray();
    for (String s: set) {
      arr.put(s);
    }
    return arr;
  }

  private static JSONObject offToJSON(Offering off) throws JSONException {
    JSONObject obj = new JSONObject(); 
    obj.put("product", Util.skuToJSON(off.getSkuDetails()));
    return obj;
  }

  private static JSONObject skuToJSON(SkuDetails sku) throws JSONException {
    JSONObject obj = new JSONObject(); 
    obj.put("productId", sku.getSku());
    obj.put("type", sku.getType());
    obj.put("price", sku.getPrice());
    obj.put("title", sku.getTitle());
    obj.put("description", sku.getDescription());
    return obj;
  }

  private static String dateToISO(Date date) {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    df.setTimeZone(tz);
    return df.format(date);
  }
}
