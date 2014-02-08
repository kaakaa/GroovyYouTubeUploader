package org.kaakaa.google.api.youtube.upload

import groovy.json.JsonSlurper

import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus;
import com.google.api.services.youtube.model.Video;

class UploadParameter {

  private HashMap param = [:]

  def UploadParameter(String path) {
    def inputFile = new File(path, "upload_conf.json")
    if(inputFile.exists()) {
      this.param = new JsonSlurper().parseText(inputFile.text)
    }
    fillNecessaryParam(path)

    this.param.put("WorkingDirectory", inputFile.getParent())
  }

  def get(String key) {
    param.get(key)
  }

  def setParameter(Video video) {
    println this.param
    video.setStatus(getVideoStatus())
    video.setSnippet(getVideoSnippet())
    return video
  }

  def getVideoFile() {
    return new File(param.get("UploadFile").toString())
  }

  private void fillNecessaryParam(String path) {
    createDefaultParam(path).each { entry ->
      if(this.param.get(entry.key) == null){
        this.param.put(entry.key, entry.value)
      }
    }
  }

  private HashMap createDefaultParam(path) {
    def fileSeparator = System.getProperty('file.separator')
    if(!path.endsWith(fileSeparator)) {
      path += fileSeparator
    }
    def rootName = new File(path).name
    def map = new HashMap()
    map.put('Title', rootName)
    map.put('Description', 'This video was uploaded by YouTubeUploader.')
    map.put('Tags', ['YouTubeUploader'])
    map.put('PrivacyStatus', 'private')
    map.put('UploadFile', "${path}work/${rootName}.mkv")
    return map
  }

  private VideoStatus getVideoStatus() {
    /*
     * Set the video to public, so it is available to everyone (what most people want). This is
     * actually the default, but I wanted you to see what it looked like in case you need to set
     * it to "unlisted" or "private" via API.
    */
    VideoStatus status = new VideoStatus()
    status.setPrivacyStatus(param.get("PrivacyStatus"))
    return status
  }

  private VideoSnippet getVideoSnippet() {
    VideoSnippet snippet = new VideoSnippet();

    snippet.setTitle(param.get("Title"))
    snippet.setDescription(param.get("Description"))
    snippet.setTags(param.get("Tags"));

    return snippet
  }
}
