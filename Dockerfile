

# nuxt
# get nodejs 12
FROM node:12.19.0-alpine3.10
# create destination directory
RUN mkdir -p /usr/src/nuxt-app
WORKDIR /usr/src/nuxt-app
# copy the app
COPY /client /usr/src/nuxt-app/
# install dependencies
RUN cd /client && npm install
# build app
RUN cd /client && npm run build
# expose port
EXPOSE 3000
#start nuxt
CMD [ "npm", "start" ]

#spring
# get openjdk 14
FROM openjdk:14
CMD [ "mvn", "spring-boot:run" ]
COPY target/oktatutor-server-0.0.1.jar application.jar
EXPOSE 8080