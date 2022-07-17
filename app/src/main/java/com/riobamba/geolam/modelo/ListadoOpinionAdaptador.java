package com.riobamba.geolam.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.R;

import java.util.List;

public class ListadoOpinionAdaptador extends RecyclerView.Adapter<ListadoOpinionAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoOpinion> opinionList;
    final ListadoOpinionAdaptador.OnClickListener listener2;


    public interface OnClickListener{
        void onClick(ListadoOpinion button);
    }

    public ListadoOpinionAdaptador(Context mCtx, List<ListadoOpinion> opinionList,  OnClickListener listener2){
        this.mCtx = mCtx;
        this.opinionList = opinionList;
        this.listener2 = listener2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView idOpinion;
        private final TextView nombreUsuario;
        private final TextView calificacion;
        private final TextView fechaIngreso;
        private final TextView comentario;
        private final ImageButton btnBorrar;
        private final ImageView imagenUsuarios;





        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            idOpinion = view.findViewById(R.id.tvIdOpinion);
            nombreUsuario = view.findViewById(R.id.tvNombreUsuariosOpinion);
            calificacion = view.findViewById(R.id.tvCalificacionOpinion);
            fechaIngreso = view.findViewById(R.id.tvFechaOpinion);
            comentario = view.findViewById(R.id.tvComentarioOpinion);
            btnBorrar = view.findViewById(R.id.ibBorrarOpinion);
            imagenUsuarios = view.findViewById(R.id.ivImagenUsuarioOpinion);

        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_listado_opinion, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListadoOpinion listadoOpinion = opinionList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Glide.with(mCtx)
                .load(listadoOpinion.getImagenUsuario())
                .into(viewHolder.imagenUsuarios);
        viewHolder.idOpinion.setText(String.valueOf(listadoOpinion.getIdOpinion()));
        viewHolder.nombreUsuario.setText(listadoOpinion.getNombreUsuario());
        viewHolder.calificacion.setText(String.valueOf(listadoOpinion.getCalificacion()));
        viewHolder.fechaIngreso.setText(listadoOpinion.getFechaOpinion());
        viewHolder.comentario.setText(listadoOpinion.getComentario());

        SharedPreferences preferences = mCtx.getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        if(!email.equals(listadoOpinion.getEmail())){viewHolder.btnBorrar.setVisibility(View.GONE);}

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.onClick(listadoOpinion);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return opinionList.size();
    }
}

