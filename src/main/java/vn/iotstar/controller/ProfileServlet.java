package vn.iotstar.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.validation.Validator;
import vn.iotstar.form.ProfileForm;
import vn.iotstar.model.User;
import vn.iotstar.service.ProfileImageStore;
import vn.iotstar.service.ProfileImageStore.InvalidProfileImageException;
import vn.iotstar.service.UserService;
import vn.iotstar.util.FormValidation;
import vn.iotstar.util.SessionSupport;

@WebServlet("/profile")
@MultipartConfig(maxFileSize = 6L * 1024 * 1024, maxRequestSize = 7L * 1024 * 1024)
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());
    private UserService userService;
    private ProfileImageStore imageStore;
    private Validator validator;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
        imageStore = (ProfileImageStore) getServletContext().getAttribute("profileImageStore");
        validator = (Validator) getServletContext().getAttribute("validator");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getSignedInUser(request, response);
        if (user == null) {
            return;
        }
        ProfileForm form = new ProfileForm();
        form.setFullname(user.getFullname());
        form.setPhone(user.getPhone());
        forwardForm(request, response, user, form, Map.of());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getSignedInUser(request, response);
        if (currentUser == null) {
            return;
        }

        ProfileForm form = new ProfileForm();
        form.setFullname(request.getParameter("fullname"));
        form.setPhone(request.getParameter("phone"));
        Map<String, String> errors = FormValidation.validate(validator, form);
        Part imagePart = request.getPart("avatar");
        boolean hasNewImage = imagePart != null && imagePart.getSize() > 0;

        if (!errors.isEmpty()) {
            forwardForm(request, response, currentUser, form, errors);
            return;
        }

        String newImageFile = null;
        try {
            if (hasNewImage) {
                newImageFile = imageStore.store(imagePart);
            }
        } catch (InvalidProfileImageException exception) {
            errors.put("avatar", exception.getMessage());
            forwardForm(request, response, currentUser, form, errors);
            return;
        }

        User updatedUser;
        try {
            updatedUser = userService.updateProfile(
                    currentUser.getId(), form.getFullname(), form.getPhone(), newImageFile);
        } catch (RuntimeException exception) {
            deleteUploadedFile(newImageFile);
            throw exception;
        }
        request.getSession().setAttribute(
                SessionSupport.CURRENT_USER, SessionSupport.sessionUser(updatedUser));
        if (newImageFile != null) {
            deleteUploadedFile(currentUser.getImageFile());
        }
        response.sendRedirect(request.getContextPath() + "/profile?updated=true");
    }

    private User getSignedInUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        Object userIdValue = session == null ? null : session.getAttribute(SessionSupport.USER_ID);
        if (!(userIdValue instanceof Long userId)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        User user = userService.findById(userId);
        if (user == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
        }
        return user;
    }

    private void forwardForm(HttpServletRequest request, HttpServletResponse response,
            User user, ProfileForm form, Map<String, String> errors) throws ServletException, IOException {
        request.setAttribute("user", user);
        request.setAttribute("form", form);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    private void deleteUploadedFile(String filename) {
        if (filename == null) {
            return;
        }
        try {
            imageStore.delete(filename);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Could not remove an unused profile image.", exception);
        }
    }
}
