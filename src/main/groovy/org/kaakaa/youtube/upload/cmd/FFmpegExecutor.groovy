package org.kaakaa.youtube.upload

import org.kaakaa.youtube.upload.cmd.factory.CommandFactory

class FFmpegExecutor {

  private static List<String> cmdHeader = ['ffmpeg', '-y']

  public static String exec(CommandFactory factory) {
    return executeCommand(factory.makeCommand(this.cmdHeader.clone()))
  }

  private static String executeCommand(String cmd) {
    println "======== Excecute follow FFmpeg command ========"
    println cmd
    ProcessBuilder pb = new ProcessBuilder(cmd.split(' '))
    pb.redirectErrorStream(true)
    def process = pb.start()

    def out = new StringBuilder()
    def err = new StringBuilder()
    process.waitForProcessOutput(out, err)
    // while(process.inputStream.read() >= 0);

    println ""
    if(process.exitValue()){
      println "Executing command is fail..."
      if(err) println "err => $err"
    } else {
      println "Executing command is success!!"
    }
    println "======== Finish executing command ========"

    return out
  }
}
