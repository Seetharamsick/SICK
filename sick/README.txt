Softwares required to have before going to run this test.

Install MYSQL SERVER 5.7 on Master box.
JDK 1.7
Mave 3.3.9
GIT
TestNG

This framework is designed and implemented with the Master and client architecute. Master Node will have all the code and will be able to control all the nodes typically linux servers. It can scale to multiple devices easily.

This will be able to configure the settings in linux server and running linux commands on  servers, Copy files from master to slave and vice versa using  jsch.
 
By Implementing Runnable interface could able to monitor multiple servers at the same time and Database will be created for each linux server on mysql under mysql DB.

Email Notifications will be sent using sendmail on linux systems by configuring cornjobs.

This framework structured the code in such a way that libraries and test. All the implementations will go under lib  as a modules and will be used in testng classes.

I have created one testNG class to have all the requirements given in the QUIZ1. Which will basically calls the modules in the library.

This test will create one table per server and poll health information every 5mins and update the SERVER_STATISTIC_TABLE_hostname table.

Clone the git hub https://github.com/Seetharamsick/SICK.git, install all the prerequsites. Run the SICKQuiz1.java using testNG. 

Update the serverConfig.java file with server details.Add as manay multiple servers to monitor.

Enhancements:
Need to give configurable parameter to run the monitoriung cycle for days,monthly and yearly.
Improve the table structure and break down into smaller tables..
We can write Schemas on top of the ubdated table for reporting.




