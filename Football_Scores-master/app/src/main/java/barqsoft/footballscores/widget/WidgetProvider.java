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






            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, WidgetProvider.class);

            remoteViews.setOnClickPendingIntent(R.id.txt_next, getPendingSelfIntent(context, ACTION_NEXT));
            remoteViews.setOnClickPendingIntent(R.id.txt_back, getPendingSelfIntent(context, ACTION_BACK));

            remoteViews.setPendingIntentTemplate(R.id.widgetCollectionList, onClickPendingIntent);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);




            Log.wtf("regi","in the loop");

            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);



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
        this.context = context;
        if (intent.getAction().equals(ACTION_TOAST)) {
            String item = intent.getExtras().getString(EXTRA_STRING);
            Toast.makeText(context, item, Toast.LENGTH_LONG).show();
//            Log.wtf("regi", "ACTION_TOAST");
        }
        if (ACTION_BACK.equals(intent.getAction()) || intent.getAction().equals(ACTION_NEXT)) {


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, WidgetProvider.class);

            remoteViews.setTextViewText(R.id.txt_title, ""+intent.getAction());


//            remoteViews.


            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(watchWidget),R.id.widgetCollectionList);




        }
        super.onReceive(context, intent);
    }

}