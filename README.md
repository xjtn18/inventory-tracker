# Inventory Tracker
## A Java Springboot inventory tracking program with an interactive command line demo.

<div style="width:100%;height:0px;position:relative;padding-bottom:56.250%;"><iframe src="https://streamable.com/e/2lmkb6" frameborder="0" width="100%" height="100%" allowfullscreen style="width:100%;height:100%;position:absolute;left:0px;top:0px;overflow:hidden;"></iframe></div>

---
### Tools needed
	- Java JDK (1.8.0_331)
	- Maven (3.8.5)
	- MySQL (8.0.29)
	- GNU Make (4.3)

In parentheses are the versions for each tool that I used when developing the program.

---
### How to build
Using a terminal, navigate to the root directory of the project and enter the following command:
> make build


---
### How to run

1. First ensure that 'src/main/resources/application.properties' exists and contains the following line:
> weather.apiKey={your personal OpenWeather API key}
2. Ensure that you have an SQL server running locally on the default port 3306.
3. Using a terminal, navigate to the root directory of the project and enter the following command to start the demo:
> make run
	- Entering the following alternate command will reset the inventory database table to a default table as specified in 'src/main/resources/init-db.sql' before starting the demo:
	> make run db=reset
