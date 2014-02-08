package org.kaakaa.google.api.youtube.upload

import org.kaakaa.google.api.youtube.upload.convert.MakeVideo

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
    new MakeVideo().make(dir)    

    def param = new UploadParameter(dir)
    new UploadVideo().upload(param)
  }
}
