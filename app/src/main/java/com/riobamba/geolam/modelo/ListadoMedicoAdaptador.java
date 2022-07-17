package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.riobamba.geolam.R;


import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

public class ListadoMedicoAdaptador extends RecyclerView.Adapter<ListadoMedicoAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoMedico> lugarList;
    private final List<ListadoMedico> medicListOriginal;


    public ListadoMedicoAdaptador(Context mCtx, List<ListadoMedico> lugarList){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        medicListOriginal = new ArrayList<>();
        medicListOriginal.addAll(lugarList);
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

    }

    public void filtrado(final String txtBuscar)
    {
       // lugarList.clear();

        if(txtBuscar.length() == 0)
        {
            lugarList.clear();
            lugarList.addAll(medicListOriginal);
        }else{
            if(txtBuscar.length()!=0) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                    List<ListadoMedico> collection = lugarList.stream()
                            .filter(i -> i.getNombreMedico().toLowerCase().contains(txtBuscar.toLowerCase()))
                            .collect(Collectors.toList());
                    lugarList.clear();
                    lugarList.addAll(collection);
                } //else {
                    lugarList.clear();
                    for (ListadoMedico l : medicListOriginal) {
                        if (l.getNombreMedico().toLowerCase().contains(txtBuscar.toLowerCase())) {

                            lugarList.add(l);
                        }
                   // }
                }
            }
        }
        //medicListOriginal.clear();
       // lugarList.clear();
        notifyDataSetChanged();
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

