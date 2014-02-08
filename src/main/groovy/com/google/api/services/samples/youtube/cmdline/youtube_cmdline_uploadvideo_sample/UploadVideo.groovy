/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License") you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.api.services.samples.youtube.cmdline.youtube_cmdline_uploadvideo_sample

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import com.google.common.collect.Lists

/**
 * Demo of uploading a video to a user's account using the YouTube Data API (V3) with OAuth2 for
 * authorization.
 *
 *  TODO: PLEASE NOTE, YOU MUST ADD YOUR VIDEO FILES TO THE PROJECT FOLDER TO UPLOAD THEM WITH THIS
 * APPLICATION!
 *
 * @author Jeremy Walker
 */
public class UploadVideo {

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport()

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory()

  /* Global instance of the format used for the video being uploaded (MIME type). */
  private static String VIDEO_FILE_FORMAT = "video/*"

  /**
   * Authorizes the installed application to access user's protected data.
   *
   * @param scopes list of scopes needed to run youtube upload.
   */
  private Credential authorize(List<String> scopes) throws Exception {

    // Load client secrets.
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
        JSON_FACTORY, UploadVideo.class.getResourceAsStream("/client_secrets.json"))

    // Checks that the defaults have been replaced (Default = "Enter X here").
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      println "Enter Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
      println "into youtube-cmdline-uploadvideo-sample/src/main/resources/client_secrets.json"
      System.exit(1)
    }

    // Set up file credential store.
    FileCredentialStore credentialStore = new FileCredentialStore(
        new File(System.getProperty("user.home"), ".credentials/youtube-api-uploadvideo.json"),
        JSON_FACTORY)

    // Set up authorization code flow.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialStore(credentialStore)
        .build()

    // Build the local server and bind it to port 9000
    LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build()

    // Authorize.
    return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user")
  }

  /**
   * Uploads user selected video in the project folder to the user's YouTube account using OAuth2
   * for authentication.
   *
   * @param param Video file parameter
   */
  def void upload(UploadParameter param) {
    // Scope required to upload to YouTube.
    List<String> scopes = ["https://www.googleapis.com/auth/youtube.upload"]

    try {
      // Authorization.
      Credential credential = authorize(scopes)

      // YouTube object used to make all API requests.
      def youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
          "YouTubeUploader").build()

      // We get the user selected local video file to upload.
      File videoFile = param.getVideoFile()
      println "You chose ${videoFile} to upload."

      // Add extra information to the video before uploading.
      Video videoObjectDefiningMetadata = new Video()
      param.setParameter(videoObjectDefiningMetadata)

      InputStreamContent mediaContent = new InputStreamContent(
          VIDEO_FILE_FORMAT, videoFile.newInputStream())
      mediaContent.setLength(videoFile.length())

      /*
       * The upload command includes: 1. Information we want returned after file is successfully
       * uploaded. 2. Metadata we want associated with the uploaded video. 3. Video file itself.
       */
      YouTube.Videos.Insert videoInsert = youtube.videos()
          .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent)

      // Set the upload type and add event listener.
      MediaHttpUploader mediaHttpUploader = videoInsert.getMediaHttpUploader()

      /*
       * Sets whether direct media upload is enabled or disabled. True = whole media content is
       * uploaded in a single request. False (default) = resumable media upload protocol to upload
       * in data chunks.
       */
      mediaHttpUploader.setDirectUploadEnabled(false)

      MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
        public void progressChanged(MediaHttpUploader uploader) throws IOException {
          switch (uploader.getUploadState()) {
            case MediaHttpUploader.UploadState.INITIATION_STARTED:
              println "Initiation Started"
              break
            case MediaHttpUploader.UploadState.INITIATION_COMPLETE:
              println "Initiation Completed"
              break
            case MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS:
              println "Upload in progress"
              println "Upload percentage: ${uploader.getProgress()}"
              break
            case MediaHttpUploader.UploadState.MEDIA_COMPLETE:
              println "Upload Completed!"
              break
            case MediaHttpUploader.UploadState.NOT_STARTED:
              println "Upload Not Started!"
              break
          }
        }
      }
      mediaHttpUploader.setProgressListener(progressListener)

      // Execute upload.
      Video returnedVideo = videoInsert.execute()

      // Print out returned results.
      println "\n================== Returned Video ==================\n"
      println "  - Id: ${returnedVideo.getId()}"
      println "  - Title: ${returnedVideo.getSnippet().getTitle()}"
      println "  - Tags: ${returnedVideo.getSnippet().getTags()}"
      println "  - Privacy Status: ${returnedVideo.getStatus().getPrivacyStatus()}"
      println "  - Video Count: ${returnedVideo.getStatistics().getViewCount()}"

    } catch (GoogleJsonResponseException e) {
      System.err << "GoogleJsonResponseException code: ${e.getDetails().getCode()} : ${e.getDetails().getMessage()}"
      e.printStackTrace()
    } catch (IOException e) {
      System.err << "IOException: ${e.getMessage()}"
      e.printStackTrace()
    } catch (Throwable t) {
      System.err << "Throwable: ${t.getMessage()}"
      t.printStackTrace()
    }
  }
}
