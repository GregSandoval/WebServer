# WebServer

A web server built in Java. Currently this project is for my education only, but feel free to browse the code.
The server uses the standard 1 thread per connection model. Rather than creating a new thread per connection, it
uses a pool of threads. 

Goals:

Multithreaded (1 Thread per connection, NIO in future version)

Support all HTTP request methods on file system, and eventually add REST capabilities. 

Support Cookies

Support query strings

Introduce modern testing framework

Better error descriptions
