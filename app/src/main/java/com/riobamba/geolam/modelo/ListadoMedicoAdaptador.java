package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.riobamba.geolam.R;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;

import java.util.List;

public class ListadoMedicoAdaptador extends RecyclerView.Adapter<ListadoMedicoAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoMedico> lugarList;


    public ListadoMedicoAdaptador(Context mCtx, List<ListadoMedico> lugarList){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView especialidadLugar;
        private final TextView descripcionLugar;
        private final TextView idLugar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreMedicoLista);
            especialidadLugar = view.findViewById(R.id.tvEspecialidadMedicoLista);
            descripcionLugar = view.findViewById(R.id.tvDescripcionMedicoLista);
            idLugar = view.findViewById(R.id.tvIdMedicoLista);
        }

        /*public TextView getTextView() {
            return textView;
        }*/
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListadoMedicoAdaptador.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_medico_usu, null);
        return new ListadoMedicoAdaptador.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ListadoMedicoAdaptador.ViewHolder viewHolder, int position) {
        ListadoMedico listadoLugar = lugarList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nombreLugar.setText(listadoLugar.getNombreMedico());
        viewHolder.especialidadLugar.setText(listadoLugar.getEspecialidadMedico());
        viewHolder.descripcionLugar.setText(listadoLugar.getDescripcionMedico());
        viewHolder.idLugar.setText(String.valueOf(listadoLugar.getIdMedico()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lugarList.size();
    }
}

