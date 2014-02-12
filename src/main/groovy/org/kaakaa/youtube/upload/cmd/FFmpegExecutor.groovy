package org.kaakaa.youtube.upload

import org.kaakaa.youtube.upload.cmd.factory.CommandFactory

class FFmpegExecutor {

  private static List<String> cmdHeader = ['ffmpeg', '-y']

  public static void exec(CommandFactory factory) {
    executeCommand(factory.makeCommand(this.cmdHeader.clone()))
  }

  private static void executeCommand(String cmd) {
    println "======== Excecute follow FFmpeg command ========"
    println cmd
    ProcessBuilder pb = new ProcessBuilder(cmd.split(' '))
    pb.redirectErrorStream(true)
    def process = pb.start()
    while(process.inputStream.read() >= 0);

    println ""
    if(process.exitValue()){
      println "Executing command is fail..."
      process.err.eachLine{ println it }
      println "======== Finish executing command ========"
    } else {
      println "Executing command is success!!"
      process.in.eachLine{ println it }
      println "======== Finish executing command ========"
    }
  }
}
