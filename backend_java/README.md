# Attendance System - Java Spring Boot Backend

Backend API cho hệ thống quản lý chấm công nhân viên, xây dựng bằng Spring Boot 3.1.5 và MySQL.

## Cấu trúc dự án

```
backend_java/
├── pom.xml                 # Maven configuration
├── src/main/java/
│   └── com/attendance/
│       ├── AttendanceSystemApplication.java
│       ├── config/         # Configuration classes
│       ├── controller/     # REST Controllers (11 modules)
│       ├── service/        # Business Logic (15 services)
│       ├── repository/     # Database Access (15 repositories)
│       ├── model/          # JPA Entities (15 entities)
│       ├── dto/            # Data Transfer Objects (15 DTOs)
│       ├── security/       # JWT & Security
│       └── exception/      # Exception Handling
└── src/main/resources/
    └── application.properties
```

## Yêu cầu

- Java 17 trở lên
- Maven 3.6+
- MySQL 8.0+

## Thiết lập cơ sở dữ liệu

```sql
CREATE DATABASE attendance_db;
USE attendance_db;
```

## Cấu hình

Chỉnh sửa `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

jwt.secret=your_very_long_secret_key_here_min_256_bits_long
jwt.expiration=86400000
```

## Chạy dự án

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

API sẽ chạy tại: `http://localhost:8080/api/v1`

## API Endpoints

### Authentication (Đăng nhập)
- `POST /auth/login` - Đăng nhập (Email + Password)
- `POST /auth/validate-token` - Kiểm tra token hợp lệ
- `GET /auth/profile` - Lấy profile (cần token)

### Employee (Nhân viên)
- `GET /employees` - Lấy danh sách nhân viên
- `GET /employees/{id}` - Lấy thông tin nhân viên
- `GET /employees/email/{email}` - Tìm nhân viên theo email
- `POST /employees` - Tạo nhân viên mới
- `PUT /employees/{id}` - Cập nhật thông tin nhân viên
- `DELETE /employees/{id}` - Xóa nhân viên

### Shift (Ca làm việc)
- `GET /shifts` - Danh sách ca làm việc
- `GET /shifts/{id}` - Chi tiết ca
- `POST /shifts` - Tạo ca làm việc
- `PUT /shifts/{id}` - Cập nhật ca
- `DELETE /shifts/{id}` - Xóa ca

### Attendance (Chấm công)
- `GET /attendance/employee/{employeeId}` - Lịch sử chấm công
- `GET /attendance/{id}` - Chi tiết chấm công
- `POST /attendance/check-in/{employeeId}` - Check in
- `POST /attendance/check-out/{employeeId}` - Check out
- `POST /attendance` - Tạo bản ghi chấm công
- `PUT /attendance/{id}` - Cập nhật chấm công
- `DELETE /attendance/{id}` - Xóa bản ghi chấm công

### Absence (Xin phép)
- `GET /absences/employee/{employeeId}` - Danh sách xin phép
- `GET /absences/employee/{employeeId}/approved` - Danh sách xin phép được phê duyệt
- `GET /absences/{id}` - Chi tiết xin phép
- `GET /absences/status/{status}?startDate=&endDate=` - Xin phép theo trạng thái
- `POST /absences` - Tạo đơn xin phép
- `PUT /absences/{id}` - Cập nhật đơn xin phép
- `DELETE /absences/{id}` - Xóa đơn xin phép

### Overtime (Tăng ca)
- `GET /overtime-requests/employee/{employeeId}` - Danh sách tăng ca
- `GET /overtime-requests/{id}` - Chi tiết tăng ca
- `GET /overtime-requests/status/{status}?startDate=&endDate=` - Tăng ca theo trạng thái
- `POST /overtime-requests` - Tạo đơn tăng ca
- `PUT /overtime-requests/{id}` - Cập nhật tăng ca
- `DELETE /overtime-requests/{id}` - Xóa tăng ca

### Payroll (Bảng lương)
- `GET /payroll/employee/{employeeId}` - Lịch sử bảng lương
- `GET /payroll/{id}` - Chi tiết bảng lương
- `GET /payroll/status/{status}` - Bảng lương theo trạng thái
- `POST /payroll` - Tạo bảng lương
- `PUT /payroll/{id}` - Cập nhật bảng lương
- `DELETE /payroll/{id}` - Xóa bảng lương

### Notification (Thông báo)
- `GET /notifications/employee/{employeeId}` - Danh sách thông báo
- `GET /notifications/employee/{employeeId}/unread` - Thông báo chưa đọc
- `GET /notifications/employee/{employeeId}/unread-count` - Số thông báo chưa đọc
- `GET /notifications/{id}` - Chi tiết thông báo
- `POST /notifications` - Tạo thông báo
- `PUT /notifications/{id}/read` - Đánh dấu đã đọc
- `DELETE /notifications/{id}` - Xóa thông báo

### Daily Work Report (Báo cáo công việc hàng ngày)
- `GET /daily-reports/employee/{employeeId}` - Danh sách báo cáo
- `GET /daily-reports/{id}` - Chi tiết báo cáo
- `GET /daily-reports/employee/{employeeId}/date/{workDate}` - Báo cáo theo ngày
- `POST /daily-reports` - Tạo báo cáo
- `PUT /daily-reports/{id}` - Cập nhật báo cáo
- `DELETE /daily-reports/{id}` - Xóa báo cáo

