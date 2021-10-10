package ru.tacos.servlets;


import ru.tacos.models.Client;
import ru.tacos.models.Taco;
import ru.tacos.services.TacosService;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Tacos")
public class TacosServlet extends HttpServlet {

    //Объект TacosService для взаимодействия с базой данных
    TacosService tacosService;

    //Источник данных
    @Resource(name="jdbc/tacos")
    private DataSource dataSource;

    /*
    В методе инициализируется объект класса TacosService
     */
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            tacosService = new TacosService(dataSource);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /*
    Метод проверяет авторизован ли посетитель сайта. Сначала он проверяет его Cookie на наличие токена авторизации,
    если не находит, то ищет токен в объекте сессии. Если токен найден, то он передается методу getAccount()
    объекта класса TacosService, который проверяет наличие записи с таким токеном в таблице пользователей. Если вернулся
    null, значит токен в базе данных не найден и пользователь не авторизован.
     */
    private Client checkAuthorization(HttpServletRequest request) throws Exception {
        Client client = null;
        Cookie[] theCookies = request.getCookies();
        if (theCookies != null) {
            for (Cookie tempCookie : theCookies) {
                if ("tacos.token".equals(tempCookie.getName())) {
                    client = tacosService.getAccount(tempCookie.getValue());
                    if (client != null)
                        return client;
                }
            }
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("tacos.token") != null)
            client = tacosService.getAccount(session.getAttribute("tacos.token").toString());
        return client;
    }

