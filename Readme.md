# Deprecation Notice
This project is no longer under active development and may be archived soon.

# jABCI

A Java implementation of the Tendermint Application BlockChain Interface ([ABCI](https://github.com/tendermint/tendermint/tree/master/types))

[![CircleCI](https://circleci.com/gh/jTendermint/jabci.svg?style=shield)](https://circleci.com/gh/jTendermint/jabci)

## How to use

Check out [StartupExampleDummy.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/StartupExampleDummy.java) or [JavaCounter.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/JavaCounter.java) for examples.

#### Maven integration
jABCI v0.32.3 Release is now available via maven central. Use the following dependency to include the latest release:
```xml
<dependency>
    <groupId>com.github.jtendermint</groupId>
    <artifactId>jabci</artifactId>
    <version>0.32.3</version>
</dependency>
```

#### Update protobuf types

When you make changes to the protobuf file, you can enable the appropriate build-phase for the compiler-plugin to generate the file(s) in the target-directory.
Just switch the comments on line [pom.xml](https://github.com/jTendermint/jabci/blob/master/pom.xml#L86).

From commandline:
```
cd jabci/src/main/java
protoc  --java_out=. --proto_path=../proto/. ../proto/types.proto
```


#### Compatibility

| jabci    | tendermint |
|----------|------------|
| 0.12.x   | 0.12.x |
| 0.15     | 0.15.0 |
| 0.16     | 0.16.0 |
| 0.17.1   | 0.17.0 |
| 0.17.1   | 0.17.1 |
| 0.17.1   | 0.18.0 |
| 0.17.1   | 0.19.0 |
| 0.20.0.x | 0.20.x |
| 0.20.0.x | 0.21.x |
| 0.20.0.x | 0.22.x |
| 0.24.0   | 0.24   |
| 0.24.0   | 0.25   |
| 0.26.0   | 0.26 - 0.30.1   |
| 0.32.3   | 0.32.3 |
