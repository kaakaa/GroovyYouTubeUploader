package org.kaakaa.google.api.youtube.upload.convert

class MakeVideo {
  def make(mp3DirPath) {
    def path = new File(mp3DirPath).absolutePath
    new ResourceDirectory(path).output()
  }
}