package service;

import dataaccess.*;

public class ClearService {
    private UserDataAccess userData;
    private AuthDataAccess authData;

    public ClearService(UserDataAccess userData, AuthDataAccess authData) {
        this.userData = userData;
        this.authData = authData;
    }

    public void clear() {
        userData.clearAll();
        authData.clearAll();
    }
}