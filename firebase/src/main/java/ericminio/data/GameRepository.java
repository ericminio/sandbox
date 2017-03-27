package ericminio.data;

import java.util.List;

public interface GameRepository {

    List<String> allGames() throws Exception;
}
