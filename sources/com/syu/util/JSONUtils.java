package com.syu.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    JSONUtils() {
    }

    public static int getInt(JSONObject jSONObject, String str) {
        if (!jSONObject.has(str)) {
            return -1;
        }
        try {
            return jSONObject.getInt(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getStr(JSONObject jSONObject, String str) {
        if (!jSONObject.has(str)) {
            return null;
        }
        try {
            return jSONObject.getString(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] jsonToIntArray(JSONObject jSONObject, String str) {
        int length;
        if (!jSONObject.has(str)) {
            return null;
        }
        try {
            JSONArray jSONArray = jSONObject.getJSONArray(str);
            if (jSONArray == null || (length = jSONArray.length()) == 0) {
                return null;
            }
            int[] iArr = new int[length];
            for (int i = 0; i < length; i++) {
                iArr[i] = jSONArray.getInt(i);
            }
            return iArr;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] jsonToStrArray(JSONObject jSONObject, String str) {
        int length;
        if (!jSONObject.has(str)) {
            return null;
        }
        try {
            JSONArray jSONArray = jSONObject.getJSONArray(str);
            if (jSONArray == null || (length = jSONArray.length()) == 0) {
                return null;
            }
            String[] strArr = new String[length];
            for (int i = 0; i < length; i++) {
                strArr[i] = jSONArray.getString(i);
            }
            return strArr;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
