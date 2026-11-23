package de.yp.gsearch

import android.app.PendingIntent
import android.app.SearchManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews

class SearchWidget : AppWidgetProvider() {
    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val sm = ctx.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val intent = Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH).apply {
            val global = sm.globalSearchActivity
            if (global != null) component = global
            else setPackage("com.google.android.googlequicksearchbox")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(SearchManager.QUERY, "")
            putExtra(SearchManager.EXTRA_SELECT_QUERY, true)
            putExtra(SearchManager.APP_DATA, Bundle().apply { putString("source", ctx.packageName) })
        }
        val pi = PendingIntent.getActivity(
            ctx, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        for (id in ids) {
            val views = RemoteViews(ctx.packageName, R.layout.widget)
            views.setOnClickPendingIntent(R.id.root, pi)
            mgr.updateAppWidget(id, views)
        }
    }
}