    /*
    Метод обрабатывает все полученные GET запросы. В зависимости от команды в запросе метод вызывает
    другие методы для выполнения требуемой задачи.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServerException{
        try {
            String command = request.getParameter("cmd");
            if (command == null)
                command = "home";
            switch (command) {
                case "home":
                    loadHomePage(request, response);
                    break;
                case "menu":
                    loadMenuPage(request, response);
                    break;
                case "auth":
                    loadAuthorizationPage(request, response);
                    break;
                case "reg":
                    loadRegistrationPage(request, response);
                    break;
                case "unauth":
                    unauthorizeClient(request, response);
                    break;
                case "account":
                    loadAccountPage(request, response);
                    break;
                case "delete":
                    deleteAccount(request, response);
                    break;
                case "orderpage":
                    loadOrderPage(request, response);
                    break;
                case "deleteOrder":
                    deleteOrder(request, response);
                    break;
                case "pay":
                    loadPaymentPage(request, response);
                    break;
                case "paid":
                    loadPaidPage(request, response);
                    break;
                default:
                    loadHomePage(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerException(e.toString());
        }
    }

    /*
    Метод выполняется после того, как была проведена оплата заказа. Он удаляет из объекта сессии информацию о
    содержимом корзины и выполняет редирект на страницу с сообщением, что оплата успешно прошла.
     */
    private void loadPaidPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("orderList", null);
        response.sendRedirect("paid.html");
    }

    /*
    Метод отвечает за загрузку страницы оплаты. Из объекта запроса он получает цену заказа "price". Если попытка
    зайти на страницу происходит не со страницы заказа, а через строку браузера, то в сессии нет значения "price".
    В таком случае страница оплаты не загружается, а выполняется редирект на главную страницу.
     */
    private void loadPaymentPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        long price = session.getAttribute("price") == null ? -1 : (long)session.getAttribute("price");
        if (price == -1) {
            response.sendRedirect("Tacos");
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("payment.jsp");
            request.setAttribute("price", price);
            dispatcher.forward(request, response);
        }
    }

    /*
    Метод удаляет выбранную позицию из заказа. Все позиции заказа хранятся в сессии в списке orderList. У каждой
    позиции есть индивидуальное значение id - время заказа в миллисекундах. Когда на веб-странице корзины выбирается
    удаление позиции, ее id передается в объекте запроса. В методе список заказов берется из объекта сессии,
    последовательно перебирается, пока не будет найден заказ с нужным id. Заказ удаляется из списка и список помещается
    обратно в сессию. Выполняется редирект обратно в корзину, где подгружается уже новый список, без удаленной
    позиции.
     */
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        List<Taco> orderList = (List<Taco>)session.getAttribute("orderList");
        if (orderList.size() == 1) {
            session.setAttribute("orderList", null);
        } else {
            long id = Long.parseLong(request.getParameter("id"));
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getId() == id) {
                    orderList.remove(i);
                    session.setAttribute("orderList", orderList);
                }
            }
        }
        response.sendRedirect("Tacos?cmd=orderpage");
    }

    /*
    Метод отвечает за загрузку страницы корзины. Сначала он проверяет, авторизован ли посетитель.
    Если авторизован, то из объекта сессии в объект запроса помещается список заказов и выполняется перенаправление
    в корзину. Если посетитель не авторизован, то он перенаправляется на страницу авторизации. При этом в объект
    запроса помещается значение showmessage, благодаря которому на странице авторизации отображается сообщение, что
    пользователю надо авторизоваться для доступа к странице.
     */
    private void loadOrderPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Client client = checkAuthorization(request);
        RequestDispatcher dispatcher;
        if (client == null) {
            dispatcher = request.getRequestDispatcher("/auth.jsp");
            request.setAttribute("showmessage", true);
            request.setAttribute("wantedPage", "Tacos?cmd=orderpage");
            dispatcher.forward(request, response);
            return;
        } else {
            dispatcher = request.getRequestDispatcher("/orderpage.jsp");
            request.setAttribute("CLIENT_NAME", client.getNickname());
            HttpSession session = request.getSession();
            List<Taco> orderList = (List<Taco>)session.getAttribute("orderList");
            request.setAttribute("ORDER", orderList);
            dispatcher.forward(request, response);
        }
    }

    /*
    Метод отвечает за удаление аккаунта из базы данных. Сначала проверяется авторизован ли клиент. Если авторизован,
    то его token передается методу deleteClass() объекта класса TacosService, который уже убирает запись об аккаунте
    из базы данных. Затем выполняется редирект на главную страницу сайта, где пользователь оказывается неавторизованным,
    потому что, хоть у него все еще есть cookie и запись в сессии, имеющийся у него токен больше не может быть найден в
    базе данных.
     */
    private void deleteAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Client client = checkAuthorization(request);
        tacosService.deleteClient(client.getToken());
        response.sendRedirect("Tacos");
    }

    /*
    Метод отвечает за загрузку страницы управления аккаунтом. Проверяется авторизация посетителя. Если авторизован,
    то из полученного при проверке авторизации объекта client в request записываются имя и почта клиента. Они потребуются
    для генерации содержимого страницы. Если не авторизован, то выполняется перенаправление на страницу авторизации
    с сообщением о том, что без авторизации желаемую страницу посетить нельзя. При этом информация о том, что
    пользователь хотел посетить страницу управления аккаунтом, сохраняется в сессии, чтобы после успешной авторизации
    сделать перенаправление именно на нее.
     */
    private void loadAccountPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Client client = checkAuthorization(request);
        RequestDispatcher dispatcher;
        if (client == null) {
            dispatcher = request.getRequestDispatcher("/auth.jsp");
            request.setAttribute("showmessage", true);
            request.setAttribute("wantedPage", "Tacos?cmd=account");
            dispatcher.forward(request, response);
            return;
        } else {
            dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("CLIENT_NAME", client.getNickname());
            request.setAttribute("CLIENT_EMAIL", client.getEmail());
            dispatcher.forward(request, response);
        }
    }

    /*
    Метод отвечает за загрузку страницы регистрации. Выполняется проверка авторизации пользователя. Если он
    уже авторизован, то выполняется перенаправление на главную страницу. Иначе, загружается страница с формой
    для регистрации.
     */
    private void loadRegistrationPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie[] theCookies = request.getCookies();
        RequestDispatcher dispatcher;
        Client client = checkAuthorization(request);
        if (client == null) {
            dispatcher = request.getRequestDispatcher("/reg.jsp");
        } else {
            dispatcher = request.getRequestDispatcher("/Tacos?cmd=home");
        }
        dispatcher.forward(request, response);
    }

    /*
    Метод выполняет выход пользователя из аккаунта. Он удаляет Cookie и запись из объекта сессии с информацией, требуемой
    для авторизации. После чего, перенаправляет пользователя на главную страницу сайта.
     */
    private void unauthorizeClient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie newCookie = new Cookie("tacos.token", "");
        newCookie.setMaxAge(0);
        response.addCookie(newCookie);
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("Tacos");
    }

    /*
    Метод отвечает за загрузку страницы авторизации. Если пользователь уже авторизован, он перенаправляется на
    главную страницу. Если не авторизован, открывается страница с формой авторизации.
     */
    private void loadAuthorizationPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher dispatcher;
        Client client = checkAuthorization(request);
        if (client == null) {
            dispatcher = request.getRequestDispatcher("/auth.jsp");
            request.setAttribute("wantedPage", "Tacos?cmd=home");
        } else {
            dispatcher = request.getRequestDispatcher("/Tacos?cmd=home");
        }
        dispatcher.forward(request, response);
    }

    /*
    Метод отвечает за загрузку страницы Меню. Если пользователь не авторизован, то он перенаправляется на страницу
    авторизации с сообщением, что необходимо авторизация. Запись о том, что пользователь хотел перейти на страницу
    меню, сохраняется в объекте запроса, чтобы после успешной авторизации он мог быть перенаправлен сразу на нее.
    Иначе, выполняется переход на страницу меню. При этом в объект запроса также передается имя аккаунта,
    для того, чтобы отобразить приветствие.
     */
    private void loadMenuPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher dispatcher;
        Client client = checkAuthorization(request);
        if (client == null) {
            dispatcher = request.getRequestDispatcher("/auth.jsp");
            request.setAttribute("showmessage", true);
            request.setAttribute("wantedPage", "Tacos?cmd=menu");
            dispatcher.forward(request, response);
            return;
        } else {
            dispatcher = request.getRequestDispatcher("/menu.jsp");
            request.setAttribute("CLIENT_NAME", client.getNickname());
            dispatcher.forward(request, response);
        }

    }

    /*
    Метод отвечает за загрузку главной страницы. Если пользователь не авторизован, то атрибут IS_AUTH устанавливается
    в false и страницы отображает кнопку ВОЙТИ. Если авторизован, то IS_AUTH равен true, а также передается имя
    пользователя для отображения приветствия авторизованного пользователя.
     */
    private void loadHomePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
        Client client = checkAuthorization(request);
        if (client == null) {
            request.setAttribute("IS_AUTH", false);
        } else {
            request.setAttribute("CLIENT_NAME", client.getNickname());
            request.setAttribute("IS_AUTH", true);
        }
        dispatcher.forward(request, response);
    }

    /*
    Метод обрабатывает все полученные POST запросы. В зависимости от команды в запросе метод вызывает
    другие методы для выполнения требуемой задачи.
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            RequestDispatcher dispatcher;
            String command = request.getParameter("cmd");
            switch (command) {
                case "authorize":
                    authorizeClient(request, response);
                    break;
                case "register":
                    if (tacosService.isNameEmailExist(request.getParameter("nickname"), request.getParameter("email"))) {
                        registerClient(request, response);
                    } else {
                        dispatcher = request.getRequestDispatcher("/reg.jsp");
                        request.setAttribute("failedreg", true);
                        dispatcher.forward(request, response);
                    }
                    break;
                case "updateProfile":
                    updateProfile(request, response);
                    break;
                case "addToOrder":
                    addToOrder(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServerException(e.toString());
        }
    }

    /*
    Метод отвечает за добавление позиции в заказ. Он формирует позицию в виде объекта Taco с помощью полученных
    из формы значений, извлекает из объекта сессии список заказов orderList<Taco>, добавляет в него новый элемент и
    помещает список обратно в объект сессии.
     */
    private void addToOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tortillaStr = request.getParameter("tortilla");
        boolean tortilla = tortillaStr.equals("wheat") ? false : true;
        String meatStr = request.getParameter("meat");
        int meat = 0;
        switch (meatStr) {
            case "no":
                meat = 0;
                break;
            case "beef":
                meat = 1;
                break;
            case "chicken":
                meat = 2;
                break;
        }
        boolean cucumber = request.getParameter("cucumber") != null;
        boolean tomato = request.getParameter("tomato") != null;
        boolean salad = request.getParameter("salad") != null;
        boolean onion = request.getParameter("onion") != null;
        boolean pepper = request.getParameter("pepper") != null;
        boolean beans = request.getParameter("beans") != null;
        boolean parsley = request.getParameter("parsley") != null;
        boolean spices = request.getParameter("spices") != null;
        Taco theTaco = new Taco(tortilla, meat, cucumber, tomato, salad, onion, pepper, beans, parsley, spices);
        HttpSession session = request.getSession();
        List<Taco> orderList = (List<Taco>)session.getAttribute("orderList");
        if (orderList == null) {
            List<Taco> newOrderList = new ArrayList<>();
            newOrderList.add(theTaco);
            session.setAttribute("orderList", newOrderList);
        } else {
            orderList.add(theTaco);
            session.setAttribute("orderList", orderList);
        }
        response.sendRedirect("Tacos?cmd=menu&status=ok");
    }

    /*
    Метод выполняет обновление данных аккаунта. С помощью метода объекта класса TacosService выполняется проверка
    введенных в форму имени и почты на наличие в базе данных, а также правильно введенного старого пароля.
    Если есть проблемы, то выполняется редирект на ту же самую страницу, но выводится сообщение о проблеме.
    Если все в порядке, то данные из формы передаются в метод объекта класса TacosService для обновления базы данных, а
    пользователь выбрасывается из аккаунта, чтобы заново авторизоваться и получить новый токен.
     */
    private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String curName = request.getParameter("curNickname");
        String curEmail = request.getParameter("curEmail");
        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        String newName = request.getParameter("newNickname");
        String newEmail = request.getParameter("newEmail");
        boolean test1 = newName.equals(curName);
        boolean test2 = newName.equals(curEmail);
        String testNewName = newName.equals(curName) ? "" : newName;
        String testNewEmail = newEmail.equals(curEmail) ? "" : newEmail;
        if (!tacosService.isNameEmailExist(testNewName, testNewEmail)) {
            response.sendRedirect("Tacos?cmd=account&mf1=true");
        } else {
            if (tacosService.checkPassword(oldPassword, (curName + curEmail + oldPassword).hashCode())) {
                tacosService.updateClient(curName, newName, newEmail, newPassword);
                response.sendRedirect("Tacos?cmd=unauth");
            } else {
                response.sendRedirect("Tacos?cmd=account&mf2=true");
            }
        }
    }

    /*
    Метод выполняет регистрацию клиента. Полученные из формы данные передаются в метод объекта класса TacosService,
    который добавляет новую запись в базу данных.
     */
    private void registerClient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name, email, password;
        name = request.getParameter("nickname");
        email = request.getParameter("email");
        password = request.getParameter("password");
        String token = tacosService.register(name, email, password);
        HttpSession session = request.getSession();
        session.setAttribute("tacos.token", token);
        response.sendRedirect("Tacos");
    }

    /*
    Метод выполняет авторизацию клиента. Почта и пароль передаются в метод объекта класса TacosService, если они верны,
    то возвращается токен аккаунта. Если отмечен чекбокс "запомнить меня", то токен сохраняется в Cookie, который
    действует 72 часа. Если чекбокс не отмечен, то токен сохранится в объекте сессии и будет храниться
    только до ее завершения.
     */
    private void authorizeClient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = tacosService.authorize(request.getParameter("email"), request.getParameter("password"));
        RequestDispatcher dispatcher;
        if (token == "failed") {
            dispatcher = request.getRequestDispatcher("/auth.jsp");
            request.setAttribute("failedauth", true);
            request.setAttribute("wantedPage", request.getParameter("page"));
            dispatcher.forward(request, response);
            return;
        }
        if (request.getParameter("rememberme") != null && request.getParameter("rememberme").equals("on")) {
            Cookie newCookie = new Cookie("tacos.token", token);
            newCookie.setMaxAge(60*60*3);
            response.addCookie(newCookie);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("tacos.token", token);
        }
        System.out.println(request.getParameter("page"));
        response.sendRedirect(request.getParameter("page"));
    }
}
