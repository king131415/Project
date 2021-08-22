package Utils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DButil {
     //云服务器的数据库名字是OJ_system,本地是oj_system
    private static final String URL="jdbc:mysql://localhost:3306/OJ_system";
    private static final String USERNAME="root";
    private static  final String PASSWORD="5258"; //云服务器的数据库密码是5258,本地的是king5258

    private static DataSource dataSource=null;

    private static DataSource getDataSource(){
        if (dataSource==null){
            dataSource=new MysqlDataSource();
            ((MysqlDataSource)dataSource).setURL(URL);
            ((MysqlDataSource) dataSource).setUser(USERNAME);
            ((MysqlDataSource) dataSource).setPassword(PASSWORD);

        }
        return  dataSource;
    }
    public static Connection getConnection(){

        try {
            return  getDataSource().getConnection();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return null;

    }
    public  static  void close(Connection connection, PreparedStatement statement, ResultSet resultSet){
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        }
    }

}
