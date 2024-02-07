FROM openjdk:11

EXPOSE 5500

ADD build/libs/CourseProject_3_TransferMoney-0.0.1-SNAPSHOT.jar transferMoney.jar

ENTRYPOINT ["java", "-jar", "/transferMoney.jar"]