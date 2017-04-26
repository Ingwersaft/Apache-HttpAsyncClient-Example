# Apache-HttpAsyncClient-Examples

This project contains some examples showing the usage and behaviour of Apache's HttpAsyncClient, which provides a non-blocking HTTP client.

This is especially useful if your http calls have a high duration and you need to do alot of them in parallel. Classical HTTP/REST client frameworks internally use a Threadpool/ExecutorService and map one thread per request, which leads to alot of overhead if you have like 1000 requests each with an average duration of... lets say 20s.  

An embedded Jetty server is used as the target service.

## Preamble

First dive into the example (see Main.java) and look at the setup, which should be enough to understand the setup.

Then run the main Method.

## What happens? - Log analysis
```
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -Didea.launcher.port=7534 -Didea.launcher.bin.path=/home/mauer/intellij-ultimate/bin -Dfile.encoding=UTF-8 -classpath /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jaccess.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/nashorn.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunec.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/management-agent.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar:/home/mauer/workspace/Xyz/target/classes:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-server/9.4.4.v20170414/jetty-server-9.4.4.v20170414.jar:/home/mauer/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-http/9.4.4.v20170414/jetty-http-9.4.4.v20170414.jar:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-util/9.4.4.v20170414/jetty-util-9.4.4.v20170414.jar:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-io/9.4.4.v20170414/jetty-io-9.4.4.v20170414.jar:/home/mauer/.m2/repository/org/apache/httpcomponents/httpasyncclient/4.1.3/httpasyncclient-4.1.3.jar:/home/mauer/.m2/repository/org/apache/httpcomponents/httpcore/4.4.6/httpcore-4.4.6.jar:/home/mauer/.m2/repository/org/apache/httpcomponents/httpcore-nio/4.4.6/httpcore-nio-4.4.6.jar:/home/mauer/.m2/repository/org/apache/httpcomponents/httpclient/4.5.3/httpclient-4.5.3.jar:/home/mauer/.m2/repository/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:/home/mauer/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar:/home/mauer/.m2/repository/org/slf4j/slf4j-simple/1.7.22/slf4j-simple-1.7.22.jar:/home/mauer/.m2/repository/org/slf4j/slf4j-api/1.7.22/slf4j-api-1.7.22.jar:/home/mauer/intellij-ultimate/lib/idea_rt.jar com.intellij.rt.execution.application.AppMain com.kesselring.apacheasyncclienttests.Main
2017-04-25T21:43:07 [pool-1-thread-1] INFO org.eclipse.jetty.util.log - Logging initialized @229ms to org.eclipse.jetty.util.log.Slf4jLog
2017-04-25T21:43:07 [pool-1-thread-1] INFO org.eclipse.jetty.server.Server - jetty-9.4.4.v20170414
2017-04-25T21:43:07 [pool-1-thread-1] INFO org.eclipse.jetty.server.AbstractConnector - Started ServerConnector@7c25047b{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
2017-04-25T21:43:07 [pool-1-thread-1] INFO org.eclipse.jetty.server.Server - Started @397ms
org.eclipse.jetty.server.Server@88ca072 - STARTED
 += qtp475379153{STARTED,8<=8<=200,i=5,q=0} - STARTED
 |   +- 14 qtp475379153-14 TIMED_WAITING @ sun.misc.Unsafe.park(Native Method) IDLE
 |   +- 18 qtp475379153-18 TIMED_WAITING @ sun.misc.Unsafe.park(Native Method) IDLE
 |   +- 17 qtp475379153-17 TIMED_WAITING @ sun.misc.Unsafe.park(Native Method) IDLE
 |   +- 13 qtp475379153-13-acceptor-0@56a83756-ServerConnector@7c25047b{HTTP/1.1,[http/1.1]}{0.0.0.0:8080} RUNNABLE @ sun.nio.ch.ServerSocketChannelImpl.accept0(Native Method) prio=3
 |   +- 15 qtp475379153-15 TIMED_WAITING @ sun.misc.Unsafe.park(Native Method) IDLE
 |   +- 12 qtp475379153-12 RUNNABLE @ sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
 |   +- 16 qtp475379153-16 TIMED_WAITING @ sun.misc.Unsafe.park(Native Method) IDLE
 |   +- 11 qtp475379153-11 RUNNABLE @ sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
 |   +- jobs
 += ServerConnector@7c25047b{HTTP/1.1,[http/1.1]}{0.0.0.0:8080} - STARTED
 |   +~ org.eclipse.jetty.server.Server@88ca072 - STARTED
 |   +~ qtp475379153{STARTED,8<=8<=200,i=5,q=0} - STARTED
 |   += org.eclipse.jetty.util.thread.ScheduledExecutorScheduler@f4a5a94 - STARTED
 |   +- org.eclipse.jetty.io.ArrayByteBufferPool@1e058c53
 |   += HttpConnectionFactory@de195c2[HTTP/1.1] - STARTED
 |   |   +- HttpConfiguration@112f2e9a{32768/8192,8192/8192,https://:0,[]}
 |   += org.eclipse.jetty.server.ServerConnector$ServerConnectorManager@3ec857b0 - STARTED
 |   |   += org.eclipse.jetty.io.ManagedSelector@53eb0fd9 id=0 keys=0 selected=0 id=0
 |   |   |   +- sun.nio.ch.EPollSelectorImpl@25916f25 keys=0
 |   |   += org.eclipse.jetty.io.ManagedSelector@75846cf6 id=1 keys=0 selected=0 id=1
 |   |       +- sun.nio.ch.EPollSelectorImpl@40f53e14 keys=0
 |   +- sun.nio.ch.ServerSocketChannelImpl[/0:0:0:0:0:0:0:0:8080]
 |   +- qtp475379153-13-acceptor-0@56a83756-ServerConnector@7c25047b{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
 += com.kesselring.apacheasyncclienttests.Main$LocalServer$1@7e1a3058 - STARTED
 += org.eclipse.jetty.server.handler.ErrorHandler@6c815816 - STARTED
 |
 +> sun.misc.Launcher$AppClassLoader@7440e464
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/charsets.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/cldrdata.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/dnsns.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/icedtea-sound.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jaccess.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/localedata.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/nashorn.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunec.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunjce_provider.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunpkcs11.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/zipfs.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jce.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jsse.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/management-agent.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/resources.jar
     +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar
     +- file:/home/mauer/workspace/Xyz/target/classes/
     +- file:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-server/9.4.4.v20170414/jetty-server-9.4.4.v20170414.jar
     +- file:/home/mauer/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar
     +- file:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-http/9.4.4.v20170414/jetty-http-9.4.4.v20170414.jar
     +- file:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-util/9.4.4.v20170414/jetty-util-9.4.4.v20170414.jar
     +- file:/home/mauer/.m2/repository/org/eclipse/jetty/jetty-io/9.4.4.v20170414/jetty-io-9.4.4.v20170414.jar
     +- file:/home/mauer/.m2/repository/org/apache/httpcomponents/httpasyncclient/4.1.3/httpasyncclient-4.1.3.jar
     +- file:/home/mauer/.m2/repository/org/apache/httpcomponents/httpcore/4.4.6/httpcore-4.4.6.jar
     +- file:/home/mauer/.m2/repository/org/apache/httpcomponents/httpcore-nio/4.4.6/httpcore-nio-4.4.6.jar
     +- file:/home/mauer/.m2/repository/org/apache/httpcomponents/httpclient/4.5.3/httpclient-4.5.3.jar
     +- file:/home/mauer/.m2/repository/commons-codec/commons-codec/1.9/commons-codec-1.9.jar
     +- file:/home/mauer/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar
     +- file:/home/mauer/.m2/repository/org/slf4j/slf4j-simple/1.7.22/slf4j-simple-1.7.22.jar
     +- file:/home/mauer/.m2/repository/org/slf4j/slf4j-api/1.7.22/slf4j-api-1.7.22.jar
     +- file:/home/mauer/intellij-ultimate/lib/idea_rt.jar
     +- sun.misc.Launcher$ExtClassLoader@2c376a1e
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/cldrdata.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/icedtea-sound.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/zipfs.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunec.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/localedata.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunjce_provider.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/dnsns.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jaccess.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunpkcs11.jar
         +- file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/nashorn.jar
2017-04-25T21:43:08 [main] INFO com.kesselring.apacheasyncclienttests.Main - startup done
```
Now the jetty server is up and running
```
2017-04-25T21:43:08 [main] INFO com.kesselring.apacheasyncclienttests.Main - client started
2017-04-25T21:43:08 [main] INFO com.kesselring.apacheasyncclienttests.Main - 10 requests triggered, now sleeping 1s
2017-04-25T21:43:08 [qtp475379153-16] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 4
2017-04-25T21:43:08 [qtp475379153-26] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 2
2017-04-25T21:43:08 [qtp475379153-15] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 6
2017-04-25T21:43:08 [qtp475379153-18] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 7
2017-04-25T21:43:08 [qtp475379153-11] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 3
2017-04-25T21:43:08 [qtp475379153-27] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 1
2017-04-25T21:43:08 [qtp475379153-12] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 5
2017-04-25T21:43:08 [qtp475379153-25] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 8
2017-04-25T21:43:09 [main] INFO com.kesselring.apacheasyncclienttests.Main - sleep done, but requests still working (sleep 10000s inside jettys handler)
2017-04-25T21:43:18 [qtp475379153-26] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 2
2017-04-25T21:43:18 [qtp475379153-11] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 3
2017-04-25T21:43:18 [qtp475379153-12] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 5
2017-04-25T21:43:18 [qtp475379153-15] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 6
2017-04-25T21:43:18 [qtp475379153-25] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 8
2017-04-25T21:43:18 [qtp475379153-18] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 7
2017-04-25T21:43:18 [qtp475379153-16] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 4
2017-04-25T21:43:18 [qtp475379153-27] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 1
2017-04-25T21:43:18 [I/O dispatcher 3] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 7 has been processed successfully
2017-04-25T21:43:18 [I/O dispatcher 2] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 4 has been processed successfully
2017-04-25T21:43:18 [I/O dispatcher 1] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 5 has been processed successfully
2017-04-25T21:43:18 [I/O dispatcher 4] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 3 has been processed successfully
2017-04-25T21:43:28 [I/O dispatcher 3] INFO Callback - callback after io: 7 has been processed successfully
2017-04-25T21:43:28 [I/O dispatcher 1] INFO Callback - callback after io: 5 has been processed successfully
2017-04-25T21:43:28 [I/O dispatcher 2] INFO Callback - callback after io: 4 has been processed successfully
2017-04-25T21:43:28 [I/O dispatcher 4] INFO Callback - callback after io: 3 has been processed successfully
2017-04-25T21:43:28 [I/O dispatcher 3] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 6 has been processed successfully
2017-04-25T21:43:28 [I/O dispatcher 4] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 1 has been processed successfully
2017-04-25T21:43:28 [qtp475379153-30] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 9
2017-04-25T21:43:28 [I/O dispatcher 1] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 2 has been processed successfully
2017-04-25T21:43:28 [I/O dispatcher 2] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 8 has been processed successfully
2017-04-25T21:43:38 [I/O dispatcher 3] INFO Callback - callback after io: 6 has been processed successfully
2017-04-25T21:43:38 [I/O dispatcher 4] INFO Callback - callback after io: 1 has been processed successfully
2017-04-25T21:43:38 [I/O dispatcher 1] INFO Callback - callback after io: 2 has been processed successfully
2017-04-25T21:43:38 [qtp475379153-30] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 9
2017-04-25T21:43:38 [qtp475379153-17] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - request: 10
2017-04-25T21:43:38 [I/O dispatcher 2] INFO Callback - callback after io: 8 has been processed successfully
2017-04-25T21:43:38 [I/O dispatcher 1] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 9 has been processed successfully
2017-04-25T21:43:48 [qtp475379153-17] INFO com.kesselring.apacheasyncclienttests.Main$LocalServer - done: 10
2017-04-25T21:43:48 [I/O dispatcher 1] INFO Callback - callback after io: 9 has been processed successfully
2017-04-25T21:43:48 [I/O dispatcher 3] INFO Callback - callback pre io: ->HTTP/1.1 200 OK: 10 has been processed successfully
2017-04-25T21:43:58 [I/O dispatcher 3] INFO Callback - callback after io: 10 has been processed successfully
2017-04-25T21:44:09 [main] INFO com.kesselring.apacheasyncclienttests.Main - everything should be done by now, shutting down
Process finished with exit code 0
```
TODO