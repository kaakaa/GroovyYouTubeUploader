package org.kaakaa.youtube.upload.cmd.factory

class CombineCommandFactory implements CommandFactory {

  private List<String> pathListForCombine = []
  private String outputPath

  public CombineCommandFactory(List<String> pathList, File outputFile) {
    this.pathListForCombine = pathList
    this.outputPath = outputFile.absolutePath
  }

  public String makeCommand(List<String> list){
    list << '-i' << "concat:${this.pathListForCombine.join('|')}"
    list << '-acodec' << 'copy' << this.outputPath
    return list.join(' ')
  } 
}
