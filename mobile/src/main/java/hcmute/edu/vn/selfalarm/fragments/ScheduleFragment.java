package hcmute.edu.vn.selfalarm.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hcmute.edu.vn.selfalarm.R;
import hcmute.edu.vn.selfalarm.adapter.EventAdapter;
import hcmute.edu.vn.selfalarm.database.AppDatabase;
import hcmute.edu.vn.selfalarm.model.Event;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleFragment extends Fragment implements EventAdapter.OnEventClickListener {
    private RecyclerView eventList;
    private EventAdapter eventAdapter;
    private List<Event> events;
    private AppDatabase database;
    private EditText eventTitleEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        database = AppDatabase.getInstance(getContext());
        eventList = view.findViewById(R.id.event_list);
        eventTitleEditText = view.findViewById(R.id.event_title);
        Button addEventButton = view.findViewById(R.id.add_event_button);

        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        events = new ArrayList<>();
        eventAdapter = new EventAdapter(events, this);
        eventList.setAdapter(eventAdapter);

        loadEvents();

        addEventButton.setOnClickListener(v -> showDateTimePicker());

        return view;
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String title = eventTitleEditText.getText().toString();
                                if (!title.isEmpty()) {
                                    Event event = new Event(title, calendar.getTimeInMillis());
                                    saveEvent(event);
                                } else {
                                    Toast.makeText(getContext(), "Please enter event title", Toast.LENGTH_SHORT).show();
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveEvent(Event event) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.eventDao().insert(event);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadEvents();
                Toast.makeText(getContext(), "Event added", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void loadEvents() {
        new AsyncTask<Void, Void, List<Event>>() {
            @Override
            protected List<Event> doInBackground(Void... voids) {
                return database.eventDao().getAllEvents();
            }

            @Override
            protected void onPostExecute(List<Event> eventList) {
                events.clear();
                events.addAll(eventList);
                eventAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onEditClick(Event event) {
        showDateTimePickerForEdit(event);
    }

    @Override
    public void onDeleteClick(Event event) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.eventDao().delete(event);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadEvents();
                Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void showDateTimePickerForEdit(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                event.setTitle(eventTitleEditText.getText().toString());
                                event.setStartTime(calendar.getTimeInMillis());
                                updateEvent(event);
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateEvent(Event event) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.eventDao().update(event);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadEvents();
                Toast.makeText(getContext(), "Event updated", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}