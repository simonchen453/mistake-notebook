# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY mn-server/pom.xml mn-server/
RUN mvn dependency:go-offline -B
COPY mn-server/src mn-server/src
RUN mvn clean package -DskipTests -pl mn-server

# 第二阶段：运行
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 安装字体（支持中文）
RUN apk add --no-cache fontconfig ttf-dejavu

COPY --from=builder /app/mn-server/target/*.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
