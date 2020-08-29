FROM bellsoft/liberica-openjdk-alpine
EXPOSE 8889
RUN mkdir -p /app/
ADD build/libs/webquizengine.jar /app/webquizengine.jar
ENTRYPOINT ["java", "-jar", "/app/webquizengine.jar"]