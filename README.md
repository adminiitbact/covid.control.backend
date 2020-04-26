# covid control

In order to run, create a spring boot run configuration from eclipse run configurations

## setting up spring boot run config
First time you Run>Run in Eclipse, you will get an option to set build configuration.
1) Select `Maven build...` (three dots) from the "Run As" dialogue which opens up.
2) In `Goals:` set `spring-boot:run`
3) In `Profiles:` set `dev`

## setting up env in run config
1) Request your onboarding buddy for firebase and database credentials. 
2) Set the following environment variables :
    - `GOOGLE_APPLICATION_CREDENTIALS`=path to firebase.json file
    - `ENV`=`DEV`
    - `DB_connection` : \<URL\>
    - `DB_password` : ***
    - `DB_user` : ***

After setting all the configurations, run the project as spring boot app (Run>Run)

## setting up Lombok with Eclipse and Intellij
Follow https://www.baeldung.com/lombok-ide

## swagger documentation
After running, hit this local endpoint to see the available APIs in the backend - http://localhost:8080/swagger-ui.html

## Fetch authtoken for dev env
