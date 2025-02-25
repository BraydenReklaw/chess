package server;

import dataaccess.DataAccessException;

import java.util.UUID;

public class UserService {
    private UserDataAccess dataAccess;

    public UserService(UserDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public UserResponse register(UserRequest request) throws Exception {
        if (dataAccess.getUser(request.getUsername()) != null) {
            throw new DataAccessException("User Already Taken");
        }
        UserData userData = new UserData(request.getUsername(),request.getPassword(), request.getEmail());

        dataAccess.createUser(userData);

        AuthData authData =  new AuthData(request.getUsername(), generateAuthToken());

        dataAccess.createAuth(authData);

        return new UserResponse(request.getUsername(), authData.getAuthToken());
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
