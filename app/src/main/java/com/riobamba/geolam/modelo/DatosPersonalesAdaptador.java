package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.R;

import java.util.List;
/*
public class DatosPersonalesAdaptador extends RecyclerView.Adapter<DatosPersonalesAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<DatosPersonales> datosList;
    final DatosPersonalesAdaptador.OnItemClickListener listener;
    final DatosPersonalesAdaptador.OnClickListener listener2;

    public interface OnItemClickListener{
        void onItemClick(DatosPersonales item);
    }

    public interface OnClickListener{
        void onClick(DatosPersonales button);
    }

    public DatosPersonalesAdaptador(Context mCtx, List<DatosPersonales> usuariosList, OnItemClickListener listener, OnClickListener listener2){
        this.mCtx = mCtx;
        this.datosList = usuariosList;
        this.listener = listener;
        this.listener2 = listener2;
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final EditText nombreUsuarios;
        private final EditText apellidoUsuarios;
        private final EditText edad;
        private final TextView emailUsuarios;
        private final TextView descripcionTipo;
        private final Button btnGuardar;
        private final Button btnCancelar;
        private final ImageView imagenUsuarios;





        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreUsuarios = view.findViewById(R.id.etNombre);
            apellidoUsuarios = view.findViewById(R.id.etApellido);
            emailUsuarios = view.findViewById(R.id.tvEmailUsu);
            btnGuardar = view.findViewById(R.id.btnGuardarCambiosUsu);
            btnCancelar = view.findViewById(R.id.btnCancelar);
            descripcionTipo = view.findViewById(R.id.tvMisDatos);
            imagenUsuarios = view.findViewById(R.id.ivPerfil);
            edad = view.findViewById(R.id.etEdad);

        }

        /*public TextView getTextView() {
            return textView;
        }*/
    //}

/*
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_datos_personales, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        DatosPersonales datosPersonales = datosList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

       Glide.with(mCtx)
                .load(datosPersonales.getImagenUsuarios())
                .into(viewHolder.imagenUsuarios);

        viewHolder.nombreUsuarios.setText(datosPersonales.getNombreUsuarios());
        viewHolder.apellidoUsuarios.setText(datosPersonales.getApellidoUsuarios());
        viewHolder.edad.setText(String.valueOf(datosPersonales.getEdad()));
        viewHolder.emailUsuarios.setText(datosPersonales.getEmailUsuarios());
        if(datosPersonales.getIdTipoUsuarios() == 1)
        {
            viewHolder.descripcionTipo.setText(datosPersonales.getDescripcionTipo());
        }
        else{
            viewHolder.descripcionTipo.setVisibility(View.GONE);
        }
        viewHolder.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(datosPersonales);
            }
        });

        viewHolder.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.onClick(datosPersonales);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return datosList.size();
    }
}

*/
