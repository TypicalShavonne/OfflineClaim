# OfflineClaim Plugin

**OfflineClaim** là một plugin Minecraft dành cho server, giúp người chơi nhận lại các vật phẩm được gửi cho họ khi họ không online. Plugin này cũng hỗ trợ ghi lại lịch sử giao dịch gửi và nhận vật phẩm để dễ dàng quản lý.

---

## 🌟 Tính năng chính
1. **Gửi vật phẩm khi người chơi offline**:
    - Cho phép gửi vật phẩm đến một người chơi ngay cả khi họ không online.
    - Vật phẩm sẽ được lưu vào cơ sở dữ liệu và có thể nhận lại sau.

2. **Nhận vật phẩm bị lưu trữ**:
    - Người chơi có thể sử dụng lệnh để nhận lại các vật phẩm được gửi trong lúc offline.

3. **Ghi lịch sử giao dịch**:
    - Tự động ghi lại thông tin chi tiết về các giao dịch gửi/nhận vật phẩm (UUID, loại vật phẩm, thời gian, hành động).

---

## 📥 Cài đặt
1. Tải file `OfflineClaim.jar`.
2. Đặt file vào thư mục `plugins/` trong server Minecraft của bạn.
3. Khởi động lại server.
4. Plugin sẽ tự động tạo các tệp và cơ sở dữ liệu cần thiết.

---

## 🔧 Hướng dẫn sử dụng

### **Các lệnh**

| **Lệnh**                          | **Mô tả**                                                         | **Quyền hạn cần thiết** |
|------------------------------------|-------------------------------------------------------------------|-------------------------|
| `/senditem <tên người chơi>`       | Gửi vật phẩm đang cầm trên tay tới một người chơi khác (kể cả offline). | `offlineclaim.admin`    |
| `/claim`                           | Nhận lại tất cả vật phẩm được gửi cho bạn khi bạn offline.         | Không yêu cầu quyền     |

### **Ví dụ sử dụng**

1. **Gửi vật phẩm cho người chơi khác**:
    - Hãy cầm vật phẩm bạn muốn gửi trên tay.
    - Nhập lệnh: `/senditem ThatCorona`.
    - Nếu **ThatCorona** offline, vật phẩm sẽ được lưu vào cơ sở dữ liệu để anh ta có thể nhận sau.

2. **Nhận lại vật phẩm**:
    - Khi online, nhập lệnh: `/claim`.
    - Tất cả các vật phẩm chưa nhận của bạn sẽ được chuyển vào túi đồ (inventory).

---

## 🛠 Cấu hình

### **Cấu hình tin nhắn**
Plugin hỗ trợ cấu hình các tin nhắn gửi tới người chơi. Sau khi khởi động lần đầu, file cấu hình sẽ được tạo tại:

## Sponsors

ej-technologies có JProfiler dùng rất ngon, tải đi b

[![JProfiler](doc/img/jprofiler_medium.png)](http://www.ej-technologies.com/products/jprofiler/overview.html)
