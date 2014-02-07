package com.google.api.services.samples.youtube.cmdline.youtube_cmdline_uploadvideo_sample;

import groovy.json.JsonSlurper

class UploadParameter {

	private HashMap param = null;

	def UploadParameter(String path) {
		def inputFile = new File(path)
		this.param = new JsonSlurper().parseText(inputFile.text)
	}

	def get(String key) {
		param.get(key)
	}
}
