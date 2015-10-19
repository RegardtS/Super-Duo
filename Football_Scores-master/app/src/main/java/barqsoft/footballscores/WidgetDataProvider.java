package barqsoft.footballscores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory, LoaderManager.LoaderCallbacks<Cursor>{

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    List mCollections = new ArrayList();

    Context mContext = null;
    private String[] fdate = new String[1];

    public WidgetDataProvider(Context context, Intent intent) {

        mContext = context;




        Date fragmentdate = new Date(System.currentTimeMillis() + ((0 - 2) * 86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

        fdate[0]= mformat.format(fragmentdate);

        onCreateLoader(0,null);
        this.onCreateLoader(0,null);

    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

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

        //String str = ""+(Utilies.getMatchDay(x.getInt(COL_MATCHDAY), x.getInt(COL_LEAGUE)) + " " + Utilies.getLeague(x.getInt(COL_LEAGUE)));

        mView.setTextViewText(android.R.id.text1, ""+mCollections.get(position));
        mView.setTextColor(android.R.id.text1, Color.BLACK);

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(MyWidgetProvider.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(MyWidgetProvider.EXTRA_STRING, ""+mCollections.get(position));
        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);
        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData() {
        mCollections.clear();
        for (int i = 1; i <= 10; i++) {
            mCollections.add("ListView item " + i);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.wtf("regi","onCreateLoader");
        return new CursorLoader(mContext, DatabaseContract.scores_table.buildScoreWithDate(), null, null, fdate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.wtf("regi", ">" + cursor.getString(COL_HOME));
        Log.wtf("regi",">"+ cursor.getString(COL_AWAY));
        Log.wtf("regi",">"+cursor.getString(COL_MATCHTIME));
        Log.wtf("regi",">"+ (Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS))));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.wtf("regi","onResetLoader");
    }
}