# Backend for covid control web portal

# Setting up

In order to run, create a Spring boot run configuration from Eclipse (Run menu > Run Configurations...).

In the "Spring Boot" tab, set the following:  
Project > covid.control  
Main  > org.iitbact.cc.CovidControlApplication  
Profile > dev

In the "Environment" tab, add/set the following new variables:  
GOOGLE_APPLICATION_CREDENTIALS = path to firebase.json file  
ENV = dev  
DB_connection = URL  
DB_password = \*\*\*  
DB_user = \*\*\*

After setting the above configuration, run the project as Spring boot app.

# Swagger documentation
Open this link once your server is running - http://localhost:8080/swagger-ui.html
