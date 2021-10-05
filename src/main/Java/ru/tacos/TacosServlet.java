package ru.tacos;


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

    TacosModel tacosModel;

    @Resource(name="jdbc/tacos")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            tacosModel = new TacosModel(dataSource);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Client checkAuthorization(HttpServletRequest request) throws Exception {
        Client client = null;
        Cookie[] theCookies = request.getCookies();
        if (theCookies != null) {
            for (Cookie tempCookie : theCookies) {
                if ("tacos.token".equals(tempCookie.getName())) {
                    client = tacosModel.getAccount(tempCookie.getValue());
                    if (client != null)
                        return client;
                }
            }
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("tacos.token") != null)
            client = tacosModel.getAccount(session.getAttribute("tacos.token").toString());
        return client;
    }

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

    private void loadPaidPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("orderList", null);
        response.sendRedirect("paid.html");
    }

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

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Client client = checkAuthorization(request);
        tacosModel.deleteClient(client.getToken());
        response.sendRedirect("Tacos");
    }

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

    private void unauthorizeClient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie newCookie = new Cookie("tacos.token", "");
        newCookie.setMaxAge(0);
        response.addCookie(newCookie);
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("Tacos");
    }

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
                    if (tacosModel.isNameEmailExist(request.getParameter("nickname"), request.getParameter("email"))) {
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
        //    System.out.println(e);
            throw new ServerException(e.toString());
        }
    }

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
        if (!tacosModel.isNameEmailExist(testNewName, testNewEmail)) {
            response.sendRedirect("Tacos?cmd=account&mf1=true");
        } else {
            if (tacosModel.checkPassword(oldPassword, (curName + curEmail + oldPassword).hashCode())) {
                tacosModel.updateClient(curName, newName, newEmail, newPassword);
                response.sendRedirect("Tacos?cmd=unauth");
            } else {
                response.sendRedirect("Tacos?cmd=account&mf2=true");
            }
        }
    }


    private void registerClient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name, email, password;
        name = request.getParameter("nickname");
        email = request.getParameter("email");
        password = request.getParameter("password");
        String token = tacosModel.register(name, email, password);
        HttpSession session = request.getSession();
        session.setAttribute("tacos.token", token);
        response.sendRedirect("Tacos");
    }

    private void authorizeClient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = tacosModel.authorize(request.getParameter("email"), request.getParameter("password"));
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
