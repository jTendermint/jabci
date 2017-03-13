# jABCI

A Java implementation of the Tendermint Application BlockChain Interface ([ABCI](https://github.com/tendermint/abci))

## How to use

Check out [StartupExampleDummy.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/StartupExampleDummy.java) or [JavaCounter.java](https://github.com/jTendermint/jabci/blob/master/src/main/java/com/github/jtendermint/jabci/JavaCounter.java) for examples.

#### Maven integration
jABCI is available via central's snapshot repository. A release version should be available soon.
use the following dependency to include jABCI in your project:
```xml
            <dependency>
                <groupId>com.github.jtendermint</groupId>
                <artifactId>jabci</artifactId>
                <version>0.9.0-SNAPSHOT</version>
                <scope>compile</scope>
                <!-- you might want to exclude slf4j, depending on your setup -->
                <exclusions>
                    <exclusion>
                        <groupId>org.slf4j</groupId>
                        <artifactId>slf4j-simple</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
```

#### Update protobuf types

When you make changes to the protobuf file, you can enable the appropriate build-phase for the compiler-plugin to generate the file(s) in the target-directory.
Just switch the comments on lines [pom.xml](https://github.com/jTendermint/jabci/blob/master/pom.xml#L27) and 28.




#### Looking for TMSP?
The Tendermint protocol was changed from TMSP to ABCI. While these where just minor changes, we wanted to completely reflect all the naming changes in this project. If you're still developing with Tendermint v0.8 and prior check out the [0.8 version of jTMSP](https://github.com/jTendermint/jabci/releases/tag/v0.8)
