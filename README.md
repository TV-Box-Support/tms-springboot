# TMS - Terminal Management System for AOSP and ATV Set-top Box product lines

The set-top box terminal management system is the University Graduation Project of student Chung Nguyen Thanh - School of Electrical and Electronics Engineering of Hanoi University of Science and Technology.

Show some ❤️ and star ⭐ the repo if you liked it.

###### contact for work, mail: chunhthanhde.dev@gmail.com

<p align="center">
  <img src="media/logo/box.png" height="100px" style="margin-right: 30px;" />
  <img src="media/logo/database-management.png" height="100px" style="margin-right: 30px;" />
  <img src="media/logo/aosp-atv.png" height="120px" />
</p>

![GitHub stars](https://img.shields.io/github/stars/chunhthanhde/Food-Restaurant-UI?style=social)
![GitHub forks](https://img.shields.io/github/forks/chunhthanhde/Food-Restaurant-UI?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/chunhthanhde/Food-Restaurant-UI?style=social)

<a href="https://www.linkedin.com/in/chunhthanhde/">
  <img src="https://img.shields.io/badge/Support-Recommend%2FEndorse%20me%20on%20Linkedin-blue?style=for-the-badge&logo=linkedin" alt="Support me on LinkedIn" />
</a>

<br>

<a href="https://www.buymeacoffee.com/chunhthanhde" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/yellow_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

This source code is the back end of the TMS Project. For security reasons, I cannot publish the application source code of this system. The following link is the source code of the web client interface for TMS Smart Box device managers: https://github.com/ChunhThanhDe/tms-reactjs

## 🗂️ Overview

Previously, the Set-top box (STB) was known as a device that acted as an intermediary between the signal source and the
TV. Its function was to decode television signals and convert them into audio and visual content displayed on the TV
screen.

Today, the STB is also known as a Smart Box, which can transform a regular TV into a Smart TV with various entertainment
features. These features include television services, movie streaming, gaming, web browsing, and access to social
networks (karaoke, online learning, etc.). Additionally, the Smart Box can integrate with other Internet of Things (IoT)
devices to create a complete home ecosystem.

For current SMB device providers, deploying, operating, managing, and troubleshooting the devices already in use on the
market with millions of products is a resource-intensive task. Resources, both human and technical, are required here.

A large-scale system without a technical management system would require a significant number of technical support
personnel while also investing in equipment and training programs for the workforce. However, even with these measures,
carrying out tasks through multiple intermediate steps may not achieve maximum efficiency. Therefore, SMB device
providers need a comprehensive management system capable of handling most user support tasks through Over-the-Air (OTA)
updates. Additionally, the system should collect device information and user device usage data. With this information,
the management team can generate reports and statistics to evaluate the performance, efficiency, and quality of the
current product, as well as consumer habits. Based on these insights, software updates or hardware solutions can be
implemented in the future.

<div style="display: flex; justify-content: center;">
  <div style="border-radius: 20px; overflow: hidden; padding-bottom: 10px;">
    <img src="media/img/System_Model.png" style="max-height: 300px;">
  </div>
</div>

<p align="center"><strong>System Model</strong></p>

Using the TMS management system will address the aforementioned requirements automatically, quickly, and accurately.
While there are existing TMS systems in the market, they have not been widely adopted for SMB products or specific
industries. Due to the aforementioned challenges and my interest in SMB technology, I have chosen this topic for my
graduation project, with the aim of building an efficient and user-friendly system.

## 🌟 Features

The TMS includes the following features:

- User management (Manage accounts allowed to access the system)
- Smart box device management (Device parameters, location, usage time,...)
- Application Management (Manage system applications and user applications)
- Monitor and track device operating history (Monitor real-time device uptime, application usage history, performance
  parameters)
- Manage Operational Policies (Create and deploy operational policies for devices including reboot, automatically
  download and install applications, uninstall applications, display notifications, warnings, and advertisements, and deploy
  support after-sales support,...)

###### If you have any questions, you can contact me via mail: chunhthanhde.dev@gmail.com

## 💡 Getting started

A few words before you configure the project according to the instructions below:

```
Dear programmer

when I wrote this code, only god
and I knew how it worked
Now, only god knows it

Therefore, if you are trying to optimize
this routine and it fails (most surely),
please increase this counter as a
warning for the next person :(

total_hours_wasted_here = 530
 
 
Best regards

ChunhThanhDe
```

<div align="center">

###### *I'm just joking, if you have any questions, feel free to ask me. 👯*

</div>

## 📺 Configure Spring Datasource, JPA, App properties

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

## 🍃 Run Spring Boot application

```
mvn spring-boot:run
```

## 📊 Run following SQL insert statements

```
INSERT INTO role (name) VALUES('ROLE_USER');
INSERT INTO role (name) VALUES('ROLE_MODERATOR');
INSERT INTO role (name) VALUES('ROLE_ADMIN');

INSERT INTO listdevice(name, location) VALUES('all', 'all');
INSERT INTO listdevice(name, location) VALUES('prototype', 'all');

INSERT INTO user(name, username, password, company, email, contact) VALUES ("MODERATOR","root","$2a$10$icvl6beFqq9A9yL8Th5xGuhaM8dRkAFQOLgLXwJv.6SUX3lqOPWXu","none","none",0);
INSERT INTO role_user(user_id, rule_id) VALUES (1, 1); 
INSERT INTO role_user(user_id, rule_id) VALUES (1, 2); 
INSERT INTO role_user(user_id, rule_id) VALUES (1, 3);

INSERT INTO listdevice_user(device_id, user_id) VALUES("1","1");
```

## ⛑️ Moderator account

```
username: root
password: root
```

## Send a 💝 Thanks to 

First of all, I would like to thank the HEC Electronic Technology Center of 🏢 VNPT Technology Company for creating
conditions for me to build and develop this topic on the company's SmartBox 3 device. I would also like to thank 💝 [Nguyen
Vinh Khang](https://github.com/NguyenVinhKhang) for helping me develop the Web client source code for the system and 💝 Le Thu An for working with me to build a
Business Analyst for the project. ❤️ 


