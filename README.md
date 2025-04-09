## Nhóm thực hiện
- 22110239 Nguyễn Hữu Thông
- 22110265 Nguyễn Ngô Ngọc Vân

## Tính năng chính
1. **Nghe nhạc**: Hỗ trợ phát nhạc khi báo thức kêu hoặc theo yêu cầu người dùng.
2. **Quản lý SMS và cuộc gọi**: Theo dõi tin nhắn và cuộc gọi đến, hỗ trợ nhắc nhở khi có cuộc gọi hoặc tin nhắn quan trọng.
3. **Lịch trình**: Tích hợp nhắc nhở công việc, sự kiện theo lịch trình cá nhân.
4. **Giám sát pin**: Theo dõi mức pin của thiết bị.

## Công nghệ sử dụng
- **Ngôn ngữ**: Java
- **Framework**: Android SDK
- **Thành phần chính**:
  - `Foreground Service`: Đảm bảo báo thức hoạt động ngay cả khi ứng dụng không hoạt động.
  - `BroadcastReceiver`: Lắng nghe và xử lý các sự kiện hệ thống như thời gian thay đổi hoặc thiết bị khởi động lại.
  - `AlarmManager`: Lên lịch báo thức chính xác.
  - `ContentProvider`: Quản lý và chia sẻ dữ liệu báo thức trong ứng dụng.

## Giao diện


## Cài đặt
1. Clone repo:
   ```sh
   git clone https://github.com/ngocvann/Self-Alarm.git
   ```
2. Mở project bằng **Android Studio**.
3. Chạy ứng dụng trên máy ảo hoặc thiết bị thật.

