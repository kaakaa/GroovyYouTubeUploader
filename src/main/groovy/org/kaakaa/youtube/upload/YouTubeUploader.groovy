package org.kaakaa.youtube.upload

import org.kaakaa.youtube.upload.convert.MakeVideo

class YouTubeUploader {
  public static void main(args) {
    args.each { dir ->
      if(!new File(dir).exists()){
        System.err << "Invalid argument : ${dir}"
      }
      process(dir)
    }
  }

  private static void process(String dir) {
    def videoFile = new MakeVideo().make(dir)    

    def param = new UploadParameter(dir, videoFile)
    new UploadVideo().upload(param)
  }
}
