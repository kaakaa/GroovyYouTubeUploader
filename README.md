Groovy Youtube Uploader
=======================

This app combines some MP3 files, then upload video converted from combined MP3 file to YouTube.

This is derived from YouTube Data API sample.  
[youtube-api-samples - YouTube API Sample Applications - Google Project Hosting](https://code.google.com/p/youtube-api-samples/source/browse/#git%2Fsamples%2Fjava%2Fyoutube-cmdline-uploadvideo-sample "youtube-api-samples - YouTube API Sample Applications - Google Project Hosting")

And, This app use FFmpeg for combining and converting MP3 file.  
[FFmpeg](http://www.ffmpeg.org/ "FFmpeg")

Requirements
============

* Java 1.6 or higher
* FFmpeg 1.2

Usage
=====

#### Clone this.

```
git clone https://github.com/kaakaa/GroovyYouTubeUploader.git
cd GroovyYouTubeUploader
```

#### Write your YouTube Data API in auth config file.

```
vi src/main/resources/client_secrets.json
```

if you don't have client_id/client_secret, regist your application here.   
  => [Google Developers Console](https://code.google.com/apis/console/?api=youtube "Google Developers Console")


#### Prepare resource for upload.

Sample resource is in SampleMusic directory.  
You must prepare some MP3 files and upload_conf.json.  

YouTube Video Infomation is written in upload_conf.json.  
The content of upload_conf.json show in the following.  

If you specify thumbnail image, you must place the image file naming "thumbnail.jpg" in resource directory.  

When You don't specify thumbnail image, be used default image.  
![alt DefaultThumbnail](https://raw2.github.com/kaakaa/GroovyYouTubeUploader/master/src/main/resources/default.jpg "DefaultThumbnail")

#### Run app. you don't need Gradle because there is gradlew.

  ```
  gradlew run -Pargs="SampleMusic"
  ```

Configuration
=============

You can set YouTube Video Infomation in upload_conf.json.  
If you don't set any item or don't write upload_conf.json, default value is used.  


- Title
  - Upload Video Title
  - DEFAULT: upload directory name(ex. SampleMusic)
- Description
  - Description about upload video
  - DEFAULT: "This video was uploaded by YouTubeUploader."
- Tags
  - Video tags
  - DEFAULT: ['YouTubeUploader']
- PrivacyStatus
  - Upload video's privacy status
    - public
    - unlisted
    - private
  - DEFAULT: private

Licenses
========

Copyright © 2013 kaakaa Distributed under the terms of MIT License.

YouTube Data API sample is distributed under the Apache License Version 2.0.
