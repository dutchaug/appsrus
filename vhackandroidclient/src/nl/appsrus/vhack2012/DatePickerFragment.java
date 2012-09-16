package nl.appsrus.vhack2012;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private int year;
	private int month;
	private int day;
	
	public interface DatePickerListener {
		public void datePicked(int year, int month, int day);
	}
	
	private DatePickerListener listener;
	
	public DatePickerFragment(int year, int month, int day, DatePickerListener listener) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.listener = listener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        
        if (year == 0) year = c.get(Calendar.YEAR);
        if (month == 0) month = c.get(Calendar.MONTH) + 1;
        if (day == 0) day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month-1, day);
        return dialog;
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		this.year = year;
		this.month = monthOfYear + 1;
		this.day = dayOfMonth;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (listener != null) {
			listener.datePicked(year, month, day);
		}
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
}
