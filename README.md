# Eraseme
 
## Prerequisites to run the program
The example uses Java, Maven and Derby database. It is tested with the versions:

* java 1.13


### Derby install
Download [Derby](https://db.apache.org/derby/derby_downloads.html)
, unzip to a folder of your choice.


Download and unzip the project to a java project (`eraseme`) or clone the repository: 

```git clone git@github.com:Thevyn/Eraseme.git``` 

### Create and populate the database 

Open a command shell and go to the directory `eraseme/database`. Open the database creation script `create-db.cmd` (Windows) or `create-db.sh` (Linux or Mac) and edit `DERBY_HOME` to point to your Derby installation folder containing the libs. 

Run the create-db script. It will create the `database/hotelsample` folder, containing the Derby database populated with sample data.


### Run the application

Start the Webserver by running Application.java
