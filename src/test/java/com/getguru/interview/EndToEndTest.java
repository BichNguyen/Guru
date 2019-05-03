package com.getguru.interview;

import com.getguru.interview.resources.Earthquake;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class EndToEndTest {

  @ClassRule
  public static final DropwizardAppRule<InterviewConfiguration> RULE =
      new DropwizardAppRule<>(InterviewApplication.class);

  @Test
  public void getsAllReturnedData() {
    // Method Call
    final Response response =
        ClientBuilder.newClient()
            .target("http://localhost:" + RULE.getLocalPort() + "/data")
            .request()
            .get();

    Earthquake[] earthquakeResponseData = response.readEntity(Earthquake[].class);

    // Verify
    Assert.assertNotNull(response);
    Assert.assertEquals(50, earthquakeResponseData.length);
    Assert.assertTrue(earthquakeResponseData[0].getMagnitude().equals(Double.valueOf(5.3)));
  }

  @Test
  public void getsAllReturnedData_filterParamGiven_NoValue() {
    // Method Call
    final Response response =
        ClientBuilder.newClient()
            .target("http://localhost:" + RULE.getLocalPort() + "/data")
            .request()
            .get();

    Earthquake[] earthquakeResponseData = response.readEntity(Earthquake[].class);

    // Verify
    Assert.assertNotNull(response);
    Assert.assertEquals(50, earthquakeResponseData.length);
    Assert.assertTrue(earthquakeResponseData[0].getMagnitude().equals(Double.valueOf(5.3)));
  }

  @Test
  public void getsAllReturnedData_ValidFilterGiven() {
    // Set Up
    String filter = "SW";

    // Method Call
    final Response response =
        ClientBuilder.newClient()
            .target("http://localhost:" + RULE.getLocalPort() + "/data?filter=" + filter)
            .request()
            .get();

    // Verify
    Earthquake[] earthquakeResponseData = response.readEntity(Earthquake[].class);
    int lengthBeforeFilterCheck = earthquakeResponseData.length;

    Assert.assertNotNull(response);
    Assert.assertEquals(
        earthquakeResponseData.length,
        Arrays.stream(earthquakeResponseData)
            .filter(data -> data.getPlace().contains(filter))
            .collect(Collectors.toList())
            .size());
  }

  @Test
  public void getsAllReturnedData_ValidFilterGiven_NoMatches() {
    // Set Up
    String filter = "SWMSDFnasmdnamn";

    // Method Call
    final Response response =
        ClientBuilder.newClient()
            .target("http://localhost:" + RULE.getLocalPort() + "/data?filter=" + filter)
            .request()
            .get();

    // Verify
    Earthquake[] earthquakeResponseData = response.readEntity(Earthquake[].class);

    Assert.assertNotNull(response);
    Assert.assertEquals(
        earthquakeResponseData.length,
        Arrays.stream(earthquakeResponseData)
            .filter(data -> data.getPlace().contains(filter))
            .collect(Collectors.toList())
            .size());
  }
}
