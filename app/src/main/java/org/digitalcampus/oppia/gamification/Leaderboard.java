package org.digitalcampus.oppia.gamification;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.splunk.mint.Mint;

import org.digitalcampus.oppia.activity.PrefsActivity;
import org.digitalcampus.oppia.application.DbHelper;
import org.digitalcampus.oppia.application.MobileLearning;
import org.digitalcampus.oppia.exception.WrongServerException;
import org.digitalcampus.oppia.task.Payload;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Leaderboard {

    public final static String TAG = Leaderboard.class.getSimpleName();

    public static int importLeaderboardJSON(Context ctx, String json) throws JSONException, ParseException, WrongServerException {

        DbHelper db = DbHelper.getInstance(ctx);
        int updatedPositions = 0;

        JSONObject leaderboard = new JSONObject(json);
        String server = leaderboard.getString("server");
        if(!server.endsWith("/")) {
            server = server + "/";
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (!prefs.getString(PrefsActivity.PREF_SERVER, "").equals(server)){

            Log.d(TAG, "Leaderboard server doesn't match with current one: " +
                    prefs.getString(PrefsActivity.PREF_SERVER, "") + " - " + server);
            throw new WrongServerException(server);
        }

        String lastUpdateStr = leaderboard.getString("generated_date");
        Date sdt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(lastUpdateStr);
        JSONArray positions = leaderboard.getJSONArray("leaderboard");
        DateTime lastUpdate = new DateTime(sdt);

        for (int i=0; i<positions.length(); i++){
            JSONObject pos = positions.getJSONObject(i);
            boolean updated = db.insertOrUpdateUserLeaderboard(
                pos.getString("username"),
                pos.getString("first_name") + " " + pos.getString("last_name"),
                pos.getInt("points"),
                lastUpdate
            );
            if (updated){
                updatedPositions++;
            }
        }
        Log.d(TAG, "leaderboard added:" + updatedPositions);

        return updatedPositions;
    }

    public static boolean shouldFetchLeaderboard(SharedPreferences prefs){
        long now = System.currentTimeMillis()/1000;
        long lastScan = prefs.getLong(PrefsActivity.PREF_LAST_LEADERBOARD_FETCH, 0);
        return (lastScan + MobileLearning.LEADERBOARD_FETCH_EXPIRATION <= now);
    }


    public static void updateLeaderboardFetchTime(SharedPreferences prefs){
        Log.d(TAG, "Updating last media scan to now");
        long now = System.currentTimeMillis()/1000;
        prefs.edit().putLong(PrefsActivity.PREF_LAST_LEADERBOARD_FETCH, now).apply();
    }
}
