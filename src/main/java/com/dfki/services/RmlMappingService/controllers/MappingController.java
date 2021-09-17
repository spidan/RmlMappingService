package com.dfki.services.RmlMappingService.controllers;

import com.dfki.services.RmlMappingService.service.RmlMappingService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MappingController {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MappingController.class);
	private static final String BASE_URI = "http://SmartMaaS.eu/ticketservice";

	@Autowired
	private RmlMappingService mappingService;

	@PostMapping(value = "/maptordf", consumes = {"application/json"})
	public ResponseEntity<?> mapJsonToRdf(@RequestParam final String mappingFile,
						@RequestBody final String input) throws IOException {
		try {
			Model result = mappingService.jsonToRdf(input, mappingFile);
			OutputStream turtleOutput = modelToTtl(result);
			return new ResponseEntity<>(turtleOutput.toString(), HttpStatus.OK);
		} catch (Exception ex) {
			LOG.error("Error processing input: " + ex.toString());
			return new ResponseEntity<>("Invalid input: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/maptordf", consumes = {"text/xml"})
	public ResponseEntity<?> mapXmlToRdf(@RequestParam final String mappingFile,
						@RequestBody final String input) throws IOException {
		try {
			Model result = mappingService.xmlToRdf(input, mappingFile);
			OutputStream turtleOutput = modelToTtl(result);
			return new ResponseEntity<>(turtleOutput.toString(), HttpStatus.OK);
		} catch (Exception ex) {
			LOG.error("Error processing input: " + ex.toString());
			return new ResponseEntity<>("Invalid input: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	private OutputStream modelToTtl(final Model result) throws UnsupportedRDFormatException, RDFHandlerException {
		OutputStream turtleOutput = new ByteArrayOutputStream();
		RDFWriter rdfWriter = Rio.createWriter(RDFFormat.TURTLE,
			turtleOutput);
		rdfWriter.startRDF();
		for (Statement st: result) {
			rdfWriter.handleStatement(st);
		}
		rdfWriter.endRDF();
		return turtleOutput;
	}
}
