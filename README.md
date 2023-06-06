# TERMINAL MANAGEMENT SYSTEM
#### The set-top box terminal management system is the university graduation project of student Nguyen Thanh Chung - School of Electrical and Electronics Engineering of Hanoi University of Science and Technology.
###### If you have any questions, you can contact me via mail: chunhthanhde.dev@gmail.com
```
Dear programer

when I wrote this code, only god
and I knew how it worked
Now, only god know it

Therefore, if you are trying to optimize
this routine and it fails (most surely),
please increase this counter as a
warning for the next person :(

total_hours_wasted_here = 230
 
 
Best regards

ChunhThanhDe
```
## Configure Spring Datasource, JPA, App properties
Open `src/main/resources/application.properties`
- For PostgreSQL:
```
spring.datasource.url= jdbc:postgresql://localhost:3306/{name_database}
spring.datasource.username= postgres
spring.datasource.password= password

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto= update

# App Properties
TMS.app.jwtSecret=ChunhthanhdeSecretKey
TMS.app.jwtExpirationMs=86400000
```
- For MySQL
```
spring.datasource.url= jdbc:mysql://localhost:3306/{name_database}
spring.datasource.username= root
spring.datasource.password= {root_password}

spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto= update

#if hosting by ubuntu
#spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# App Properties
TMS.app.jwtSecret=ChunhthanhdeSecretKey
TMS.app.jwtExpirationMs=86400000
```
## Run Spring Boot application
```
mvn spring-boot:run
```
## Run following SQL insert statements
```
INSERT INTO role (name) VALUES('ROLE_USER');
INSERT INTO role (name) VALUES('ROLE_MODERATOR');
INSERT INTO role (name) VALUES('ROLE_ADMIN');

INSERT INTO listdevice(name, location) VALUES('all', 'all');
INSERT INTO listdevice(name, location) VALUES('prototype', 'all');
INSERT INTO listdevice(name, location) VALUES('An Giang', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Bà Rịa - Vũng Tàu', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Bắc Giang', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Bắc Kạn', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Bạc Liêu', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Bắc Ninh', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Bến Tre', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Bình Định', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Bình Dương', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Bình Phước', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Bình Thuận', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Cà Mau', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Cần Thơ', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Cao Bằng', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Đà Nẵng', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Đắk Lắk', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Đắk Nông', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Điện Biên', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Đồng Nai', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Đồng Tháp', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Gia Lai', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Hà Giang', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Hà Nam', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Hà Nội', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Hà Tĩnh', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Hải Dương', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Hải Phòng', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Hậu Giang', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Hòa Bình', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Hưng Yên', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Khánh Hòa', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Kiên Giang', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Kon Tum', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Lai Châu', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Lâm Đồng', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Lạng Sơn', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Lào Cai', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Long An', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Nam Định', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Nghệ An', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Ninh Bình', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Ninh Thuận', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Phú Thọ','Bắc');
INSERT INTO listdevice(name, location) VALUES('Phú Yên', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Quảng Bình', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Quảng Nam', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Quảng Ngãi', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Quảng Ninh', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Quảng Trị', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Sóc Trăng', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Sơn La', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Tây Ninh', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Thái Bình', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Thái Nguyên', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Thanh Hóa', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Thừa Thiên Huế', 'Trung');
INSERT INTO listdevice(name, location) VALUES('Tiền Giang', 'Nam');
INSERT INTO listdevice(name, location) VALUES('TP. Hồ Chí Minh', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Trà Vinh', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Tuyên Quang', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Vĩnh Long', 'Nam');
INSERT INTO listdevice(name, location) VALUES('Vĩnh Phúc', 'Bắc');
INSERT INTO listdevice(name, location) VALUES('Yên Bái', 'Bắc');


INSERT INTO listdevice_user(device_id, user_id) VALUES("1","1");

INSERT INTO user(name, username, password, company, email, contact) VALUES ("MODERATOR","root","$2a$10$icvl6beFqq9A9yL8Th5xGuhaM8dRkAFQOLgLXwJv.6SUX3lqOPWXu","none","none",0);
INSERT INTO role_user(user_id, rule_id) VALUES (1, 1); 
INSERT INTO role_user(user_id, rule_id) VALUES (1, 2); 
INSERT INTO role_user(user_id, rule_id) VALUES (1, 3);
```
## Moderator account
```
username: root
password: root
```

