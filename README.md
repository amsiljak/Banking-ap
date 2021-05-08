# Transactions manager

Android application which allows user to manage and keep track of his personal finances. 

### Application functionalities
If the phone is in landscape mode, the user switches between three different fragments by sliding left and right.  

The **first fragment**, the one that is being showed when the application is opened contains a list of all transactions, filtered by month, year and type of transaction. Transactions can be sorted by price, title or date. There are five types of transactions: *individual payment, regular payment, purchase, individual income and regular income*. Each of these types is different and has a different impact on overall budget. It is possible to show details of each transaction, edit or delete it, as well as add a new transaction. All fields of a new/edited transaction are being validated before saving.  

If the user is in online mode, application uses API for all listed operations, including filtering and sorting. If there is no internet connection, application goes to offline mode and saves changes to local database until the connection is established. 

The **second fragent** shows information about users account: *his budget, his total limit and his month limit*. If the user goes over the limit when adding a transaction, a warning shows. These information can be edited.  

The **third fragment** shows graphically represented statistics for transactions. There are three types of graphs: *spendings graph, earnings graph, and graph thats showing overall state*. The user chooses if he wants the graphs to show data *by day, week or month*. This was implemented using MPAndroidChart library.  

If the application is being used in landscape mode, a fragment for editing a chosen transaction/adding a new one is showed alongside the fragment showing a list.  


<p float="left">
  <img src="prototype.jpg" alt="prototype" width="250"/> 
  <img src="Inkedlist_LI.jpg" alt="Inkedlist_LI" width="200"/> 
  <img src="Inkedgraph_LI.jpg" alt="Inkedgraph_LI" width="200"/> 
</p>

This application was made for the purpose of lerning on Mobile Application Development course on my faculty. The API that it uses and that was created for this course is no longer available.  

### Built using
[<img align="left" alt="Java" width="50px" src="https://logoeps.com/wp-content/uploads/2013/03/java-eps-vector-logo.png" />][java]
[<img align="left" alt="JavaFx" width="100px" src="https://upload.wikimedia.org/wikipedia/en/c/cc/JavaFX_Logo.png" />][javafx]
[<img align="left" alt="SQLite" width="60px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/SQLite370.svg/1200px-SQLite370.svg.png" />][SQLite]
[<img align="left" alt="JasperReports" width="60px" src="https://miro.medium.com/max/424/1*278ccGR72lK73_1l0BuKUw.png" />][jasperreports]


[java]: https://www.java.com/en/
[SQLite]: https://www.sqlite.org/index.html
[javafx]: https://openjfx.io/
[jasperreports]: https://community.jaspersoft.com/



