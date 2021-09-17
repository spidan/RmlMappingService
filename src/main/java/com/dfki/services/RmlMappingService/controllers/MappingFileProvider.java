package com.dfki.services.RmlMappingService.controllers;

import io.micrometer.core.instrument.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;

@RestController()
public class MappingFileProvider {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MappingFileProvider.class);

	@GetMapping(value = "/mappingfile", produces = {"text/turtle"})
	public ResponseEntity<?> getMappingFile(@RequestParam final String filename) {
		File mappingFile = new File(getMappingFileDir().concat(filename));
		try (InputStream mappingStream = new FileInputStream(mappingFile)) {
			return new ResponseEntity<>(IOUtils.toString(mappingStream), HttpStatus.OK);
		} catch (FileNotFoundException ex) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		} catch (IOException ex) {
			return new ResponseEntity<>("Error processing file: " + ex.getMessage(),
							HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/mappingfile", consumes = {"text/turtle"})
	public ResponseEntity<?> postMappingFile(@RequestBody final String mapping,
						@RequestParam final String filename) {
		File mappingFile = new File(getMappingFileDir().concat(filename));
		if (!mappingFile.getParentFile().mkdirs()) {
			if (!Files.exists(Paths.get(getMappingFileDir()))) {
				return new ResponseEntity<>("Could not create mapping file folder",
								HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		try (OutputStream outStream = new FileOutputStream(mappingFile)) {
			outStream.write(mapping.getBytes("utf-8"));
			return new ResponseEntity<>("Worked", HttpStatus.OK);
		} catch (FileNotFoundException ex) {
			LOG.error("Could not open file: " + ex.getMessage());
			return new ResponseEntity<>("Error opening file: " + ex.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IOException ex) {
			LOG.error("Error saving file: " + ex.getMessage());
			return new ResponseEntity<>("Error opening file: " + ex.getMessage(),
							HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String getMappingFileDir() {
		String workingDirPath = System.getProperty("user.dir");
		String targetDirPath = workingDirPath.concat("/mappingfiles/");
		return targetDirPath;
	}
}
