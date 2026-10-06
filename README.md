# Bài tập 03 - Hồ sơ người dùng

Ứng dụng Servlet/JSP dùng JPA (Hibernate) và SQL Server. Các trang đăng nhập,
đăng ký và cập nhật hồ sơ dùng chung một decorator Bootstrap qua Sitemesh 3.

## Yêu cầu chạy

- JDK 17
- Maven 3.8+
- Tomcat 10.1+ (Jakarta Servlet 6)
- SQL Server đang chạy; ứng dụng tự tạo database `Bai4DB` nếu chưa có

## Cấu hình SQL Server

`src/main/resources/META-INF/persistence.xml` chứa JDBC driver, dialect, URL
mặc định `localhost:1433/Bai4DB` và thông tin đăng nhập SQL Server cục bộ
`sa`/`123` dùng cho môi trường bài tập. Khi ứng dụng khởi động, nó kết nối vào
database `master`, tạo `Bai4DB` nếu chưa tồn tại, sau đó Hibernate tự tạo/cập
nhật bảng `Users`. Login SQL Server phải được phép kết nối tới `master` và có
quyền `CREATE DATABASE`. Biến môi trường dưới đây là tùy chọn để ghi đè cấu
hình mặc định:

```powershell
$env:DB_USERNAME = "<sql-server-user>"
$env:DB_PASSWORD = "<sql-server-password>"
# Tùy chọn: ghi đè JDBC URL mặc định
$env:DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=Bai4DB;encrypt=true;trustServerCertificate=true"
$env:DB_NAME = "Bai4DB"
# Tùy chọn: nơi lưu ảnh bên ngoài thư mục triển khai
$env:PROFILE_UPLOAD_DIR = "$env:USERPROFILE\Bai4-uploads"
mvn clean package
```

Khi chạy Tomcat từ Eclipse, nếu cần ghi đè cấu hình, mở **Servers**, nhấp đúp
Tomcat, chọn **Open launch configuration...**, mở tab **Environment**, rồi thêm
các biến cần thiết. Chọn **Apply** và khởi động lại Tomcat. Ứng dụng đọc các giá
trị ghi đè bằng `System.getenv`, vì vậy thêm chúng dưới dạng VM arguments
(`-DDB_USERNAME=...`) sẽ không thay thế được biến môi trường.

Triển khai `target/Bai_4.war` lên Tomcat. Hibernate tự cập nhật bảng `Users`
theo cấu hình `hibernate.hbm2ddl.auto=update`.

## Luồng chức năng

- `/register`: tạo tài khoản; mật khẩu được băm BCrypt.
- `/login` và `/logout`: đăng nhập/đăng xuất bằng session.
- `/profile`: cập nhật họ tên, số điện thoại và ảnh PNG/JPEG/GIF (tối đa 5 MB).
- Ảnh được kiểm tra nội dung thực tế, giới hạn kích thước giải nén và đặt tên
  ngẫu nhiên; dữ liệu tên ảnh được lưu trong entity `User`.
- Validation chạy phía server bằng Jakarta Bean Validation; lỗi được hiển thị
  cạnh trường tương ứng.
- `WEB-INF/decorators/main.jsp` là template Bootstrap duy nhất, được áp dụng
  qua `WEB-INF/sitemesh3.xml`. TLD tương thích JSP được khai báo tại
  `WEB-INF/sitemesh3.tld` và đọc các property SiteMesh qua
  `vn.iotstar.tag.SiteMeshWriteTag`.

Bootstrap CSS được tải từ jsDelivr nên cần kết nối mạng khi mở giao diện.
