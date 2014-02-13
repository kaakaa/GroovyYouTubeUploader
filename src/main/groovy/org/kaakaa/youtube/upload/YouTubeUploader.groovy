package org.kaakaa.youtube.upload

import org.kaakaa.youtube.upload.convert.ResourceDirectory

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
    def resources = new ResourceDirectory(new File(dir).absolutePath)
    resources.output()
    def param = resources.createParameter()
    // def param = new UploadParameter(dir, videoFile)
    new UploadVideo().upload(param)
  }
}
