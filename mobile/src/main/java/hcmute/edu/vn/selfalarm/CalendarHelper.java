package hcmute.edu.vn.selfalarm;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

public class CalendarHelper {
    public static void addEventToCalendar(Context context, String title, long startTime) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        context.startActivity(intent);
    }
}