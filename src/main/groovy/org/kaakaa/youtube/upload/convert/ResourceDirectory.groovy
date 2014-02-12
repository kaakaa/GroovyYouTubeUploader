package org.kaakaa.youtube.upload.convert

import org.kaakaa.youtube.upload.FFmpegExecutor
import org.kaakaa.youtube.upload.cmd.factory.CommandFactory
import org.kaakaa.youtube.upload.cmd.factory.CombineCommandFactory
import org.kaakaa.youtube.upload.cmd.factory.ConvertToVideoCommandFactory

class ResourceDirectory {
  private File rootDir 
  private File combinedMp3
  private File outputFile
  private File thumbnail

  private static final String SEP = System.getProperty('file.separator')

  def ResourceDirectory(String rootDir){
    new File(rootDir, 'work').mkdir()

    this.rootDir = new File(rootDir)
    this.combinedMp3 = new File(rootDir, "work${SEP}${this.rootDir.name}.mp3")
    this.outputFile = new File(rootDir, "work${SEP}${this.rootDir.name}.mkv")
    this.thumbnail = new File(rootDir, "thumbnail.jpg")
    if(!this.thumbnail.exists()){
      def encodedPath = getClass().getResource('/default.jpg').path
      this.thumbnail = new File(URLDecoder.decode(encodedPath))
    }
  }
	
  def output(){
    List<String> mp3List = this.rootDir.listFiles(mp3Filter).collect{ it.absolutePath }
    CommandFactory combineCommand = new CombineCommandFactory(mp3List, this.combinedMp3)
    FFmpegExecutor.exec(combineCommand)

    CommandFactory convertToVideoCommand = new ConvertToVideoCommandFactory(this.combinedMp3, this.outputFile, this.thumbnail)
    FFmpegExecutor.exec(convertToVideoCommand)
    return this.outputFile
  }

  private FileFilter getMp3Filter(){
    return { it.name.endsWith(".MP3") || it.name.endsWith(".mp3") } as FileFilter
  }
}
