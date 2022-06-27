package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.riobamba.geolam.R;

import java.util.List;

public class DatosOpinionAdaptador extends RecyclerView.Adapter<DatosOpinionAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<DatosOpinion> opinionList;


    public DatosOpinionAdaptador(Context mCtx, List<DatosOpinion> opinionList){
        this.mCtx = mCtx;
        this.opinionList = opinionList;

    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView email;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            email = view.findViewById(R.id.tvEmail);
        }

        /*public TextView getTextView() {
            return textView;
        }*/
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_inicio, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        DatosOpinion datosOpinion = opinionList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.email.setText(datosOpinion.getEmail());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return opinionList.size();
    }
}
