querifier
=========

Interface for students to submit queries and get the submitted queries verified with the desired result as per the query provided by the instructor. The crux of the problem is in determining whether or not a pair of queries will produce identical result sets for ALL the data sets and not just a particular data set. This will require running the queries on a set of test data sets and comparing the results. Thus, generation of these test data sets is very important which will ensure the correctness of the queries.

Dependencies & How to run
-------------------------
* [Netbeans](https://netbeans.org/downloads/start.html?platform=linux&lang=en&option=all)
* Glassfish server or equivalent installed and integrated with Netbeans
* mysql server running on default port
* install the database `querfier` from the folder db with username `DB_project` and password `dbproject`
* open the following [link](http://localhost:8080/WEB-INF/)
* Login with
    - username: 10000, password: amanmangal* [instructor]
    - username: 20000, password: amanmangal* [student]
* Note that the details in the `db` table has to be modified before they are used!
