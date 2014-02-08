package org.kaakaa.google.api.youtube.upload

class YouTubeUploader {
	public static void main(args){
		args = ["../hoge.mkv"]
		def confPath = "sample/conf.json"

		def param = new UploadParameter(confPath)
		new UploadVideo().upload(param)
	}
}
