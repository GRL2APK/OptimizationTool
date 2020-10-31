**1 Overview:** 

Our proposed formal optimization framework identifies conflicting NFR operationalizations, analyses and evaluates alternative solutions, and recommends
optimal solutions to the developer when constructing an application. Our tool corresponding to this framework takes all the NFR related information from the user, and based on the evaluated contribution and conflict functions, the tool
evaluates the optimal choice of NFR operationalizations.
We also provide user manual elaborating execution steps of the tool with actual tool interface images.

**2 Platform Settings**

In this section, a clear guide is provided in order to build the required platform for the tool.
1. Downloading GitHub Repository: The very first step is to download
the our GitHub repository1 in your system.
2. Placing Database files in SQL Server Directory: Find two database
files namely OptimizationDB.mdf and Optimization log.ldf in \Data" folder
of the repository. Make copy of these two files in SQL Server data directory. Generally, the default location of the directory is "C:\Program
Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA"
3. Importing Database to SQL Server Management Studio: In SQL
Server Management Studio, we can find "Object Explorer" window. Under
this window, we can find "Attach" option on right clicking the "Database"
folder item. In the next step, we have to attach newly copied SQL Server
database file (.mdf). The ".ldf" file will be linked automatically with the
.mdf file.
4. Installing Python Packages: In this step, two required python packages need to be installed. First package (Matplotlib) is used to create plots
and another package (Pyodbc) is used to communicate with database.
Once above steps are completed successfully, we are ready to execute our tool.

**There are two ways to run the tool.**
1. Import entire java project in Eclipse IDE (recommended) or any other
IDE and run Home.java file in "src" folder.
2. Another way to execute our tool is by simply running OptimizationTool.jar
from "dist" folder.

**3 System Requirements**
1. Operating System: Microsoft Windows 7, 8, 10.
2. Java: jdk-1.7 or higher version.
3. Eclipse: Eclipse Oxygen or higher version.
4. Python: Python 3.5 or above
5. SQL Server: SQL Server 2014 or higher version.
