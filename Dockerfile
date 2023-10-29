FROM node:21 as fronted
COPY --chown=node:node . /home/node/src
WORKDIR /home/node/src/app
RUN npm install && npm run build

FROM gradle:7-jdk11 AS build
COPY --from=fronted --chown=gradle:gradle  /home/node/src /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 3002
RUN mkdir /app
COPY --from=build /home/gradle/src/.env /home/gradle/src/*.json /home/gradle/src/build/libs/*.jar /app/

ENV PORT=3002
ENTRYPOINT ["java","-jar","/app/schools-all.jar"]
