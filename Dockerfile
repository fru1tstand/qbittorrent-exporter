FROM gradle:latest
COPY . /build
EXPOSE 9561
RUN cd /build ; \
    sh gradlew fatJar ; \
	find . ; \
    cp build/libs/qbt-exporter*.jar /qbt-exporter.jar

ENTRYPOINT java -jar /qbt-exporter.jar
