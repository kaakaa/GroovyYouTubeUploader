package org.kaakaa.google.api.youtube.upload
import groovy.json.JsonSlurper

import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus;
import com.google.api.services.youtube.model.Video;

class UploadParameter {

  private HashMap param = null;

  def UploadParameter(String path) {
    def inputFile = new File(path)
    this.param = new JsonSlurper().parseText(inputFile.text)
    this.param.put("WorkingDirectory", inputFile.getParent())
  }

  def get(String key) {
    param.get(key)
  }

  def setParameter(Video video) {
    video.setStatus(getVideoStatus())
    video.setSnippet(getVideoSnippet())
    return video
  }

  def getVideoFile() {
    return new File(param.get("UploadFile"))
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
