package br.com.vostre.songlist.util;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.vostre.songlist.R;

/**
 * Created by Almir on 16/12/2015.
 */
public class ToolbarUtils {

    public static void preparaMenu(Menu menu, Activity activity, View.OnClickListener listener){

        activity.getMenuInflater().inflate(R.menu.main, menu);

    }

    public static void onMenuItemClick(View v, Activity activity){
        switch(v.getId()){
            case android.R.id.home:
                //activity.onBackPressed();
                break;
        }
    }

}
