AirPlay For Java
============================

A Java application that enables playing compatible videos on AirPlay devices.

## DESCRIPTION

This is a simple AirPlay player put together using [Jetty](http://www.eclipse.org/jetty/), [JmDNS](http://jmdns.sourceforge.net/),
[JQuery](http://jquery.com/), and [JQueryUI](http://jqueryui.com/) that will allow you to play videos on an AirPlay compatible
device such as the AppleTV V2. This application has been tested on Windows, Mac and Linux using the Java 6 JDK from Oracle. 

## FEATURES

- User interface to control of playback of video.

## REQUIREMENTS

Java 6 and Apache Ant are required to build this project.

## INSTALL

Compile using Ant then run the resulting jar by running the startup.bat file on windows, double clicking on the AP4JPlayer.jar
file or by using the following command in a terminal:

java -jar AP4JPlayer.jar

## Usage

After starting the application use a web browser to access the user interface at http://localhost:7070/

On startup the application will detect AirPlay devices on your network and display them in the user interface. Copy a
URL into the play video entry field and then click the play video link.

If you have a firewall you may need to make sure that it will allow mDNS broadcasts.

## LICENSE

Copyright (c) 2011 Carson McDonald

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.