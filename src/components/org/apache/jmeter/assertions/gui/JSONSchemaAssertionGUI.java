package org.apache.jmeter.assertions;

import java.io.IOException;
import java.io.Serializable;

import com.github.fge.jsonschema.core.report.LogLevel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;


/**
 * This class (JSONSchemaAssertion.java) allows to validate response against a JSON schema.
 * Created by Denis Krasilnikov (kde-intro) on 06.03.2017.
 *
 */
public class JSONSchemaAssertion extends AbstractTestElement implements Serializable, Assertion {

    private static final long serialVersionUID = 234L;
    public static final String FILE_NAME_IS_REQUIRED = "FileName is required";
    public static final String JSD_FILENAME_KEY = "jsonschema_assertion_filename";
    private static final Logger log = LoggerFactory.getLogger(JSONSchemaAssertion.class);


    /**
     * Get assertion result.
     *
     * @return
     */
    @Override
    public AssertionResult getResult(SampleResult response) {
        AssertionResult result = new AssertionResult(getName());       

        String resultData = response.getResponseDataAsString();
        if (resultData.length() == 0) {
            return result.setResultForNull();
        }

        String jsdFileName = getJsdFileName();
        log.debug("jsonString: {}, jsonFileName: {}", resultData, jsdFileName);
        if (jsdFileName == null || jsdFileName.length() == 0) {
            result.setResultForFailure(FILE_NAME_IS_REQUIRED);
        } else {
            setSchemaResult(result, resultData, jsdFileName);
        }

        return result;
    }

    public void setJsdFileName(String jsonSchemaFileName) throws IllegalArgumentException {
        setProperty(JSD_FILENAME_KEY, jsonSchemaFileName);
    }

    public String getJsdFileName() {
        return getPropertyAsString(JSD_FILENAME_KEY);
    }


    /**
     * Set schema result.
     *
     * @param result
     * @param jsonStr
     * @param jsdFileName
     */
    public void setSchemaResult(AssertionResult result, String jsonStr, String jsdFileName) {
        try {
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonNode schemaFile = JsonLoader.fromPath(jsdFileName);
            JsonSchema schema = factory.getJsonSchema(schemaFile);

            ProcessingReport response = schema.validate(JsonLoader.fromString(jsonStr));
            if (!response.isSuccess()) {
                result.setResultForFailure(response.toString());
            }
        } catch (ProcessingException e) {
            if (log.isWarnEnabled()) {
                log.warn(e.getMessage());
            }

            result.setFailureMessage(e.getMessage());

            LogLevel level = e.getProcessingMessage().getLogLevel();
            if ( (level == LogLevel.FATAL) || (level == LogLevel.ERROR) ) {
                result.setError(true);
            }
        } catch (IOException e) {
            log.warn("IO error", e);
            result.setResultForFailure(e.getMessage());
        }
    }

}
