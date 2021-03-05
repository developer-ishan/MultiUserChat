package Client;
import java.sql.*;

public class Database {
    private final String PATH_DB;
    private final Connection conn;
    public static final int SENT=1, RECEIVED=2;

    public Database(){
        PATH_DB = System.getProperty("user.home") + "/database.db";
        conn = connect();
        CreateTables();
    }

    public Database(String login) {
        PATH_DB = System.getProperty("user.home") + "/database.db";
        conn = connect();
        CreateTables();
    }

    private void CreateTables(){
        String create_history_message = "create table if not exists history_message(" +
                "name varchar(100) not null, " +
                "message varchar(500) not null, " +
                "timestamp datetime default CURRENT_TIMESTAMP, " +
                "direction int not null);";
        //1: sent
        //2: received
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(create_history_message);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:"+PATH_DB;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void message(String name, String message, int type) throws Exception {
        if(type==1 || type==2){
            try {
                String query="INSERT INTO history_message(name, message, direction) values(?, ?, ?) ";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1,name);
                ps.setString(2,message);
                ps.setInt(3,type);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else{
            throw new Exception("Invalid message type.");
        }
    }
    public ResultSet getMessages(String name) {
        try {
            String query="SELECT * from history_message where name=? order by timestamp asc";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,name);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public ResultSet getUsers() {
        try {
            String query="SELECT DISTINCT name from history_message";
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static void main(String[] args) {
        Database db = new Database("ishan");
        db.CreateTables();
        try {
            while(true){
                db.message("dennis", "rdrdrdrdrd!", Database.SENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
