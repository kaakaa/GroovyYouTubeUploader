package org.kaakaa.youtube.upload.cmd.factory

class ConvertToVideoCommandFactory implements CommandFactory {

  private String inputFilePath
  private String outputFilePath
  private String thumbnailFilePath

  def ConvertToVideoCommandFactory(File input, File output, File thumbnail){
    this.inputFilePath = input.absolutePath
    this.outputFilePath = output.absolutePath
    this.thumbnailFilePath = thumbnail.absolutePath
  }

  public String makeCommand(List<String> list) {
    list << '-loop' << '1' << '-r' << '2' << '-i' << this.thumbnailFilePath
    list << '-i' << this.inputFilePath
    list << '-c:v' << 'libx264' << '-preset' << 'medium' << '-tune' << 'stillimage' << '-crf' << '18'
    list << '-c:a' << 'copy' << '-shortest' << '-pix_fmt' << 'yuv420p' << this.outputFilePath
    // exec("ffmpeg -y -loop 1 -r 2 -i ${this.thumbnail.absolutePath} -i ${this.combinedMp3} -c:v libx264 -preset medium -tune stillimage -crf 18 -c:a copy -shortest -pix_fmt yuv420p ${this.outputFile}")
    return list.join(' ')
  }
}
