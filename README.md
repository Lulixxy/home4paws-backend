# 🐾 Home4Paws (Backend API)

**Home4Paws Backend** คือ RESTful API Service ที่ทำงานเบื้องหลังแอปพลิเคชัน Home4Paws (Mobile App) ทำหน้าที่จัดการ Business Logic, การยืนยันตัวตน, และการจัดการข้อมูลในฐานข้อมูลทั้งหมด

📌 **Project Status:** Senior Project (Information Technology, Maejo University)

---

## 🏗️ Project Structure
โครงสร้างโปรเจ็คจัดทำตามรูปแบบ **Layered Architecture** เพื่อแยกส่วนรับข้อมูล, Business Logic, และการจัดการข้อมูลออกจากกัน:
```
src/main/java/com/home4paws 
├── 📂 controller # REST Controllers (API Endpoints) 
├── 📂 service # Business Logic & Transaction Management 
├── 📂 repository # Data Access Layer (JPA/Hibernate) 
├── 📂 model # JPA Entities (Database Models) 
└── 📂 dto # Data Transfer Objects (Request/Response Models) 
```
---

## 🛠️ Tech Stack
* **Language:** Java
* **Framework:** [Spring Boot](https://spring.io/projects/spring-boot) (Web, Security, JPA)
* **Database:** MySQL
* **Build Tool:** Maven

---

## 🔌 API Features
ระบบ Backend ให้บริการ API endpoints ครอบคลุมการทำงานตาม Use Case ดังนี้:

### 🔐 Authentication & Users
* **Preview Posts:** ดูโพสต์ประกาศหาบ้านของสัตว์ที่สถานะพร้อมหาบ้าน
* **Register/Login:** ระบบสมัครสมาชิกและยืนยันตัวตน (JWT/Session)
* **User Profile:** จัดการข้อมูลส่วนตัวและบทบาทผู้ใช้ (Adopter/Owner/Admin)

### 🐶 Animal Management (สำหรับ Owner/Shelter)
* **Post Animal:** ลงทะเบียนสัตว์เลี้ยงเพื่อหาบ้าน (พร้อมสถานะ Available)
* **Update Status:** อัปเดตข้อมูลและสถานะของสัตว์ (Adopted/Available)
* **Search & Filter:** ระบบค้นหาสัตว์เลี้ยงตามสายพันธุ์, อายุ, และจังหวัด

### 🏠 Adoption Process
* **Request Handling:** จัดการคำขอรับเลี้ยง (ส่งคำขอ, อนุมัติ, ปฏิเสธ)
* **Tracking System:** ระบบติดตามหลังการรับเลี้ยง
    * **Handover:** บันทึกการส่งมอบสัตว์
    * **Monitoring:** รับข้อมูลอัปเดตความเป็นอยู่ (Update Animal's Life) จาก Adopter

### 🛡️ Admin Controls
* **Content Moderation:** ตรวจสอบและลบโพสต์ที่ไม่เหมาะสม
* **User Ban:** ระงับการใช้งานผู้ใช้ที่ทำผิดกฎ
* **Review System:** จัดการรีวิวและการให้คะแนนระหว่างผู้ใช้

---

## 🗄️ Database Design

<img width="984" height="1037" alt="Final_ER-Diagram" src="https://github.com/user-attachments/assets/83494b8c-9488-4c31-87a5-0b66dad99e48" />

---

## 🖥️ Web Admin Panel
ระบบมาพร้อมกับ **Back-office Dashboard** สำหรับผู้ดูแลระบบ (Admin) เพื่อจัดการข้อมูลต่างๆ ผ่านหน้าเว็บเบราว์เซอร์ โดยไม่ต้องยิง API เอง

**Features:**
* **Admin Login:** หน้าเข้าสู่ระบบสำหรับแอดมินโดยเฉพาะ
* **Dashboard:** ดูภาพรวมสถิติของระบบ
* **User Management:** ตรวจสอบและแบนผู้ใช้งานที่ทำผิดกฎ
* **Content Moderation:** อนุมัติหรือลบโพสต์ที่ไม่เหมาะสม

**🚀 How to Access:**
เมื่อรัน Server (`mvn spring-boot:run`) เสร็จแล้ว สามารถเข้าใช้งานได้ที่:
👉 **URL:** [http://localhost:9090/admin/login](http://localhost:9090/admin/login)

*(💡 Note: อย่าลืมตั้งค่า `server.port=9090` ในไฟล์ application.properties ด้วยนะคะ)*

---

## ⚙️ Setup & Installation

### 1. Prerequisites
* Java Development Kit (JDK) 17 หรือใหม่กว่า
* MySQL Database
* Maven

### 2. Database Configuration
สร้าง Database ชื่อ `home4paws_db` (หรือชื่อตามที่ตั้งไว้) และแก้ไขค่าในไฟล์ `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/home4paws_db?useSSL=false
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```
### 3. Run the Application
เปิด Terminal หรือ Command Prompt ในโฟลเดอร์โปรเจ็ค แล้วรันคำสั่ง:
```Bash
mvn spring-boot:run
เมื่อ Server รันสำเร็จ API จะทำงานที่: http://localhost:8080
```
---

## 🔗 Related Repository
โปรเจ็คนี้ทำงานร่วมกับ Mobile Application (Frontend) สามารถดู Source Code ฝั่งหน้าบ้านได้ที่: 👉 Mobile Repository: https://github.com/Lulixxy/home4paws-mobile

---

## 👩‍💻 Author
**Kulissara S. (Lulixxy)**

Github: [Lulixxy](https://github.com/Lulixxy)
