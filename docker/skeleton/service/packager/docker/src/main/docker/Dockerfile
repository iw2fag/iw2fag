FROM azul/zulu-openjdk:8u112

#install Jetty
RUN groupadd -r jetty && useradd -r -g jetty jetty
ENV JETTY_HOME /usr/local/jetty
ENV PATH $JETTY_HOME/bin:$PATH
RUN mkdir -p "$JETTY_HOME"
WORKDIR $JETTY_HOME

COPY /maven/jetty.tar.gz $JETTY_HOME

RUN set -xe \
	&& tar -xvf jetty.tar.gz --strip-components=1 \
	&& sed -i '/jetty-logging/d' etc/jetty.conf \
	&& rm -fr demo-base javadoc \
	&& rm jetty.tar.gz

ENV JETTY_BASE /var/lib/jetty
RUN mkdir -p "$JETTY_BASE"
WORKDIR $JETTY_BASE

RUN modules="$(grep -- ^--module= "$JETTY_HOME/start.ini" | cut -d= -f2 | paste -d, -s)" \
	&& set -xe \
	&& java -jar "$JETTY_HOME/start.jar" --add-to-startd="$modules,setuid"

ENV TMPDIR /tmp/jetty
RUN set -xe \
	&& mkdir -p "$TMPDIR" \
	&& chown -R jetty:jetty "$TMPDIR" "$JETTY_BASE"
#install Jetty ends

ARG WAR_FILE
ARG BUILD_DIR
ARG IMAGE_TYPE
ARG IMAGE_BUILD_NUMBER
ARG MAJOR_MINOR_VERSION
ARG GIT_COMMIT
ARG HTTP_PORT

ENV DOCKER_RESOURCE_HOME /usr/local/docker-resource
ENV JETTY_BASE $JETTY_HOME
ENV SKELETON_HOME $DOCKER_RESOURCE_HOME

COPY /$BUILD_DIR/jetty $JETTY_HOME/

COPY /$BUILD_DIR/docker-resource $DOCKER_RESOURCE_HOME/
COPY /$BUILD_DIR/$WAR_FILE $JETTY_BASE/webapps/$WAR_FILE

RUN chmod 777 $DOCKER_RESOURCE_HOME/bin/*.sh
RUN chmod 777 $DOCKER_RESOURCE_HOME/utils/*

RUN $DOCKER_RESOURCE_HOME/utils/merge_properties.sh && rm -rf $DOCKER_RESOURCE_HOME/docker-conf

EXPOSE $HTTP_PORT
WORKDIR $JETTY_BASE

LABEL skeleton_image_type=$IMAGE_TYPE \
      build.number=$IMAGE_BUILD_NUMBER \
      version=$MAJOR_MINOR_VERSION
#	  version=$MAJOR_MINOR_VERSION \
#	  commit=$GIT_COMMIT

CMD exec /usr/local/docker-resource/bin/start_service.sh