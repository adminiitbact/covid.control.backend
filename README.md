# covid.control.backend
Backend For covid control web portal

In order to run, create a spring boot run configuration from eclipse run configurations

setting up spring boot run config
Project > covid.control Main  > org.iitbact.covid.control Profile > dev

setting up env in run config
GOOGLE_APPLICATION_CREDENTIALS=path to firebase.json file

ENV=DEV
DB_connection : URL
DB_password : ***
DB_user : ***

After setting all the configurations, run the project as spring boot app

swagger documentation
http://localhost:8080/swagger-ui.html
