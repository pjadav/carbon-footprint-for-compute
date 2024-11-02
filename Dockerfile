FROM sbtscala/scala-sbt:eclipse-temurin-11.0.17_8_1.8.2_2.13.10 as builder
LABEL authors="prakash.jadav"
COPY project/ project/
COPY build.sbt .
RUN sbt update

COPY . .
RUN sbt test
RUN sbt assembly

FROM openjdk:11.0.6-slim as runtime
LABEL authors="prakash.jadav"
ARG API_KEY_USER
ENV API_KEY=$API_KEY_USER
COPY --from=builder /root/target/scala-2.13/carbon-footprint-for-compute.jar .
CMD java -jar carbon-footprint-for-compute.jar

#FROM ghcr.io/graalvm/native-image:22.2.0 AS native-build
#WORKDIR /opt/graalvm
#COPY reflection-config.json .
#COPY --from=builder /root/target/scala-2.13/carbon-footprint-for-compute.jar .
#RUN native-image -jar carbon-footprint-for-compute.jar --no-fallback -H:ReflectionConfigurationFiles=reflection-config.json
#FROM debian:11-slim AS client
#COPY --from=native-build /opt/graalvm/carbon-footprint-for-compute .