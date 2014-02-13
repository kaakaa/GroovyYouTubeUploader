package org.kaakaa.youtube.upload.convert

import org.kaakaa.youtube.upload.UploadParameter
import org.kaakaa.youtube.upload.FFmpegExecutor
import org.kaakaa.youtube.upload.cmd.factory.CommandFactory
import org.kaakaa.youtube.upload.cmd.factory.CombineCommandFactory
import org.kaakaa.youtube.upload.cmd.factory.ConvertToVideoCommandFactory
import org.kaakaa.youtube.upload.cmd.factory.GetDurationCommandFactory

class ResourceDirectory {
  private File rootDir 
  private List<File> inputMp3s
  private File combinedMp3
  private File outputFile
  private File thumbnail

  private static final String SEP = System.getProperty('file.separator')

  def ResourceDirectory(String rootDir){
    new File(rootDir, 'work').mkdir()

    this.rootDir = new File(rootDir)
    this.inputMp3s = this.rootDir.listFiles(mp3Filter)
    this.combinedMp3 = new File(rootDir, "work${SEP}${this.rootDir.name}.mp3")
    this.outputFile = new File(rootDir, "work${SEP}${this.rootDir.name}.mkv")
    this.thumbnail = new File(rootDir, "thumbnail.jpg")
    if(!this.thumbnail.exists()){
      def encodedPath = getClass().getResource('/default.jpg').path
      this.thumbnail = new File(URLDecoder.decode(encodedPath))
    }
  }
	
  def output(){
    List<String> mp3List = this.inputMp3s.collect{ it.absolutePath }
    CommandFactory combineCommand = new CombineCommandFactory(mp3List, this.combinedMp3)
    FFmpegExecutor.exec(combineCommand)

    CommandFactory convertToVideoCommand = new ConvertToVideoCommandFactory(this.combinedMp3, this.outputFile, this.thumbnail)
    FFmpegExecutor.exec(convertToVideoCommand)

    return this.outputFile
  }

  def createParameter() {
    def param = new UploadParameter(this.rootDir, this.outputFile) 
    param.addDurationDescription(getDurationDescription())
    return param
  }

  private Map getDurationDescription() {
    final LINE_SEP = System.getProperty('line.separator')
    def durations = [:]
    inputMp3s.each{ mp3File ->
      CommandFactory durationCommand = new GetDurationCommandFactory(mp3File)
      String result = FFmpegExecutor.exec(durationCommand)

      def lines = result.split(LINE_SEP).findAll { it.indexOf('Duration: ') >= 0 }
      if(lines.size() == 1) {
        def map = [:]
        lines[0].split(',').each {
          def elems = it.split(':')
          map.put(elems[0].trim(), elems[1..-1].join(':').trim())
        }
        durations.put(mp3File.name, map.get('Duration'))
      }
    }
    return durations
  }

  private FileFilter getMp3Filter(){
    return { it.name.endsWith(".MP3") || it.name.endsWith(".mp3") } as FileFilter
  }
}
