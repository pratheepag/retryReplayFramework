# Retry Replay Framework for Spring Boot

# Pre-requisite

- JDK 17
- Maven 3.9.x
- Gmail Id and Google App Password to get retry replay notifications

# Build Project Using 
- Command `mvn clean install`

# Execute Using

#### Option 1:
```
#Set the Environment Variables
spring.mail.password=Google App Password
spring.mail.username=Gmail ID
spring.security.user.admin.password=admin123
spring.security.user.admin.username=admin
spring.security.user.user.password=user123
spring.security.user.user.username=user
```
Run the spring boot application

#### Option 2:
- Command `mvn -Dspring.mail.username=<<yourEmailHere>> -Dspring.mail.password=<<YourPasswordHere>> -Dspring.security.user.admin.password=<<adminPasswordHere>> -Dspring.security.user.admin.username=<<adminUsernameHere>> -Dspring.security.user.user.password=<<userPasswordHere>> -Dspring.security.user.user.username=<<usernameHere>> spring-boot:run`
