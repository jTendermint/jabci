# jABCI

A Java implementation of the Tendermint Application BlockChain Interface ([ABCI](https://github.com/tendermint/abci))

## How to use

Check out [StartupExampleDummy.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/StartupExampleDummy.java) or [JavaCounter.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/JavaCounter.java) for examples.

#### Update protobuf types

When you make changes to the protobuf file, you can enable the appropriate build-phase for the compiler-plugin to generate the file(s) in the target-directory.
Just switch the comments on lines [pom.xml](https://github.com/jTendermint/jabci/blob/master/pom.xml#L27) and 28.
