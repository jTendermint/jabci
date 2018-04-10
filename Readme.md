# jABCI

A Java implementation of the Tendermint Application BlockChain Interface ([ABCI](https://github.com/tendermint/abci))

[![CircleCI](https://circleci.com/gh/jTendermint/jabci.svg?style=shield)](https://circleci.com/gh/jTendermint/jabci)

## How to use

Check out [StartupExampleDummy.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/StartupExampleDummy.java) or [JavaCounter.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/JavaCounter.java) for examples.

#### Maven integration
jABCI v0.17.1 Release is now available via maven central. Use the following dependency to include the latest release:
```xml
<dependency>
    <groupId>com.github.jtendermint</groupId>
    <artifactId>jabci</artifactId>
    <version>0.17.1</version>
</dependency>
```

#### Update protobuf types

When you make changes to the protobuf file, you can enable the appropriate build-phase for the compiler-plugin to generate the file(s) in the target-directory.
Just switch the comments on line [pom.xml](https://github.com/jTendermint/jabci/blob/master/pom.xml#L86).

