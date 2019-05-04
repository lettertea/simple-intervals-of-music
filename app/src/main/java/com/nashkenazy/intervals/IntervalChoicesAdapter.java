package com.nashkenazy.intervals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IntervalChoicesAdapter extends RecyclerView.Adapter<IntervalChoicesAdapter.ViewHolder> {
	private List<Interval> intervals;
	private OnIntervalClick callback;

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
		holder.txtHeader.setText(intervals.get(position).getName());
		holder.txtHeader.setOnClickListener(v -> callback.onClick(intervalValue));
	}

	@Override
	public int getItemCount() {
		return intervals.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		View layout;
		TextView txtHeader;

		ViewHolder(View v) {
			super(v);
			layout = v;
			txtHeader = v.findViewById(R.id.button_interval);
		}
	}

}