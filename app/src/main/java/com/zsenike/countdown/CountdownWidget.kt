package com.zsenike.countdown

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CountdownWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        private val targetTime: Long by lazy {
            Calendar.getInstance().apply {
                set(2026, Calendar.NOVEMBER, 19, 16, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_countdown)

            val now = System.currentTimeMillis()
            val diff = targetTime - now

            val text = if (diff <= 0) {
                "Es ist soweit!"
            } else {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
                "%dd %02dh %02dm".format(days, hours, minutes)
            }

            views.setTextViewText(R.id.widget_countdown, text)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
