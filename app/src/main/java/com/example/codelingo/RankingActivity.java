package com.example.codelingo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private ListView rankingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.rankingListView);

        List<String[]> rankedUsers = getRankedUsers();
        List<String> displayList = new ArrayList<>();

        for (String[] user : rankedUsers) {
            displayList.add("Usuario: " + user[0] + " - Puntaje: " + user[2]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
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
                users.add(userData);  // userData[0]: username, userData[1]: password, userData[2]: score
            }
            reader.close();
            fis.close();

            // Ordena los usuarios por puntaje en orden descendente
            Collections.sort(users, (a, b) -> Integer.parseInt(b[2]) - Integer.parseInt(a[2]));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}