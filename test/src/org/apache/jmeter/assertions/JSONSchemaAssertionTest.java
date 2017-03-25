package org.apache.jmeter.assertions;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Tests of JSON Schema Assertion (JSA).
 */
public class JSONSchemaAssertionTest extends JMeterTestCase {
    private JSONSchemaAssertion assertion;
    private SampleResult result;
    private JMeterContext jmctx;
    private AssertionResult res;

    @Before
    public void setUp() throws Exception {
        jmctx = JMeterContextService.getContext();
        assertion = new JSONSchemaAssertion();
        assertion.setThreadContext(jmctx);

        result = new SampleResult();
        JMeterVariables vars = new JMeterVariables();
        jmctx.setVariables(vars);
        jmctx.setPreviousResult(result);
    }

    private ByteArrayOutputStream readBA(String name) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(findTestFile(name)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        int len = 0;
        byte[] data = new byte[512];
        while ((len = bis.read(data)) >= 0) {
            baos.write(data, 0, len);
        }
        bis.close();
        return baos;
    }

    private byte[] readFile(String name) throws IOException {
        return readBA(name).toByteArray();
    }

    private AssertionResult testlog() {
        AssertionResult ares = assertion.getResult(jmctx.getPreviousResult());
        testLog.debug("isError() " + ares.isError() + " isFailure() " + ares.isFailure());
        testLog.debug("failure " + ares.getFailureMessage());

        return ares;
    }

    /* Test Cases:
        1 - [ok] Pass: Response - pass (empty valid responce: {}, [{}]), Schema - pass (empty valid schema: {}, {"$schema": "http://json-schema.org/draft-04/schema#"})
        2 - [error] Fail: Response has error (different types), Schema - pass
        3 - [failure] Incorrect JSON Schema: Response - pass, Schema has the undefined type value
        4 - [failure] No File Name: Response - pass, Schema - no value of File Name
        5 - [failure] No response result: Response - no response, Schema - pass
        6 - [failure] Empty response result: Response - empty, Schema - pass
        7 - [error] Blank response result: Response - blank, Schema - pass
        8 - [error] JSON trailing content result: Response has the trailing content, Schema - pass
        9 - [ok] JSON trailing whitespace result: Response has the trailing whitespace, Schema - pass
        13 - [failure] Invalid path to Schema: Response - pass, Schema - invalid file path format
        15 - [failure] Schema file not found: Response - pass, Schema - fake (nonexistent) file name, fake (nonexistent) path, access denied
     */

    @Test // 1 - Pass: Response - pass, Schema - pass
    public void testJSAPass() throws Exception {
        result.setResponseData(readFile("testfiles/JSA_JSONResponse_pass.json"));
        assertion.setJsdFileName(findTestPath("testfiles/JSA_JSONSchema_pass.json"));
        res = testlog();
        assertFalse(res.isError());
        assertFalse(res.isFailure());
    }

    @Test // 2 - Fail: Response has error (different types), Schema - pass
    public void testJSAFail() throws Exception {
        result.setResponseData(readFile("testfiles/JSA_JSONResponse_fail.json"));
        assertion.setJsdFileName("testfiles/JSA_JSONSchema_pass.json");
        res = testlog();
        assertTrue(res.isError());
        assertFalse(res.isFailure());
    }

    @Test // 3 - Incorrect JSON Schema: Response - pass, Schema has the undefined type value
    public void testJSAIncorrectSchema() throws Exception {
        result.setResponseData(readFile("testfiles/JSA_JSONResponse_pass.json"));
        assertion.setJsdFileName("testfiles/JSA_JSONSchema_bad.json");
        res = testlog();
        assertFalse(res.isError());
        assertTrue(res.isFailure());
        assertTrue(res.getFailureMessage().indexOf("invalid JSON Schema, cannot continue") >= 0);
    }

    @Test // 4 - No File Name: Response - pass, Schema - no value of File Name
    public void testJSANoFileName() throws Exception {
        // IO error
        result.setResponseData(readFile("testfiles/JSA_JSONResponse_pass.json"));
        assertion.setJsdFileName("");
        res = testlog();
        assertFalse(res.isError());
        assertTrue(res.isFailure());
        assertTrue(res.getFailureMessage().indexOf(JSONSchemaAssertion.FILE_NAME_IS_REQUIRED) >= 0);
    }

    @Test // 5 - No response result: Response - no response, Schema - pass
    public void testJSANoResult() throws Exception {
        assertion.setJsdFileName("testfiles/JSA_JSONSchema_pass.json");
        res = testlog();
        assertFalse(res.isError());
        assertTrue(res.isFailure());
        assertEquals(AssertionResult.RESPONSE_WAS_NULL, res.getFailureMessage());
    }

