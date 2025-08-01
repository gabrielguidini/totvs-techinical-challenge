FROM node:23.3.0 AS build-front

RUN npm install -g http-server  \
    && npm install -g pnpm

WORKDIR /technical-challenge-front

COPY /package.json .

COPY . .

RUN npm run build

EXPOSE 3000

CMD [ "http-server", "-p", "3000", "build" ]