package com.example.codelingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RankingAdapter extends ArrayAdapter<String[]> {

    public RankingAdapter(Context context, List<String[]> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutiliza la vista si ya existe, de lo contrario infla una nueva
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ranking_item, parent, false);
        }

        // Obtiene el usuario actual
        String[] user = getItem(position);

        // Encuentra los elementos en el layout
        TextView rankingNumber = convertView.findViewById(R.id.rankingNumber);
        TextView userName = convertView.findViewById(R.id.userName);
        TextView userScore = convertView.findViewById(R.id.userScore);

        // Configura el contenido del elemento
        rankingNumber.setText(String.valueOf(position + 1));  // Número de ranking
        userName.setText(user[0]);  // Nombre del usuario
        userScore.setText("Puntos: " + user[3]);  // Puntaje del usuario, ahora está en el índice [3]

        return convertView;
    }
}
