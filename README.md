# Introduction

This is a very simple Web Framework. This framework can be used to create Rest APIs (GET and POST) using Java programming language. It can handle multiple clients simultaneouly and it is created using mainly Socket Programming and Multithreading in Java Programming language. It's very easy to use.


## How To Use

- Create your own java project

- Add WebServer.jar file in your project

- Write your own class which would handle the request

- In that class make your own request handling methods and annotate with @Route and provide required mapping for APIs

- Create a directory named templates in the project and put all the .html files in it

- Create a directory named static in the project and put all the .css and .js files in it

- Create a main method in that class and in main method create an object of WebServer class and provide port number, classpath and class name in WebServer's constructor

- Now Run your main method, this would start the webserver and now APIs can be hit on localhost sever (Sample URL: http://localhost:5002/get)

- See sample/Router.java for a sample class

### Prerequisite

Things need to install 
- Java 
- IDE (like intellij, eclipse)


## Brief Overview

- In web/server/WebServer.java, server is listening on a particular listening port

- In web/server/Request.java, each request is handled on one seprate thread

- In http/packet/HttpPacket.java, encoding and decoding of http packet is handled

- In rendering/template/RenderTemplate.java, parsing of html, css and javascript file is handled



