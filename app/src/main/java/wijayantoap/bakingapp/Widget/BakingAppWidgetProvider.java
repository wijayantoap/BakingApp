package wijayantoap.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;

import wijayantoap.bakingapp.Activity.MainActivity;
import wijayantoap.bakingapp.R;

import static wijayantoap.bakingapp.Fragment.IngredientsFragment.ingredientsForWidget;
import static wijayantoap.bakingapp.Fragment.IngredientsFragment.nameForWidget;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static final String STRING = "name";
    public static final String INGREDIENTS = "ingredients";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        // Use shared preference to get the ingredients list
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String stringIngredients = prefs.getString(INGREDIENTS, ingredientsForWidget);
        String stringName = prefs.getString(STRING, nameForWidget);


        if (stringIngredients.equals("")) {
            views.setTextViewText(R.id.appwidget_text, widgetText);
        } else {
            views.setTextViewText(R.id.appwidget_text, stringIngredients);
            views.setTextViewText(R.id.widget_txt_name, stringName);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

