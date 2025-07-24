# Build Stage
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build


FROM node:20-alpine AS runner
WORKDIR /app


COPY --from=builder /app/.next .next
COPY --from=builder /app/public public
COPY --from=builder /app/package*.json ./

ENV NODE_ENV production
ENV PORT 3000
EXPOSE 3000

RUN npm install --omit=dev

CMD ["npm", "start"]
