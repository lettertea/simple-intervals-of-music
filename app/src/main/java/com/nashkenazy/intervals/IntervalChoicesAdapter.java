package com.nashkenazy.intervals;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IntervalChoicesAdapter extends RecyclerView.Adapter<IntervalChoicesAdapter.ViewHolder> {
	private List<Interval> intervals;
	private OnIntervalClick callback;

	class ViewHolder extends RecyclerView.ViewHolder {
		View layout;
		TextView txtSemitone;
		TextView txtHeader;

		ViewHolder(View v) {
			super(v);
			layout = v;
			txtSemitone = v.findViewById(R.id.text_semitone);
			txtHeader = v.findViewById(R.id.button_interval);
		}
	}

	IntervalChoicesAdapter(List<Interval> intervals, OnIntervalClick listener) {
		this.intervals = intervals;
		this.callback = listener;
	}

	@NonNull
	public IntervalChoicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_layout, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
		int intervalValue = intervals.get(position).getSemitones();
		holder.txtSemitone.setText(String.valueOf(intervalValue));
		holder.txtHeader.setText(intervals.get(position).getName());
		holder.txtHeader.setOnClickListener(v -> callback.onClick(intervalValue));
	}

	@Override
	public int getItemCount() {
		return intervals.size();
	}

}