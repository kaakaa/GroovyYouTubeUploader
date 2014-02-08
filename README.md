Groovy Youtube Uploader
=======================

This app combines some mp3 files, then upload video converted from combined mp3 file to YouTube.

This is derived from YouTube Data API sample.
[youtube-api-samples - YouTube API Sample Applications - Google Project Hosting](https://code.google.com/p/youtube-api-samples/source/browse/#git%2Fsamples%2Fjava%2Fyoutube-cmdline-uploadvideo-sample "youtube-api-samples - YouTube API Sample Applications - Google Project Hosting")

And, This app use ffmpeg for combining and convert mp3 file.
[FFmpeg](http://www.ffmpeg.org/ "FFmpeg")

Requirements
============

* Java 1.6 higher
* ffmpeg

Usage
=====

1. Clone this.

```
git clone https://github.com/kaakaa/GroovyYouTubeUploader.git
cd GroovyYouTubeUploader
```

2. Write your YouTube Data API in auth config file.

```
vi src/main/resources/client_secrets.json
```

if you don't have client_id/client_secret, regist your application here. 
  => [Google Developers Console](https://code.google.com/apis/console/?api=youtube "Google Developers Console")


3. Run app. you don't need Gradle because there is gradlew.

```
gradlew run -Pargs="SampleMusic"
```

Licenses
========

Copyright © 2013 kaakaa Distributed under the terms of MIT License.

YouTube Data API sample is distributed under the Apache License Version 2.0.
