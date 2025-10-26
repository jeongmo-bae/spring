package study.db.jdbc.connection;

public final class ConnectionConst {
    public static final String URL = "jdbc:mysql://localhost:3306/project_spring_db_study?serverTimezone=UTC";
    public static final String USER = "admin";
    public static final String PASSWORD = "admin";

    private ConnectionConst() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
