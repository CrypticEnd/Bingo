FROM node:lts-alpine3.18

WORKDIR /app

# [host machine] [container]
COPY package.json package-lock.json ./

RUN npm install

# Get environment value and set it
ARG REACT_APP_CONNECTION_URL

ENV REACT_APP_CONNECTION_URL $REACT_APP_CONNECTION_URL

COPY . .

# RUN npm run build

CMD ["npm", "start"]
