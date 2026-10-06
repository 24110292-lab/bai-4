package vn.iotstar.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginForm {
    @NotBlank(message = "Tên đăng nhập không được để trống.")
    @Pattern(regexp = "^[A-Za-z0-9._-]{3,50}$", message = "Tên đăng nhập không hợp lệ.")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Pattern(regexp = "^[\\x20-\\x7E]{8,60}$", message = "Mật khẩu không hợp lệ.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
