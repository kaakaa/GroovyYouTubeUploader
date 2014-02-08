package org.kaakaa.google.api.youtube.upload.convert

class ResourceDirectory {
  private File rootDir 
  private File conbinedMp3
  private File outputFile
  private File thumbnail

  private static final String sep = System.getProperty('file.separator')

  def ResourceDirectory(String rootDir){
    new File(rootDir, 'work').mkdir()

    this.rootDir = new File(rootDir)
    this.conbinedMp3 = new File(rootDir, "work${sep}${this.rootDir.name}.mp3")
    this.outputFile = new File(rootDir, "work${sep}${this.rootDir.name}.mkv")
    this.thumbnail = new File(rootDir, "thumbnail.jpg")
    if(!this.thumbnail.exists()){
      def encodedPath = getClass().getResource('/default.jpg').path
      this.thumbnail = new File(URLDecoder.decode(encodedPath))
    }
  }
	
  def output(){
    def mp3List = this.rootDir.listFiles(mp3Filter).collect{ it.absolutePath }
    exec("ffmpeg -y -i concat:${mp3List.join('|')} -acodec copy ${this.conbinedMp3}")
    exec("ffmpeg -y -loop 1 -r 2 -i ${this.thumbnail.absolutePath} -i ${this.conbinedMp3} -c:v libx264 -preset medium -tune stillimage -crf 18 -c:a copy -shortest -pix_fmt yuv420p ${this.outputFile}")
  }

  private FileFilter getMp3Filter(){
    return { it.name.endsWith(".MP3") || it.name.endsWith(".mp3") } as FileFilter
  }

  def exec(String cmd){
    println "Excecure cmmand => ${cmd}"
    ProcessBuilder pb = new ProcessBuilder(cmd.split(' '))
    pb.redirectErrorStream(true)
    def process = pb.start()
    while(process.inputStream.read() >= 0);

    if(process.exitValue()){
      println "error"
      process.err.eachLine{ println it }
    } else {
      println "success"
      process.in.eachLine{ println it }
    }
  }
}
