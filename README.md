# Dropwizard Debian Package

A maven plugin for packaging your Dropwizard Application as a Debian package. Your Application is managed as an Upstart job, with the service name `${project.artifactId}`.

[![Build Status](https://api.travis-ci.org/reines/dropwizard-debpkg-maven-plugin.png)](https://travis-ci.org/reines/dropwizard-debpkg-maven-plugin)

File paths are configurable, and default to:

- __jarFile__: `/usr/share/java/${project.artifactId}.jar`
- __configFile__: `/etc/${project.artifactId}.yml`
- __upstartFile__: `/etc/init/${project.artifactId}.conf`
- __logDirectory__: `/var/log/${project.artifactId}`

JVM parameters are also configurable, and default to:

- __memory__: `128m`
- __packageName__: `openjdk-7-jdk`
- __packageVersion__: `latest`

The unix environment is configurable, and defaults to:

- __user__: `dropwizard`

Your Application configuration file should be a Mustache template, with parameters passed to the plugin configuration injected at package time. Parameters are exposed prefixed with either `dw.` or `dropwizard.` (see usage, below). 

## Usage

### Sample plugin definition in project pom
    <plugin>
        <groupId>com.jamierf.dropwizard</groupId>
        <artifactId>dropwizard-debpkg-maven-plugin</artifactId>
        <version>${dropwizard-debpkg-maven-plugin.version}</version>
        <configuration>
            <configTemplate>${basedir}/src/config.yml</configTemplate>
            <jvm>
                <memory>2g</memory>
            </jvm>
            <dropwizard>
                <httpPort>8080</httpPort>
                <httpAdminPort>8081</httpAdminPort>
            </dropwizard>
        </configuration>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>dwpackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

### Sample Application configuration template

    # {{{project.name}}} - {{{project.description}}}
    # {{{project.artifactId}}} configuration

    server:
      applicationConnectors:
        - type: http
          port: {{{dw.httpPort}}}
      adminConnectors:
        - type: http
          port: {{{dw.httpAdminPort}}}

    logging:
      level: INFO

      appenders:
        - type: console
        - type: file
          currentLogFilename: "{{{path.logDirectory}}}/test.log"
          archive: false

## License

Released under the Apache 2.0 License
