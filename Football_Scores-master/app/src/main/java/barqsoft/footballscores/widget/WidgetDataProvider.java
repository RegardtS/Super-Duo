package barqsoft.footballscores.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.scoresAdapter;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory{


    List mCollections = new ArrayList();

    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    private void updateScores(){

        SharedPreferences prefs = mContext.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);


        String tempDate = prefs.getString("temp", "");
        if (TextUtils.isEmpty(tempDate)){
            tempDate = Utilies.getFormattedDate();
        }

        int COL_HOME = 3;
        int COL_AWAY = 4;
        int COL_MATCHTIME = 2;


        String[] fragmentdate = new String[1];
        fragmentdate[0] = tempDate;


        Cursor cursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, fragmentdate, null);
        mCollections.clear();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
             do{
                mCollections.add(cursor.getString(COL_HOME) + " vs " + cursor.getString(COL_AWAY) + " @ " + cursor.getString(COL_MATCHTIME));
            }while (cursor.moveToNext());

            cursor.close();

        }else {
            mCollections.add(mContext.getString(R.string.no_matches_found_description));
        }
    }


    @Override
    public int getCount() {return mCollections.size();}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);

        mView.setTextViewText(android.R.id.text1, String.valueOf(mCollections.get(position)));
        mView.setTextColor(android.R.id.text1, Color.BLACK);

        return mView;
    }

    @Override
    public int getViewTypeCount() {return 1;}

    @Override
    public boolean hasStableIds() {return true;}

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        updateScores();
    }

    @Override
    public void onDestroy() {}


}