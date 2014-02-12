package org.kaakaa.youtube.upload.convert

class MakeVideo {
  def make(mp3DirPath) {
    def path = new File(mp3DirPath).absolutePath
    return new ResourceDirectory(path).output()
  }
}
