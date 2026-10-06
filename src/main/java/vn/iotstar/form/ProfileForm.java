package vn.iotstar.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProfileForm {
    @NotBlank(message = "Họ và tên không được để trống.")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự.")
    private String fullname;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^[+0-9][0-9 .()\\-]{7,19}$", message = "Số điện thoại không hợp lệ.")
    private String phone;

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
