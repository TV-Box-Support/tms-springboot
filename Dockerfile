FROM openjdk:11
ADD target/tms-server-docker-ec2.jar tms-server-docker-ec2.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","tms-server-docker-ec2.jar"]