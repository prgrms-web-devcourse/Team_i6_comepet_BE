#!/bin/bash
BUILD_JAR=$(ls /home/ubuntu/app/compet-0.0.1.jar)
DEPLOY_PATH=/home/ubuntu/app/
JAR_NAME=$(basename $BUILD_JAR)
echo "> dev deploy"
echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/app/log/deploy_dev.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/app/deploy_dev.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/app/deploy_dev.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

source ~/.env
ENVS=$(printenv)
echo "> 환경변수 :$ENVS" >> /home/ubuntu/app/deploy_dev.log

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 이름: $DEPLOY_JAR"    >> /home/ubuntu/app/deploy_dev.log
chmod 755 $DEPLOY_JAR
echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/app/deploy_dev.log
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=dev $DEPLOY_JAR >> /home/ubuntu/app/deploy_dev.log 2>/home/ubuntu/app/deploy_dev_err.log &
