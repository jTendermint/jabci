# jTMSP

A Java implementation of the Tendermint socket protocol ([TMSP](https://github.com/tendermint/tmsp))

## How to use

Check out [StartupExampleDummy.java](https://github.com/jTMSP/jTMSP/blob/master/src/main/java/com/github/jtmsp/StartupExampleDummy.java) or [JavaCounter.java](https://github.com/jTMSP/jTMSP/blob/master/src/main/java/com/github/jtmsp/JavaCounter.java) for examples.

#### Update protobuf types

When you make changes to the protobuf file, you can enable the appropriate build-phase for the compiler-plugin to generate the file(s) in the target-directory.
Just switch the comments on lines [pom.xml](https://github.com/jTMSP/jTMSP/blob/master/pom.xml#L27) and 28.
