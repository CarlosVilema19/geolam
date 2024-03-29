package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itextpdf.text.pdf.parser.Line;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LugarMapaAdaptador extends RecyclerView.Adapter<LugarMapaAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugar> lugarList;
    final LugarMapaAdaptador.OnItemClickListener listener;
    private final List<ListadoLugar> lugarListOriginal;
    public void filtrado(String txtBuscar) {

        // lugarList.clear();

        if(txtBuscar.length() == 0)
        {
            lugarList.clear();
            lugarList.addAll(lugarListOriginal);
        }else{
            if(txtBuscar.length()!=0) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                    List<ListadoLugar> collection = lugarList.stream()
                            .filter(i -> i.getNombreLugar().toLowerCase().contains(txtBuscar.toLowerCase()))
                            .collect(Collectors.toList());
                    lugarList.clear();
                    lugarList.addAll(collection);
                } //else {
                lugarList.clear();
                for (ListadoLugar l : lugarListOriginal) {
                    if (l.getNombreLugar().toLowerCase().contains(txtBuscar.toLowerCase())) {

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

    public interface OnItemClickListener{
        void onItemClick(ListadoLugar item);
    }

    public interface OnClickListener{
        void onClick(ListadoLugar item);
    }

    public LugarMapaAdaptador(Context mCtx, List<ListadoLugar> lugarList, LugarMapaAdaptador.OnItemClickListener listener){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.listener = listener;
        lugarListOriginal = new ArrayList<>();
        lugarListOriginal.addAll(lugarList);
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView direccionLugar;
        private final TextView telefonoLugar;
        private final ImageView imagenLugar;
        private final TextView idLugar;
        private final TextView distanciaLugar;
        private final TextView categoria;
        private final LinearLayout llDistancia;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreLugarLista);
            direccionLugar = view.findViewById(R.id.tvDireccionLista);
            telefonoLugar = view.findViewById(R.id.tvTelefonoLista);
            imagenLugar = view.findViewById(R.id.ivImagenLugarLista);
            idLugar = view.findViewById(R.id.tvId);
            distanciaLugar = view.findViewById(R.id.tvDistancia);
            categoria = view.findViewById(R.id.tvCategoria);
            llDistancia = view.findViewById(R.id.llDistancia);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_listado, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ListadoLugar listadoLugar = lugarList.get(position);

        if(!listadoLugar.getDistancia().equals(""))
        {
            viewHolder.llDistancia.setVisibility(View.VISIBLE);
        }

        //Cargar Imagen
        Glide.with(mCtx)
                .load(listadoLugar.getImagenLugar())
                .into(viewHolder.imagenLugar);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.direccionLugar.setText(listadoLugar.getDireccionLugar());
        viewHolder.telefonoLugar.setText(listadoLugar.getTelefonoLugar());
        viewHolder.idLugar.setText(String.valueOf(listadoLugar.getId()));
        //viewHolder.direccionLugar.setVisibility(View.GONE);
        //Control para ocultar un text view en el caso de que el valor llegue vacio
        if(listadoLugar.getDistancia().equals("")) {viewHolder.distanciaLugar.setVisibility(View.GONE);}
        else {viewHolder.distanciaLugar.setText(listadoLugar.getDistancia());}
        viewHolder.categoria.setText(listadoLugar.getCategoria());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(listadoLugar);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lugarList.size();
    }
}
