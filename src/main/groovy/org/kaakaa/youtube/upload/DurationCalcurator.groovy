package org.kaakaa.youtube.upload

class DurationCalcurator {

  private static int hour = 0
  private static int minuite = 0
  private static int second = 0
  private static int milliSecond = 0

  public static String total(String time) {
    def result =  String.format('%d:%02d:%02d', this.hour, this.minuite, this.second)
    def elems = time.split(':')
    def secElems = elems[-1].split(/\./)

    this.milliSecond = this.milliSecond + Integer.parseInt(secElems[-1])
    this.second = this.second + Integer.parseInt(secElems[0])
    this.minuite = this.minuite + Integer.parseInt(elems[1])
    this.hour = this.hour + Integer.parseInt(elems[0])

    if(this.milliSecond >= 100) {
      this.milliSecond %= 100
      this.second++
    }
    if(this.second >= 60) {
      this.second %= 60
      this.minuite++
    }
    if(this.minuite >= 60) {
      this.minuite %= 60
      this.hour++
    }

    return result
  }
}
