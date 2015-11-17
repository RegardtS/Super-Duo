package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.widget.Toast;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;


public class WidgetProvider extends AppWidgetProvider{

    public static final String ACTION_TOAST = "com.dharmangsoni.widgets.ACTION_TOAST";
    public static final String EXTRA_STRING = "com.dharmangsoni.widgets.EXTRA_STRING";



    private static final String ACTION_BACK = "ACTION_BACK";
    private static final String ACTION_NEXT = "ACTION_NEXT";


    Context context;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);

            // Adding collection list item handler
            final Intent onItemClick = new Intent(context, WidgetProvider.class);
            onItemClick.setAction(ACTION_TOAST);
            onItemClick.setData(Uri.parse(onItemClick.toUri(Intent.URI_INTENT_SCHEME)));


            final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onItemClick, PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.widgetCollectionList, onClickPendingIntent);





            updateScores();

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, WidgetProvider.class);

            remoteViews.setOnClickPendingIntent(R.id.txt_next, getPendingSelfIntent(context, ACTION_NEXT));
            remoteViews.setOnClickPendingIntent(R.id.txt_back, getPendingSelfIntent(context, ACTION_BACK));
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);






            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);



    }

    private void updateScores(){

        int COL_HOME = 3;
        int COL_AWAY = 4;
        int COL_HOME_GOALS = 6;
        int COL_AWAY_GOALS = 7;
        int COL_DATE = 1;
        int COL_LEAGUE = 5;
        int COL_MATCHDAY = 9;
        int COL_ID = 8;
        int COL_MATCHTIME = 2;
        double detail_match_id = 0;
        String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";


        String[] fragmentdate = new String[1];
        fragmentdate[0] = "2015-11-06";


        Cursor cursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, fragmentdate, null);
        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            while (cursor.moveToNext()) {
                Log.wtf("regi","COL_HOME "+cursor.getString(COL_HOME));
                Log.wtf("regi", "COL_AWAY " + cursor.getString(COL_AWAY));
                Log.wtf("regi", "COL_MATCHTIME " + cursor.getString(COL_MATCHTIME));
                Log.wtf("regi", "getScores " + Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
                Log.wtf("regi", "getDouble " + cursor.getDouble(COL_ID));
                Log.wtf("regi","nah ");
            }

            cursor.close();

        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context, AppWidgetManager widgetManager, int widgetId) {
        this.context = context;

        RemoteViews mView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putExtra("tester", "testedsasadadsadsadsr");

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
//        mView.setRemoteAdapter(widgetId, R.id.widgetCollectionList, intent);
        mView.setRemoteAdapter(R.id.widgetCollectionList,intent);

        return mView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.wtf("regi","onReceive");
        this.context = context;
        if (intent.getAction().equals(ACTION_TOAST)) {
            String item = intent.getExtras().getString(EXTRA_STRING);
            Toast.makeText(context, item, Toast.LENGTH_LONG).show();
            Log.wtf("regi", "ACTION_TOAST");
        }
        if (ACTION_BACK.equals(intent.getAction()) || intent.getAction().equals(ACTION_NEXT)) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, WidgetProvider.class);

            remoteViews.setTextViewText(R.id.txt_title, ""+intent.getAction());

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }
        super.onReceive(context, intent);
    }

}