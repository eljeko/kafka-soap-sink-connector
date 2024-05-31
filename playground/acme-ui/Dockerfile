FROM node:18.16.0-alpine3.17
RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY ./package.json package.json .
RUN npm install
COPY ./* .
COPY ./views /opt/app/views
COPY ./public /opt/app/public
EXPOSE 9090
CMD [ "npm", "start"]