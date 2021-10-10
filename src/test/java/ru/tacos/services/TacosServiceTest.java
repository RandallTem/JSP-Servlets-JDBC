package ru.tacos.services;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.tacos.models.Client;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RunWith(MockitoJUnitRunner.class)
public class TacosServiceTest extends TestCase {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement1;
    @Mock
    private PreparedStatement statement2;
    @Mock
    private PreparedStatement statement3;
    @Mock
    private PreparedStatement statement4;

    @Mock
    private ResultSet result1;
    @Mock
    private ResultSet result2;
    @Mock
    private ResultSet result3;
    @Mock
    private ResultSet result4;


    /*
    Метод инициализирует Mock объекты.
     */
    @Before
    public void setUp() throws Exception {
        assertNotNull(dataSource);
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        //Имитация для теста метода checkPassword()
        Mockito.when(connection.prepareStatement( "select password from clients where token=?")).thenReturn(statement1);
        Mockito.when(result1.next()).thenReturn(true);
        Mockito.when(result1.getString("password")).thenReturn("qwerty");
        Mockito.when(statement1.executeQuery()).thenReturn(result1);
        //Имитация для теста метода getAccount()
        Mockito.when(connection.prepareStatement( "select * from clients where token=?")).thenReturn(statement2);
        Mockito.when(result2.next()).thenReturn(true);
        Mockito.when(result2.getInt("id")).thenReturn(1);
        Mockito.when(result2.getString("nickname")).thenReturn("testUser");
        Mockito.when(result2.getString("email")).thenReturn("test@user.com");
        Mockito.when(result2.getString("password")).thenReturn("qwerty");
        Mockito.when(statement2.executeQuery()).thenReturn(result2);
        //Имитация для теста метода isNameEmailExist()
        Mockito.when(connection.prepareStatement( "select * from clients where nickname=?")).thenReturn(statement3);
        Mockito.when(connection.prepareStatement( "select * from clients where email=?")).thenReturn(statement3);
        Mockito.when(result3.next()).thenReturn(false);
        Mockito.when(statement3.executeQuery()).thenReturn(result3);
        //Имитация для теста метода authorize()
        Mockito.when(connection.prepareStatement(
                "select token from clients where email=? and password=?")).thenReturn(statement4);
        Mockito.when(result4.next()).thenReturn(false);
        Mockito.when(statement4.executeQuery()).thenReturn(result4);
    }


    /*
    Тестирование метода checkPassword(). Методу передается пароль и токен авторизованного аккаунта.
    Из базы данных берется значение поля 'password' для записи с токеном и сверяется с
    переданным методу значением пароля. Если значения совпали, возвращается true, иначе, false.
     */
    @Test
    public void testCheckPassword() {
        try {
            TacosService ts = new TacosService(dataSource);
            String password = "qwerty";
            int token = 10203040;
            boolean res = ts.checkPassword(password, token);
            assertTrue(res);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
    Тестирование метода getAccount(). Метод принимает значение токена аккаунта и берет из базы
    данных значения всех полей для этого токена. Используя значения полей, создается экземпляр
    класса Client и ссылка на него возвращается из метода. Если записи в базе данных для токена не найдены,
    возвращается null.
     */
    @Test
    public void testGetAccount() {
        try {
            TacosService ts = new TacosService(dataSource);
            String token = "10203040";
            Client client = ts.getAccount(token);
            assertNotNull(client);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
    Тестирование метода isNameEmailExist(). Методу передаются строки с именем и email.
    Он пытается получить из базы данных значения из соответствующих полей. Если удается, возвращает false,
    иначе true.
     */
    @Test(expected = AssertionFailedError.class)
    public void testIsNameEmailExist() {
        try {
            TacosService ts = new TacosService(dataSource);
            String name = "nonexistentUser";
            String email = "nonexistent@mail.com";
            boolean res = ts.isNameEmailExist(name, email);
            assertFalse(res);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
    Тестирование метода authorize(). Методу передаются строки email и password. Если метод
    находит в базе данных запись с такими значениями полей email и password, то возвращает значение поля token,
    иначе возвращает failed.
     */
    @Test(expected = AssertionFailedError.class)
    public void testAuthorize() {
        try {
            TacosService ts = new TacosService(dataSource);
            String email = "test@user.com";
            String password = "wrongpass";
            String token = ts.authorize(email, password);
            assertNotSame("failed", token);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}