package ericminio.firebase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ericminio.data.GameRepository;
import ericminio.data.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FirebaseRepository implements GameRepository, UserRepository {

    private final Gson gson;
    private Firebase database;

    public FirebaseRepository(Firebase database) {
        this.database = database;
        gson = new Gson();
    }

    @Override
    public List<String> allGames() throws Exception {
        String response = database.read("games");
        Map<String, Named> entries = gson.fromJson(response, new TypeToken<Map<String, Named>>(){}.getType());

        return entries.values().stream().map(g -> g.name).collect(Collectors.toList());
    }

    @Override
    public List<String> allUsers() throws Exception {
        String response = database.read("users");
        Map<String, Named> entries = gson.fromJson(response, new TypeToken<Map<String, Named>>(){}.getType());

        return entries.values().stream().map(g -> g.name).collect(Collectors.toList());
    }
}
