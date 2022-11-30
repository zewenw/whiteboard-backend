FROM docker.io/ascdc/jdk8
EXPOSE 10001
COPY ./target/whiteboard-1.0.0.jar /home/admin/whiteboard.jar
CMD [ "java", "-jar", "/home/admin/whiteboard.jar" ]