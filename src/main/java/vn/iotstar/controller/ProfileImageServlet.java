package vn.iotstar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.ProfileImageStore;

@WebServlet("/profile-image/*")
public class ProfileImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProfileImageStore imageStore;

    @Override
    public void init() {
        imageStore = (ProfileImageStore) getServletContext().getAttribute("profileImageStore");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String filename = pathInfo == null || pathInfo.length() < 2 ? null : pathInfo.substring(1);
        Path image;
        try {
            image = imageStore.resolve(filename);
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (!Files.isRegularFile(image)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = switch (image.getFileName().toString().substring(
                image.getFileName().toString().lastIndexOf('.') + 1)) {
            case "png" -> "image/png";
            case "jpg" -> "image/jpeg";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(image));
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Cache-Control", "private, max-age=3600");
        try (InputStream input = Files.newInputStream(image)) {
            input.transferTo(response.getOutputStream());
        }
    }
}
