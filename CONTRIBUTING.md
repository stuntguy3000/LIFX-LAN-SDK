# Contributing to LIFX Lan SDK

----------------

Firstly, welcome! 

This is an open source project, and as such, contributions are more than welcome. 

Generally speaking, this should be pretty simple. 

## Maven
LIFX LAN SDK uses **Maven** for dependency management. It also features jUnit 5 for test cases.

It's worth pointing out that these test cases are highly specific to my home environment as they use test against real world LIFX devices, so your mileage may vary.

## Compiling
Simply, execute ```mvn```  in the package root

To skip test cases, add this flag: **-Dmaven.test.skip=true**

Like so: ```mvn -Dmaven.test.skip=true``` 

## Pull Requests & Issues
I'll be reviewing any pull requests and issues. No formal standards are defined, just be sure they are reasonable and clear.