### Vacation (Nghỉ phép)
- `GET /vacations/employee/{employeeId}` - Danh sách nghỉ phép
- `GET /vacations/{id}` - Chi tiết nghỉ phép
- `GET /vacations/status/{status}?startDate=&endDate=` - Nghỉ phép theo trạng thái
- `POST /vacations` - Đăng ký nghỉ phép
- `PUT /vacations/{id}` - Cập nhật nghỉ phép
- `DELETE /vacations/{id}` - Xóa nghỉ phép

### Shift Change Request (Yêu cầu đổi ca)
- `GET /shift-change-requests/employee/{employeeId}` - Danh sách yêu cầu đổi ca
- `GET /shift-change-requests/{id}` - Chi tiết yêu cầu
- `GET /shift-change-requests/status/{status}` - Yêu cầu theo trạng thái
- `POST /shift-change-requests` - Tạo yêu cầu đổi ca
- `PUT /shift-change-requests/{id}` - Cập nhật yêu cầu
- `DELETE /shift-change-requests/{id}` - Xóa yêu cầu

### Attendance Correction (Sửa chấm công)
- `GET /attendance-corrections/employee/{employeeId}` - Danh sách sửa chấm công
- `GET /attendance-corrections/{id}` - Chi tiết sửa
- `GET /attendance-corrections/status/{status}` - Sửa theo trạng thái
- `POST /attendance-corrections` - Tạo yêu cầu sửa
- `PUT /attendance-corrections/{id}` - Cập nhật yêu cầu sửa
- `DELETE /attendance-corrections/{id}` - Xóa yêu cầu sửa

### Employee Benefits (Phúc lợi nhân viên)
- `GET /employee-benefits/employee/{employeeId}` - Danh sách phúc lợi
- `GET /employee-benefits/{id}` - Chi tiết phúc lợi
- `POST /employee-benefits` - Thêm phúc lợi
- `PUT /employee-benefits/{id}` - Cập nhật phúc lợi
- `DELETE /employee-benefits/{id}` - Xóa phúc lợi

### Monthly Report (Báo cáo tháng)
- `GET /monthly-reports/employee/{employeeId}` - Danh sách báo cáo
- `GET /monthly-reports/{id}` - Chi tiết báo cáo
- `GET /monthly-reports/employee/{employeeId}/period?month=&year=` - Báo cáo theo tháng
- `GET /monthly-reports/period?year=&month=` - Báo cáo tất cả nhân viên
- `POST /monthly-reports` - Tạo báo cáo
- `PUT /monthly-reports/{id}` - Cập nhật báo cáo
- `DELETE /monthly-reports/{id}` - Xóa báo cáo

### System Settings (Cài đặt hệ thống)
- `GET /settings` - Danh sách cài đặt
- `GET /settings/{id}` - Chi tiết cài đặt
- `GET /settings/key/{key}` - Lấy cài đặt theo key
- `POST /settings` - Tạo cài đặt
- `PUT /settings/{id}` - Cập nhật cài đặt
- `DELETE /settings/{id}` - Xóa cài đặt

### Timesheet Period Control (Kiểm soát kỳ chấm công)
- `GET /timesheet-periods` - Danh sách kỳ
- `GET /timesheet-periods/{id}` - Chi tiết kỳ
- `GET /timesheet-periods/status/{status}` - Kỳ theo trạng thái
- `POST /timesheet-periods` - Tạo kỳ chấm công
- `PUT /timesheet-periods/{id}` - Cập nhật kỳ
- `DELETE /timesheet-periods/{id}` - Xóa kỳ

## Cấu trúc dữ liệu (Database Schema)

### Models tạo được:
1. Employee - Nhân viên
2. Shift - Ca làm việc
3. AttendanceLog - Bản ghi chấm công
4. Absence - Xin phép
5. OvertimeRequest - Tăng ca
6. Payroll - Bảng lương
7. Notification - Thông báo
8. DailyWorkReport - Báo cáo công việc hàng ngày
9. Vacation - Nghỉ phép
10. ShiftChangeRequest - Yêu cầu đổi ca
11. AttendanceCorrection - Sửa chấm công
12. EmployeeBenefitLog - Phúc lợi nhân viên
13. MonthlyReport - Báo cáo tháng
14. SystemSetting - Cài đặt hệ thống
15. TimesheetPeriodControl - Kiểm soát kỳ chấm công

## Development Guide

### Thêm Model mới

1. **Tạo Entity** trong `model/`:
```java
@Entity
@Table(name = "table_name")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEntity {
    // fields...
}
```

2. **Tạo DTO** trong `dto/`:
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEntityDTO {
    // fields...
}
```

3. **Tạo Repository** trong `repository/`:
```java
@Repository
public interface NewEntityRepository extends JpaRepository<NewEntity, Integer> {
    // custom queries...
}
```

4. **Tạo Service** trong `service/`:
```java
@Service
public class NewEntityService {
    @Autowired
    private NewEntityRepository repository;
    
    // business methods...
}
```

5. **Tạo Controller** trong `controller/`:
```java
@RestController
@RequestMapping("/new-entities")
@CrossOrigin(origins = "*")
public class NewEntityController {
    @Autowired
    private NewEntityService service;
    
    // REST endpoints...
}
```

## Technologies Used

- **Framework**: Spring Boot 3.1.5
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Security**: Spring Security + JWT
- **API Documentation**: REST API
- **Password Encoding**: BCrypt
- **Mapper**: ModelMapper

## Notes

- JWT tokens có thời hạn 24 giờ
- Tất cả endpoints hỗ trợ CORS
- Password được mã hóa bằng BCrypt
- Soft delete cho employees (using isActive flag)
- Lỗi được xử lý qua GlobalExceptionHandler
