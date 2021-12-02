#!/bin/bash
BUILD_JAR=$(ls /home/ubuntu/app/*.jar)
DEPLOY_PATH=/home/ubuntu/app/
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/app/log/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/app/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/app/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
chmod 755 $DEPLOY_JAR
echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/app/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ubuntu/app/deploy.log 2>/home/ubuntu/app/deploy_err.log &
