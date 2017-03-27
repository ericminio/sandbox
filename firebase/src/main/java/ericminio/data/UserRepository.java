package ericminio.data;

import java.util.List;

public interface UserRepository {

    List<String> allUsers() throws Exception;
}
