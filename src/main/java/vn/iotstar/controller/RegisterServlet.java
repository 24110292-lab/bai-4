package vn.iotstar.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import vn.iotstar.form.RegisterForm;
import vn.iotstar.service.UserService;
import vn.iotstar.service.UserService.DuplicateUsernameException;
import vn.iotstar.util.FormValidation;
import vn.iotstar.util.SessionSupport;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
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
        request.setAttribute("form", new RegisterForm());
        request.setAttribute("errors", Map.of());
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegisterForm form = new RegisterForm();
        form.setUsername(request.getParameter("username"));
        form.setPassword(request.getParameter("password"));
        form.setConfirmPassword(request.getParameter("confirmPassword"));
        form.setFullname(request.getParameter("fullname"));
        form.setPhone(request.getParameter("phone"));

        Map<String, String> errors = FormValidation.validate(validator, form);
        if (form.getPassword() != null && form.getConfirmPassword() != null
                && !form.getPassword().equals(form.getConfirmPassword())) {
            errors.put("confirmPassword", "Mật khẩu xác nhận không khớp.");
        }
        if (!errors.isEmpty()) {
            forwardForm(request, response, form, errors);
            return;
        }

        try {
            var user = userService.register(
                    form.getUsername(), form.getPassword(), form.getFullname(), form.getPhone());
            SessionSupport.signIn(request, user);
            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (DuplicateUsernameException exception) {
            errors.put("username", "Tên đăng nhập đã được sử dụng.");
            forwardForm(request, response, form, errors);
        }
    }

    private void forwardForm(HttpServletRequest request, HttpServletResponse response,
            RegisterForm form, Map<String, String> errors) throws ServletException, IOException {
        request.setAttribute("form", form);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }
}
