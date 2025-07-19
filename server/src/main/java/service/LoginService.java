public LoginResult login(LoginRequest req) throws DataAccessException {
    if (req.username==null || req.password==null) throw new DataAccessException("Bad request");
    var user = userDAO.getUser(req.username);
    if (!user.getPassword().equals(req.password)) throw new DataAccessException("Unauthorized");
    String token = UUID.randomUUID().toString();
    authDAO.insertAuth(new AuthData(token, req.username));
    return new LoginResult(req.username, token);
}


