# qbittorrent exporter
A kotlin-based, prometheus exporter for qBittorrent that relies on qBittorrent's web ui. This
exporter exposes all statistics available through the qBt web ui, but also provides in-depth
configuration of each available metric.

#### Huh?
 - [qBittorrent](https://www.qbittorrent.org/) - a "free and reliable P2P bittorrent client" that's
   open source.
 - [Prometheus](https://prometheus.io/) - a feature-complete metrics database that's open source.
 - Exporter - with respect to prometheus, a web server that exposes metrics.

## Download / Usage
#### Download
Grab the [latest release](https://github.com/fru1tstand/qbittorrent-exporter/releases).

Or compile from source by running `./gradlew fatJar` in the project root. This will produce a jar
file called `qbt-exporter-fat-<version>.jar` in `build/libs`.

#### Usage
Run `java -jar <qbt-exporter.jar>`.

#### Notes
 + It's advisable to `cd` to the same directory as the jar before running. A lot of paths within the
 application are relative, which means the paths will resolve from the directory the run command is
 executed (and not from the location of the jar itself).
 + To run this application on startup, it's advisable to create a `run.sh` that first `cd`s into the
 jar location before execution and run that script in `Startup Applications` to mitigate the effects
 of the first note.

## Features
tbd

## Configuration
Settings are stored in json format with each collector having its own toggle. **All collectors are
disabled by default**.

To generate a new settings file, rename or delete `qbt-exporter-settings.json` if the file exists,
then start the program. A new settings file will be generated in the same folder the start command
was executed in (the absolute path for this will be printed on application startup).

 + `exporterServerSettings`
   + `port` - The port the exporter server should listen on. This cannot be a wildcard. Defaults to
   `9561`.
   + `host` - The host the exporter server should listen on. Setting to the default of `0.0.0.0`
   will make the server listen on all hosts.
 + `qbtSettings`
   + `webUiAddress` - The address (protocol, host, and port) the qBittorrent web UI is hosted at.
   Defaults to `http://localhost:8080`.
 + `collectorSettings` - This list is dynamically generated with all available collectors. Set
 `true` to enable the collector. Set `false` or delete the line to disable the collector.
 See #Features for a description of each collector. Note it's possible for a collector to not
 produce an output if its value is `0`.
