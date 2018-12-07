Enzyme Efficieny
==============

This is a java web application that will allow you to experiment with building a model for 
a protein enzyme using FASTA file input and a set of targets.

It was built in 2010 while I was working for the [Pisabarro Group](http://www.biotec.tu-dresden.de/research/pisabarro.html) 
within the [BioTec in Dresden, Germany](www.biotec.tu-dresden.de/)

It has since sat forgotten and neglected on a hard drive until I discovered
it again today (6 Dec 2018).

I have uploaded it in close to its initial format, after updating the minimal necessary
to get it to work with a newer version of Tomcat.


## Installation Instructions

#### 1. Install Tomcat V8.5.23
http://www-us.apache.org/dist/tomcat/tomcat-8/v8.5.23/README.html


## Docker

We also provide a docker script for building and running the entire application in a container
 
[Install Instructions](docker/README.md)
 

## Build Instructions
 
I recommend getting the latest version and compiling using Java 8, even though the code base was originally meant for Java 7.

Install [Eclipse Oxygen for Java EE Applications](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/oxygen1a)

There are a bunch of external libraries (included in the extlib folder) 
the build script will wrap them into the web application


Create a new Eclipse Java Project pointing at the WebApp directory.

Modify whichever files your need to.

Compile and Export as a War file.

Run inside TomCat


## Example

Inside the [example](example) directory you will find a simple example data set you can run.


### Additional

The [Tre](Tre) folder contains some datasets and experiments I was doing in 2010.

This work never produced anything worthy of publication, but maybe someone will find it useful.



