## Intrustions to run
### Front-end setup
Make sure your device has npm and node installed
\
`npm -v`   - to check version of npm
\
`node -v` - to check version of node

Create a new directory for the project, open the folder in terminal and run the below command
\
`npx create-react-app ipl-data-dash`

A new dir ipl-data-dash gets created, go to that dir and run the below command to install build modules
`npm run build`

Replace the `src` folder in `ipl-data-dash` with the `src` folder provided.

This completes the setup for front-end

### Nodejs connection
Download `Mongo` to the directory of project
\
Go to `Mongo/server` and type following cmds
\
\
`npm init -y`
\
\
`npm install express`
\
This completes setup for the connection

### Back-end Setup
Make sure u have mongo installed. Follow this link
Check installation by `mongosh`
Then download the `Mongo/IPL_Ball.csv file and run LoadCSV.java
Check if cricket db is in mongo by typing show databases in mongosh

Running
Open terminal and type:
sudo systemctl start mongod

Now open two terminals. In one terminal open ipl-data-dash and another Mongo
in ipl-data-dash run: npm start
in Mongo run: node server/server.js

DONE Thankyou
