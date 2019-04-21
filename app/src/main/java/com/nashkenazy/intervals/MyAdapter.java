package com.nashkenazy.intervals;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
	private List<String> intervalNames;
	private List<Integer> intervalValues;
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


	MyAdapter(List<String> intervalNames, List<Integer> intervalValues, OnIntervalClick listener) {
		this.intervalNames = intervalNames;
		this.intervalValues = intervalValues;
		this.callback = listener;
	}

	@NonNull
	public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_layout, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
		int intervalValue = intervalValues.get(position);
		holder.txtSemitone.setText(String.valueOf(intervalValue));
		holder.txtHeader.setText(intervalNames.get(position));
		holder.txtHeader.setOnClickListener(v -> callback.onClick(intervalValue));
	}

	@Override
	public int getItemCount() {
		return intervalValues.size();
	}

}