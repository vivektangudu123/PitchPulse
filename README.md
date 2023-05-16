Intrustions to run
1) Front-end
Make sure your device has npm and node installed
npm -v //to check version of npm
node -v //to check version of node

Open your project folder and run the below command
npx create-react-app ipl-data-dash

A new dir ipl-data-dash gets created, go to that dir
npm run build //installs build modules

Replace the src folder with the given src folder
This completes the setup of front-end

2) For the Nodejs connection
Go to Mongo/server and type following cmds
npm init -y
npm install express

3) Back-end
Make sure u have mongo installed. Follow this link
Check installation by mongosh
Then download the .csv file and run LoadCSV.java
Check if cricket db is in mongo by typing show databases in mongosh

Running
Open terminal and type:
sudo systemctl start mongod

Now open two terminals. In one terminal open ipl-data-dash and another Mongo
in ipl-data-dash run: npm start
in Mongo run: node server/server.js

DONE Thankyou
