package com.github.onsdigital.perkin.services;

import com.github.davidcarboni.httpino.Response;
import com.github.onsdigital.ConfigurationManager;
import com.github.onsdigital.HttpManager;
import com.github.onsdigital.perkin.decrypt.Decrypt;
import com.github.onsdigital.perkin.helper.FileHelper;
import com.github.onsdigital.perkin.helper.Http;
import com.github.onsdigital.perkin.transform.TransformException;
import org.eclipse.jetty.http.HttpStatus;
import org.apache.http.StatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by ian on 05/04/2016.
 */
public class DecryptTest {

    private Http mockedHttp;

    private static final String DECRYPT_HOST = "http://localhost:5000";
    private static final String DECRYPT_PATH = "decrypt";

    @Before
    public void setUp() throws IOException {
        ConfigurationManager.set("DECRYPT_HOST", DECRYPT_HOST);
        ConfigurationManager.set("DECRYPT_PATH", DECRYPT_PATH);

        mockedHttp = mock(Http.class);

        HttpManager.setInstance(mockedHttp);
    }

    @Test
    public void shouldNotDecryptUnencryptedJson() throws IOException {

        String testSurveyJson = FileHelper.loadFile("survey.ftp.json");
        Decrypt decrypt = new Decrypt(testSurveyJson);

        assertEquals(decrypt.getDecrypted(), testSurveyJson);
    }

    @Test
    public void shouldCallDecryptWithCorrectParams() throws Exception {
        String mockEncrypted = "aksdlkadlkasdkml";
        Decrypt decrypt = new Decrypt(mockEncrypted);

        Response mockResponse = new MockedResponse(mock(StatusLine.class), "Some Mock Response");

        when(mockResponse.statusLine.getStatusCode()).thenReturn(HttpStatus.OK_200);

        when(mockedHttp.postString(any(), anyString())).thenReturn(mockResponse);

        decrypt.getDecrypted();

        verify(mockedHttp).postString(eq(decrypt.getEndpoint()), eq(mockEncrypted));
    }

    @Test(expected=TransformException.class)
    public void shouldThrowTransformationExceptionOnError() throws IOException {

        String mockEncrypted = "aksdlkadlkasdkml";
        Decrypt decrypt = new Decrypt(mockEncrypted);

        Response mockResponse = new MockedResponse(mock(StatusLine.class), "Some Mock Response");

        when(mockResponse.statusLine.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR_500);

        when(mockedHttp.postString(any(), anyString())).thenReturn(mockResponse);

        decrypt.getDecrypted();
    }
}