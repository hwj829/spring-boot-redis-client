FROM image-registry.openshift-image-registry.svc:5000/mdc/base-banking-services@sha256:106b4f0848c832fdb65f0e5726e63c49626099aa01167745c96e0dfc83b9240c

WORKDIR /build/resource/

RUN git clone https://github.com/hwj829/spring-boot-redis-client.git /build/resource/ && \
    mvn -DskipTests=true package install -f /build/resource/pom.xml && \
    ls -tral /build/resource/ && \
    mkdir /apps-data/ && \
    chgrp -R 0 /apps-data/ && \
    chmod -R 777 /apps-data/

ENTRYPOINT ["java","-jar","/build/resource/target/spring-boot-redis-client-0.0.1-SNAPSHOT.jar","-Dfile.encoding=UTF-8"]

EXPOSE 9090



