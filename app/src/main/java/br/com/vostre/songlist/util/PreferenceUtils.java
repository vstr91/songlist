package br.com.vostre.songlist.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtils {

    public static void salvarPreferencia(Context ctx, String chave, String valor){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(chave, valor);
        editor.apply();
    }

    public static void salvarPreferencia(Context ctx, String chave, int valor){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(chave, valor);
        editor.apply();
    }

    public static void salvarPreferencia(Context ctx, String chave, boolean valor){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(chave, valor);
        editor.apply();
    }

    public static void gravaMostraToast(Context ctx, boolean valor){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("mostra_toast", valor);
        editor.apply();
    }

    public static boolean carregarMostraToast(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        return sharedPreferences.getBoolean("mostra_toast", false);
    }

    public static String carregarPreferencia(Context ctx, String chave){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        return sharedPreferences.getString(chave, "");
    }

    public static int carregarPreferenciaInt(Context ctx, String chave){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        return sharedPreferences.getInt(chave, -1);
    }

    public static boolean carregarPreferenciaBoolean(Context ctx, String chave){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        return sharedPreferences.getBoolean(chave, false);
    }

    public static void salvarUsuarioLogado(Context ctx, String id){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", id);
        editor.apply();

//        SessionUtils.logUser(id);
    }

    public static String carregarUsuarioLogado(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);
        return sharedPreferences.getString("usuario", "");
    }

}
