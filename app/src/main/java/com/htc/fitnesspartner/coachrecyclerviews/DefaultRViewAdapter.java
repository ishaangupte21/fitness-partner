package com.htc.fitnesspartner.coachrecyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;

public class DefaultRViewAdapter extends FirestoreRecyclerAdapter<GroupClass, DefaultRViewAdapter.Holder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DefaultRViewAdapter(@NonNull FirestoreRecyclerOptions<GroupClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull GroupClass model) {
        holder.textView.setText(model.getName());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.def_rview_layout, parent, false);
        return new Holder(v);
    }

    public class Holder extends RecyclerView.ViewHolder {
        MaterialTextView textView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.def_r_view_text);
        }
    }
}
