FROM maven:3.6.3-openjdk-11-slim AS build
 
ENV DB_HOST=db \
     DB_USERNAME=username \
     DB_PASSWORD=password
 
COPY ./mobile-app-ws /usr/local/mobile-app-ws
 
WORKDIR /usr/local/mobile-app-ws/
 
RUN mvn -Dmaven.test.skip=true clean package
 

FROM tomcat
 
COPY --from=build /usr/local/mobile-app-ws/target/mobile-app-ws-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/mobile-app-ws.war
 
EXPOSE 8080
 
CMD ["catalina.sh", "run"]