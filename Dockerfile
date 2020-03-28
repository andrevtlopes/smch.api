FROM openjdk:8

# Application variables
ENV APP_HOME /app
ENV APP_NAME smch-api
ENV APP_FILE ${APP_NAME}-*.war

RUN mkdir -p $APP_HOME/
WORKDIR $APP_HOME

COPY ./target/$APP_FILE $APP_HOME/

CMD ["sh", "-c", "java -jar $APP_FILE"]

# docker build -t unioeste/smch-api .