    @Test // 6 - Empty response result: Response - empty, Schema - pass
    public void testJSAEmptyResult() throws Exception {
        result.setResponseData("", null);
        assertion.setJsdFileName("testfiles/JSA_JSONSchema_pass.json");
        res = testlog();
        assertFalse(res.isError());
        assertTrue(res.isFailure());
        assertEquals(AssertionResult.RESPONSE_WAS_NULL, res.getFailureMessage());
    }

    @Test // 7 - Blank response result: Response - blank, Schema - pass
    public void testJSABlankResult() throws Exception {
        // IO error
        result.setResponseData(" ", null);
        assertion.setJsdFileName("testfiles/JSA_JSONSchema_pass.json");
        res = testlog();
        assertTrue(res.isError());
        assertFalse(res.isFailure());
        assertTrue(res.getFailureMessage().indexOf("no JSON Text to read from input") >= 0);
    }

    @Test // 8 - JSON trailing content result: Response has the trailing content, Schema - pass
    public void testJSATrailingContent() throws Exception {
        // IO error
        ByteArrayOutputStream baos = readBA("testfiles/JSA_JSONResponse_pass.json");
        baos.write("end".getBytes());
        result.setResponseData(baos.toByteArray());
        assertion.setJsdFileName(findTestPath("testfiles/JSA_JSONSchema_pass.json"));
        res = testlog();
        assertTrue(res.isError());
        assertFalse(res.isFailure());
        assertTrue(res.getFailureMessage().indexOf("input has trailing data after first JSON Text") >= 0);
    }

    @Test // 9 - JSON trailing whitespace result: Response has the trailing whitespace, Schema - pass
    public void testJSATrailingWhitespace() throws Exception {
        ByteArrayOutputStream baos = readBA("testfiles/JSA_JSONResponse_pass.json");
        baos.write(" \t\n".getBytes());
        result.setResponseData(baos.toByteArray());
        assertion.setJsdFileName(findTestPath("testfiles/JSA_JSONSchema_pass.json"));
        res = testlog();
        assertFalse(res.isError());
        assertFalse(res.isFailure());
    }

    @Test // 13 - Invalid path to Schema: Response - pass, Schema - invalid file path format
    public void testJSAInvalidSchemaPath() throws Exception {
        // IO error
        result.setResponseData(readFile("testfiles/JSA_JSONResponse_pass.json"));
        assertion.setJsdFileName(findTestPath("`/1234567890-=[];',./~!@#%^&*()_+{}:\"<>?/JSA_JSONSchema_pass.json"));
        res = testlog();
        assertFalse(res.isError());
        assertTrue(res.isFailure());
    }

    @Test // 15.1 - Schema file not found: Response - pass, Schema - fake (nonexistent) file name, fake (nonexistent) path, access denied
    public void testJSANoSchema1() throws Exception {
        // IO error
        result.setResponseData(readFile("testfiles/JSA_JSONResponse_pass.json"));
        assertion.setJsdFileName("testfiles/JSA_JSONResponse_fake.json");
        res = testlog();
        assertFalse(res.isError());
        assertTrue(res.isFailure());
        assertTrue(res.getFailureMessage()
                        .replace("\\","")
                        .replace("/","")
                        .indexOf(assertion.getJsdFileName()
                                .replace("/","")
                                .replace("\\","")) >= 0);
    }

    @Test // 15.2 - Schema file not found: Response - pass, Schema - fake (nonexistent) file name, fake (nonexistent) path, access denied
        public void testJSANoSchema2() throws Exception {
            // IO error
            result.setResponseData(readFile("testfiles/JSA_JSONResponse_pass.json"));
            assertion.setJsdFileName("testfiles/");
            res = testlog();
            assertFalse(res.isError());
            assertTrue(res.isFailure());
            assertTrue(res.getFailureMessage()
                            .replace("\\","")
                            .replace("/","")
                            .indexOf(assertion.getJsdFileName()
                                    .replace("/","")
                                    .replace("\\","")) >= 0);
    }

    @Test // 15.3 - Schema file not found: Response - pass, Schema - fake (nonexistent) file name, fake (nonexistent) path, access denied
        public void testJSANoSchema3() throws Exception {
            // IO error
            result.setResponseData(readFile("testfiles/JSA_JSONResponse_pass.json"));
            assertion.setJsdFileName("test/JSA_JSONResponse_pass.json");
            res = testlog();
            assertFalse(res.isError());
            assertTrue(res.isFailure());
            assertTrue(res.getFailureMessage()
                            .replace("\\","")
                            .replace("/","")
                            .indexOf(assertion.getJsdFileName()
                                    .replace("/","")
                                    .replace("\\","")) >= 0);
    }

}
