package com.dfki.services.RmlMappingService.service;

import com.dfki.services.RmlMappingService.Utils;
import com.taxonic.carml.engine.RmlMapper;
import com.taxonic.carml.logical_source_resolver.JsonPathResolver;
import com.taxonic.carml.logical_source_resolver.XPathResolver;
import com.taxonic.carml.model.TriplesMap;
import com.taxonic.carml.util.RmlMappingLoader;
import com.taxonic.carml.vocab.Rdf;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.stereotype.Component;

@Component
public class RmlMappingService {

	private static final String MAPPING_FILE_SERVER = "Http://localhost:8801/mappingfile";
	private static final String XML_MAPPING_FILE = "xml_mapping.ttl";
	private static final String JSON_MAPPING_FILE = "transport_mapping.ttl";

	public RmlMappingService() {

	}

	public Model xmlToRdf(final String xmlString, final String mappingFileName) throws Exception {
		if (!Utils.isValidXml(xmlString)) {
			throw new IllegalArgumentException("Input was no valid XML");
		}
		String mappingFile = Utils.sendGetRequest(MAPPING_FILE_SERVER
								.concat("?filename=")
								.concat(mappingFileName));
		InputStream recordStream = new ByteArrayInputStream(xmlString.getBytes("utf-8"));
		InputStream mappingStream = new ByteArrayInputStream(mappingFile.getBytes("utf-8"));
		Model carmlModel = mapXMLWithCarml(mappingStream, recordStream);
		return carmlModel;
	}

	public Model jsonToRdf(final String jsonString, final String mappingFileName) throws Exception {
		if (!Utils.isValidJson(jsonString)) {
			throw new IllegalArgumentException("Input was no valid JSON");
		}
		String mappingFile = Utils.sendGetRequest(MAPPING_FILE_SERVER
								.concat("?filename=")
								.concat(mappingFileName));
		InputStream recordStream = new ByteArrayInputStream(jsonString.getBytes("utf-8"));
		InputStream mappingStream = new ByteArrayInputStream(mappingFile.getBytes("utf-8"));
		Model carmlModel = mapJSONWithCarml(mappingStream, recordStream);
		return carmlModel;
	}

	public Model mapJSONWithCarml(final InputStream mappingFileStream, final InputStream resourceStream) {
		Set<TriplesMap> mapping
			= RmlMappingLoader
				.build()
				.load(RDFFormat.TURTLE, mappingFileStream);
		RmlMapper mapper
			= RmlMapper
				.newBuilder()
				.setLogicalSourceResolver(Rdf.Ql.JsonPath, new JsonPathResolver())
				.build();
		mapper.bindInputStream(resourceStream);
		Model result = mapper.map(mapping);
		return result;
	}

	public Model mapXMLWithCarml(final InputStream mappingFileStream, final InputStream resourceStream) {
		Set<TriplesMap> mapping
			= RmlMappingLoader
				.build()
				.load(RDFFormat.TURTLE, mappingFileStream);
		RmlMapper mapper
			= RmlMapper
				.newBuilder()
				.setLogicalSourceResolver(Rdf.Ql.XPath, new XPathResolver())
				.build();
		mapper.bindInputStream(resourceStream);
		Model result = mapper.map(mapping);
		return result;
	}
}
