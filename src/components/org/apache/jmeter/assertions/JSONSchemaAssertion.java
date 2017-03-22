package org.apache.jmeter.assertions;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;


/**
 * This class (JSONSchemaAssertion.java) allows to validate a response against an JSON Schema.
 */
public class JSONSchemaAssertion extends AbstractTestElement implements Serializable, Assertion {

    private static final long serialVersionUID = 234L;
    public static final String FILE_NAME_IS_REQUIRED = "FileName is required";
    public static final String JSD_FILENAME_KEY = "jsonschema_assertion_filename";
    private static final Logger log = LoggerFactory.getLogger(JSONSchemaAssertion.class);
    private String errObj = null;


    /**
     * Get assertion result.
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
     * Set validation result.
     */
    public void setSchemaResult(AssertionResult result, String jsonStr, String jsdFileName) {
        try {
            /* IO error 'Unexpected character' can be returned for a schema in case of blank schema (failure)
                and response in cases of trailing comma (error - the JSON specification does not allow a trailing comma)
                */
            JsonSchema schema;
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

            errObj = "(responce) ";
            JsonNode response = JsonLoader.fromString(jsonStr);
            errObj = "(schema) ";
            JsonNode schemaFile = JsonLoader.fromPath(jsdFileName);
            schema = factory.getJsonSchema(schemaFile);
            errObj = null;
            ProcessingReport report = schema.validate(response);
            if (!report.isSuccess()) {
                result.setFailureMessage(report.toString());
                result.setError(true);
            } // If response.isSuccess() then json schema validation is Ok.
        } catch (IOException e) {
            log.warn("IO error", e);
            result.setFailureMessage( errObj == null ? "" : errObj + e.getMessage() );

            if (( e.getMessage().indexOf("no JSON Text to read from input") >= 0 ) || ( e.getMessage().indexOf("input has trailing data after first JSON Text") >=0 ) || ( e.getMessage().indexOf("Unexpected character") >= 0 )) {
                result.setError(true);
            } else {
                result.setFailure(true);
            }
        } catch (ProcessingException e) {
            if (log.isWarnEnabled()) {
                log.warn(e.getMessage());
            }

            result.setFailureMessage(errObj == null ? "" : errObj + e.getMessage());
            LogLevel level = e.getProcessingMessage().getLogLevel();

            if (level == LogLevel.ERROR) {
                result.setError(true);
            }
            if (level == LogLevel.FATAL) {
                result.setFailure(true);
            }
            if (!result.isError() && !result.isFailure()) {
                result.setFailure(true);
                result.setFailureMessage("Something went wrong or something strange happened: " + e.getMessage());
            }
        }
    }

}
