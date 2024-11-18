package com.example.codelingo;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private ListView rankingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.rankingListView);

        List<String[]> rankedUsers = getRankedUsers();  // Obtiene la lista de usuarios ordenados

        // Configura el adaptador personalizado
        RankingAdapter adapter = new RankingAdapter(this, rankedUsers);
        rankingListView.setAdapter(adapter);
    }

    // Método para obtener y ordenar los usuarios por puntaje
    private List<String[]> getRankedUsers() {
        List<String[]> users = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                // Asegúrate de que userData tiene al menos 4 elementos (username, password, score)
                if (userData.length >= 4) {  // Cambié el valor a 4 para tener en cuenta el índice [3]
                    users.add(userData);  // userData[0]: username, userData[1]: password, userData[2]: score
                } else {
                    Log.e("RankingActivity", "Formato incorrecto en la línea: " + line);
                }
            }
            reader.close();
            fis.close();

            // Ordena los usuarios por puntaje en orden descendente usando el índice [3] para el puntaje
            Collections.sort(users, (a, b) -> Integer.parseInt(b[3]) - Integer.parseInt(a[3]));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
