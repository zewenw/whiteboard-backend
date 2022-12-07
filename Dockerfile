FROM docker.io/ascdc/jdk8
COPY ./target/whiteboard-1.0.0.jar /home/admin/whiteboard.jar
ENTRYPOINT ["java", "-jar", "/home/admin/whiteboard.jar"]
EXPOSE 10001
