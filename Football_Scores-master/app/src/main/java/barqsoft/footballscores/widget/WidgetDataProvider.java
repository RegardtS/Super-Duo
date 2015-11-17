package barqsoft.footballscores.widget;

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

        String meh = intent.getStringExtra("tester");
        Log.wtf("regi","meh == " + meh);

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

        //String str = ""+(Utilies.getMatchDay(x.getInt(COL_MATCHDAY), x.getInt(COL_LEAGUE)) + " " + Utilies.getLeague(x.getInt(COL_LEAGUE)));

        mView.setTextViewText(android.R.id.text1, "" + mCollections.get(position));
        mView.setTextColor(android.R.id.text1, Color.BLACK);


        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(WidgetProvider.ACTION_TOAST);

        final Bundle bundle = new Bundle();
        bundle.putString(WidgetProvider.EXTRA_STRING, "" + mCollections.get(position));

        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);
        return mView;
    }

    @Override
    public int getViewTypeCount() {return 1;}

    @Override
    public boolean hasStableIds() {return true;}

    @Override
    public void onCreate() {initData();}

    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData() {

        Log.wtf("regi","initData");
        mCollections.clear();
        for (int i = 1; i <= 10; i++) {
            mCollections.add("ListView item " + i);
        }
    }

    @Override
    public void onDestroy() {}


}