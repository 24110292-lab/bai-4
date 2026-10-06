package vn.iotstar.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import vn.iotstar.form.LoginForm;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.FormValidation;
import vn.iotstar.util.SessionSupport;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private Validator validator;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
        validator = (Validator) getServletContext().getAttribute("validator");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("form", new LoginForm());
        request.setAttribute("errors", Map.of());
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginForm form = new LoginForm();
        form.setUsername(request.getParameter("username"));
        form.setPassword(request.getParameter("password"));
        Map<String, String> errors = FormValidation.validate(validator, form);
        if (!errors.isEmpty()) {
            forwardForm(request, response, form, errors);
            return;
        }

        User user = userService.authenticate(form.getUsername(), form.getPassword());
        if (user == null) {
            request.setAttribute("loginError", "Tên đăng nhập hoặc mật khẩu không chính xác.");
            forwardForm(request, response, form, errors);
            return;
        }
        SessionSupport.signIn(request, user);
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private void forwardForm(HttpServletRequest request, HttpServletResponse response,
            LoginForm form, Map<String, String> errors) throws ServletException, IOException {
        request.setAttribute("form", form);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }
}
