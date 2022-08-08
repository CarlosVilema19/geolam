package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.R;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EspecialidadAdminAdaptador extends RecyclerView.Adapter<com.riobamba.geolam.modelo.EspecialidadAdminAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarAdmin> lugarList;
    final EspecialidadAdminAdaptador.OnItemClickListener listener;
    final EspecialidadAdminAdaptador.OnElimListener listener2;
    private final List<ListadoLugarAdmin> espeListOriginal;
    private final List<ListadoLugarAdmin> espeList;


    public interface OnItemClickListener{
        void onItemClick(ListadoLugarAdmin item);
    }

    public interface OnElimListener{
        void onItemClick(ListadoLugarAdmin item);
    }


    public EspecialidadAdminAdaptador(Context mCtx, 
                                      List<ListadoLugarAdmin> lugarList, 
                                      EspecialidadAdminAdaptador.OnItemClickListener listener,
                                      EspecialidadAdminAdaptador.OnElimListener listener2){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
        this.listener = listener;
        this.listener2 = listener2;
        espeListOriginal = new ArrayList<>();
        espeListOriginal.addAll(lugarList);
        espeList = new ArrayList<>();
        espeList.addAll(espeListOriginal);
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView idLugar;
        private final ImageButton btnActualizar;
        private final ImageButton btnBorrar;
        public ImageView imagenEspe;
        public CardView cardEspe;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreLugarLista);
            idLugar = view.findViewById(R.id.tvId);
            btnActualizar = view.findViewById(R.id.ibActualizarLista);
            btnBorrar = view.findViewById(R.id.ibBorrarLista);
            imagenEspe = view.findViewById(R.id.ivLogo);
            cardEspe = view.findViewById(R.id.cvLogo);
            
        }

        /*public TextView getTextView() {
            return textView;
        }*/
    }

    public void filtrado(final String txtBuscar)
    {
        // lugarList.clear();

        if(txtBuscar.length() == 0)
        {
            lugarList.clear();
            lugarList.addAll(espeListOriginal);
        }else{
            if(txtBuscar.length()!=0) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                    List<ListadoLugarAdmin> collection = lugarList.stream()
                            .filter(i -> i.getNombreLugar().toLowerCase().contains(txtBuscar.toLowerCase()))
                            .collect(Collectors.toList());
                    lugarList.clear();
                    lugarList.addAll(collection);
                } //else {
                lugarList.clear();
                for (ListadoLugarAdmin l : espeListOriginal) {
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

    // Create new views (invoked by the layout manager)
    @Override
    public com.riobamba.geolam.modelo.EspecialidadAdminAdaptador.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_listado_lugar_crud, null);
        return new com.riobamba.geolam.modelo.EspecialidadAdminAdaptador.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListadoLugarAdmin listadoLugar = lugarList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.cardEspe.setVisibility(View.VISIBLE);

        Glide.with(mCtx)
                .load(listadoLugar.getImagen())
                .into(viewHolder.imagenEspe);

        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.idLugar.setVisibility(View.GONE);
        viewHolder.btnActualizar.setVisibility(View.GONE);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(listadoLugar);
            }
        });
        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.onItemClick(listadoLugar);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lugarList.size();
    }
}
