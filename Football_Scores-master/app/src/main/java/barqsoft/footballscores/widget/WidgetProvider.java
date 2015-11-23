package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.widget.Toast;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;


public class WidgetProvider extends AppWidgetProvider{


    private static final String ACTION_BACK = "ACTION_BACK";
    private static final String ACTION_NEXT = "ACTION_NEXT";


    Context context;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);

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

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(R.id.widgetCollectionList,intent);

        return mView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (ACTION_BACK.equals(intent.getAction()) || intent.getAction().equals(ACTION_NEXT)) {


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, WidgetProvider.class);


            SharedPreferences prefs = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
            String tempDate = prefs.getString("temp", "");
            if (TextUtils.isEmpty(tempDate)){
                tempDate = Utilies.getFormattedDate();
            }

            tempDate = Utilies.modifyDate(tempDate,intent.getAction().equals(ACTION_NEXT));

            remoteViews.setTextViewText(R.id.txt_title, "" + tempDate);


            prefs.edit().putString("temp", tempDate).apply();



            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(watchWidget), R.id.widgetCollectionList);
        }
        super.onReceive(context, intent);
    }

}