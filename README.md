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

**Warning:** If you're polling faster than once every 1500ms, you must change qBittorrent settings.
See the FAQ for details.

#### Notes
 + It's advisable to `cd` to the same directory as the jar before running. A lot of paths within the
 application are relative, which means the paths will resolve from the directory the run command is
 executed (and not from the location of the jar itself).
 + To run this application on startup, it's advisable to create a `run.sh` that first `cd`s into the
 jar location before execution and run that script in `Startup Applications` to mitigate the effects
 of the first note.

## Features
 + Metrics-complete exporter with settings for each collector (listed below).
 + On-the-fly settings loading (no need to restart the exporter after every settings change).
 + Automatic omission (removes sliced metrics that return `0`) to save storage space.
 + Beautiful home page design.

#### Collectors
```
Server State
  allTimeDownloadBytes - The total number of downloaded bytes from all torrents (including deleted ones) across all sessions.
  allTimeUploadBytes - The total number of uploaded bytes from all torrents (including deleted ones) across all sessions.
  allTimeShareRatio - The share ratio calculated by taking the all time upload bytes divided by the all time download bytes and rounding to two decimal places.
  diskAverageCacheQueueTimeMs - The average time an I/O disk request stays in the queue in milliseconds.
  diskCacheSizeBytes - The number of used bytes (not the maximum) for the disk cache.
  diskFreeSpaceBytes - The number of bytes available in the default qBittorrent save path.
  diskQueueJobCount - The number of disk jobs queued up on the disk cache.
  diskQueueSizeBytes - The number of bytes pending to be flushed to/from disk to/from the disk cache.
  diskReadCacheHitPercent - The percent (rounded to two decimal places) of disk read requests that were handled by the read cache.
  diskReadCacheOverloadPercent - The percent of connected peers that are issuing read requests (from disk or cache). Literally, the number of peers utilizing disk read I/O divided by the total number of peers connected.
  diskWriteCacheOverloadPercent - The percent of connected peers that are issuing write requests (from disk or cache). Literally, the number of peers utilizing disk write I/O divided by the total number of peers connected.
  networkDhtNodeCount - The number of DHT nodes connected.
  currentSessionDownloadedBytes - The number of bytes downloaded this session.
  currentSessionUploadedBytes - The number of bytes uploaded this session.
  currentSessionWastedBytes - The number of bytes wasted this session.
  networkPeerConnectionCount - The number of peers connected to.
  networkDownloadSpeedBytesPerSecond - The instantaneous download rate in bytes per second.
  networkDownloadSpeedCapBytesSerSecond - The user set maximum download rate in bytes per second.
  networkUploadSpeedBytesPerSecond - The instantaneous upload rate in bytes per second.
  networkUploadSpeedCapBytesPerSecond - The user set maximum upload rate allowed in bytes per second.
Torrents
  downloadRemainingBytes - The amount of bytes remaining to download, including those of unwanted files.
  completedBytes - The amount of 'actual' bytes completed from any source. 'Actual', meaning, blocks of data that have passed CRC and are verified to be non-corrupt. Any source as it's possible that a torrent could be pieced together from sources other than that of qbt.
  downloadPayloadRateBytesPerSecond - The download rate of the torrent's payload only (ie. doesn't include protocol chatter) in bytes per second.
  downloadTotalBytes - The number of downloaded bytes across all sessions including wasted data.
  downloadSessionBytes - The number of downloaded bytes during the current session including wasted data. A session is defined by torrent lifecycle (ie. when a torrent is stopped, its session is ended).
  seedersAvailable - The number of seeders seeding this torrent. A tracker's announce for the number of seeders takes precedence for this value; otherwise qBt will take a best guess by polling the swarm.
  seedersConnected - The number of seeders this client is connected to.
  leechersAvailable - The number of leechers downloading this torrent (excluding oneself, if applicable). A tracker's announce for the number of leechers takes precedence for this value; otherwise qBt will take a best guess by polling the swarm.
  leechersConnected - The number of leechers this client is connected to.
  ratio - The share ratio of this torrent. If a torrent is downloading, this ratio is calculated using the total download bytes, but if the torrent is fully downloaded, the ratio is calculated using the size of the torrent.
  activeTimeSeconds - The amount of time in seconds this torrent has spent "started" (that is, not complete and not paused).
  uploadTotalBytes - The number of uploaded bytes across all sessions including wasted data.
  uploadSessionBytes - The number of uploaded bytes including wasted data for the current session. A session is defined by when the torrent is paused (including qBittorrent client shutdown).
  uploadPayloadRateBytesPerSecond - The upload rate of the torrent's payload data only (ie. doesn't include protocol chatter) in bytes per second.
Aggregate Torrents
  downloadRemainingBytes - The total number of bytes remaining to download from all in-memory torrents, including those of unwanted files.
  completedBytes - The total number of 'actual' bytes completed from any source from all in-memory torrents. 'Actual' meaning blocks of data that have passed CRC and are verified to be non-corrupt. 'Any source' as it's possible that a torrent's files can be pieced together from outside qBittorrent.
  downloadPayloadRateBytesPerSecond - The summed download rate of all in-memory torrents' payload data.
  downloadTotalBytes - The number of downloaded bytes across all session from all in-memory torrents, including wasted data.
  downloadSessionBytes - The number of downloaded bytes from all in-memory torrents for this session, including wasted data.
  seedersAvailable - The total number of seeders from all in-memory torrents. Note this value can contain duplicate counts if a peer shares multiple torrents.
  leechersAvailable - The total number of leechers from all in-memory torrents. Note this value can contain duplicate counts if a peer shares multiple torrents.
  seedersConnected - The total number of seeders this client is connected to from all in-memory torrents. Note this value can contain duplicate counts if a peer shares multiple torrents.
  leechersConnected - The total number of leechers this client is connected to from all in-memory torrents. Note this value can contain duplicate counts if a peer shares multiple torrents.
  activeTimeSeconds - The total number of seconds all in-memory torrents have spent 'started' (that is, not complete and not paused).
  sizeWantedBytes - The number of bytes wanted by all in-memory torrents. This value omits unwanted files.
  sizeTotalBytes - The number of bytes of all in-memory torrents regardless of 'want'.
  uploadTotalBytes - The number of uploaded bytes across all sessions from all in-memory torrents, including wasted data.
  uploadSessionBytes - The number of uploaded bytes for the current session, including wasted data, for all in-memory torrents.
  uploadPayloadRateBytesPerSecond - The summed upload rate of all in-memory torrents' payload data.
```

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
 See #Features for a description of each collector.

## FAQ
 + **My metrics aren't updating faster than once every 1500ms**  
   qBittorrent's default web ui refresh rate is 1500ms. To poll faster, you must change advanced
   settings (which cannot be done with the web ui). The easiest way to do this is to use the desktop
   client `Tools > Options > Advanced > Transfer list refresh interval` and change the value to
   your polling frequency (it's not recommended to set this value lower than 1000ms as there's an
   [open bug](https://github.com/qbittorrent/qBittorrent/issues/8346) about the resulting values not
   being correct).
 + **I've enabled a collector but it's not showing up in `/metrics`**  
   If it's a sliced metric (note all `aggregate_torrent` metrics are sliced), automatic omission
   will prevent slices that return `0` from appearing. If all slices are omitted for a given metric,
   it will not show up in `/metrics`. This is done to save database disk space.
