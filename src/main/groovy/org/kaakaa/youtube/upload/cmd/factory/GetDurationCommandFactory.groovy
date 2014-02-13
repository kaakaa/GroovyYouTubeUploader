package org.kaakaa.youtube.upload.cmd.factory

class GetDurationCommandFactory implements CommandFactory {

  private File file;

  public GetDurationCommandFactory(File file) {
    this.file = file
  }

  public String makeCommand(List<String> list){
    list << '-i' << this.file.absolutePath
    return list.join(' ')
  } 
}
