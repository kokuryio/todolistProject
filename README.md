# Todo - APP for organizing your daily tasks

## Reminder
Please remember - this is my first project as a student and therefore is not made well. Please be kind to me and do not expect the App to do any wonders - you can get way better apps
of this kind at the PlayStore/Appstore already.

## How to execute
It is possible to store the tasks from the app remotely using a REST service. I used a WildFly server as the backened, however you need to keep in mind to set everything up accordingly
and change the IP - Adress in LoginViwActivity.java (line 28), MainActivity.java (line 23), AsyncCRUDAccessor.java (line 33) and in the 
network_security_config.xml. 

However, this i not necessary for the use of the app. Todo items will be saved locally on your device as well. 
Remember that the app MUST be used whilst having the dark mode of your device turned on, or otherwise you will not see the titles of the todos because I accidently set their font color to white
(please excuse this - it is my first time making an app and I was just testing it with dark mode turned on).

## APK file (for quick start)
You can also just download the apps apk file from below and run it instantly on your device if you wish to.
https://drive.google.com/file/d/1sM88NshP63EObcb-4Llxh-bfAZTP8k1z/view?usp=drive_link
