package com.dfki.services.RmlMappingService.service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.util.JenaUtil;

public final class ShaclValidator {

	private static final Logger LOG = LoggerFactory.getLogger(ShaclValidator.class);

	private ShaclValidator() {

	}
	public static boolean validateMappingResult(final String model,
				final String shapeFileName) throws UnsupportedEncodingException {

		Path path = Paths.get(".").toAbsolutePath().normalize();
		String shape = "file:" + path.toFile().getAbsolutePath() + "/shapeFiles/" + shapeFileName;
		Model dataModel = JenaUtil.createDefaultModel();
		dataModel.read(new ByteArrayInputStream(model.getBytes("utf-8")), null, "NT");
		Model shapeModel = JenaUtil.createDefaultModel();
		shapeModel.read(shape);

		Resource reportResource = ValidationUtil.validateModel(dataModel, shapeModel, true);
	reportResource.listProperties().forEachRemaining(p -> LOG.info(p.toString()));
		return reportResource.getProperty(SH.conforms).getBoolean();
	}

}
