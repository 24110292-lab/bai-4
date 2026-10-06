package vn.iotstar.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jakarta.servlet.http.Part;

public class ProfileImageStore {
    public static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_PIXEL_COUNT = 20_000_000L;
    private final Path directory;

    public ProfileImageStore(Path directory) throws IOException {
        this.directory = directory.toAbsolutePath().normalize();
        Files.createDirectories(this.directory);
    }

    public String store(Part part) throws IOException, InvalidProfileImageException {
        if (part.getSize() > MAX_IMAGE_SIZE) {
            throw new InvalidProfileImageException("Ảnh đại diện không được vượt quá 5 MB.");
        }

        Path temporaryFile = Files.createTempFile(directory, "profile-", ".upload");
        try {
            try (InputStream input = part.getInputStream()) {
                Files.copy(input, temporaryFile, StandardCopyOption.REPLACE_EXISTING);
            }
            String extension = validateImage(temporaryFile);
            String filename = UUID.randomUUID() + extension;
            Files.move(temporaryFile, resolve(filename), StandardCopyOption.ATOMIC_MOVE);
            return filename;
        } catch (IOException | RuntimeException exception) {
            Files.deleteIfExists(temporaryFile);
            throw exception;
        }
    }

    public Path resolve(String filename) {
        if (filename == null || !filename.matches(
                "^[0-9a-fA-F-]{36}\\.(png|jpg|gif)$")) {
            throw new IllegalArgumentException("Invalid profile image name.");
        }
        Path resolved = directory.resolve(filename).normalize();
        if (!resolved.getParent().equals(directory)) {
            throw new IllegalArgumentException("Invalid profile image path.");
        }
        return resolved;
    }

    public void delete(String filename) throws IOException {
        if (filename != null) {
            Files.deleteIfExists(resolve(filename));
        }
    }

    private String validateImage(Path file) throws IOException, InvalidProfileImageException {
        try (ImageInputStream imageInput = ImageIO.createImageInputStream(file.toFile())) {
            if (imageInput == null) {
                throw new InvalidProfileImageException("Tệp tải lên không phải ảnh PNG, JPEG hoặc GIF hợp lệ.");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new InvalidProfileImageException("Tệp tải lên không phải ảnh PNG, JPEG hoặc GIF hợp lệ.");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, true, true);
                String format = reader.getFormatName().toLowerCase(Locale.ROOT);
                String extension = switch (format) {
                    case "png" -> ".png";
                    case "jpeg", "jpg" -> ".jpg";
                    case "gif" -> ".gif";
                    default -> throw new InvalidProfileImageException("Chỉ chấp nhận ảnh PNG, JPEG hoặc GIF.");
                };
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || (long) width * height > MAX_PIXEL_COUNT) {
                    throw new InvalidProfileImageException("Kích thước ảnh vượt quá giới hạn cho phép.");
                }
                BufferedImage decoded = reader.read(0);
                if (decoded == null) {
                    throw new InvalidProfileImageException("Tệp tải lên không phải ảnh hợp lệ.");
                }
                decoded.flush();
                return extension;
            } catch (InvalidProfileImageException exception) {
                throw exception;
            } catch (IOException exception) {
                throw new InvalidProfileImageException("Không thể đọc ảnh tải lên.");
            } finally {
                reader.dispose();
            }
        }
    }

    public static final class InvalidProfileImageException extends IOException {
        private static final long serialVersionUID = 1L;

        public InvalidProfileImageException(String message) {
            super(message);
        }
    }
}
