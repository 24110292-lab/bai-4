package vn.iotstar.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterForm {
    @NotBlank(message = "Tên đăng nhập không được để trống.")
    @Pattern(regexp = "^[A-Za-z0-9._-]{3,50}$", message = "Tên đăng nhập phải có 3-50 ký tự, chỉ gồm chữ, số, dấu chấm, gạch dưới hoặc gạch ngang.")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Pattern(regexp = "^[\\x20-\\x7E]{8,60}$",
            message = "Mật khẩu phải gồm 8-60 ký tự ASCII hiển thị.")
    private String password;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu.")
    private String confirmPassword;

    @NotBlank(message = "Họ và tên không được để trống.")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự.")
    private String fullname;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^[+0-9][0-9 .()\\-]{7,19}$", message = "Số điện thoại không hợp lệ.")
    private String phone;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
