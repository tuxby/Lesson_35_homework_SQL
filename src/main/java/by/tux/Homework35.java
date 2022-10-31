package by.tux;

import java.sql.*;

public class Homework35 {
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String username = "postgres";
    private String passwords = "postgres";

    private Connection connection;
    private Statement statement;

    public Homework35(String url, String username, String passwords)  {
        this.url = url;
        this.username = username;
        this.passwords = passwords;

        init();
    }
    public Homework35() {
        init();
    }

    private void init(){
        try{
            connection = DriverManager.getConnection(url,username,passwords);
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println("init() ERROR!");
            throw new RuntimeException(e);
        }


    }
    public void recreateTables(){
        String sql;
        sql = "DROP TABLE IF EXISTS managers CASCADE;";
        executeUpdate(sql);
        sql = "DROP TABLE IF EXISTS users CASCADE;";
        executeUpdate(sql);
        sql = "CREATE TABLE managers ("+
              "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
              "name VARCHAR(100))";
        executeUpdate(sql);
        sql = "CREATE TABLE users (" +
              "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
              "name VARCHAR(100)," +
              "sum INT," +
              "manager_id INT," +
              "CONSTRAINT fk_manager" +
              "     FOREIGN KEY(manager_id)" +
              "     REFERENCES managers(id))" ;
        executeUpdate(sql);
    }

    public void fillTables(){
        addManager("Duck");
        addManager("Jessie");
        addManager("Woody");
        addManager("Bill");
        addManager("Trump");

        addUser("Moana",23,1);
        addUser("Jumba",25,1);
        addUser("Aladdin",11,2);
        addUser("Hercules",54,2);
        addUser("Goofy",34,3);
        addUser("Ariel",12,3);
    }

    public void close() throws SQLException {
        connection.close();
    }

    private int executeUpdate(String query)  {
        try{
            Statement statement = connection.createStatement();
            // Для Insert, Update, Delete
            int result = statement.executeUpdate(query);
            return result;
        }catch (Exception e) {
            System.out.println("executeUpdate() ERROR!");
            System.out.println("========");
            System.out.println(query);
            System.out.println("========");
            throw new RuntimeException(e);
        }
    }

    public void print() {
        try{
            ResultSet resultSetManagers = statement.executeQuery("select * from managers");
            System.out.println();
            System.out.println("MANAGERS ---------------------");
            while (resultSetManagers.next()) {
                int id = resultSetManagers.getInt("id");
                String name = resultSetManagers.getString("name");
                System.out.printf("%d. %s \n", id, name);
            }
            ResultSet resultSetUsers = statement.executeQuery("select id, name, sum , (select name from managers where manager_id = managers.id) as managerName from users");
            System.out.println();
            System.out.println("USERS ------------------------");
            while(resultSetUsers.next()) {
                int id = resultSetUsers.getInt("id");
                int sum = resultSetUsers.getInt("sum");
                String name = resultSetUsers.getString("name");
                String managerName = resultSetUsers.getString("managerName");
                System.out.printf( "%d. %s - %d$ ( manager is %s ) \n" , id , name , sum , managerName );
            }
        }catch (Exception e) {
            System.out.println("print()ERROR!");
            throw new RuntimeException(e);
        }
    }

    public void printMoneyByManagers(){
        try{
            ResultSet resultSetManagers = statement.executeQuery("select id , name , (select sum(sum) from users where manager_id = managers.id) as sum from managers");
            System.out.println();
            System.out.println("Money by managers ------------------------");
            while (resultSetManagers.next()) {
                int id = resultSetManagers.getInt("id");
                String name = resultSetManagers.getString("name");
                int sum = resultSetManagers.getInt("sum");
                System.out.printf("%d. %s = %d$\n", id, name , sum);
            }
        }catch (Exception e) {
            System.out.println("printManagerMoney() ERROR!");
            throw new RuntimeException(e);
        }
    }

    public void addManager(String name){
        executeUpdate("insert into managers (name) values ('"+ name +"')");
    }
    public void addUser(String name, int sum, int managerId){
        executeUpdate("insert into users (name,sum,manager_id) values ('"+name+"','"+sum+"','"+managerId+"')");
    }
    public void userUpdateSum(String name, int sum){
        executeUpdate("update users set sum = '" + sum + "' where name = '" + name + "'");
    }
}
