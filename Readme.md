# jABCI

A Java implementation of the Tendermint Application BlockChain Interface ([ABCI](https://github.com/tendermint/abci))

[![CircleCI](https://circleci.com/gh/jTendermint/jabci.svg?style=shield)](https://circleci.com/gh/jTendermint/jabci)

## How to use

Check out [StartupExampleDummy.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/StartupExampleDummy.java) or [JavaCounter.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/JavaCounter.java) for examples.

#### Maven integration
jABCI v0.16 Release is now available via maven central. Use the following dependency to include the latest release:
```xml
<dependency>
    <groupId>com.github.jtendermint</groupId>
    <artifactId>jabci</artifactId>
    <version>0.16</version>
</dependency>
```

#### Update protobuf types

When you make changes to the protobuf file, you can enable the appropriate build-phase for the compiler-plugin to generate the file(s) in the target-directory.
Just switch the comments on line [pom.xml](https://github.com/jTendermint/jabci/blob/master/pom.xml#L86).




#### Looking for TMSP?
The Tendermint protocol was changed from TMSP to ABCI. While these where just minor changes, we wanted to completely reflect all the naming changes in this project. If you're still developing with Tendermint v0.8 and prior check out the [0.8 version of jTMSP](https://github.com/jTendermint/jabci/releases/tag/v0.8)
