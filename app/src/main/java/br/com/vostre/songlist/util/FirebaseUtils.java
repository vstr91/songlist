package br.com.vostre.songlist.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseUtils {

    public static FirebaseAnalytics iniciaAnalytics(Context ctx){
        return FirebaseAnalytics.getInstance(ctx);
    }

    public static void gravaEvento(FirebaseAnalytics mFirebaseAnalytics, Bundle bundle, String evento){
        mFirebaseAnalytics.logEvent(evento, bundle);
    }

}
