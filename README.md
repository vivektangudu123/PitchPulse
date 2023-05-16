## Intrustions to run
### Front-end setup
Make sure your device has npm and node installed
\
`npm -v`   - to check version of npm
\
`node -v` - to check version of node

Download `Nosql_Mini_project_ipl_dash_board.zip` and extract the contents `git-deliverable.zip` to a new folder `project`
\
Make sure `project` folder contains two folders `Mongo` and `src`. Open this folder in terminal
\
`npx create-react-app ipl-data-dash`

A new dir ipl-data-dash gets created, go to that dir and run the below command to install build modules
`npm run build`

Move `project/src` folder into `ipl-data-dash` by replacing the existing one.

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
If mongo is set, then run the `Mongo/LoadCSV.java`. Edit the location of csv file in the Java code based on your device.
Check if `cricket` db is loaded mongodb by typing `show databases` in `mongosh`

### Running
Open terminal and run:
`sudo systemctl start mongod`

Now open two terminals:
1) In one terminal open `project/ipl-data-dash` and run `npm start`
2) In the other terminal open `project/Mongo` and run `node server/server.js`

You will have the website running on your localport 3000 and server at localport 3001!

Thankyou!
