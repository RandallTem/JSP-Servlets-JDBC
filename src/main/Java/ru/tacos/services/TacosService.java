package ru.tacos.services;

import ru.tacos.models.Client;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TacosService {

    //Источник данных
    private DataSource dataSource;

    //Конструктор, инициализирует источник данных
    public TacosService(DataSource theDataSource) {
        dataSource = theDataSource;
    }

    /*
    Метод выполняет удаление записи из таблицы с клиентами в базе данных по значению поля token.
     */
    public void deleteClient(int token) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            String sql = "delete from clients where token=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, token);
            statement.execute();
        } finally {
            close(connection, statement, null);
        }
    }

    /*
    Метод обновляет запись в таблице с клиентами в базе данных. Сначала рассчитывается токен для нового набора
    значений, затем в записи с нужным значением поля nickname обновляются значения всех полей.
     */
    public void updateClient(String old_nickname, String nickname, String email, String password) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            int new_token = (nickname+email+password).hashCode();
            String sql = "update clients " +
                    "set nickname=?, email=?, password=?, token=? " +
                    "where nickname=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, nickname);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, new_token);
            statement.setString(5, old_nickname);
            statement.execute();
        } finally {
            close(connection, statement, null);
        }
    }

    /*
    Метод проверяет совпадение принятого пароля и пароля в записи таблицы клиентов со значением токена token.
    Возвращает true, если пароли равны, и false в противном случае.
     */
    public boolean checkPassword(String password, int token) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = dataSource.getConnection();
            String sql = "select password from clients where token=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, token);
            result = statement.executeQuery();
            if (result.next())
                if (password.equals(result.getString("password")))
                    return true;
                else
                    return false;
            else
                return false;
        } finally {
            close(connection, statement, result);
        }
    }

    /*
    Метод предназначен для получения объекта, содержащего данные аккаунта клиента, по токену.
    Он получает значения всех полей строки с токеном token, инициализирует ими экземпляр класса
    Client и возвращает ссылку на него.
     */
    public Client getAccount(String token) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = dataSource.getConnection();
            String sql = "select * from clients where token=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(token));
            result = statement.executeQuery();
            if (result.next()) {
                int id = result.getInt("id");
                String nickname = result.getString("nickname");
                String email = result.getString("email");
                String password = result.getString("password");
                Client theClient = new Client(id, nickname, email, password, Integer.parseInt(token));
                return theClient;
            } else {
                return null;
            }
        } finally {
            close(connection, statement, result);
        }
    }

    /*
    Метод проверяет существование в таблице клиентов записей с принятыми методом значениями в полях nickname или email.
    Если такие записи существует, то возвращается false, иначе true.
     */
    public boolean isNameEmailExist(String name, String email) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = dataSource.getConnection();
            String sql = "select * from clients where nickname=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            result = statement.executeQuery();
            if (result.next())
                return false;
            sql = "select * from clients where email=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            result = statement.executeQuery();
            if (result.next())
                return false;
            return true;
        } finally {
            close(connection, statement, result);
        }
    }

    /*
    Метод выполняет авторизацию пользователя по почте и паролю. Метод проверяет, существует ли
    в таблице клиентов запись, одновременно содержащая и почту и пароль в соответствующих полях.
    Если есть, то метод возвращает значение поля token этой записи, иначе возвращает строку "failed".
     */
    public String authorize(String email, String password) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        String token;
        try {
            connection = dataSource.getConnection();
            String sql = "select token from clients where email=? and password=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            result = statement.executeQuery();
            if (result.next()) {
                token = String.valueOf(result.getInt("token"));
            } else {
                token = "failed";
            }
            return token;
        } finally {
            close(connection, statement, result);
        }
    }

    /*
    Метод создает новую запись в таблице клиентов. Параметры name, email, password передаются методу
    в виде аргументов, id присваивается автоматически базой данных, а token высчитывается как hashCode()
    конкатенации имени, почты и пароля.
     */
    public String register(String name, String email, String password) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        int token = (name+email+password).hashCode();
        try {
            connection = dataSource.getConnection();
            String sql = "insert into clients (nickname, email, password, token) " +
                    "values (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, token);
            statement.execute();
            return String.valueOf(token);
        } finally {
            close(connection, statement, null);
        }
    }

    /*
    Метод отвечает за закрытие соединения с базой данных
     */
    private void close(Connection connection, Statement statement, ResultSet result) {
        try {
            if (connection != null ) connection.close();
            if (statement != null ) statement.close();
            if (result != null ) result.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
